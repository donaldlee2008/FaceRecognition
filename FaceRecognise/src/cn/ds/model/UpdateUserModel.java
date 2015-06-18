package cn.ds.model;

import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;

import javax.swing.table.DefaultTableModel;

import cn.ds.domain.User;
import cn.ds.service.RecogniseService;
import cn.ds.service.UserDBService;

public class UpdateUserModel {
	public void updateUser(User user){
		RecogniseService.getInstance(null).updateUser(user);
	}
	public void entryPhotos(BlockingQueue result){
		if(RecogniseService.getInstance(null).getState() == State.WAITING){
			RecogniseService.getInstance(null).Resume();
		}
		//RecogniseService.getInstance(result).setM_mode(MODES.MODE_COLLECT_FACES);
		RecogniseService.getInstance(result).entryPhoto();
	}
	public User getUserById(int id){
		return new UserDBService().getUserById(id);
	}
	public void refreshUserTableUpdate(DefaultTableModel model, int index,
			User user) {
		model.setValueAt(user.getId(), index, 0);
		model.setValueAt(user.getAccount(), index, 1);
		model.setValueAt(user.getName(), index, 2);
		model.setValueAt(user.getSex(), index, 3);
		model.setValueAt(user.getHavePhoto(), index, 4);
		model.setValueAt(user.getDepartment(), index, 5);
		model.setValueAt(user.getTel(), index, 6);
	}
}
