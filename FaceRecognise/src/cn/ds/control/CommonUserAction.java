/**
 * Project             :FaceRecognise project
 * Comments            :普通用户管理类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-5-20 | 创建 | jxm
 * 2 | 2013-8-11 | 添加双击打开视频识别结果文件夹| jxm 
 */
package cn.ds.control;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import cn.ds.domain.User;
import cn.ds.domain.VideoTask;
import cn.ds.model.CommonUserModel;
import cn.ds.service.RecogniseService;
import cn.ds.service.ScheduleManage;
import cn.ds.service.VideoTasksManageThread;
import cn.ds.view.CommonUserView;

public class CommonUserAction implements ActionListener, ChangeListener,
		MouseListener {

	// 普通用户界面
	private CommonUserView commonUserView;
	// 进度（显示）管理
	private ScheduleManage scheduleManage = null;
	// 视频处理任务集合
	private Map<Integer, VideoTask> videoTasks = new HashMap<Integer, VideoTask>();
	// 正在处理的任务集合
	private Map<Integer, VideoTask> runningVideoTasks = new HashMap<Integer, VideoTask>();
	// 视频人脸识别任务管理
	private VideoTasksManageThread videoTasksManageThread = null;
	// 最大视频人脸识别任务数
	private int maxVideoTaskNum = 2;
	// 视频人脸识别任务序号
	private int videoTaskIndex = 0;
	// 当前用户
	private User user;
	// 普通用户管理模型
	private CommonUserModel commonUserModel;

	/**
	 * Description :构造函数
	 * 
	 * @param id
	 *            :用户id
	 * @param mode
	 *            ：登陆模式
	 * @return CommonUserAction
	 */
	public CommonUserAction(int id, int mode, BlockingQueue<Boolean> result) {

		commonUserModel = new CommonUserModel();

		// 根据用户ID得到用户
		user = commonUserModel.getUserById(id);

		if (mode == 0) {// "管理模式"登陆，可以更改用户信息
			commonUserView = new CommonUserView(this, user);
		} else {// 普通模式，只可以使用视频人脸识别功能
			commonUserView = new CommonUserView(this, null);
		}
		commonUserView.setTitle("普通用户:" + user.getName());

		// 重写commonUserView窗口的关闭方法
		commonUserView.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (RecogniseService.getInstance() != null) {

					// 结束RecogniseService线程
					RecogniseService.getInstance().setFlag(false);

					// 释放摄像头capture资源
					RecogniseService.getInstance().releaseCapture();
				}
				System.exit(0);
			}
		});

		try {
			result.put(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description :响应TabbedPane标签改变
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (commonUserView.getTabbedPane().getSelectedIndex() == 1) {// 切换到视频人脸识别任务界面
			if (scheduleManage == null) {
				new Thread(new Runnable() {

					@Override
					public void run() {

						// 开启进度（显示）管理线程
						scheduleManage = new ScheduleManage(videoTasks,
								(DefaultTableModel) commonUserView
										.getTableVideoProcessTask().getModel());
						scheduleManage.start();

						// 开启任务管理线程
						videoTasksManageThread = new VideoTasksManageThread(
								videoTasks, runningVideoTasks, scheduleManage,
								maxVideoTaskNum);
						videoTasksManageThread.start();

					}
				}).start();
			}

		}
	}

	/**
	 * Description :普通用户管理界面的按钮响应函数
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		new CommonUserResponseThread(e.getActionCommand()).start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() == 2) {
				int rowNum = commonUserView.getTableVideoProcessTask()
						.getModel().getRowCount() - 1;
				int taskId = Integer.valueOf(commonUserView
						.getTableVideoProcessTask().getValueAt(rowNum, 0)
						.toString());
				String path = videoTasks.get(taskId).getSavePath();
				try {
					Desktop.getDesktop().open(new File(path));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Description :内部类，处理按钮响应
	 */
	class CommonUserResponseThread extends Thread {
		private String actionCommand;

		public CommonUserResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {
			if (actionCommand.equals("buttonAddVideoProcessTask")) {// 添加视频处理任务按钮响应
				new AddVideoProcessTaskAction(videoTasksManageThread,
						videoTaskIndex);
				videoTaskIndex += 1;
			} else if (actionCommand.equals("buttonUpdate")) {// 修改用户信息按钮响应
				int id = user.getId();
				new UpdateUserAction(id, 0, null, 1);
			}

		}
	}

}
