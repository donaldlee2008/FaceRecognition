/**
 * Project             :FaceRecognise project
 * Comments            :修改用户信息界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.view;


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

import cn.ds.control.UpdateUserAction;
import cn.ds.domain.User;

import java.awt.BorderLayout;
import java.awt.Toolkit;

public class UpdateUserView extends JFrame{
	private JTextField textFieldAccount;
	private JPasswordField passwordField;
	private JTextField textFieldName;
	private JTextField textFieldDepartment;
	private JTextField textFieldTel;
	private JRadioButton radioButtonMan;
	private JLabel labelShowResult;
	private JLabel lblelEntryPhoto;
	private JButton buttonEntryPhotos;
	public UpdateUserView(UpdateUserAction updateUserAction,User user) {
		setTitle("\u4FEE\u6539\u4FE1\u606F");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.WEST);
		
		JLabel label = new JLabel("\u8D26\u53F7(*)");
		
		textFieldAccount = new JTextField();
		textFieldAccount.setColumns(20);
		textFieldAccount.setText(user.getAccount());
		
		JLabel label_1 = new JLabel("\u5BC6\u7801(*)");
		
		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		passwordField.setText(user.getPwd());
		
		JLabel label_2 = new JLabel("\u59D3\u540D(*)");
		
		textFieldName = new JTextField();
		textFieldName.setColumns(20);
		textFieldName.setText(user.getName());
		
		JLabel label_3 = new JLabel("\u6027\u522B");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		radioButtonMan = new JRadioButton("\u7537");
		//
		
		JRadioButton rdbtnNv = new JRadioButton("\u5973");
		
		if("男".equals(user.getSex())){
			radioButtonMan.setSelected(true);	
		}else{
			rdbtnNv.setSelected(true);
		}
		
		buttonGroup.add(rdbtnNv);
		buttonGroup.add(radioButtonMan);
		
		JLabel label_4 = new JLabel("\u90E8\u95E8");
		
		textFieldDepartment = new JTextField();
		textFieldDepartment.setColumns(20);
		textFieldDepartment.setText(user.getDepartment());
		
		JLabel label_5 = new JLabel("\u8054\u7CFB\u65B9\u5F0F");
		
		textFieldTel = new JTextField();
		textFieldTel.setColumns(20);
		textFieldTel.setText(user.getTel());
		
		buttonEntryPhotos = new JButton("\u5F55\u5165\u7167\u7247");
		buttonEntryPhotos.addActionListener(updateUserAction);
		buttonEntryPhotos.setActionCommand("buttonEntryPhotos");
		
		labelShowResult = new JLabel("");
		
		JButton button_1 = new JButton("\u786E\u5B9A");
		button_1.addActionListener(updateUserAction);
		button_1.setActionCommand("buttonSure");
		
		JButton button_2 = new JButton("\u91CD\u7F6E");
		button_2.addActionListener(updateUserAction);
		button_2.setActionCommand("buttonReset");
		
		
		JButton button_3 = new JButton("\u9000\u51FA");
		button_3.addActionListener(updateUserAction);
		button_3.setActionCommand("buttonQuit");
		
		JLabel lblId = new JLabel("ID");
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setText(String.valueOf(user.getId()));
		
		JLabel label_6 = new JLabel("\u5F55\u5165\u7167\u7247");
		
		lblelEntryPhoto = new JLabel();
		lblelEntryPhoto.setText(user.getHavePhoto());
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(label_5)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(label_3)
							.addGap(63)
							.addComponent(radioButtonMan)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnNv))
						.addComponent(buttonEntryPhotos)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(16)
									.addComponent(button_1))
								.addComponent(label_2)
								.addComponent(label_1)
								.addComponent(label)
								.addComponent(lblId)
								.addComponent(label_4)
								.addComponent(label_6))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblelEntryPhoto)
								.addComponent(textFieldDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel)
								.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldTel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(25)
									.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
										.addComponent(labelShowResult)
										.addComponent(button_2))
									.addGap(28)
									.addComponent(button_3)))))
					.addContainerGap(89, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblId)
						.addComponent(lblNewLabel))
					.addGap(12)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2)
						.addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(radioButtonMan)
						.addComponent(rdbtnNv)
						.addComponent(label_3))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_4)
						.addComponent(textFieldDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_5)
						.addComponent(textFieldTel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_6)
						.addComponent(lblelEntryPhoto))
					.addGap(25)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonEntryPhotos)
						.addComponent(labelShowResult))
					.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_1)
						.addComponent(button_2)
						.addComponent(button_3))
					.addGap(24))
		);
		panel.setLayout(gl_panel);
		
		this.setSize(348, 428);
		this.setLocation(100,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 214);
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
	public synchronized JLabel getLblelEntryPhoto() {
		return lblelEntryPhoto;
	}
	public synchronized JButton getButtonEntryPhotos() {
		return buttonEntryPhotos;
	}
}
