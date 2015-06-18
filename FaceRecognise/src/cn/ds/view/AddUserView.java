/**
 * Project             :FaceRecognise project
 * Comments            :添加用户界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.view;


import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;

import cn.ds.control.AddUserAction;


public class AddUserView extends JFrame{
	private JTextField textFieldAccount;
	private JPasswordField passwordField;
	private JTextField textFieldName;
	private JTextField textFieldDepartment;
	private JTextField textFieldTel;
	private JRadioButton radioButtonMan;
	private JLabel labelShowResult;
	private JLabel labelHavePhoto;
	public AddUserView(AddUserAction addUserAction) {
		setTitle("\u6DFB\u52A0\u7528\u6237");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		
		JLabel label = new JLabel("\u8D26\u53F7(*)");
		
		textFieldAccount = new JTextField();
		textFieldAccount.setColumns(20);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801(*)");
		
		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		
		JLabel label_2 = new JLabel("\u59D3\u540D(*)");
		
		textFieldName = new JTextField();
		textFieldName.setColumns(20);
		
		JLabel label_3 = new JLabel("\u6027\u522B");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		radioButtonMan = new JRadioButton("\u7537");
		radioButtonMan.setSelected(true);
		
		JRadioButton rdbtnNv = new JRadioButton("\u5973");
		
		buttonGroup.add(rdbtnNv);
		buttonGroup.add(radioButtonMan);
		
		JLabel label_4 = new JLabel("\u90E8\u95E8");
		
		textFieldDepartment = new JTextField();
		textFieldDepartment.setColumns(20);
		
		JLabel label_5 = new JLabel("\u8054\u7CFB\u65B9\u5F0F");
		
		textFieldTel = new JTextField();
		textFieldTel.setColumns(20);
		
		JButton button = new JButton("\u5F55\u5165\u7167\u7247");
		button.addActionListener(addUserAction);
		button.setActionCommand("buttonEntryPhotos");
		
		labelShowResult = new JLabel("");
		
		JButton button_1 = new JButton("\u786E\u5B9A");
		button_1.addActionListener(addUserAction);
		button_1.setActionCommand("buttonSure");
		
		JButton button_2 = new JButton("\u91CD\u7F6E");
		button_2.addActionListener(addUserAction);
		button_2.setActionCommand("buttonReset");
		
		
		JButton button_3 = new JButton("\u9000\u51FA");
		button_3.addActionListener(addUserAction);
		button_3.setActionCommand("buttonQuit");
		
		JLabel label_6 = new JLabel("\u5F55\u5165\u7167\u7247");
		
		labelHavePhoto = new JLabel();
		labelHavePhoto.setText("否");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(label)
								.addComponent(label_1)
								.addComponent(label_2)
								.addComponent(label_3)
								.addComponent(label_4)
								.addComponent(label_5)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(16)
									.addComponent(button_1)))
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(textFieldDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_panel.createSequentialGroup()
											.addGap(10)
											.addComponent(radioButtonMan)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(rdbtnNv))
										.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(textFieldTel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(labelHavePhoto)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(26)
									.addComponent(button_2)
									.addGap(29)
									.addComponent(button_3))))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(button)
							.addGap(77)
							.addComponent(labelShowResult))
						.addComponent(label_6))
					.addContainerGap(88, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_3)
						.addComponent(radioButtonMan)
						.addComponent(rdbtnNv))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_4)
						.addComponent(textFieldDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_5)
						.addComponent(textFieldTel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_6)
						.addComponent(labelHavePhoto))
					.addPreferredGap(ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(button)
						.addComponent(labelShowResult))
					.addGap(33)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_1)
						.addComponent(button_2)
						.addComponent(button_3))
					.addGap(24))
		);
		panel.setLayout(gl_panel);
		
		//this.setSize(301, 327);
		this.setSize(348, 393);
		this.setLocation(100,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 197);
		this.setVisible(true);
	}
	public JTextField getTextFieldAccount() {
		return textFieldAccount;
	}
	public void setTextFieldAccount(JTextField textFieldAccount) {
		this.textFieldAccount = textFieldAccount;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}
	public JTextField getTextFieldName() {
		return textFieldName;
	}
	public void setTextFieldName(JTextField textFieldName) {
		this.textFieldName = textFieldName;
	}
	public JTextField getTextFieldDepartment() {
		return textFieldDepartment;
	}
	public void setTextFieldDepartment(JTextField textFieldDepartment) {
		this.textFieldDepartment = textFieldDepartment;
	}
	public JTextField getTextFieldTel() {
		return textFieldTel;
	}
	public void setTextFieldTel(JTextField textFieldTel) {
		this.textFieldTel = textFieldTel;
	}
	public JRadioButton getRadioButton() {
		return radioButtonMan;
	}
	public void setRadioButton(JRadioButton radioButton) {
		this.radioButtonMan = radioButton;
	}
	public JLabel getLabelShowResult() {
		return labelShowResult;
	}
	public void setLabelShowResult(JLabel labelShowResult) {
		this.labelShowResult = labelShowResult;
	}
	public synchronized JLabel getLabelHavePhoto() {
		return labelHavePhoto;
	}
}
