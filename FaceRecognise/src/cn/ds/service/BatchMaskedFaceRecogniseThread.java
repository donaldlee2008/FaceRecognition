package cn.ds.service;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import cn.ds.domain.User;
import cn.ds.face.Recognition;
import cn.ds.utils.CsvService;

public class BatchMaskedFaceRecogniseThread extends Thread {

	private UserDBService userDBService;
	private Recognition recognition;
	private CsvService csvService;
	private float unknowThreshold = 0.6f;
	private List<String> photoPathList = new ArrayList<String>();
	private String savePath;
	private BlockingQueue<Boolean> result;

	private String tip = "<html>您好:<br/>正在进行已处理照片批量识别，请等待</html>";
	private ProcessingViewThread processingViewThread;
	
	public BatchMaskedFaceRecogniseThread(String savePath, float threshold,
			List<String> photoPath,BlockingQueue<Boolean> result) {
		this.savePath = savePath;
		this.unknowThreshold = threshold;
		for (int i = 0; i < photoPath.size(); i++) {
			this.photoPathList.add(photoPath.get(i));
		}
		this.result = result;
		
		userDBService = new UserDBService();
		csvService = new CsvService(savePath + "DS_face.csv", null);
		recognition = RecogniseService.getInstance(null).getRecognition();
	}

	@Override
	public void run() {
		processingViewThread = new ProcessingViewThread(tip);
		processingViewThread.start();
		
		csvService.write(new String[]{"Account","Tel","photoName","photoPath"});
	for(int i = 0;i < photoPathList.size();i++){
		String photoPath = photoPathList.get(i);
		String photoName = photoPath.substring(photoPath.lastIndexOf("\\") + 1);
		CvMat mat = cvLoadImageM(photoPath,CV_8U);
		int result = recognition.recognise(mat);
		
		if(result > -1){
			User user = userDBService.getUserById(result);
			csvService.write(new String[]{user.getAccount(),user.getTel(),photoName,photoPath});
		}else{
			csvService.write(new String[]{"NO","NO","NO",photoName,photoPath});
		}
	}
	if(csvService != null){
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
