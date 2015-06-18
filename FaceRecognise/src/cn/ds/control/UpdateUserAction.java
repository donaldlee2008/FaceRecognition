/**
 * Project             :FaceRecognise project
 * Comments            :增加用户控制类，
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import cn.ds.domain.User;
import cn.ds.model.UpdateUserModel;
import cn.ds.service.RecogniseService;
import cn.ds.view.UpdateUserView;

public class UpdateUserAction implements ActionListener {

	// 修改用户信息解密那
	private UpdateUserView updateUserView;
	// 修改用户信息模型
	private UpdateUserModel updateUserModel;
	// Jtable模型
	private DefaultTableModel model;
	// 行号
	private int row;
	// 修改用户
	private User user;

	/**
	 * Description :构造函数
	 * 
	 * @param id
	 *            :修改用户ID
	 * @param row
	 *            ：该用户所在行号
	 * @param model
	 *            ：Jtable 模型
	 * @param mode
	 *            : 修改模式
	 * @return UpdateUserAction
	 */
	public UpdateUserAction(int id, int row, DefaultTableModel model, int mode) {
		updateUserModel = new UpdateUserModel();

		// 根据ID号获的用户
		user = updateUserModel.getUserById(id);

		this.model = model;
		this.row = row;

		updateUserView = new UpdateUserView(this, user);

		if (mode != 0) {// 普通用户不能修改照片
			updateUserView.getButtonEntryPhotos().setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new UpdateResponseThread(e.getActionCommand()).start();
	}

	/**
	 * Description :内部类，处理按钮响应
	 */
	class UpdateResponseThread extends Thread {
		private String actionCommand;

		public UpdateResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {

			if (actionCommand.equals("buttonEntryPhotos")) {// 录入照片按钮响应
				updateUserView.getLabelShowResult().setText("正在录入照片！");
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();

				// 设置为录入照片模式
				updateUserModel.entryPhotos(result);

				try {
					result.take();
					user.setHavePhoto("是");

					updateUserView.getLblelEntryPhoto().setText("是");
					updateUserView.getLabelShowResult().setText("结束！");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else if (actionCommand.equals("buttonSure")) {// 确定按钮响应
				user.setAccount(updateUserView.getTextFieldAccount().getText());
				user.setPwd(new String(updateUserView.getPasswordField()
						.getPassword()));
				user.setName(updateUserView.getTextFieldName().getText());
				if (updateUserView.getRadioButton().isSelected()) {
					user.setSex("男");
				} else {
					user.setSex("女");
				}

				user.setDepartment(updateUserView.getTextFieldDepartment()
						.getText());
				user.setTel(updateUserView.getTextFieldTel().getText());
				user.setPicsPath("pics/");

				updateUserModel.updateUser(user);

				updateUserView.getLabelShowResult().setText("成功!");

				if (model != null) {// 刷新列表
					if (SwingUtilities.isEventDispatchThread()) {

					} else {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								updateUserModel.refreshUserTableUpdate(model,
										row, user);
							}
						});
					}

				}
			} else if (actionCommand.equals("buttonReset")) {// 重置按钮响应
				reset();
			} else if (actionCommand.equals("buttonQuit")) {// 退出按钮响应
				if (RecogniseService.getInstance() != null
						&& RecogniseService.getInstance().getState() != State.WAITING) {
					RecogniseService.getInstance(null).waitMode();
				}
				updateUserView.dispose();
			}
		}

	}

	public void reset() {
		updateUserView.getTextFieldAccount().setText("");
		updateUserView.getPasswordField().setText("");
		updateUserView.getTextFieldName().setText("");
		updateUserView.getTextFieldDepartment().setText("");
		updateUserView.getTextFieldTel().setText("");
	}

}
