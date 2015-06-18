package cn.ds.view;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Font;

public class LoadingView extends JDialog {
	private ImageIcon image = new ImageIcon("image/loading.gif");
	private JLabel labelContent;
	@Override
	  public void paint(Graphics g) {
		super.paint(g);
	    g.drawImage(image.getImage(), 50, 60, this);
	  }
	public LoadingView(String content) {
		getContentPane().setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		getContentPane().setLayout(null);
		setTitle("\u6B63\u5728\u5904\u7406");
		
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		
		labelContent = new JLabel("New label");
		labelContent.setText(content);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(labelContent, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(74)
					.addComponent(labelContent, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
		
		this.setSize(300, 200);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 150,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 100);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	public JLabel getLabelContent() {
		return labelContent;
	}
}
