/**
 * Project             :FaceRecognise project
 * Comments            :普通用户界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.view;


import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;

import javax.swing.table.DefaultTableModel;


import cn.ds.control.CommonUserAction;
import cn.ds.domain.User;
import cn.ds.view.customview.BarRenderer;
import javax.swing.JLabel;


public class CommonUserView extends JFrame {
	private JTable tableVideoProcessTask;
	private JTabbedPane tabbedPane;

	public CommonUserView(CommonUserAction commonUserAction,User user) {
		
		setTitle("\u666E\u901A\u7528\u6237");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);
		

		JPanel panelManage = new JPanel();
		tabbedPane.addTab("管理", null, panelManage, null);
				
				JLabel label1 = new JLabel("ID\u53F7:");
				
				JLabel label2 = new JLabel("\u8D26\u53F7:");
				
				JLabel label3 = new JLabel("\u59D3\u540D:");
				
				JLabel label = new JLabel("\u6027\u522B:");
				
				JLabel label_1 = new JLabel("\u5F55\u5165\u7167\u7247:");
				
				JLabel label_2 = new JLabel("\u90E8\u95E8:");
				
				JLabel label_3 = new JLabel("\u8054\u7CFB\u65B9\u5F0F:");
				
				JLabel labelId,labelAccount,labelName,labelSex,labelHavePhoto,labelDepartment,labelTel;
				JButton buttonUpdate;
				
				labelId = new JLabel();
				
				labelAccount = new JLabel();
				
				labelName = new JLabel();
				
				labelSex = new JLabel();
				
				labelHavePhoto = new JLabel();
				
				labelDepartment = new JLabel();
				
				labelTel = new JLabel();
				
				buttonUpdate = new JButton("\u4FEE\u6539\u4FE1\u606F");
				buttonUpdate.addActionListener(commonUserAction);
				buttonUpdate.setActionCommand("buttonUpdate");
				buttonUpdate.setEnabled(false);
				
				if(user  != null){
					labelId.setText(String.valueOf(user.getId()));
					
					labelAccount.setText(user.getAccount());
					
					labelName.setText(user.getName());
					
					labelSex.setText(user.getSex());
					
					labelHavePhoto.setText(user.getHavePhoto());
					
					labelDepartment.setText(user.getDepartment());
					
					labelTel.setText(user.getTel());
					
					//ImageIcon icon = new ImageIcon(user.getPicsPath() + user.getId() +"_0.bmp");
//					ImageIcon icon = new ImageIcon("D:\\ert.bmp");
//					labelPic.setIcon(icon);
//					labelPic.setVisible(true);
					
					
					buttonUpdate.setEnabled(true);
				}
				
				
		
				GroupLayout gl_panelManage = new GroupLayout(panelManage);
				gl_panelManage.setHorizontalGroup(
					gl_panelManage.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelManage.createSequentialGroup()
							.addGroup(gl_panelManage.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelManage.createSequentialGroup()
									.addGap(27)
									.addGroup(gl_panelManage.createParallelGroup(Alignment.TRAILING)
										.addComponent(label2)
										.addComponent(label3)
										.addComponent(label)
										.addComponent(label_1)
										.addComponent(label_2)
										.addComponent(label_3)
										.addComponent(label1))
									.addGap(18)
									.addGroup(gl_panelManage.createParallelGroup(Alignment.LEADING)
										.addComponent(labelAccount)
										.addComponent(labelName)
										.addComponent(labelSex)
										.addComponent(labelId)
										.addComponent(labelHavePhoto)
										.addComponent(labelDepartment)
										.addComponent(labelTel)))
								.addGroup(gl_panelManage.createSequentialGroup()
									.addGap(262)
									.addComponent(buttonUpdate)))
							.addContainerGap(284, Short.MAX_VALUE))
				);
				gl_panelManage.setVerticalGroup(
					gl_panelManage.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelManage.createSequentialGroup()
							.addGap(38)
							.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
								.addComponent(label1)
								.addComponent(labelId))
							.addGap(18)
							.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
								.addComponent(label2)
								.addComponent(labelAccount))
							.addGap(18)
							.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
								.addComponent(label3)
								.addComponent(labelName))
							.addGap(18)
							.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
								.addComponent(label)
								.addComponent(labelSex))
							.addGap(18)
							.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_1)
								.addComponent(labelHavePhoto))
							.addGap(18)
							.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_2)
								.addComponent(labelDepartment))
							.addGap(18)
							.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_3)
								.addComponent(labelTel))
							.addGap(80)
							.addComponent(buttonUpdate)
							.addContainerGap(67, Short.MAX_VALUE))
				);
				
				panelManage.setLayout(gl_panelManage);
		

		JPanel panelVideoProcess = new JPanel();
		tabbedPane.addTab("视频处理", null, panelVideoProcess, null);
		
		tabbedPane.addChangeListener(commonUserAction);
		
		JButton buttonAddVideoProcessTask = new JButton("\u65B0\u5EFA\u4EFB\u52A1");
		buttonAddVideoProcessTask.addActionListener(commonUserAction);
		buttonAddVideoProcessTask.setActionCommand("buttonAddVideoProcessTask");
		
		JScrollPane scrollPaneVideoProcessTask = new JScrollPane();
		
		GroupLayout gl_panelVideoProcess = new GroupLayout(panelVideoProcess);
		gl_panelVideoProcess.setHorizontalGroup(
			gl_panelVideoProcess.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelVideoProcess.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelVideoProcess.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonAddVideoProcessTask)
						.addComponent(scrollPaneVideoProcessTask, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelVideoProcess.setVerticalGroup(
			gl_panelVideoProcess.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelVideoProcess.createSequentialGroup()
					.addGap(23)
					.addComponent(buttonAddVideoProcessTask)
					.addGap(38)
					.addComponent(scrollPaneVideoProcessTask, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(43, Short.MAX_VALUE))
		);
		
		tableVideoProcessTask = new JTable();
		tableVideoProcessTask.addMouseListener(commonUserAction);
		tableVideoProcessTask.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"\u4EFB\u52A1\u5E8F\u5217\u53F7", "\u8FDB\u5EA6", "\u6240\u7528\u65F6\u95F4"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		tableVideoProcessTask.addMouseListener(commonUserAction);
		
		tableVideoProcessTask.getColumnModel().getColumn(1).setPreferredWidth(459);
		tableVideoProcessTask.getColumnModel().getColumn(1).setCellRenderer(new BarRenderer());
		tableVideoProcessTask.getColumnModel().getColumn(2).setPreferredWidth(97);
		tableVideoProcessTask.setRowHeight(20);
		
		scrollPaneVideoProcessTask.setViewportView(tableVideoProcessTask);
		panelVideoProcess.setLayout(gl_panelVideoProcess);

		this.setSize(640, 480);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 240);
		this.setVisible(true);
	}

	

	

	public JTable getTableVideoProcessTask() {
		return tableVideoProcessTask;
	}

	public void setTableVideoProcessTask(JTable tableVideoProcessTask) {
		this.tableVideoProcessTask = tableVideoProcessTask;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}
}
