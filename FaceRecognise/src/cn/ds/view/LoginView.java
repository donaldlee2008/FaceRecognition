/**
 * Project             :FaceRecognise project
 * Comments            :登陆界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Toolkit;


import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import cn.ds.control.LoginAction;
import javax.swing.LayoutStyle.ComponentPlacement;


public class LoginView extends JFrame{
	private JTextField textFieldAccount;
	private JPasswordField passwordField;
	private JTabbedPane tabbedPane;
	private JTextField textFieldMAccount;
	private JPasswordField passwordFieldM;
	
	public LoginView(LoginAction loginAction) {
		
		this.addKeyListener(loginAction);
		this.setFocusable(true);
		
		setTitle("\u767B\u9646");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
	
	
		
		tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		
		JPanel panelPwd = new JPanel();
		tabbedPane.addTab("\u5BC6\u7801\u767B\u9646", panelPwd);
		
		JLabel label = new JLabel("\u7528\u6237\u540D");
		
		textFieldAccount = new JTextField();
		textFieldAccount.setColumns(10);
		textFieldAccount.addKeyListener(loginAction);
		textFieldAccount.setFocusable(true);
		
		JLabel label_1 = new JLabel("\u5BC6\u3000\u7801");
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(loginAction);
		passwordField.setFocusable(true);
		
		GroupLayout groupLayout = new GroupLayout(panelPwd);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(74)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(label)
						.addComponent(label_1))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(passwordField)
						.addComponent(textFieldAccount, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
					.addContainerGap(121, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(textFieldAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		panelPwd.setLayout(groupLayout);
		
		JPanel panelFace = new JPanel();
		tabbedPane.addTab("\u4EBA\u8138\u8BC6\u522B\u767B\u9646",panelFace);
		
		JPanel panelFaceAndPwd = new JPanel();
		tabbedPane.addTab("\u7BA1\u7406\u6A21\u5F0F\u767B\u9646",panelFaceAndPwd);
		
		JLabel labelAccount = new JLabel("\u7528\u6237\u540D");
		
		textFieldMAccount = new JTextField();
		textFieldMAccount.setColumns(10);
		textFieldMAccount.addKeyListener(loginAction);
		textFieldMAccount.setFocusable(true);
		
		JLabel label_2 = new JLabel("\u5BC6\u3000\u7801");
		
		passwordFieldM = new JPasswordField();
	    passwordFieldM.addKeyListener(loginAction);
	    passwordFieldM.setFocusable(true);
	    
		GroupLayout gl_panelFaceAndPwd = new GroupLayout(panelFaceAndPwd);
		gl_panelFaceAndPwd.setHorizontalGroup(
			gl_panelFaceAndPwd.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFaceAndPwd.createSequentialGroup()
					.addGap(63)
					.addGroup(gl_panelFaceAndPwd.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelFaceAndPwd.createSequentialGroup()
							.addComponent(label_2)
							.addGap(18)
							.addComponent(passwordFieldM))
						.addGroup(gl_panelFaceAndPwd.createSequentialGroup()
							.addComponent(labelAccount)
							.addGap(18)
							.addComponent(textFieldMAccount, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(143, Short.MAX_VALUE))
		);
		gl_panelFaceAndPwd.setVerticalGroup(
			gl_panelFaceAndPwd.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFaceAndPwd.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_panelFaceAndPwd.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelAccount)
						.addComponent(textFieldMAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelFaceAndPwd.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordFieldM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_2))
					.addContainerGap(72, Short.MAX_VALUE))
		);
		panelFaceAndPwd.setLayout(gl_panelFaceAndPwd);
		tabbedPane.addChangeListener(loginAction);
		
		JPanel panelButton = new JPanel();
		getContentPane().add(panelButton, BorderLayout.SOUTH);
		
		JButton buttonLogin = new JButton("\u767B \u9646");
		buttonLogin.addActionListener(loginAction);
		buttonLogin.setActionCommand("buttonLogin");

		
		JButton buttonCancle = new JButton("\u53D6 \u6D88");
		buttonCancle.addActionListener(loginAction);
		buttonCancle.setActionCommand("buttonCancle");
		
		GroupLayout gl_panelButton = new GroupLayout(panelButton);
		gl_panelButton.setHorizontalGroup(
			gl_panelButton.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelButton.createSequentialGroup()
					.addGap(85)
					.addComponent(buttonLogin)
					.addGap(71)
					.addComponent(buttonCancle)
					.addContainerGap(110, Short.MAX_VALUE))
		);
		gl_panelButton.setVerticalGroup(
			gl_panelButton.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelButton.createSequentialGroup()
					.addGroup(gl_panelButton.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonCancle)
						.addComponent(buttonLogin))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelButton.setLayout(gl_panelButton);
		
		JLabel lblNewLabel = new JLabel(new ImageIcon("image/banner.jpg"));
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		this.setSize(400, 250);
		this.setResizable(false);
		this.setLocation(100,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 125);
		this.setVisible(true);	
	}
	public JTextField getTextFieldAccount() {
		return textFieldAccount;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	public JTextField getTextFieldMAccount() {
		return textFieldMAccount;
	}
	public JPasswordField getPasswordFieldM() {
		return passwordFieldM;
	}
	
}
