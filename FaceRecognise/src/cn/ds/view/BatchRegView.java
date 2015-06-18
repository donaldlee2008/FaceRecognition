package cn.ds.view;

import java.awt.Toolkit;

import javax.swing.JFrame;

import cn.ds.control.BatchRegAction;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

public class BatchRegView extends JFrame {
	private JTextField textFieldExcelData;
	private JButton buttonExcelDataBatchReg;
	private JTextField textFieldPhotoPath;
	public BatchRegView(BatchRegAction batchRegAction) {
		setTitle("\u6279\u91CF\u6DFB\u52A0\u7528\u6237");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(25, Short.MAX_VALUE))
		);
		
		textFieldExcelData = new JTextField();
		textFieldExcelData.setColumns(10);
		
		buttonExcelDataBatchReg = new JButton("\u6DFB\u52A0\u6587\u4EF6");
		buttonExcelDataBatchReg.addActionListener(batchRegAction);
		buttonExcelDataBatchReg.setActionCommand("buttonExcelDataBatchReg");
		
		JButton buttonExcelDataSure = new JButton("\u786E\u5B9A");
		buttonExcelDataSure.addActionListener(batchRegAction);
		buttonExcelDataSure.setActionCommand("buttonExcelDataSure");
		
		JButton buttonCancle = new JButton("\u53D6\u6D88");
		buttonCancle.addActionListener(batchRegAction);
		buttonCancle.setActionCommand("buttonCancle");
		
		textFieldPhotoPath = new JTextField();
		textFieldPhotoPath.setColumns(10);
		
		JButton buttonPhotoBatchReg = new JButton("\u6DFB\u52A0\u6587\u4EF6");
		buttonPhotoBatchReg.addActionListener(batchRegAction);
		buttonPhotoBatchReg.setActionCommand("buttonPhotoBatchReg");
		
		JLabel lblExcel = new JLabel("Excel\u6587\u4EF6\u6279\u91CF\u6CE8\u518C");
		
		JLabel label = new JLabel("\u7167\u7247\u6279\u91CF\u6CE8\u518C");
		
		JButton buttonPhotoPathSure = new JButton("\u786E\u5B9A");
		buttonPhotoPathSure.addActionListener(batchRegAction);
		buttonPhotoPathSure.setActionCommand("buttonPhotoPathSure");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(buttonCancle)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(textFieldExcelData, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
							.addComponent(lblExcel, Alignment.LEADING)
							.addComponent(textFieldPhotoPath, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
							.addComponent(label, Alignment.LEADING)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(buttonExcelDataBatchReg)
							.addGap(18)
							.addComponent(buttonExcelDataSure))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(buttonPhotoBatchReg)
							.addGap(18)
							.addComponent(buttonPhotoPathSure)))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addComponent(lblExcel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonExcelDataBatchReg)
						.addComponent(textFieldExcelData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonExcelDataSure))
					.addGap(27)
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldPhotoPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonPhotoBatchReg)
						.addComponent(buttonPhotoPathSure))
					.addGap(18)
					.addComponent(buttonCancle)
					.addGap(47))
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		
		this.setSize(449, 250);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 350,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 150);
		this.setVisible(true);
	}
	public JTextField getTextFieldExcelData() {
		return textFieldExcelData;
	}
	public void setTextFieldExcelData(JTextField textFieldExcelData) {
		this.textFieldExcelData = textFieldExcelData;
	}
	public JTextField getTextFieldPhotoPath() {
		return textFieldPhotoPath;
	}
	public void setTextFieldPhotoPath(JTextField textFieldPhotoPath) {
		this.textFieldPhotoPath = textFieldPhotoPath;
	}
}
