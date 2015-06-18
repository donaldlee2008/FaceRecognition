/**
 * Project             :FaceRecognise project
 * Comments            :添加视频人脸识别任务控制类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-22 | 创建 | jxm
 * 2 | 2013-8-11 | 修改图片，视频选择为可以一次选择多个文件 | jxm 
 */
package cn.ds.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import cn.ds.domain.VideoTask;
import cn.ds.service.VideoTasksManageThread;
import cn.ds.view.AddVideoProcessTaskView;

public class AddVideoProcessTaskAction implements ActionListener,
		ListSelectionListener, ChangeListener {
	// 添加视频人脸识别任务
	private AddVideoProcessTaskView addVideoProcessView;
	// 文件选择
	private JFileChooser fileChooser = new JFileChooser();
	// JTable要显示的添加照片数据
	private Vector<Object[]> photoData = new Vector<Object[]>();
	// JTable要显示的添加视频数据
	private Vector<Object[]> videoData = new Vector<Object[]>();
	// 图片格式过滤器
	private FileNameExtensionFilter filterPhoto = new FileNameExtensionFilter(
			"图片", "jpg", "bmp");
	// 视频格式过滤器
	private FileNameExtensionFilter filterVideo = new FileNameExtensionFilter(
			"视频", "avi", "wmv");
	// 视频人脸识别处理结果存储路径，默认路径为C:\
	private String savePath = "";
	// 需要处理的照片路径集合
	private List<String> photo = new ArrayList<String>();
	// 需要处理的视频路径集合
	private List<String> video = new ArrayList<String>();
	// 被选择的照片行序号的数组
	private int[] photoSelectedRows;
	// 被选择的视频行序号的数组
	private int[] videoSelectedRows;
	// 帧率倍数，默认值是1.0
	private float magnification;
	// 识别阈值，默认值为0.7
	private float threshold;
	// 视频人脸识别任务管理线程
	private VideoTasksManageThread videoTasksManageThread;
	// 视频人脸识别序号
	private int index;
	// Jtable显示照片路径，序号
	private int photoListIndex = 1;
	// Jtable显示视频路径，序号
	private int videoListIndex = 1;

	/**
	 * Description :构造函数
	 * 
	 * @param VideoTasksManageThread
	 *            :视频人脸识别任务管理线程
	 * @param index
	 *            ：视频人脸识别序号
	 * @return AddVideoProcessTaskAction
	 */
	public AddVideoProcessTaskAction(
			VideoTasksManageThread videoTasksManageThread, int index) {

		addVideoProcessView = new AddVideoProcessTaskView(this);

		this.videoTasksManageThread = videoTasksManageThread;
		this.index = index;
	}

	/**
	 * Description :添加视频人脸识别界面的按钮响应函数
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		new AddVideoProcessThread(e.getActionCommand()).start();
	}

	/**
	 * Description :内部类，处理按响应
	 */
	class AddVideoProcessThread extends Thread {
		private String actionCommand;

		public AddVideoProcessThread(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void run() {
			if (actionCommand.equals("buttonAddPhoto")) {// 添加照片按钮响应
				if (!"".equals(addVideoProcessView.getTextFieldPhotoPath()
						.getText())) {// 若照片路径不为空，则就打开其所对应文件选择窗口
					fileChooser.setCurrentDirectory(new File(
							addVideoProcessView.getTextFieldPhotoPath()
									.getText()));
				} else {// 若照片路径为空，则就打开默认的文件选择窗口
					fileChooser.setCurrentDirectory(new File("C:\\"));
				}

				//
				fileChooser.setMultiSelectionEnabled(true);
				// 设置文件选择过滤器
				fileChooser.setFileFilter(filterPhoto);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fileChooser.showOpenDialog(addVideoProcessView);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] files = fileChooser.getSelectedFiles();

					if (files != null) {
						for (int i = 0; i < files.length; i++) {
							String photoPath = files[i].getAbsolutePath();

							Object[] data = new Object[3];
							data[0] = photoListIndex;
							photoListIndex += 1;
							data[1] = photoPath.substring(photoPath
									.lastIndexOf("\\") + 1);
							data[2] = photoPath;

							photo.add(photoPath);
System.out.println(photoPath);
							photoData.add(data);
						}
						addVideoProcessView.getTextFieldPhotoPath().setText(
								files[files.length - 1].getAbsolutePath());
					}
				}

				// 刷新照片显示列表
				refreshTable(photoData, (DefaultTableModel) addVideoProcessView
						.getTablePhoto().getModel());
			} else if (actionCommand.equals("buttonAddVideo")) {// 添加视频按钮响应
				if (!"".equals(addVideoProcessView.getTextFieldVideoPath()
						.getText())) {
					fileChooser.setCurrentDirectory(new File(
							addVideoProcessView.getTextFieldVideoPath()
									.getText()));
				} else {
					fileChooser.setCurrentDirectory(new File("C:\\"));
				}
				fileChooser.setFileFilter(filterVideo);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(true);
				int reVal = fileChooser.showOpenDialog(addVideoProcessView);
				if (reVal == JFileChooser.APPROVE_OPTION) {

					File[] files = fileChooser.getSelectedFiles();
					if (files != null) {
						for (int i = 0; i < files.length; i++) {
							String videoPath = files[i].getAbsolutePath();

							Object[] data = new Object[3];
							data[0] = videoListIndex;
							videoListIndex += 1;
							data[1] = videoPath.substring(videoPath
									.lastIndexOf("\\") + 1);
							data[2] = videoPath;

							video.add(videoPath);
System.out.println(videoPath);
							videoData.add(data);
						}
						addVideoProcessView.getTextFieldVideoPath().setText(
								files[files.length - 1].getAbsolutePath());
					}

				}
				refreshTable(videoData, (DefaultTableModel) addVideoProcessView
						.getTableVideo().getModel());
			} else if (actionCommand.equals("buttonSave")) {// 设置处理结果存储路径
				if (!"".equals(addVideoProcessView.getTextFieldSave().getText())) {
					fileChooser.setCurrentDirectory(new File(
							addVideoProcessView.getTextFieldSave().getText()));
				} else {
					fileChooser.setCurrentDirectory(new File("C:\\"));
				}
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int re = fileChooser.showOpenDialog(addVideoProcessView);
				if (re == JFileChooser.APPROVE_OPTION) {
					
					savePath = fileChooser.getSelectedFile().getAbsolutePath();
					if (!savePath.endsWith("\\")) {
						savePath += "\\";
					}
					addVideoProcessView.getTextFieldSave().setText(savePath);
				}
			} else if (actionCommand.equals("buttonSure")) {// 确认按钮响应

				if (addVideoProcessView.getTablePhoto().getModel()
						.getRowCount() == 0) {
					JOptionPane.showMessageDialog(addVideoProcessView, "未添加照片");
				} else if (addVideoProcessView.getTableVideo().getModel()
						.getRowCount() == 0) {
					JOptionPane.showMessageDialog(addVideoProcessView, "未添加视频");
				} else if (savePath.equals("")) {
					JOptionPane
							.showMessageDialog(addVideoProcessView, "保存路径为空");
				} else {
					addVideoProcessView.dispose();

					// 添加新的视频人脸识别任务
					VideoTask videoTask = new VideoTask(index, photo, video,
							savePath, magnification, threshold);
					videoTasksManageThread.put(index, videoTask);

					// 如果视频人脸识别任务管理线程在等待状态就将其唤醒
					if (videoTasksManageThread != null) {
						if (videoTasksManageThread.getState() == State.WAITING) {
							videoTasksManageThread.Resume();
						}
					}
				}

			} else if (actionCommand.equals("buttonDelete")) {// 删除按钮响应
				if (photoSelectedRows != null) {
					for (int i = 0; i < photoSelectedRows.length; i++) {
						int id = Integer
								.valueOf(addVideoProcessView.getTablePhoto()
										.getValueAt(photoSelectedRows[i], 0)
										.toString()) - 1;
						photoData.remove(id);
						photo.remove(id);
					}
					// 刷新列表
					refreshTable(photoData,
							(DefaultTableModel) addVideoProcessView
									.getTablePhoto().getModel());
				}

				if (videoSelectedRows != null) {

					for (int i = 0; i < videoSelectedRows.length; i++) {
						int id = Integer
								.valueOf(addVideoProcessView.getTableVideo()
										.getValueAt(videoSelectedRows[i], 0)
										.toString()) - 1;
						videoData.remove(id);
						video.remove(id);
					}
					refreshTable(videoData,
							(DefaultTableModel) addVideoProcessView
									.getTableVideo().getModel());
				}

			}
		}

		// 刷新列表
		public void refreshTable(Vector<Object[]> data, DefaultTableModel model) {
			while (model.getRowCount() > 0) {
				model.removeRow(0);
			}

			for (int i = 0; i < data.size(); i++) {
				model.addRow(data.get(i));
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		photoSelectedRows = addVideoProcessView.getTablePhoto()
				.getSelectedRows();
		videoSelectedRows = addVideoProcessView.getTableVideo()
				.getSelectedRows();

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider jSlider = (JSlider) e.getSource();
		if (jSlider.equals(addVideoProcessView.getSliderFPS())) {
			if (jSlider.getValueIsAdjusting()) {
				addVideoProcessView.getLblfps().setText(
						jSlider.getValue() / (float) jSlider.getMaximum()
								+ "*FPS");
			} else {
				magnification = jSlider.getValue()
						/ (float) jSlider.getMaximum();
				addVideoProcessView.getLblfps().setText(magnification + "*FPS");
			}
		} else if (jSlider.equals(addVideoProcessView.getSliderThreshold())) {
			if (jSlider.getValueIsAdjusting()) {
				addVideoProcessView.getLabelThreshold().setText(
						jSlider.getValue() / (float) jSlider.getMaximum() + "");
			} else {
				threshold = jSlider.getValue() / (float) jSlider.getMaximum();
				addVideoProcessView.getLabelThreshold().setText(
						String.valueOf(threshold));
			}
		}

	}

}
