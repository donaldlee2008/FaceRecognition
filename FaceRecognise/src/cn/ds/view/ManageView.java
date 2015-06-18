/**
 * Project             :FaceRecognise project
 * Comments            :¹ÜÀíÔ±½çÃæ
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | ´´½¨ | jxm 
 */
package cn.ds.view;


import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;


import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

import cn.ds.control.ManageAction;
import cn.ds.utils.Page;
import cn.ds.view.customview.BarRenderer;

import javax.swing.ListSelectionModel;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ManageView extends JFrame {
	private JTextField textFieldSearch;
	private JTextField textFieldPageNum;
	private Object[][] dat = null;
	private Object[] title = new String[] { "ID", "ÕËºÅ", "ÐÕÃû", "ÐÔ±ð", "²¿ÃÅ",
			"ÁªÏµ·½Ê½" };
	private JButton buttonUpdate;
	private JButton buttonDelete;
	private JButton buttonNextPage;
	private JButton buttonPreviousPage;
	private JButton buttonSeacher;
	private JButton buttonTrain;
	private JButton buttonAdd;
	private JButton btnGo;
	private JButton buttonDeleteAllUsers;
	private JTable table;
	private DefaultTableModel model;
	private Page page;
	private JTable tableVideoProcessTask;
	private JTabbedPane tabbedPane;
	private JLabel labelPage;
	private JButton buttonBatchReg;
	private JTextField textFieldSavePath;
	private JTextField textFieldPhotoToRecognise;
	private JTextField textFieldSourcePhoto;
	private ButtonGroup buttonGroup;
	private JButton buttonChooseSourcePhoto;
	private JRadioButton radioButtonMaskFace;
	private JRadioButton radioButtonUnmaskFace;
	private JLabel label_3;
	private JComboBox comboBox;
	private JButton buttonSeeResult;
	

	public ManageView(ManageAction manageAction, Object[][] data1, Page p) {
		this.dat = data1;
		this.page = p;

		setTitle("\u7BA1\u7406\u5458");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);

		JPanel panelManage = new JPanel();
		tabbedPane.addTab("¹ÜÀí", null, panelManage, null);

		textFieldSearch = new JTextField();
		textFieldSearch.setColumns(15);

		buttonSeacher = new JButton("\u67E5\u8BE2");
		buttonSeacher.addActionListener(manageAction);
		buttonSeacher.setActionCommand("buttonSeacher");

		JScrollPane scrollPane = new JScrollPane();

		buttonTrain = new JButton("\u66F4\u65B0\u4EBA\u8138\u5E93");
		buttonTrain.addActionListener(manageAction);
		buttonTrain.setActionCommand("buttonTrain");

		buttonAdd = new JButton("\u6DFB\u52A0");
		buttonAdd.addActionListener(manageAction);
		buttonAdd.setActionCommand("buttonAdd");

		buttonUpdate = new JButton("\u4FEE\u6539");
		buttonUpdate.addActionListener(manageAction);
		buttonUpdate.setActionCommand("buttonUpdate");
		buttonUpdate.setEnabled(false);

		buttonDelete = new JButton("\u5220\u9664");
		buttonDelete.addActionListener(manageAction);
		buttonDelete.setActionCommand("buttonDelete");
		buttonDelete.setEnabled(false);

		buttonNextPage = new JButton(">>");
		buttonNextPage.addActionListener(manageAction);
		buttonNextPage.setActionCommand("buttonNextPage");
		if (page.getPageNow() >= page.getPageCount()) {
			buttonNextPage.setEnabled(false);
		} else {
			buttonNextPage.setEnabled(true);
		}

		buttonPreviousPage = new JButton("<<");
		buttonPreviousPage.addActionListener(manageAction);
		buttonPreviousPage.setActionCommand("buttonPreviouPage");

		if (page.getPageNow() <= 1) {
			buttonPreviousPage.setEnabled(false);
		} else {
			buttonPreviousPage.setEnabled(true);
		}

		labelPage = new JLabel();
		labelPage.setText(page.getPageNow() + "/" + page.getPageCount());

		textFieldPageNum = new JTextField();
		textFieldPageNum.setColumns(5);

		btnGo = new JButton("\u8DF3\u8F6C");
		btnGo.addActionListener(manageAction);
		btnGo.setActionCommand("buttonGo");
		
		buttonDeleteAllUsers = new JButton("\u5220\u9664\u6240\u6709\u7528\u6237");
		buttonDeleteAllUsers.addActionListener(manageAction);
		buttonDeleteAllUsers.setActionCommand("buttonDeleteAllUsers");
		
		buttonBatchReg = new JButton("\u6279\u91CF\u6CE8\u518C");
		buttonBatchReg.addActionListener(manageAction);
		buttonBatchReg.setActionCommand("buttonBatchReg");

		GroupLayout gl_panelManage = new GroupLayout(panelManage);
		gl_panelManage.setHorizontalGroup(
			gl_panelManage.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelManage.createSequentialGroup()
					.addGroup(gl_panelManage.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelManage.createSequentialGroup()
							.addGap(79)
							.addComponent(textFieldSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(buttonSeacher)
							.addGap(74)
							.addComponent(buttonTrain)
							.addGap(18)
							.addComponent(buttonDeleteAllUsers)
							.addGap(18)
							.addComponent(buttonBatchReg))
						.addGroup(gl_panelManage.createSequentialGroup()
							.addGap(27)
							.addComponent(labelPage)
							.addGap(27)
							.addComponent(buttonPreviousPage)
							.addGap(18)
							.addComponent(buttonNextPage)
							.addGap(18)
							.addComponent(textFieldPageNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnGo))
						.addGroup(gl_panelManage.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE))
						.addGroup(gl_panelManage.createSequentialGroup()
							.addGap(193)
							.addComponent(buttonAdd)
							.addGap(18)
							.addComponent(buttonDelete)
							.addGap(18)
							.addComponent(buttonUpdate)))
					.addContainerGap())
		);
		gl_panelManage.setVerticalGroup(
			gl_panelManage.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelManage.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonSeacher)
						.addComponent(textFieldSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonTrain)
						.addComponent(buttonDeleteAllUsers)
						.addComponent(buttonBatchReg))
					.addGap(31)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelPage)
						.addComponent(buttonPreviousPage)
						.addComponent(buttonNextPage)
						.addComponent(textFieldPageNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnGo))
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(gl_panelManage.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonAdd)
						.addComponent(buttonDelete)
						.addComponent(buttonUpdate))
					.addGap(23))
		);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(manageAction);

