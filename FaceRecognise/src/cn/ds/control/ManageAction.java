/**
 * Project             :FaceRecognise project
 * Comments            :管理控制类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 * 2 | 2013-8-11 | 添加双击打开视频识别结果文件夹;添加删除所有用户按钮| jxm
 * 3 | 2013-8-13 | 修改“删除所有用户”中关于界面更新的bug| jxm
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import cn.ds.domain.VideoTask;
import cn.ds.model.ManageModel;
import cn.ds.service.RecogniseService;
import cn.ds.service.ScheduleManage;
import cn.ds.service.VideoTasksManageThread;
import cn.ds.utils.Page;
import cn.ds.view.ManageView;

public class ManageAction implements ActionListener, ListSelectionListener,
		ChangeListener, MouseListener {
	// 管理员界面
	private ManageView manageView;
	// 管理员模型
	private ManageModel manageModel;
	// 页面，设置为第一页，每页最多10条记录
	private Page page = new Page(1, 10);
	// 被选择的行数组
	private int[] selectedRows;
	// 视频识别任务集合
	private Map<Integer, VideoTask> videoTasks = new HashMap<Integer, VideoTask>();
	// 正在运行视频识别任务集合
	private Map<Integer, VideoTask> runningVideoTasks = new HashMap<Integer, VideoTask>();

	// 最大处理任务书
	private int maxVideoTaskNum = 2;
	// 任务序号
	private int videoTaskIndex = 0;
	// 视频识别任务管理线程
	private VideoTasksManageThread videoTasksManageThread = null;
	// 进度（显示）管理线程
	private ScheduleManage scheduleManage = null;

	private String strSearch;

	private FileNameExtensionFilter filterPhoto = new FileNameExtensionFilter(
			"图片", "bmp", "jpg");
	private JFileChooser fileChooser = new JFileChooser();
	private String batchPhotoProcessSavePath;
	private BlockingQueue<Boolean> batchRecogniseResult = new LinkedBlockingQueue<Boolean>();
	private List<String> photoPathList = new ArrayList<String>();
	private List<String> sourcePhotoList = new ArrayList<String>();
	private BlockingQueue<Boolean> loadViewResult;

	/**
	 * Description :构造函数
	 * 
	 * @param mode
	 *            : 登陆模式
	 * @return ManageAction
	 */
	public ManageAction(int mode, BlockingQueue<Boolean> loadViewResult) {

		this.loadViewResult = loadViewResult;

		manageModel = new ManageModel();

		if (mode == 0) {// 管理模式登陆
			manageModel.updatePage(page);
			Object[][] users = manageModel.getUserByPage(page);
			manageView = new ManageView(this, users, page);
		} else {// 非管理模式登陆
			manageView = new ManageView(this, null, page);
			// 管理模式下所有的按钮都不可用
			disableAllButton();
		}

		// 重写manageView窗口的关闭方法
		manageView.addWindowListener(new WindowAdapter() {

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
			this.loadViewResult.put(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Description :管理界面的按钮响应函数
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new ManageResponseThread(e.getActionCommand()).start();

	}

	/**
	 * Description :Jtable事件响应
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// 得到选择的行数组
		selectedRows = manageView.getTable().getSelectedRows();

		// 激活删除和修改按钮
		manageView.getButtonDelete().setEnabled(true);
		manageView.getButtonUpdate().setEnabled(true);

	}

	/**
	 * Description :内部类，处理按钮响应
	 */
	class ManageResponseThread extends Thread {
		private String actionCommand;

		public ManageResponseThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {

			if (actionCommand.equals("buttonSeacher")) {// 查询按钮响应

				strSearch = manageView.getTextFieldSearch().getText();

				// 设置为第一页
				page.setPageNow(1);

				if ("".equals(strSearch)) {// 查询字段为空，则为全部查询（第一页）

					// //设置为第一页
					// page.setPageNow(1);
					if (SwingUtilities.isEventDispatchThread()) {
						// 更新Jtable
						manageModel.refreshUserTable(
								(DefaultTableModel) manageView.getTable()
										.getModel(), page);

					} else {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								// 更新Jtable
								manageModel.refreshUserTable(
										(DefaultTableModel) manageView
												.getTable().getModel(), page);
								// 更新其他控件
								manageView.refreshUI();

							}
						});
					}

				} else {// 查询字段不为空

					if (SwingUtilities.isEventDispatchThread()) {
						// 显示全部查询字段结果，不设分页
						manageModel.refreshUserTable(
								(DefaultTableModel) manageView.getTable()
										.getModel(), manageModel
										.getUsersByAccount(strSearch));

					} else {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								// 显示全部查询字段结果，不设分页
								manageModel.refreshUserTable(
										(DefaultTableModel) manageView
												.getTable().getModel(),
										manageModel
												.getUsersByAccount(strSearch));
								// 取消分页信息
								manageView.getButtonNextPage()
										.setEnabled(false);
								manageView.getButtonPreviousPage().setEnabled(
										false);

								manageView.getLabelPage().setText("");

							}

						});
					}

				}
			} else if (actionCommand.equals("buttonTrain")) {// 训练图片按钮响应
				manageView.getButtonTrain().setEnabled(false);
				
				BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();
				manageModel.train(result);

				try {
					boolean r = result.take();
					if (r) {// 返回true，成功更新人脸库，弹出提示对话框
						JOptionPane.showMessageDialog(manageView, "人脸库更新完毕！");
					} else {// 返回false，更新人脸库失败，弹出提示对话框
						JOptionPane.showMessageDialog(manageView,
								"至少需要两个人照片信息才能更新人脸库");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				manageView.getButtonTrain().setEnabled(true);
			} else if (actionCommand.equals("buttonAdd")) {// 添加用户按钮响应

				// 添加用户
				new AddUserAction(manageView, (DefaultTableModel) manageView
						.getTable().getModel(), page);
			} else if (actionCommand.equals("buttonUpdate")) {// 修改用户信息按钮响应
				if (selectedRows != null) {

					// 得到ID号，只取一个
					int id = Integer.valueOf(manageView.getTable()
							.getValueAt(selectedRows[0], 0).toString());
					// 修改用户
					new UpdateUserAction(id, selectedRows[0],
							(DefaultTableModel) manageView.getTable()
									.getModel(), 0);

					manageView.getButtonUpdate().setEnabled(false);
				}

			} else if (actionCommand.equals("buttonDelete")) {// 删除按钮响应

				if (selectedRows != null) {// 删除
					for (int i = 0; i < selectedRows.length; i++) {
						int sn = Integer.valueOf(manageView.getTable()
								.getValueAt(selectedRows[i], 0).toString());
						if (sn != 0) {
							manageModel.deleteUserById(sn);
						}

					}
				}
				manageModel.updatePage(page);

				if (page.getNowPageSize() == 0 && page.getPageNow() > 1) {
					page.setPageNow(page.getPageNow() - 1);
				}

				if (SwingUtilities.isEventDispatchThread()) {
					manageModel.refreshUserTable((DefaultTableModel) manageView
							.getTable().getModel(), page);

				} else {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							manageModel.refreshUserTable(
									(DefaultTableModel) manageView.getTable()
											.getModel(), page);
							manageView.refreshUI();
							manageView.getButtonDelete().setEnabled(false);

						}
					});
				}

			} else if (actionCommand.equals("buttonNextPage")) {// 下一页按钮响应

				page.setPageNow(page.getPageNow() + 1);
				if (SwingUtilities.isEventDispatchThread()) {
					// 刷新列表
					manageModel.refreshUserTable((DefaultTableModel) manageView
							.getTable().getModel(), page);

				} else {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							manageModel.refreshUserTable(
									(DefaultTableModel) manageView.getTable()
											.getModel(), page);
							manageView.refreshUI();

						}
					});
				}
				manageView.refreshUI();
			} else if (actionCommand.equals("buttonPreviouPage")) {// 上一页按钮响应
				page.setPageNow(page.getPageNow() - 1);
				if (SwingUtilities.isEventDispatchThread()) {
					// 刷新列表
					manageModel.refreshUserTable((DefaultTableModel) manageView
							.getTable().getModel(), page);

				} else {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							manageModel.refreshUserTable(
									(DefaultTableModel) manageView.getTable()
											.getModel(), page);
							manageView.refreshUI();

						}
					});
				}

			} else if (actionCommand.equals("buttonGo")) {// 跳转页面
				String str = manageView.getTextFieldPageNum().getText();
				int pageNum;
				try {
					pageNum = Integer.parseInt(str);
					if (pageNum <= 0) {
						JOptionPane.showMessageDialog(manageView, "请输入非负数");
					} else if (pageNum > page.getPageCount()) {
						JOptionPane
								.showMessageDialog(manageView, "输入的数字大于总的页数");
					} else {
						// 设置当前页数
						page.setPageNow(pageNum);
						if (SwingUtilities.isEventDispatchThread()) {
							// 刷新列表
							manageModel.refreshUserTable(
									(DefaultTableModel) manageView.getTable()
											.getModel(), page);

						} else {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									manageModel.refreshUserTable(
											(DefaultTableModel) manageView
													.getTable().getModel(),
											page);
									manageView.refreshUI();

								}
							});
						}

					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(manageView, "请输入数字");
				}
			} else if (actionCommand.equals("buttonAddVideoProcessTask")) {// 添加视频人脸识别任务
				new AddVideoProcessTaskAction(videoTasksManageThread,
						videoTaskIndex);
				videoTaskIndex += 1;
			} else if (actionCommand.equals("buttonDeleteAllUsers")) {
				manageView.getButtonDeleteAllUsers().setEnabled(false);
				if (manageModel.deleteAllUsers()) {

					page.setRowCount(1);
					page.setPageNow(1);

					if (SwingUtilities.isEventDispatchThread()) {
						manageModel.refreshUserTable(
								(DefaultTableModel) manageView.getTable()
										.getModel(), page);

					} else {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								manageModel.refreshUserTable(
										(DefaultTableModel) manageView
												.getTable().getModel(), page);

							}
						});
					}
					manageView.refreshUI();
					manageView.getButtonDeleteAllUsers().setEnabled(true);

					JOptionPane.showMessageDialog(manageView, "用户已全部清除");
				}
			} else if (actionCommand.equals("buttonBatchReg")) {
				new BatchRegAction(manageView, (DefaultTableModel) manageView
						.getTable().getModel(), page);
			} else if (actionCommand.equals("buttonChooseSavePath")) {
				if (!"".equals(manageView.getTextFieldSavePath().getText())) {// 若照片路径不为空，则就打开其所对应文件选择窗口
					fileChooser.setCurrentDirectory(new File(manageView
							.getTextFieldSavePath().getText()));
				} else {// 若照片路径为空，则就打开默认的文件选择窗口
					fileChooser.setCurrentDirectory(new File("C:\\"));
				}

				// 设置文件选择过滤器
				fileChooser.setFileFilter(filterPhoto);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileChooser.showOpenDialog(manageView);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					batchPhotoProcessSavePath = fileChooser.getSelectedFile()
							.getAbsolutePath();
					if (!batchPhotoProcessSavePath.endsWith("\\")) {
						batchPhotoProcessSavePath += "\\";
					}
					manageView.getTextFieldSavePath().setText(
							batchPhotoProcessSavePath);
				}
			} else if (actionCommand.equals("buttonChooseToRecognisePhoto")) {
				if (!"".equals(manageView.getTextFieldPhotoToRecognise()
						.getText())) {// 若照片路径不为空，则就打开其所对应文件选择窗口
					fileChooser.setCurrentDirectory(new File(manageView
							.getTextFieldPhotoToRecognise().getText()));
				} else {// 若照片路径为空，则就打开默认的文件选择窗口
					fileChooser.setCurrentDirectory(new File("C:\\"));
				}
				//
				fileChooser.setMultiSelectionEnabled(true);
				// 设置文件选择过滤器
				fileChooser.setFileFilter(filterPhoto);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fileChooser.showOpenDialog(manageView);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] files = fileChooser.getSelectedFiles();
					if (files != null) {
						for (int i = 0; i < files.length; i++) {
							String dataPath = files[i].getAbsolutePath();
							photoPathList.add(dataPath);
						}
						manageView.getTextFieldPhotoToRecognise().setText(
								files[files.length - 1].getAbsolutePath());
					}

				}
			} else if (actionCommand.equals("buttonBatchFaceSure")) {
				if (batchPhotoProcessSavePath != null
						&& photoPathList.size() > 0) {
					float threshold = Float.parseFloat(manageView.getComboBox()
							.getSelectedItem().toString());

					if (manageView.getRadioButtonMaskFace().isSelected()) {
						manageModel.batchMaskedFaceRecognise(
								batchPhotoProcessSavePath, threshold,
								photoPathList, batchRecogniseResult);
					} else if (manageView.getRadioButtonUnmaskFace()
							.isSelected()) {

						manageModel.batchUnmaskedFaceRecognise(
								batchPhotoProcessSavePath, threshold,
								photoPathList, sourcePhotoList, 6, "_", "jpg",
								batchRecogniseResult);
					}
					try {
						batchRecogniseResult.take();
					} catch (Exception e) {
						e.printStackTrace();
					}
					photoPathList.clear();
					JOptionPane.showMessageDialog(manageView, "已识别完毕");
					manageView.getButtonSeeResult().setEnabled(true);
				}
			} else if (actionCommand.equals("buttonSeeResult")) {
				if (batchPhotoProcessSavePath != null) {
					try {
						Desktop.getDesktop().open(
								new File(batchPhotoProcessSavePath));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else if (actionCommand.equals("radioButtonUnmaskFace")) {
				if (manageView.getRadioButtonUnmaskFace().isSelected()) {
					manageView.getButtonChooseSourcePhoto().setEnabled(true);
				}
			} else if (actionCommand.equals("buttonChooseSourcePhoto")) {
				if (!"".equals(manageView.getTextFieldSourcePhoto().getText())) {// 若照片路径不为空，则就打开其所对应文件选择窗口
					fileChooser.setCurrentDirectory(new File(manageView
							.getTextFieldSourcePhoto().getText()));
				} else {// 若照片路径为空，则就打开默认的文件选择窗口
					fileChooser.setCurrentDirectory(new File("C:\\"));
				}
				//
				fileChooser.setMultiSelectionEnabled(true);
				// 设置文件选择过滤器
				fileChooser.setFileFilter(filterPhoto);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileChooser.showOpenDialog(manageView);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] files = fileChooser.getSelectedFiles();
					if (files != null) {
						for (int i = 0; i < files.length; i++) {
							String dataPath = files[i].getAbsolutePath();
							sourcePhotoList.add(dataPath);
						}
						manageView.getTextFieldSourcePhoto().setText(
								files[files.length - 1].getAbsolutePath());
					}

				}
			}else if(actionCommand.equals("radioButtonMaskFace")){
				if (manageView.getRadioButtonMaskFace().isSelected()) {
					manageView.getButtonChooseSourcePhoto().setEnabled(false);
				}	
			}
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
		if (manageView.getTabbedPane().getSelectedIndex() == 1) {// 切换到视频人脸识别任务

			if (scheduleManage == null) {

				new Thread(new Runnable() {

					@Override
					public void run() {

						// 开启进度（显示）管理线程
						scheduleManage = new ScheduleManage(videoTasks,
								(DefaultTableModel) manageView
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
	 * Description :设置按钮不能使用
	 * 
	 * @return void
	 */
	public void disableAllButton() {
		manageView.getButtonNextPage().setEnabled(false);
		manageView.getButtonPreviousPage().setEnabled(false);
		manageView.getButtonAdd().setEnabled(false);
		manageView.getButtonSeacher().setEnabled(false);
		manageView.getButtonTrain().setEnabled(false);
		manageView.getButtonUpdate().setEnabled(false);
		manageView.getBtnGo().setEnabled(false);
		manageView.getButtonDeleteAllUsers().setEnabled(false);
		manageView.getButtonBatchReg().setEnabled(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() == 2) {
				int rowNum = manageView.getTableVideoProcessTask().getModel()
						.getRowCount() - 1;
				int taskId = Integer.valueOf(manageView
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

	class OpenManageView extends Thread {

		@Override
		public void run() {
			manageView = new ManageView(ManageAction.this, null, page);
		}

	}
}
