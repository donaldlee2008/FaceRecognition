/**
 * Project             :FaceRecognise project
 * Comments            :用户信息数据库操作类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.service;

import java.util.ArrayList;
import java.util.List;

import cn.ds.db.SqlHelper;
import cn.ds.domain.User;
import cn.ds.utils.Page;

public class UserDBService {
	private SqlHelper sqlHelper = new SqlHelper();

	public boolean insertUser(User user) {
		String sql = "INSERT INTO USER_TABLE VALUES ( USER_SEQ.nextval,?,?,?,?,?,?,?,?)";
		String[] parameters = { user.getAccount(), user.getPwd(),
				user.getName(), user.getSex(), user.getHavePhoto(),
				user.getDepartment(), user.getTel(), user.getPicsPath() };
		boolean flag = sqlHelper.executeUpdate(sql, parameters);
		return flag;
	}

	public void batchInsertUser(List<User> users) {
		String sql = "INSERT INTO USER_TABLE VALUES ( USER_SEQ.nextval,?,?,?,?,?,?,?,?)";
		String[][] parameters = new String[users.size()][];
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);
				String[] para = { user.getAccount(), user.getPwd(),
						user.getName(), user.getSex(), user.getHavePhoto(),
						user.getDepartment(), user.getTel(), user.getPicsPath() };
				parameters[i] = para;
			}
			sqlHelper.batchexecute(sql, parameters);
		}
	}

	public boolean deleteUser(int id) {
		String sql = "DELETE FROM USER_TABLE WHERE ID = ?";
		String[] parameters = { String.valueOf(id) };
		boolean flag = sqlHelper.executeUpdate(sql, parameters);
		return flag;
	}

	public boolean updateUser(User user) {
		String sql = "UPDATE USER_TABLE SET ACCOUNT = ?,PWD = ?,NAME = ?,SEX = ?,HAVEPHOTO = ?,DEPARTMENT = ?,TEL = ?,PICSPATH = ? WHERE ID = ?";
		String[] parameters = { user.getAccount(), user.getPwd(),
				user.getName(), user.getSex(), user.getHavePhoto(),
				user.getDepartment(), user.getTel(), user.getPicsPath(),
				String.valueOf(user.getId()) };
		boolean flag = sqlHelper.executeUpdate(sql, parameters);
		return flag;
	}

	public User getUserById(int id) {
		String sql = "SELECT * FROM USER_TABLE WHERE ID = ?";
		String[] parameters = { String.valueOf(id) };
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, parameters);
		if (al.size() > 0) {
			Object[] objs = al.get(0);
			User user = new User();
			user.setId(Integer.valueOf(objs[0].toString()));
			user.setAccount(objs[1].toString());
			user.setPwd(objs[2].toString());
			user.setName(objs[3].toString());
			user.setSex(objs[4].toString());
			user.setHavePhoto(objs[5].toString());
			user.setDepartment(objs[6].toString());
			user.setTel(objs[7].toString());
			user.setPicsPath(objs[8].toString());
			return user;
		} else {
			return null;
		}
	}

	public ArrayList<User> getUsersBySN(int startSN, int endSN) {
		String sql = "SELECT * FROM USER_TABLE WHERE ID >= ? and ID <= ?";
		String[] parameters = { String.valueOf(startSN), String.valueOf(endSN) };
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, parameters);

		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; i < al.size(); i++) {
			Object[] objs = al.get(i);
			User user = new User(Integer.valueOf(objs[0].toString()),
					objs[1].toString(), objs[2].toString(), objs[3].toString(),
					objs[4].toString(), objs[5].toString(), objs[6].toString(),
					objs[7].toString(), objs[8].toString());
			users.add(user);
		}
		return users;
	}

	public ArrayList<User> getAllUsers() {
		String sql = "SELECT * FROM USER_TABLE ";
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, null);

		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; i < al.size(); i++) {
			Object[] objs = al.get(i);
			User user = new User(Integer.valueOf(objs[0].toString()),
					objs[1].toString(), objs[2].toString(), objs[3].toString(),
					objs[4].toString(), objs[5].toString(), objs[6].toString(),
					objs[7].toString(), objs[8].toString());
			users.add(user);
		}
		return users;
	}

	public ArrayList<User> getAllHavePhotoUsers() {
		String sql = "SELECT * FROM USER_TABLE WHERE HAVEPHOTO = '是'";
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, null);

		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; i < al.size(); i++) {
			Object[] objs = al.get(i);
			User user = new User(Integer.valueOf(objs[0].toString()),
					objs[1].toString(), objs[2].toString(), objs[3].toString(),
					objs[4].toString(), objs[5].toString(), objs[6].toString(),
					objs[7].toString(), objs[8].toString());
			users.add(user);
		}
		return users;
	}

	public int getNextId() {
		String sql = "SELECT LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME='USER_SEQ'";
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, null);
		if (al.size() > 0) {
			Object[] objs = al.get(0);
			int nextId = Integer.valueOf(objs[0].toString());
			return nextId;
		} else {
			return -1;
		}
	}

	public int getCount() {
		String sql = "SELECT COUNT(*)  FROM USER_TABLE";
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, null);
		int count = Integer.valueOf(al.get(0)[0].toString());

		return count;

	}

	public List<Object[]> getUsersByPage(Page page) {
		String sql = "SELECT ID,ACCOUNT,NAME,SEX,HAVEPHOTO,DEPARTMENT,TEL FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM USER_TABLE ORDER BY ID ) A WHERE ROWNUM <= ?)WHERE RN >= ?";
		String[] parameters = { String.valueOf(page.getEndNUm()),
				String.valueOf(page.getStartNum()) };
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, parameters);
		return al;
	}

	public boolean checkUserByPwd(User user) {
		boolean flag = false;
		String sql = "SELECT * FROM USER_TABLE WHERE ACCOUNT = ? AND PWD = ?";
		String[] parameters = { user.getName(), user.getPwd() };
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, parameters);

		if (al.size() > 0) {
			Object[] objs = al.get(0);
			user.setId(Integer.valueOf(objs[0].toString()));
			user.setAccount(objs[1].toString());
			user.setPwd(objs[2].toString());
			user.setName(objs[3].toString());
			user.setSex(objs[4].toString());
			user.setHavePhoto(objs[5].toString());
			user.setDepartment(objs[6].toString());
			user.setTel(objs[7].toString());
			user.setPicsPath(objs[8].toString());

			flag = true;
		}

		return flag;
	}

	public List<Object[]> getUsersByAccount(String account) {
		String sql = "SELECT * FROM USER_TABLE WHERE ACCOUNT LIKE ? ORDER BY ID";
		String[] parameters = new String[1];
		if ("%".equals(account)) {
			parameters[0] = "%[%]%";
		} else {
			parameters[0] = "%" + account + "%";
		}

		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, parameters);
		return al;
	}

	public User getUniqueUserByAccount(String account) {
		String sql = "SELECT * FROM USER_TABLE WHERE ACCOUNT = ?";
		String[] parameters = {account};
		ArrayList<Object[]> al = sqlHelper.queryExecute(sql, parameters);
		if (al.size() > 0) {
			Object[] objs = al.get(0);
			User user = new User(Integer.valueOf(objs[0].toString()),
					objs[1].toString(), objs[2].toString(), objs[3].toString(),
					objs[4].toString(), objs[5].toString(), objs[6].toString(),
					objs[7].toString(), objs[8].toString());
			return user;
		}else{
			return null;	
		}
	}

	public boolean deleteAllUsers() {
		String sql = "DELETE FROM USER_TABLE WHERE ID > 0";
		return sqlHelper.executeUpdate(sql, null);
	}

}
