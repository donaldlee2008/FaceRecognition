package cn.ds.model;

import cn.ds.domain.User;
import cn.ds.service.UserDBService;

public class CommonUserModel {
public User getUserById(int id){
	return new UserDBService().getUserById(id);
}
}
