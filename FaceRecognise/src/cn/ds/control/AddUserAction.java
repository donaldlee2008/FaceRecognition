/**
 * Project             :FaceRecognise project
 * Comments            :增加用户控制类，
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 * 2 | 2013-4-30 |增加“是否有照片”这一项的判断 |jxm  
 */
package cn.ds.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import cn.ds.domain.User;
import cn.ds.model.AddUserModel;
import cn.ds.service.RecogniseService;
import cn.ds.utils.Page;
import cn.ds.view.AddUserView;
import cn.ds.view.ManageView;

public class AddUserAction implements ActionListener {
	
	//添加用户界面
	private AddUserView addUserView;
	//添加用户模型
	private AddUserModel addUserModel;
	//JTable模型
	private DefaultTableModel model;
	//
	private ManageView manageView;
	//显示页
	private Page page;
	//是否已录入照片标识
	private boolean isEntryPhotos = false;

	/**
	 * Description :构造函数
	 * 
	 * @param model
	 *            :Jtable模型
	 * @param page
	 *            ：当前所处的页
	 * @return AddUserAction
	 */
	public AddUserAction(ManageView manageView,DefaultTableModel model, Page page) {
		addUserView = new AddUserView(this);

		this.manageView = manageView;
		this.model = model;
		this.page = page;

		addUserModel = new AddUserModel();
	}

	/**
	 * Description :增加用户界面的按钮响应函数
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		new AddUserResponseThread(e.getActionCommand()).start();
	}

	/**
	 * Description :将增加用户界面的填写信息和提示信息清空
	 * 
	 * @return void
	 */
	public void reset() {
		addUserView.getTextFieldAccount().setText("");
		addUserView.getPasswordField().setText("");
		addUserView.getTextFieldName().setText("");
		addUserView.getTextFieldDepartment().setText("");
		addUserView.getTextFieldTel().setText("");
		addUserView.getLabelShowResult().setText("");
	}

	/**
	 * Description :内部类，处理按钮响应
	 */
	class AddUserResponseThread extends Thread {
		private String actionCommand;

		public AddUserResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {
			if (actionCommand.equals("buttonEntryPhotos")) {// 录入照片按钮响应

				// 显示提示信息
				addUserView.getLabelShowResult().setText("正在录入照片！");

				// 调用RecogniseService录入照片，result用来存放结果
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				addUserModel.entryPhotos(result);

				try {
					// 取出结果
					result.take();

					// 含有照片标识置为true
					isEntryPhotos = true;

					// 显示提示信息
					addUserView.getLabelHavePhoto().setText("是");
					addUserView.getLabelShowResult().setText("结束！");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else if (actionCommand.equals("buttonSure")) {// 确定按钮响应

				/*
				 * 读取填入的用户信息
				 */
				User user = new User();
				user.setId(addUserModel.getAddUserId());
				
				if("".equals(addUserView.getTextFieldAccount().getText())){
					JOptionPane.showMessageDialog(addUserView, "用户名不能为空");	
				}else{
					user.setAccount(addUserView.getTextFieldAccount().getText());
					
					String pwd = new String(addUserView.getPasswordField()
							.getPassword());
					if("".equals(pwd)){
						JOptionPane.showMessageDialog(addUserView, "密码不能为空");	
					}else{
						user.setPwd(pwd);
						
						if("".equals(addUserView.getTextFieldName().getText())){
							user.setName("未填写");	
						}else{
							user.setName(addUserView.getTextFieldName().getText());	
						}
						
						if (addUserView.getRadioButton().isSelected()) {
							user.setSex("男");
						} else {
							user.setSex("女");
						}

						if (isEntryPhotos) {
							user.setHavePhoto("是");
							isEntryPhotos = false;
						} else {
							user.setHavePhoto("否");
						}
						
						if("".equals(addUserView.getTextFieldDepartment().getText())){
							user.setDepartment("未填写");	
						}else{
							user.setDepartment(addUserView.getTextFieldDepartment()
									.getText());
						}
						
						if("".equals(addUserView.getTextFieldTel().getText())){
							user.setTel("未填写");	
						}else{
							user.setTel(addUserView.getTextFieldTel().getText());
						}
						
						user.setPicsPath("pics/");

						// 添加用户信息，包括数据存储（存储身份信息）和文件存储（存储照片信息）
						addUserModel.addUser(user);

						// 设置提示信息
						addUserView.getLabelShowResult().setText("成功！");
					}
				}
				
				
				
				
			} else if (actionCommand.equals("buttonReset")) {// 重置按钮响应
				// 重置
				reset();
			} else if (actionCommand.equals("buttonQuit")) {// 退出按钮响应
				if (RecogniseService.getInstance() != null
						&& RecogniseService.getInstance().getState() != State.WAITING) {
					// 若开启了RecogniseService服务并且其状态不是等待状态，那么就将其设置为等待状态
					RecogniseService.getInstance(null).waitMode();
				}

				// 添加用户窗口消失
				addUserView.dispose();

				
				//page.setPageNow(1);
				if (page.getNowPageSize() < page.getPageSize()) {
					
					addUserModel.updatePage(page);
					
					if(SwingUtilities.isEventDispatchThread()){
						addUserModel.refreshUserTable(model, page);	
					}else{
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								addUserModel.refreshUserTable(model, page);
							}
						});
					}
					
			}else{
				addUserModel.updatePage(page);	
			}
				manageView.refreshUI();
				
			}

		}
	}
}
