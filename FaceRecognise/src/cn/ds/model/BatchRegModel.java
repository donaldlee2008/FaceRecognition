package cn.ds.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.DefaultTableModel;

import cn.ds.domain.User;
import cn.ds.service.ProcessingViewThread;
import cn.ds.service.UserDBService;
import cn.ds.utils.ExcelService;
import cn.ds.utils.Page;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class BatchRegModel {
	private UserDBService userDBService;
	private BlockingQueue<Boolean> resultBq;

	public BatchRegModel(BlockingQueue<Boolean> resultBq) {
		this.resultBq = resultBq;

		userDBService = new UserDBService();
	}

	public void batchInsertUsersByExcel(List<String> dataPathList) {
		new BatchInsertByExcelThread(dataPathList).start();
	}

	public void batchInsertUsersByPhotos(List<String> photoPath,
			String delimiter, int photoNum) {
		new BatchInsertByPhotoThread(photoPath, delimiter, photoNum).start();
	}

	class BatchInsertByExcelThread extends Thread {

		private List<String> excelPathList = new ArrayList<String>();
		private String tip = "<html>您好：<br/>正在进行Excel批量注册，请稍后</html>";
		private ProcessingViewThread processingViewThread;

		public BatchInsertByExcelThread(List<String> excelPathList) {
			for (int i = 0; i < excelPathList.size(); i++) {
				this.excelPathList.add(excelPathList.get(i));
			}
		}

		@Override
		public void run() {
			processingViewThread = new ProcessingViewThread(tip);
			processingViewThread.start();
			
			if (excelPathList.size() > 0) {
				ExcelService excelService = null;
				List<User> users = new ArrayList<User>();

				for (int i = 0; i < excelPathList.size(); i++) {
					String dataPath = excelPathList.get(i);
					excelService = new ExcelService(dataPath);
					List<String[]> excelContent = excelService.readAll(0);
					for (int j = 1; j < excelContent.size(); j++) {
						String[] contents = excelContent.get(j);
						User user = new User();
						if (contents[0] != null && !"".equals(contents[0])) {
							user.setAccount(contents[0]);
						} else {
							user.setAccount("NO");
						}
						if (contents[1] != null && !"".equals(contents[1])) {
							user.setPwd(contents[1]);
						} else {
							user.setPwd("123");
						}
						if (contents[2] != null && !"".equals(contents[2])) {
							user.setName(contents[2]);
						} else {
							user.setName("NO");
						}
						if (contents[3] != null && !"".equals(contents[3])) {
							user.setSex(contents[3]);
						} else {
							user.setSex("男");
						}
						if (contents[4] != null && !"".equals(contents[4])) {
							user.setDepartment(contents[4]);
						} else {
							user.setDepartment("NO");
						}
						if (contents[5] != null && !"".equals(contents[5])) {
							user.setTel(contents[5]);
						} else {
							user.setTel("NO");
						}
						user.setHavePhoto("否");
						user.setPicsPath("pics/");
						users.add(user);
					}
					userDBService.batchInsertUser(users);
					users.clear();
				}
				
				try {
					processingViewThread.getResult().put(true);
					resultBq.put(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	class BatchInsertByPhotoThread extends Thread {
		private List<String> photoPathList = new ArrayList<String>();
		private Map<String, String> namePathMap = new HashMap<String, String>();
		private String delimiter;
		private int photoNum;
		private String tip = "<html>您好：<br/>正在进行照片批量注册，请稍后</html>";
		private ProcessingViewThread processingViewThread;

		public BatchInsertByPhotoThread(List<String> photoPathList,
				String delimiter, int photoNum) {
			
			this.delimiter = delimiter;
			this.photoNum = photoNum;
			for (int i = 0; i < photoPathList.size(); i++) {
				this.photoPathList.add(photoPathList.get(i));
			}
		}

		@Override
		public void run() {
			processingViewThread = new ProcessingViewThread(tip);
			processingViewThread.start();
			
			List<User> users = new ArrayList<User>();

			for (int i = 0; i < photoPathList.size(); i++) {
				String photoPath = photoPathList.get(i);
				String photoName = photoPath.substring(photoPath
						.lastIndexOf("\\") + 1);
				String userName = photoName.substring(0,
						photoName.indexOf(delimiter));

				if (!namePathMap.containsKey(userName)) {
					namePathMap.put(userName, photoPath);
				}
			}

			for (Entry<String, String> e : namePathMap.entrySet()) {

				String photoPath = e.getValue();
				String photoDire = photoPath.substring(0,
						photoPath.lastIndexOf("\\") + 1);
				String imageType = photoPath.substring(photoPath
						.lastIndexOf(".") + 1);

				User tempUser = userDBService
						.getUniqueUserByAccount(e.getKey());

				if (tempUser != null) {
					int id = tempUser.getId();
					for (int j = 0; j < photoNum; j++) {

						CvMat mat = cvLoadImageM(photoDire + e.getKey()
								+ delimiter + j + "." + imageType, CV_8U);
						cvSaveImage("pics/" + id + "_" + j + ".bmp", mat);
						cvReleaseMat(mat);
					}
					tempUser.setHavePhoto("是");
					userDBService.updateUser(tempUser);
				} else {
					User user = new User();
					user.setAccount(e.getKey());
					user.setPwd(e.getKey());
					user.setDepartment("NO");
					user.setHavePhoto("是");
					user.setPicsPath("pics/");
					user.setSex("NO");
					user.setTel("NO");
					user.setName("NO");

					users.add(user);
				}
			}

			userDBService.batchInsertUser(users);

			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);
				String userAccount = user.getAccount();
				User dBUser = userDBService.getUniqueUserByAccount(userAccount);
				int userId = dBUser.getId();

				String photoPath = namePathMap.get(userAccount);
				String photoDire = photoPath.substring(0,
						photoPath.lastIndexOf("\\") + 1);
				String imageType = photoPath.substring(photoPath
						.lastIndexOf(".") + 1);
				for (int j = 0; j < photoNum; j++) {

					CvMat mat = cvLoadImageM(photoDire + userAccount
							+ delimiter + j + "." + imageType, CV_8U);
					cvSaveImage("pics/" + userId + "_" + j + ".bmp", mat);
					cvReleaseMat(mat);
				}
			}
			// 回收namePathMap;
			namePathMap.clear();
			// 回收users
			users.clear();

			try {
				processingViewThread.getResult().put(true);
				resultBq.put(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void updatePage(Page page) {
		int count = userDBService.getCount();
		page.setRowCount(count);
	}

	public void refreshUserTable(DefaultTableModel model, Page page) {

		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		Object[][] data = getUserByPage(page);

		if (data.length == 0 && page.getPageNow() > 1) {
			page.setPageNow(page.getPageNow() - 1);
			data = getUserByPage(page);
		}
		for (int i = 0; i < data.length; i++) {
			model.addRow(data[i]);
		}
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
}
