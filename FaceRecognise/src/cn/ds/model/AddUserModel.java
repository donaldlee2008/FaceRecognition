package cn.ds.model;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.DefaultTableModel;

import cn.ds.domain.User;
import cn.ds.service.RecogniseService;
import cn.ds.service.UserDBService;
import cn.ds.utils.Page;

public class AddUserModel {

	private UserDBService userDBService;

	public AddUserModel() {
		userDBService = new UserDBService();
	}

	public void addUser(User user) {
		RecogniseService.getInstance(null).saveUser(user);
	}

	public void entryPhotos(BlockingQueue result) {
		if (RecogniseService.getInstance(null).getState() == State.WAITING) {
			RecogniseService.getInstance(null).Resume();
		}
		RecogniseService.getInstance(result).entryPhoto();
	}

	public int getAddUserId() {
		return userDBService.getNextId();
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

	public void updatePage(Page page) {
		int count = userDBService.getCount();
		page.setRowCount(count);
	}
}
