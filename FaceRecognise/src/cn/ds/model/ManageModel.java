package cn.ds.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.DefaultTableModel;

import cn.ds.domain.User;
import cn.ds.service.BatchMaskedFaceRecogniseThread;
import cn.ds.service.BatchUnmaskFaceRecogniseThread;
import cn.ds.service.ProcessingViewThread;
import cn.ds.service.RecogniseService;
import cn.ds.service.UserDBService;
import cn.ds.utils.Page;

public class ManageModel {
	private UserDBService userDBService;

	public ManageModel() {
		userDBService = new UserDBService();
	}

	public void train(BlockingQueue<Boolean> result) {
		RecogniseService.getInstance(result).train();
	}

	public Object[][] getUserByPage(Page page) {
		// int count = userDBService.getCount();
		// page.setRowCount(count);

		ArrayList<Object[]> al = (ArrayList<Object[]>) userDBService
				.getUsersByPage(page);
		Object[][] data = new Object[al.size()][];
		for (int i = 0; i < al.size(); i++) {
			data[i] = al.get(i);
		}
		return data;
	}

	public Object[][] getUsersByAccount(String account) {

		ArrayList<Object[]> al = (ArrayList<Object[]>) userDBService
				.getUsersByAccount(account);
		Object[][] data = new Object[al.size()][];
		for (int i = 0; i < al.size(); i++) {
			data[i] = al.get(i);
		}
		return data;
	}

	public User getUserById(int id) {
		return userDBService.getUserById(id);
	}

	public void deleteUserById(int id) {
		userDBService.deleteUser(id);
		for (int i = 0; i < 6; i++) {
			File file = new File("pics/" + id + "_" + i + ".bmp");
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public void refreshUserTable(DefaultTableModel model, Object[][] data) {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		for (int i = 0; i < data.length; i++) {
			model.addRow(data[i]);
		}
	}

	public void refreshUserTable(DefaultTableModel model, Page page) {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		Object[][] data = getUserByPage(page);

		for (int i = 0; i < data.length; i++) {
			model.addRow(data[i]);
		}
	}

	public void updatePage(Page page) {
		int count = userDBService.getCount();
		page.setRowCount(count);
	}

	public boolean deleteAllUsers() {
		ProcessingViewThread processingViewThread = new ProcessingViewThread("<html>您好：<br/>正在删除所有用户，请稍等</html>");
		processingViewThread.start();
		
		File srcFile = new File("pics/");
		File[] files = srcFile.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.startsWith("0")) {
					return false;
				} else {
					return true;
				}
			}
		});
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].exists()) {
					files[i].delete();
				}
			}
		}
		
		File file = new File("dat/data.xml");
		if(file.exists()){
			file.delete();
		}
		userDBService.deleteAllUsers();
		
		try {
		processingViewThread.getResult().put(true);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public void batchMaskedFaceRecognise(String savePath, float threshold,
			List<String> photoPath, BlockingQueue<Boolean> result) {
    new BatchMaskedFaceRecogniseThread(savePath, threshold, photoPath, result).start();
	}
	
	public void batchUnmaskedFaceRecognise(String savePath, float threshold,
			List<String> photoPath, List<String> sourcePhotoPath, int faceNum,
			String identifier, String photoType, BlockingQueue<Boolean> result){
		new BatchUnmaskFaceRecogniseThread(savePath,threshold,photoPath,sourcePhotoPath,faceNum,identifier,photoType,result).start();
	}
}
