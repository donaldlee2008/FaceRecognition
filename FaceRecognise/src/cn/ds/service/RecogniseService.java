/**
 * Project             :FaceRecognise project
 * Comments            :人脸识别服务类，是人脸识别的基础
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.service;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_contrib.*;

import cn.ds.domain.User;
import cn.ds.face.PreprocessFace;
import cn.ds.face.Recognition;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_objdetect.*;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class RecogniseService extends Thread {

	private final double CHANGE_IN_IMAGE_FOR_COLLECTION = 0.3;//0.3;
	private final double CHANGE_IN_SECONDS_FOR_COLLECTION = 1.0;
	private final int FACE_WIDTH = 70;
	//private final int FACE_HEIGHT = 90;
	private final int DESIRE_IMAGE_WIDTH = 320;
	private final boolean LRS = true;
	private final int BORDER = 8;
	private int FACENUM = 6;
	private int recogniseTime = 0;

	private CanvasFrame canvasFrame;

	private static Recognition recognition;
	private PreprocessFace preprocessFace;

	private CascadeClassifier faceCascade;
	private CascadeClassifier eyeCascade1;
	private CascadeClassifier eyeCascade2;
	private CvCapture capture = null;

	private int faceCounter = 0;
	private Vector<CvMat> preprocessedFaces = new Vector<CvMat>();
	boolean gotFaceAndEyes = false;
	private boolean flag = true;
	private boolean recogniseFlag = false;

	private MODES m_mode = MODES.MODE_DETECTION;

	public enum MODES {
		MODE_DETECTION, MODE_COLLECT_FACES, MODE_RECOGNITION, MODE_WAIT, MODE_COUNTDOWN, MODE_ENTRY_OVER
	};

	private BlockingQueue result;

	private static RecogniseService recogniseService;

	private int cameraNumber = 0;
	private int width = 640;
	private int height = 480;
	private String windowName = "人脸识别";
	private volatile boolean run = true;
	private int countDownNum = 3;
	private CvMat displayedFrame;
	private Timer timer;
	private BlockingQueue<MODES> modeQueue = new LinkedBlockingQueue<MODES>();

	public static RecogniseService getInstance(BlockingQueue recogniseResult) {
		if (recogniseService == null) {
			recogniseService = new RecogniseService(recogniseResult);
			recogniseService.start();
		} else {
			recogniseService.setRecogniseResult(recogniseResult);
		}
		return recogniseService;
	}

	public static RecogniseService getInstance() {
		return recogniseService;
	}

	private RecogniseService(BlockingQueue recogniseResult) {
		this.result = recogniseResult;
		
		Properties pp = new Properties();
		InputStream fis = null;
		float threshold = 0.7f;
		try {
			fis = new FileInputStream("dat/oracle.properties");
			pp.load(fis);
			String thresholdStr = pp.getProperty("threshold");
			threshold = Float.parseFloat(thresholdStr);
			String camIdStr = pp.getProperty("camId");
			cameraNumber = Integer.parseInt(camIdStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.recognition = new Recognition(createFisherFaceRecognizer(), threshold);

		this.preprocessFace = new PreprocessFace();

		initDetectors();
		initWebcam(cameraNumber, width, height);

		File file = new File("dat/data.xml");
		if (file.exists()) {
			this.recognition.load("dat/data.xml");
			recogniseFlag = true;
		}
	}

	public void initDetectors() {

		try {
			faceCascade = new CascadeClassifier();
			faceCascade.load("dat/haarcascade_frontalface_alt.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			eyeCascade1 = new CascadeClassifier();
			eyeCascade1.load("dat/haarcascade_eye.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			eyeCascade2 = new CascadeClassifier();
			eyeCascade2.load("dat/haarcascade_eye_tree_eyeglasses.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CvCapture initWebcam(int cameraNumber, int width, int height) {

		try {
			capture = cvCreateCameraCapture(cameraNumber);
			cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_WIDTH, width);
			cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_HEIGHT, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return capture;
	}

	public void initRecogniseWindow() {
		this.canvasFrame = new CanvasFrame(windowName);
		this.canvasFrame.setSize(width, height);
		this.canvasFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		this.canvasFrame.setLocation(Toolkit.getDefaultToolkit()
				.getScreenSize().width - 740, Toolkit.getDefaultToolkit()
				.getScreenSize().height / 2 - 240);
		 this.canvasFrame.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					if (RecogniseService.getInstance() != null && RecogniseService.getInstance().getState() != State.WAITING) {
						recogniseService.waitMode();
					}
				}
			});
	}

	@Override
	public void run() {
		initRecogniseWindow();

		CvMat old_prepreprocessedFace = null;
		double old_time = 0;
		while (flag) {
			CvMat cameraFrame;
			cameraFrame = (CvMat) cvQueryFrame(capture).asCvMat();
			if (cameraFrame.empty()) {
				System.out
						.println("ERROR: Couldn't grab the next camera frame.\n");
				return;
			}

			displayedFrame = cvCreateMat(cameraFrame.rows(),
					cameraFrame.cols(), cameraFrame.type());
			cvCopy(cameraFrame, displayedFrame);

			CvRect faceRect = new CvRect();
			CvPoint leftEye = new CvPoint(), rightEye = new CvPoint();

			CvMat preprocessedFace = preprocessFace.getPreprocessedFace(
					displayedFrame, FACE_WIDTH, DESIRE_IMAGE_WIDTH,
					faceCascade, eyeCascade1, eyeCascade2, LRS, faceRect,
					leftEye, rightEye);
			

			gotFaceAndEyes = false;
			if (preprocessedFace != null)
				gotFaceAndEyes = true;

			// 检测到人脸，在人脸区域画出边界
			if (faceRect.width() > 0) {
				cvRectangleR(displayedFrame, faceRect, CV_RGB(255, 255, 0), 2,
						CV_AA, 0);

				// 检测出人眼，一人眼中心为圆点画圆圈
				CvScalar eyeColor = CV_RGB(0, 255, 255);
				if (leftEye.x() >= 0) { // Check if the eye was detected
					cvCircle(
							displayedFrame,
							cvPoint(faceRect.x() + leftEye.x(), faceRect.y()
									+ leftEye.y()), 6, eyeColor, 1, CV_AA, 0);
				}
				if (rightEye.x() >= 0) { // Check if the eye was detected
					cvCircle(
							displayedFrame,
							cvPoint(faceRect.x() + rightEye.x(), faceRect.y()
									+ rightEye.y()), 6, eyeColor, 1, CV_AA, 0);
				}
			}

			MODES m = modeQueue.poll();
			if (m != null) {
				m_mode = m;
			}

			if (m_mode == MODES.MODE_DETECTION) {

			} else if (m_mode == MODES.MODE_COLLECT_FACES) {

				if (faceCounter < FACENUM) {
					if (gotFaceAndEyes) {

						double imageDiff = 10000000000.0;
						if (old_prepreprocessedFace != null) {
							imageDiff = recognition.getSimilarity(
									preprocessedFace, old_prepreprocessedFace);
						}

						double current_time = (double) cvGetTickCount();
						double timeDiff_seconds = (current_time - old_time)
								/ cvGetTickFrequency();

						if ((imageDiff > CHANGE_IN_IMAGE_FOR_COLLECTION)
								&& (timeDiff_seconds > CHANGE_IN_SECONDS_FOR_COLLECTION)) {

							if(preprocessedFaces.size() < FACENUM){
								preprocessedFaces.add(preprocessedFace);
							}

							faceCounter += 1;

							CvMat displayedFaceRegion = new CvMat();

							cvGetSubArr(displayedFrame, displayedFaceRegion,
									faceRect);

							cvAddS(displayedFaceRegion, CV_RGB(90, 90, 90),
									displayedFaceRegion, null);

							if (old_prepreprocessedFace == null) {
								old_prepreprocessedFace = cvCreateMat(
										preprocessedFace.rows(),
										preprocessedFace.cols(),
										preprocessedFace.type());
							}
							cvCopy(preprocessedFace, old_prepreprocessedFace);

							old_time = current_time;

						}
					}

				} else {
					try {
						result.put(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					faceCounter = 0;
					//detectMode();
					entryPhotoOver();
				}
			} else if (m_mode == MODES.MODE_COUNTDOWN) {

				CvPoint point = new CvPoint(displayedFrame.cols() / 2,
						displayedFrame.rows() / 2);
				drawString(displayedFrame, String.valueOf(getCountDownNum()),
						point, CV_RGB(0, 85, 230), 4, 3, 3);

			} else if (getM_mode() == MODES.MODE_RECOGNITION) {
				if(recogniseFlag){
					if (gotFaceAndEyes) {

						int r = recognition.recognise(preprocessedFace);
						double similarity = recognition.getSimilarity();
						if (recognition.getUnknowThreshold() <= similarity
								&& similarity <= recognition.getUnknowThreshold() + 0.1f
								&& recogniseTime < 3) {
							recogniseTime += 1;
						} else {
							recogniseTime = 0;
							try {
								result.put(r);
							} catch (Exception e) {
								e.printStackTrace();
							}
							detectMode();
						}

					}	
				}else{
					try {
						result.put(-2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					detectMode();	
				}
					
			} else if (m_mode == MODES.MODE_ENTRY_OVER) {
				CvPoint point = new CvPoint(displayedFrame.cols() / 2,
						displayedFrame.rows() / 2);
				drawString(displayedFrame, "OK", point, CV_RGB(0, 85, 230), 4,
						3, 3);

			} else if (getM_mode() == MODES.MODE_WAIT) {
				canvasFrame.dispose();

				WaitSyn();
			}

			int cx = (displayedFrame.cols() - FACE_WIDTH) / 2;
			if (preprocessedFace != null) {
				CvMat srcBGR = cvCreateMat(preprocessedFace.rows(),
						preprocessedFace.cols(), CV_8UC3);
				cvCvtColor(preprocessedFace, srcBGR, CV_GRAY2BGR);

				CvRect dstRC = cvRect(cx, BORDER, FACE_WIDTH, FACE_WIDTH);

				CvMat dstROI = cvCreateMat(srcBGR.rows(), srcBGR.cols(),
						srcBGR.type());
				cvGetSubArr(displayedFrame, dstROI, dstRC);

				cvCopy(srcBGR, dstROI);
			}

			canvasFrame.showImage(displayedFrame.asIplImage());

			int keypress = cvWaitKey(25);
			if (keypress == 27) {
				break;
			}

			if (displayedFrame != null) {
				cvReleaseMat(displayedFrame);
			}

		}
		
		
		

	}

	public void releaseCapture() {
		// 最后必须得释放capture，否则程序无法关闭
		if (capture != null) {
			cvReleaseCapture(capture);
		}
	}
	public CvRect drawString(CvMat img, String text, CvPoint coord,
			CvScalar color, float fontScale, int thickness, int fontFace) {

		int[] baseline = new int[] { 0 };
		CvFont font = new CvFont(fontFace, fontScale, thickness);
		CvSize textSize = new CvSize();
		cvGetTextSize(text, font, textSize, baseline);
		baseline[0] += thickness;

		if (coord.y() >= 0) {
			coord.y(coord.y() + textSize.height() / 4);
		} else {
			coord.y(coord.y() + img.rows() - baseline[0] + 1);
		}

		if (coord.x() > 0) {
			coord.x(coord.x() - textSize.width() / 2 + 1);
		}

		CvRect boundingRect = cvRect(coord.x(), coord.y() - textSize.height(),
				textSize.width(), baseline[0] + textSize.height());
		cvPutText(img, text, coord, font, color);

		return boundingRect;
	}

	public void entryPhoto() {
		countDownMOde();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// countDownNum -= 1;
				setCountDownNum(getCountDownNum() - 1);

				if (countDownNum <= 0) {
					timer.cancel();
					// countDownNum = 3;
					setCountDownNum(3);
					// m_mode = MODES.MODE_COLLECT_FACES;
					// setM_mode(MODES.MODE_COLLECT_FACES);
					collectMode();
				}

			}
		}, 1000, 1000);

	}

	public void entryPhotoOver() {
		
		enrtyOverMode();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				timer.cancel();
				detectMode();
			}
		}, 1000, 1000);

	}

	public synchronized void WaitSyn() {
		try {

			this.wait();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void Resume() {

		synchronized (this) {
			initRecogniseWindow();
			detectMode();
			this.notify();
		}
	}

	public void train() {
		new TrainThread(recognition, new UserDBService()).start();
	}

	public void saveUser(User user) {
		new SaveThread(preprocessedFaces, user, new UserDBService()).start();
	}

	public void updateUser(User user) {
		new UpdateThread(preprocessedFaces, user, new UserDBService()).start();
	}

	class SaveThread extends Thread {
		private Vector<CvMat> faces;
		private User user;
		private UserDBService db;

		public SaveThread(Vector<CvMat> faces, User user, UserDBService db) {
			this.faces = faces;
			this.user = user;
			this.db = db;
		}

		@Override
		public void run() {
			db.insertUser(user);
			if (faces.size() > 0 && "是".equals(user.getHavePhoto())) {
				String picsPath = user.getPicsPath();
				int id = user.getId();
				for (int i = 0; i < faces.size(); i++) {
					cvSaveImage(picsPath + id + "_" + i + ".bmp", faces.get(i));
				}
				faces.clear();
			}

		}

	}

	class TrainThread extends Thread {
		private Recognition recognition;
		private UserDBService db;

		public TrainThread(Recognition recognition, UserDBService db) {
			this.recognition = recognition;
			this.db = db;
		}

		@Override
		public void run() {
			ArrayList<User> users = db.getAllHavePhotoUsers();
			
			if(users.size() > 1){
				MatVector Faces = new MatVector(users.size() * FACENUM);
				int[] lables = new int[users.size() * FACENUM];
				for (int i = 0; i < users.size(); i++) {
					User user = users.get(i);
					int id = user.getId();
					String picsPath = user.getPicsPath();

					for (int j = 0; j < FACENUM; j++) {
						CvMat m = cvLoadImageM("pics/" + id + "_" + j + ".bmp",
								CV_8U);
						Faces.put(i * FACENUM + j, m);
						lables[i * FACENUM + j] = id;
					}
				}
				recognition.train(Faces, lables);

				try {
					result.put(true);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else{

				try {
					result.put(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	class UpdateThread extends Thread {
		private Vector<CvMat> faces;
		private User user;
		private UserDBService db;

		public UpdateThread(Vector<CvMat> faces, User user, UserDBService db) {
			this.faces = faces;
			this.user = user;
			this.db = db;
		}

		@Override
		public void run() {
			db.updateUser(user);

			if (faces.size() > 0 && "是".equals(user.getHavePhoto())) {
				String picsPath = user.getPicsPath();
				int id = user.getId();
				for (int i = 0; i < faces.size(); i++) {
					cvSaveImage(picsPath + id + "_" + i + ".bmp", faces.get(i));
				}
				faces.clear();
			}

		}

	}

	public void detectMode() {
		modeQueue.offer(MODES.MODE_DETECTION);
	}

	public void collectMode() {
		modeQueue.offer(MODES.MODE_COLLECT_FACES);
	}

	public void recognitionMode() {
		modeQueue.offer(MODES.MODE_RECOGNITION);
	}

	public void countDownMOde() {
		modeQueue.offer(MODES.MODE_COUNTDOWN);
	}

	public void enrtyOverMode() {
		modeQueue.offer(MODES.MODE_ENTRY_OVER);
	}


	public void waitMode() {
		modeQueue.offer(MODES.MODE_WAIT);
	}


	public MODES getM_mode() {
		return m_mode;
	}

	public void setM_mode(MODES m_mode) {
		this.m_mode = m_mode;
	}
	
	public BlockingQueue<Integer> getRecogniseResult() {
		return result;
	}

	public void setRecogniseResult(BlockingQueue<Integer> recogniseResult) {
		this.result = recogniseResult;
	}

	public int getCameraNumber() {
		return cameraNumber;
	}

	public void setCameraNumber(int cameraNumber) {
		this.cameraNumber = cameraNumber;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public synchronized int getCountDownNum() {
		return countDownNum;
	}

	public synchronized void setCountDownNum(int countDownNum) {
		this.countDownNum = countDownNum;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isRecogniseFlag() {
		return recogniseFlag;
	}

	public static Recognition getRecognition() {
		return recognition;
	}
}
