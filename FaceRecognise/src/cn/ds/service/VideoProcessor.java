/**
 * Project             :FaceRecognise project
 * Comments            :进行视频识别
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-3-20 | 创建 | jxm 
 * 2 | 2013-4-26 |优化代码，增加注释量 |jxm 
 */
package cn.ds.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.MatVector;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import com.googlecode.javacv.cpp.opencv_objdetect.CascadeClassifier;

import cn.ds.face.PreprocessFace;
import cn.ds.face.Recognition;
import cn.ds.utils.ExcelService;

import static com.googlecode.javacv.cpp.opencv_contrib.createFisherFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class VideoProcessor extends Thread {

	// 总处理图片数量
	private double calTotalProcessedSize = 0;
	private double oldCalTotalProcessedSize = 0;
	// 视频图片数量
	private double videoSize = 0;
	// 处理线程数组
	private VideoProcessThread[] threads;
	/* 缓存各线程下载的长度 */
	private Map<Integer, Integer> processedLenght = new ConcurrentHashMap<Integer, Integer>();
	/* 每条线程处理的长度 */
	private double block;
	private int videoId;
	private int taskId;
	private int threadNum;
	private String videoPath;
	private String savePath;
	private List<String> picPath;
	private String faceCascadePath = "dat/haarcascade_frontalface_alt.xml";
	private Recognition recognition;
	private int fps;
	private float magnification = 1.0f;
	private float threshold = 0.7f;
	private Map<Integer, String> photoNames = new HashMap<Integer, String>();
	private boolean allFinish = false;
	private boolean haveFace = false;
	private List<String> haveNoFace = new ArrayList<String>();
	private ProcessListenerImp listener;
	private double calVideoSize;
	private ExcelService excelService;
	private SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat format2 = new SimpleDateFormat("HH_mm_ss");
	private int maxThreadNum = 2;

	protected synchronized void append(double size) {
		calTotalProcessedSize += size;
	}

	public VideoProcessor(int taskId, int videoId, float magnification,
			float threshold, List<String> picPath, String videoPath,
			String savePath, ProcessListenerImp listener,
			ExcelService excelService) {
		this.taskId = taskId;
		this.videoId = videoId;
		if (magnification > 0) {
			this.magnification = magnification;
		}
		if (threshold > 0) {
			this.threshold = threshold;
		}
		this.picPath = picPath;
		this.videoPath = videoPath;
		// this.videoname = videoPath.substring(videoPath.lastIndexOf("\\") +
		// 1);
		this.savePath = savePath;
		this.listener = listener;
		this.excelService = excelService;

		format1.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		format2.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));

		init();
	}

	/**
	 * Description :初始化操作，包括训练待识别人脸图片，计算block
	 * 
	 * @return void
	 */
	public void init() {
		CascadeClassifier faceCascade = new CascadeClassifier();
		faceCascade.load(faceCascadePath);
		PreprocessFace preprocessFace = new PreprocessFace();

		MatVector faces = null;
		int[] faceLabels = null;

		List<CvMat> tempFaces = new ArrayList<CvMat>();
		List<String> tempPath = new ArrayList<String>();

		for (String path : picPath) {
			CvMat srcFace = cvLoadImageM(path);
			CvMat face = preprocessFace.getFaceWithoutProcess(srcFace, 70, 320,
					faceCascade);

			if (face != null) {
				tempFaces.add(face);
				tempPath.add(path);
			} else {
				haveNoFace.add(path);
			}
		}

		if (tempFaces.size() == 0) {

		} else if (tempFaces.size() == 1) {
			faces = new MatVector(4);
			faceLabels = new int[4];
			CvMat face = tempFaces.get(0);
			CvMat mirroredFace = cvCreateMat(face.rows(), face.cols(),
					face.type());
			cvFlip(face, mirroredFace, 1);

			faces.put(0, face);
			faces.put(1, mirroredFace);
			faces.put(2, face);
			faces.put(3, mirroredFace);

			faceLabels[0] = 0;
			faceLabels[1] = 0;
			faceLabels[2] = 1;
			faceLabels[3] = 1;

			photoNames.put(0, tempPath.get(0));
			photoNames.put(1, tempPath.get(0));

			haveFace = true;
		} else if (tempFaces.size() > 0) {

			faces = new MatVector(tempFaces.size() * 2);
			faceLabels = new int[tempFaces.size() * 2];

			for (int i = 0; i < tempFaces.size(); i++) {
				CvMat face = tempFaces.get(i);
				CvMat mirroredFace = cvCreateMat(face.rows(), face.cols(),
						face.type());
				cvFlip(face, mirroredFace, 1);

				faces.put(i * 2, face);
				faces.put(i * 2 + 1, mirroredFace);

				faceLabels[i * 2] = i;
				faceLabels[i * 2 + 1] = i;

				photoNames.put(i, tempPath.get(i));
			}
			haveFace = true;
		}

		if (haveFace) {
			recognition = new Recognition(createFisherFaceRecognizer(),
					threshold);
			recognition.trainWithoutSave(faces, faceLabels);
		}

		CvCapture capture = cvCreateFileCapture(videoPath);

		videoSize = cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);
		fps = (int) cvGetCaptureProperty(capture, CV_CAP_PROP_FPS);

		calVideoSize = videoSize / (fps * magnification);

		if (capture != null)
			cvReleaseCapture(capture);

		// 计算线程的数量，根据测试当处理帧数小于60帧时线程的性能较低，规定开线程的最大数量为5个
		int temp = (int) Math.round(this.videoSize / (fps * 60));

		if (temp > maxThreadNum)
			temp = maxThreadNum;
		this.threadNum = temp;

		this.threads = new VideoProcessThread[threadNum];

		// 计算每条线程处理的长度
		this.block = (this.videoSize % this.threads.length) == 0 ? this.videoSize
				/ this.threads.length
				: this.videoSize / this.threads.length + 1;

		
		for (String path : haveNoFace) {
			String[] contents = { taskId + "", "", "无法检测到人脸", path, "" };
			excelService.addRow(0, contents);
		}
	}

	public double process(ProcessListenerImp listener) throws Exception {
		for (int i = 0; i < this.threads.length; i++) {
			this.processedLenght.put(i, 0);
		}

		for (int i = 0; i < this.threads.length; i++) {
			int processedLength = this.processedLenght.get(i);
			if (processedLength < this.block
					&& this.calTotalProcessedSize < this.videoSize) {
				this.threads[i] = new VideoProcessThread(i,
						processedLenght.get(i));
				this.threads[i].setPriority(7);
				this.threads[i].start();
			} else {
				this.threads[i] = null;
			}
		}
		boolean notFinish = true;// 处理未完成
		while (notFinish) {// 循环判断所有线程是否完成处理
			Thread.sleep(1000);
			// Thread.sleep(2000);
			notFinish = false;// 假定全部线程处理完成
			for (int i = 0; i < this.threads.length; i++) {
				if (this.threads[i] != null && !this.threads[i].isFinish()) {// 如果发现线程未完成处理
					notFinish = true;// 设置标志为下载没有完成
					// if (this.threads[i].getProcessedNum() == -1) {//
					// 如果处理失败,再重新处理
					// this.threads[i] = new VideoProcessThread(
					// threads[i].getThreadId(), 0);
					// this.threads[i].start();
					// }
				}
			}
			if (listener != null) {
				double r = calTotalProcessedSize - oldCalTotalProcessedSize;

				oldCalTotalProcessedSize = calTotalProcessedSize;
				listener.onProcessSize(r);// 通知目前已经完成的长度
			}

		}
		allFinish = true;
		return this.calTotalProcessedSize;
	}

	@Override
	public void run() {
		try {
			if (haveFace) {
				process(listener);
			} else {
				append(calVideoSize);
				allFinish = true;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		// try {
		// process(listener);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	public class VideoProcessThread extends Thread {
		private int threadId = -1;
		private double processedNum = -1;
		private boolean finish = false;
		private CvCapture capture;
		private PreprocessFace preprocessFace;
		private Recognition recog;
		private CascadeClassifier faceCascade;

		public VideoProcessThread(int threadId, int processedNum) {
			this.threadId = threadId;
			this.processedNum = processedNum;

			capture = cvCreateFileCapture(videoPath);

			preprocessFace = new PreprocessFace();
			recog = VideoProcessor.this.getRecognition();

			faceCascade = new CascadeClassifier();
			faceCascade.load(faceCascadePath);
		}

		@Override
		public void run() {

			double startPosition = threadId * block + processedNum;
			CvMat displayedFrame = null;
			while (processedNum <= block) {

				cvSetCaptureProperty(capture, CV_CAP_PROP_POS_FRAMES,
						startPosition);

				CvMat frame = cvQueryFrame(capture).asCvMat();

				if (displayedFrame == null) {
					displayedFrame = cvCreateMat(frame.rows(), frame.cols(),
							frame.type());
				}
				cvCopy(frame, displayedFrame);

				Vector<CvMat> faces = preprocessFace
						.getManyFacesWithoutProcess(displayedFrame, 70, 320,
								faceCascade);
				// Vector<CvMat> faces =
				// preprocessFace.getManyPreprocessedFacesWithoutDetectEyes(displayedFrame,
				// 70, 320,faceCascade,true);

				String foundPhotoName = taskId + "_" + videoId + "_";
				String photoName = "";
				boolean have = false;
				for (int i = 0; i < faces.size(); i++) {

					CvMat f = faces.get(i);

					int result = recog.recognise(f);
					if (result != -1) {
						have = true;
						// double time = cvGetCaptureProperty(capture,
						// CV_CAP_PROP_POS_MSEC);
						foundPhotoName += result + "_";
						photoName += photoNames.get(result) + ";";
						// excelService.addCell(new VideoTaskResult(taskId,
						// videoname, foundPhotoName, photoName, foundTime));
						// cvSaveImage(savePath + videoId + "_" + result + "_"
						// + time + ".jpg", displayedFrame);

					}
				}
				if (have) {
					double time = cvGetCaptureProperty(capture,
							CV_CAP_PROP_POS_MSEC);
					String foundTime = format1.format(time);
					String foundTime2 = format2.format(time);
					foundPhotoName += foundTime2 + ".jpg";
					String[] contents = { taskId + "", videoPath,
							foundPhotoName,
							photoName.substring(0, photoName.length() - 1),
							foundTime };
					excelService.addRow(0, contents);
					cvSaveImage(savePath + foundPhotoName, displayedFrame);
				}

				if (faces != null)
					faces.clear();
				// if (displayedFrame != null)
				// cvReleaseMat(displayedFrame);

				processedNum += magnification * fps;
				// calProcessedNum += magnification;

				append(magnification);

				if (threadId == threadNum - 1
						&& processedNum >= videoSize - threadId * block)
					break;

				startPosition += magnification * fps;
				//
				// if (displayedFrame != null)
				// cvReleaseMat(displayedFrame);
			}

			// 处理完成
			finish = true;

			if (capture != null)
				cvReleaseCapture(capture);
		}

		public boolean isFinish() {
			return finish;
		}

		public double getProcessedNum() {
			return processedNum;
		}

		public int getThreadId() {
			return threadId;
		}

		public void setThreadId(int threadId) {
			this.threadId = threadId;
		}
	}

	public Recognition getRecognition() {
		try {
			return (Recognition) recognition.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public double getBlock() {
		return block;
	}

	public boolean isAllFinish() {
		return allFinish;
	}

	public double getCalSize() {
		return calVideoSize;
	}

	public void setCalSize(double calSize) {
		this.calVideoSize = calSize;
	}

	public double getCalTotalProcessedSize() {
		return calTotalProcessedSize;
	}

	public void setCalTotalProcessedSize(double calTotalProcessedSize) {
		this.calTotalProcessedSize = calTotalProcessedSize;
	}

}
