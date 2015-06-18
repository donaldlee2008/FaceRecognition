package cn.ds.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import cn.ds.face.Recognition;
import cn.ds.utils.CsvService;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_contrib.*;

public class BatchUnmaskFaceRecogniseThread extends Thread {
	private Recognition recognition;
	private CsvService csvService;
	private float unknowThreshold = 0.6f;
	private List<String> photoPathList = new ArrayList<String>();
	private List<String> sourcePhotoPathList = new ArrayList<String>();
	private String savePath;
	private BlockingQueue<Boolean> result;
	private Map<Integer, String> nameIdMap = new HashMap<Integer, String>();
	private MatVector faces;
	private int[] ids;
	private int index = 0;
	private int faceNum;
	private String identifier;
	private String photoType;
	
	private String tip = "<html>您好:<br/>正在进行未处理照片批量识别，请等待</html>";
	private ProcessingViewThread processingViewThread;

	public BatchUnmaskFaceRecogniseThread(String savePath, float threshold,
			List<String> photoPath, List<String> sourcePhotoPath, int faceNum,
			String identifier, String photoType, BlockingQueue<Boolean> result) {
		this.savePath = savePath;
		this.unknowThreshold = threshold;

		for (int i = 0; i < photoPath.size(); i++) {
			this.photoPathList.add(photoPath.get(i));
		}

		for (int i = 0; i < sourcePhotoPath.size(); i++) {
			this.sourcePhotoPathList.add(sourcePhotoPath.get(i));
		}

		this.faceNum = faceNum;
		this.identifier = identifier;
		this.photoType = photoType;
		this.result = result;

		this.recognition = new Recognition(createFisherFaceRecognizer(),
				this.unknowThreshold);
		

	}

	@Override
	public void run() {
		
		processingViewThread = new ProcessingViewThread(tip);
		processingViewThread.start();

		faces = new MatVector(sourcePhotoPathList.size()*faceNum);
		ids = new int[sourcePhotoPathList.size()*faceNum];
		
		for(int i = 0;i < sourcePhotoPathList.size();i++){
			String photoPath = sourcePhotoPathList.get(i);
			String name = photoPath.substring(photoPath.lastIndexOf("\\") + 1);
			File fileDir = new File(photoPath);
			File[] files = fileDir.listFiles();
			
			nameIdMap.put(index, name);
			
			for(int j = 0;j < faceNum;j++){
				CvMat mat = cvLoadImageM(files[j].getAbsolutePath(),CV_8U);
				
				faces.put(faceNum * index + j, mat);
				ids[faceNum * index + j] = index;
				
			}
			
			index += 1;
		}
		recognition.trainWithoutSave(faces, ids);

		csvService = new CsvService(savePath + "DS_face.csv", null);
		csvService.write(new String[] { "photoToBeRecognise","result" });

		String[] content = new String[2];
		for (int i = 0; i < photoPathList.size(); i++) {
			String photoPath = photoPathList.get(i);
			CvMat mat = cvLoadImageM(photoPath, CV_8U);
			int r = recognition.recognise(mat);
			
			if (r >= 0) {
				String name = nameIdMap.get(r);
				content[0] = photoPath;
				content[1] = name;
			} else {
				content[0] = photoPath;
				content[1] = "NO";
			}
			csvService.write(content);
		}

		if (csvService != null) {
			csvService.closeWriter();
		}

		try {
			processingViewThread.getResult().put(true);
			result.put(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