//		model = new DefaultTableModel(dat, new String[] { "ID", "\u8D26\u53F7",
//				"\u59D3\u540D", "\u6027\u522B", "\u90E8\u95E8",
//				"\u8054\u7CFB\u65B9\u5F0F" }) {
//			boolean[] columnEditables = new boolean[] { false, false, false,
//					false, false, false };
//
//			public boolean isCellEditable(int row, int column) {
//				return columnEditables[column];
//			}
//		};
		table.setModel(new DefaultTableModel(
			dat,
			new String[] {
				"ID", "\u8D26\u53F7", "\u59D3\u540D", "\u6027\u522B", "\u5F55\u5165\u7167\u7247", "\u90E8\u95E8", "\u8054\u7CFB\u65B9\u5F0F"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(6).setPreferredWidth(130);
		

		table.setRowHeight(20);

		scrollPane.setViewportView(table);

		panelManage.setLayout(gl_panelManage);

		JPanel panelVideoProcess = new JPanel();
		tabbedPane.addTab("ÊÓÆµ´¦Àí", null, panelVideoProcess, null);

		tabbedPane.addChangeListener(manageAction);

		JButton buttonAddVideoProcessTask = new JButton(
				"\u65B0\u5EFA\u4EFB\u52A1");
		buttonAddVideoProcessTask.addActionListener(manageAction);
		buttonAddVideoProcessTask.setActionCommand("buttonAddVideoProcessTask");

		JScrollPane scrollPaneVideoProcessTask = new JScrollPane();

		GroupLayout gl_panelVideoProcess = new GroupLayout(panelVideoProcess);
		gl_panelVideoProcess
				.setHorizontalGroup(gl_panelVideoProcess
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelVideoProcess
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_panelVideoProcess
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																buttonAddVideoProcessTask)
														.addComponent(
																scrollPaneVideoProcessTask,
																GroupLayout.DEFAULT_SIZE,
																607,
																Short.MAX_VALUE))
										.addContainerGap()));
		gl_panelVideoProcess.setVerticalGroup(gl_panelVideoProcess
				.createParallelGroup(Alignment.LEADING).addGroup(
						gl_panelVideoProcess
								.createSequentialGroup()
								.addGap(23)
								.addComponent(buttonAddVideoProcessTask)
								.addGap(38)
								.addComponent(scrollPaneVideoProcessTask,
										GroupLayout.PREFERRED_SIZE, 294,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(43, Short.MAX_VALUE)));

		tableVideoProcessTask = new JTable();
        tableVideoProcessTask.addMouseListener(manageAction);
		tableVideoProcessTask.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "\u4EFB\u52A1\u5E8F\u5217\u53F7",
						"\u8FDB\u5EA6", "\u6240\u7528\u65F6\u95F4" }) {
			boolean[] columnEditables = new boolean[] { false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});


		tableVideoProcessTask.getColumnModel().getColumn(1)
				.setPreferredWidth(459);
		tableVideoProcessTask.getColumnModel().getColumn(1)
				.setCellRenderer(new BarRenderer());
		tableVideoProcessTask.getColumnModel().getColumn(2)
				.setPreferredWidth(97);
		tableVideoProcessTask.setRowHeight(20);

		scrollPaneVideoProcessTask.setViewportView(tableVideoProcessTask);
		panelVideoProcess.setLayout(gl_panelVideoProcess);
		
		JPanel panelBatchPhoto = new JPanel();
		tabbedPane.addTab("\u6279\u91CF\u7167\u7247\u8BC6\u522B", null, panelBatchPhoto, null);
		
		JLabel label = new JLabel("\u9009\u9879:");
		label.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		
		buttonGroup = new ButtonGroup();
		
		radioButtonMaskFace = new JRadioButton("\u5DF2\u5904\u7406");
		radioButtonMaskFace.setSelected(true);
		radioButtonMaskFace.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		radioButtonMaskFace.addActionListener(manageAction);
		radioButtonMaskFace.setActionCommand("radioButtonMaskFace");

		
		radioButtonUnmaskFace = new JRadioButton("\u672A\u5904\u7406");
		radioButtonUnmaskFace.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		radioButtonUnmaskFace.addActionListener(manageAction);
		radioButtonUnmaskFace.setActionCommand("radioButtonUnmaskFace");
		
		JLabel lblNewLabel = new JLabel("(\u7167\u7247\u662F\u7ECF\u8FC7\u672C\u7CFB\u7EDF\u7684mask\u5904\u7406\u7684)");
		lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		
		JLabel lblNewLabel_1 = new JLabel("(\u7167\u7247\u672A\u7ECF\u8FC7\u672C\u7CFB\u7EDF\u7684mask\u5904\u7406)");
		lblNewLabel_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 14));
		buttonGroup.add(radioButtonUnmaskFace);
		buttonGroup.add(radioButtonMaskFace);
		
		JLabel label_1 = new JLabel("\u7ED3\u679C\u4FDD\u5B58:");
		label_1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		
		textFieldSavePath = new JTextField();
		textFieldSavePath.setColumns(10);
		
		JButton buttonChooseSavePath = new JButton("\u9009\u62E9\u6587\u4EF6\u5939");
		buttonChooseSavePath.addActionListener(manageAction);
		buttonChooseSavePath.setActionCommand("buttonChooseSavePath");
		
		JLabel label_2 = new JLabel("\u5F85\u8BC6\u522B\u7167\u7247:");
		label_2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		
		buttonSeeResult = new JButton("\u67E5\u770B\u7ED3\u679C");
		buttonSeeResult.addActionListener(manageAction);
		buttonSeeResult.setActionCommand("buttonSeeResult");
		buttonSeeResult.setEnabled(false);
		
		textFieldPhotoToRecognise = new JTextField();
		textFieldPhotoToRecognise.setColumns(10);
		
		JButton buttonChooseToRecognisePhoto = new JButton("\u9009\u62E9\u6587\u4EF6");
		buttonChooseToRecognisePhoto.addActionListener(manageAction);
		buttonChooseToRecognisePhoto.setActionCommand("buttonChooseToRecognisePhoto");
		
		JButton buttonBatchFaceSure = new JButton("\u786E\u5B9A");
		buttonBatchFaceSure.addActionListener(manageAction);
		buttonBatchFaceSure.setActionCommand("buttonBatchFaceSure");
		
		
		JLabel lblNewLabel_3 = new JLabel("\u6E90\u7167\u7247:");
		lblNewLabel_3.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		
		textFieldSourcePhoto = new JTextField();
		textFieldSourcePhoto.setColumns(10);
		
		buttonChooseSourcePhoto = new JButton("\u9009\u62E9\u6587\u4EF6");
		buttonChooseSourcePhoto.addActionListener(manageAction);
		buttonChooseSourcePhoto.setActionCommand("buttonChooseSourcePhoto");
		buttonChooseSourcePhoto.setEnabled(false);
		
		label_3 = new JLabel("\u8BC6\u522B\u9608\u503C:");
		label_3.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		
		comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0.50", "0.55", "0.60", "0.65", "0.70", "0.75", "0.80", "0.85", "0.90", "0.95", "1.00"}));
		comboBox.setSelectedIndex(2);
		comboBox.setMaximumRowCount(10);
		comboBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		
		GroupLayout gl_panelBatchPhoto = new GroupLayout(panelBatchPhoto);
		gl_panelBatchPhoto.setHorizontalGroup(
			gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelBatchPhoto.createSequentialGroup()
					.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelBatchPhoto.createSequentialGroup()
							.addContainerGap()
							.addComponent(label))
						.addGroup(gl_panelBatchPhoto.createSequentialGroup()
							.addGap(37)
							.addComponent(radioButtonMaskFace)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel)
							.addGap(18)
							.addComponent(radioButtonUnmaskFace)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel_1))
						.addGroup(gl_panelBatchPhoto.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelBatchPhoto.createSequentialGroup()
									.addGap(27)
									.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.TRAILING)
										.addComponent(textFieldSavePath, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING, false)
											.addComponent(textFieldSourcePhoto)
											.addComponent(textFieldPhotoToRecognise, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)))
									.addGap(18)
									.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panelBatchPhoto.createSequentialGroup()
											.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING)
												.addComponent(buttonChooseSavePath)
												.addComponent(buttonChooseToRecognisePhoto))
											.addGap(18)
											.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING)
												.addComponent(buttonBatchFaceSure)
												.addComponent(buttonSeeResult)))
										.addComponent(buttonChooseSourcePhoto)))
								.addComponent(label_2)
								.addComponent(lblNewLabel_3)
								.addComponent(label_1)))
						.addGroup(gl_panelBatchPhoto.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_3)
							.addGap(28)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(144, Short.MAX_VALUE))
		);
		gl_panelBatchPhoto.setVerticalGroup(
			gl_panelBatchPhoto.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelBatchPhoto.createSequentialGroup()
					.addGap(27)
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.BASELINE)
						.addComponent(radioButtonMaskFace)
						.addComponent(lblNewLabel)
						.addComponent(radioButtonUnmaskFace)
						.addComponent(lblNewLabel_1))
					.addGap(47)
					.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_3)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
					.addComponent(label_1)
					.addGap(18)
					.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldSavePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonChooseSavePath)
						.addComponent(buttonSeeResult))
					.addGap(18)
					.addComponent(label_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldPhotoToRecognise, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonChooseToRecognisePhoto)
						.addComponent(buttonBatchFaceSure))
					.addGap(18)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelBatchPhoto.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldSourcePhoto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonChooseSourcePhoto))
					.addGap(37))
		);
		panelBatchPhoto.setLayout(gl_panelBatchPhoto);

		this.setSize(770, 480);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 385,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 240);
		this.setVisible(true);
	}

	public void refreshUI() {
		labelPage.setText(page.getPageNow() + "/" + page.getPageCount());

		if (page.getPageNow() <= 1) {
			buttonPreviousPage.setEnabled(false);
		} else {
			buttonPreviousPage.setEnabled(true);
		}

		if (page.getPageNow() >= page.getPageCount()) {
			buttonNextPage.setEnabled(false);
		} else {
			buttonNextPage.setEnabled(true);
		}
	}

	public JTextField getTextFieldSearch() {
		return textFieldSearch;
	}

	public void setTextFieldSearch(JTextField textFieldSearch) {
		this.textFieldSearch = textFieldSearch;
	}

	public JTextField getTextFieldPageNum() {
		return textFieldPageNum;
	}

	public void setTextFieldPageNum(JTextField textFieldPageNum) {
		this.textFieldPageNum = textFieldPageNum;
	}

	public Object[][] getData() {
		return dat;
	}

	public void setData(Object[][] data) {
		this.dat = data;
	}

	public JButton getButtonUpdate() {
		return buttonUpdate;
	}

	public void setButtonUpdate(JButton buttonUpdate) {
		this.buttonUpdate = buttonUpdate;
	}

	public JButton getButtonDelete() {
		return buttonDelete;
	}

	public void setButtonDelete(JButton buttonDelete) {
		this.buttonDelete = buttonDelete;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public JButton getButtonNextPage() {
		return buttonNextPage;
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

	public synchronized JButton getButtonPreviousPage() {
		return buttonPreviousPage;
	}

	public synchronized JLabel getLabelPage() {
		return labelPage;
	}

	public synchronized JButton getButtonSeacher() {
		return buttonSeacher;
	}

	public synchronized JButton getButtonTrain() {
		return buttonTrain;
	}

	public synchronized JButton getButtonAdd() {
		return buttonAdd;
	}

	public synchronized JButton getBtnGo() {
		return btnGo;
	}

	public JButton getButtonDeleteAllUsers() {
		return buttonDeleteAllUsers;
	}

	public JTextField getTextFieldSavePath() {
		return textFieldSavePath;
	}

	public JTextField getTextFieldPhotoToRecognise() {
		return textFieldPhotoToRecognise;
	}

	public JTextField getTextFieldSourcePhoto() {
		return textFieldSourcePhoto;
	}

	public JButton getButtonChooseSourcePhoto() {
		return buttonChooseSourcePhoto;
	}

	public JRadioButton getRadioButtonMaskFace() {
		return radioButtonMaskFace;
	}

	public JRadioButton getRadioButtonUnmaskFace() {
		return radioButtonUnmaskFace;
	}

	public JComboBox getComboBox() {
		return comboBox;
	}

	public JButton getButtonSeeResult() {
		return buttonSeeResult;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public JButton getButtonBatchReg() {
		return buttonBatchReg;
	}
}
