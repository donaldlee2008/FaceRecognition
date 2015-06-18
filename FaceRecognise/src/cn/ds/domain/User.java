/**
 * Project             :FaceRecognise project
 * Comments            :用户信息类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.domain;

public class User {
	private int id;
	private String account;
	private String pwd;
	private String name;
	private String sex;
	private String havePhoto;
	private String department;
	private String tel;
	private String picsPath;
	
	public User(){
		
	}
	public User(int id, String account, String pwd, String name, String sex,
			String department, String tel, String picsPath) {
		super();
		this.id = id;
		this.account = account;
		this.pwd = pwd;
		this.name = name;
		this.sex = sex;
		this.department = department;
		this.tel = tel;
		this.picsPath = picsPath;
	}
	public User(int id, String account, String pwd, String name, String sex,
			String havePhoto, String department, String tel, String picsPath) {
		super();
		this.id = id;
		this.account = account;
		this.pwd = pwd;
		this.name = name;
		this.sex = sex;
		this.havePhoto = havePhoto;
		this.department = department;
		this.tel = tel;
		this.picsPath = picsPath;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPicsPath() {
		return picsPath;
	}
	public void setPicsPath(String picsPath) {
		this.picsPath = picsPath;
	}
	public synchronized String getHavePhoto() {
		return havePhoto;
	}
	public synchronized void setHavePhoto(String havePhoto) {
		this.havePhoto = havePhoto;
	}

	

}
