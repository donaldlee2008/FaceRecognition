package cn.ds.model;

import cn.ds.domain.User;
import cn.ds.service.UserDBService;

public class LoginModel {

	private UserDBService userDBService;

	public LoginModel() {
		userDBService = new UserDBService();
	}

	public boolean checkUserByPwd(User user) {
		return userDBService.checkUserByPwd(user);
	}

	public boolean checkUserByFace() {
		return false;
	}

}
