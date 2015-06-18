/**
 * Project             :FaceRecognise project
 * Comments            :批量注册控制类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-8-14 | 创建 | jxm  
 */
package cn.ds.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import cn.ds.model.BatchRegModel;
import cn.ds.utils.Page;
import cn.ds.view.BatchRegView;
import cn.ds.view.ManageView;

public class BatchRegAction implements ActionListener {

	private BatchRegView batchRegView;
	private JFileChooser fileChooser = new JFileChooser();
	private FileNameExtensionFilter filterExcel = new FileNameExtensionFilter(
			"excel", "xls");
	private FileNameExtensionFilter filterPhoto = new FileNameExtensionFilter(
			"图片", "bmp", "jpg");
	private BatchRegModel batchRegModel;
	private List<String> excelDataPathList = new ArrayList<String>();
	private List<String> photoPathList = new ArrayList<String>();

	private BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();

	private ManageView manageView;
	private DefaultTableModel model;
	private Page page;

	private String delimiter = "_";
	private int PHOTONUM = 6;

	public BatchRegAction(ManageView manageView, DefaultTableModel model,
			Page page) {
		this.manageView = manageView;
		this.model = model;
		this.page = page;

		batchRegView = new BatchRegView(this);
		batchRegModel = new BatchRegModel(result);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if ("buttonExcelDataBatchReg".equals(command)) {
			if (!"".equals(batchRegView.getTextFieldExcelData().getText())) {// 若照片路径不为空，则就打开其所对应文件选择窗口
				fileChooser.setCurrentDirectory(new File(batchRegView
						.getTextFieldExcelData().getText()));
			} else {// 若照片路径为空，则就打开默认的文件选择窗口
				fileChooser.setCurrentDirectory(new File("C:\\"));
			}
			//
			fileChooser.setMultiSelectionEnabled(true);
			// 设置文件选择过滤器
			fileChooser.setFileFilter(filterExcel);
			int returnVal = fileChooser.showOpenDialog(batchRegView);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File[] files = fileChooser.getSelectedFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						String dataPath = files[i].getAbsolutePath();
						excelDataPathList.add(dataPath);
					}
					batchRegView.getTextFieldExcelData().setText(
							files[files.length - 1].getAbsolutePath());
				}

			}
		} else if ("buttonExcelDataSure".equals(command)) {
			manageView.getButtonBatchReg().setEnabled(false);
			batchRegView.dispose();

			batchRegModel.batchInsertUsersByExcel(excelDataPathList);
			excelDataPathList.clear();

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						result.take();
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (page.getNowPageSize() < page.getPageSize()) {

						batchRegModel.updatePage(page);

						if (SwingUtilities.isEventDispatchThread()) {
							batchRegModel.refreshUserTable(model, page);
						} else {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									batchRegModel.refreshUserTable(model, page);
								}
							});
						}

					} else {
						batchRegModel.updatePage(page);
					}
					manageView.refreshUI();
					manageView.getButtonBatchReg().setEnabled(true);
				}
			}).start();
		} else if ("buttonPhotoBatchReg".equals(command)) {
			if (!"".equals(batchRegView.getTextFieldPhotoPath().getText())) {// 若照片路径不为空，则就打开其所对应文件选择窗口
				fileChooser.setCurrentDirectory(new File(batchRegView
						.getTextFieldPhotoPath().getText()));
			} else {// 若照片路径为空，则就打开默认的文件选择窗口
				fileChooser.setCurrentDirectory(new File("C:\\"));
			}
			//
			fileChooser.setMultiSelectionEnabled(true);
			// 设置文件选择过滤器
			fileChooser.setFileFilter(filterPhoto);
			int returnVal = fileChooser.showOpenDialog(batchRegView);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File[] files = fileChooser.getSelectedFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						String dataPath = files[i].getAbsolutePath();
						photoPathList.add(dataPath);
					}
					batchRegView.getTextFieldPhotoPath().setText(
							files[files.length - 1].getAbsolutePath());
				}

			}
		} else if ("buttonPhotoPathSure".equals(command)) {
			
			 manageView.getButtonBatchReg().setEnabled(false);
			 batchRegView.dispose();

			batchRegModel.batchInsertUsersByPhotos(photoPathList, delimiter,
					PHOTONUM);
			photoPathList.clear();

			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						result.take();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// (page.getNowPageSize() < page.getPageSize()) {

						batchRegModel.updatePage(page);

						if (SwingUtilities.isEventDispatchThread()) {
							batchRegModel.refreshUserTable(model, page);
						} else {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									batchRegModel.refreshUserTable(model, page);
								}
							});
						}

//					} else {
//						batchRegModel.updatePage(page);
//					}
					manageView.refreshUI();
					manageView.getButtonBatchReg().setEnabled(true);
				}
			}).start();

		} else if ("buttonCancle".equals(command)) {
			batchRegView.dispose();
		}

	}

	// 刷新列表
	public void refreshTable(List<Object[]> data, DefaultTableModel model) {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		for (int i = 0; i < data.size(); i++) {
			model.addRow(data.get(i));
		}
	}
}
