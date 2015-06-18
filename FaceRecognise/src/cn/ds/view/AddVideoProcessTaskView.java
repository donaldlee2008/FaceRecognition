/**
 * Project             :FaceRecognise project
 * Comments            :添加视频人脸识别界面
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import cn.ds.control.AddVideoProcessTaskAction;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.JSlider;

public class AddVideoProcessTaskView extends JFrame{
	private JTable tablePhoto;
	private JTable tableVideo;
	private JTextField textFieldPhotoPath;
	private JTextField textFieldVideoPath;
	private JTextField textFieldSave;
	private JLabel lblfps;
	private JLabel labelThreshold;
	private JSlider sliderFPS;
	private JSlider sliderThreshold;
	public AddVideoProcessTaskView(AddVideoProcessTaskAction addVideoProcessAction) {
		setTitle("\u6DFB\u52A0\u4EFB\u52A1");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/logo.jpg"));
		
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		JScrollPane scrollPanePhoto = new JScrollPane();
		
		JScrollPane scrollPaneVideo = new JScrollPane();
		
		JButton buttonAddPhoto = new JButton("\u6DFB\u52A0\u7167\u7247");
		buttonAddPhoto.addActionListener(addVideoProcessAction);
		buttonAddPhoto.setActionCommand("buttonAddPhoto");
		
		
		textFieldPhotoPath = new JTextField();
		textFieldPhotoPath.setColumns(20);
		
		JButton buttonAddVideo = new JButton("\u6DFB\u52A0\u89C6\u9891");
		buttonAddVideo.addActionListener(addVideoProcessAction);
		buttonAddVideo.setActionCommand("buttonAddVideo");
		
		textFieldVideoPath = new JTextField();
		textFieldVideoPath.setColumns(20);
		
		JLabel label = new JLabel("\u5F85\u67E5\u7167\u7247\u5217\u8868");
		
		JLabel label_1 = new JLabel("\u5F85\u67E5\u89C6\u9891\u5217\u8868");
		
		JButton buttonDelete = new JButton("\u5220\u9664");
		buttonDelete.addActionListener(addVideoProcessAction);
		buttonDelete.setActionCommand("buttonDelete");
		
		JButton buttonSure = new JButton("\u786E\u5B9A");
		buttonSure.addActionListener(addVideoProcessAction);
		buttonSure.setActionCommand("buttonSure");
		
		JButton buttonSave = new JButton("\u4FDD\u5B58");
		buttonSave.addActionListener(addVideoProcessAction);
		buttonSave.setActionCommand("buttonSave");
		
		textFieldSave = new JTextField();
		textFieldSave.setColumns(20);
		
		JLabel label_2 = new JLabel("\u5E27\u7387");
		
		sliderFPS = new JSlider();
		sliderFPS.setSnapToTicks(true);
		sliderFPS.setPaintLabels(true);
		sliderFPS.setPaintTicks(true);
		sliderFPS.setValue(100);
		sliderFPS.setMinorTickSpacing(10);
		sliderFPS.addChangeListener(addVideoProcessAction);
	
		
		lblfps = new JLabel("1*FPS");
		
		sliderThreshold = new JSlider();
		sliderThreshold.setValue(70);
		sliderThreshold.setPaintTicks(true);
		sliderThreshold.setPaintLabels(true);
		sliderThreshold.setMinorTickSpacing(10);
		sliderThreshold.addChangeListener(addVideoProcessAction);
		
		JLabel label_3 = new JLabel("\u8BC6\u522B\u9608\u503C");
		
		labelThreshold = new JLabel("0.7");
	
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(229)
							.addComponent(buttonDelete)
							.addGap(31)
							.addComponent(buttonSure))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(39)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(label)
								.addComponent(label_1)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(25)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(textFieldSave, 0, 0, Short.MAX_VALUE)
										.addComponent(textFieldVideoPath, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
										.addComponent(textFieldPhotoPath))
									.addGap(18)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
											.addComponent(buttonAddVideo)
											.addComponent(buttonAddPhoto))
										.addComponent(buttonSave))
									.addGap(61)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(label_2)
										.addComponent(label_3))
									.addGap(18)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
											.addComponent(sliderFPS, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(lblfps))
										.addComponent(sliderThreshold, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(labelThreshold)
									.addGap(12))
								.addComponent(scrollPanePhoto, GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
								.addComponent(scrollPaneVideo))))
					.addContainerGap(76, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(textFieldPhotoPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(buttonAddPhoto))
								.addComponent(sliderFPS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_2)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(18)
							.addComponent(lblfps)))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(labelThreshold)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(textFieldVideoPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(buttonAddVideo)
									.addComponent(label_3))
								.addComponent(sliderThreshold, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldSave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(buttonSave))
							.addGap(41)
							.addComponent(label)))
					.addGap(18)
					.addComponent(scrollPanePhoto, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(label_1)
					.addGap(18)
					.addComponent(scrollPaneVideo, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
					.addGap(39)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonSure)
						.addComponent(buttonDelete))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		
		tableVideo = new JTable();
		tableVideo.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableVideo.getSelectionModel().addListSelectionListener(addVideoProcessAction);
		
		tableVideo.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"\u5E8F\u53F7", "\u89C6\u9891\u540D", "\u89C6\u9891\u8DEF\u5F84"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableVideo.getColumnModel().getColumn(0).setPreferredWidth(87);
		tableVideo.getColumnModel().getColumn(1).setPreferredWidth(194);
		tableVideo.getColumnModel().getColumn(2).setPreferredWidth(348);
		tableVideo.setRowHeight(20);
		scrollPaneVideo.setViewportView(tableVideo);
		
		tablePhoto = new JTable();
		tablePhoto.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tablePhoto.getSelectionModel().addListSelectionListener(addVideoProcessAction);
		
		tablePhoto.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"\u5E8F\u53F7", "\u7167\u7247\u540D", "\u7167\u7247\u8DEF\u5F84"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tablePhoto.getColumnModel().getColumn(0).setPreferredWidth(87);
		tablePhoto.getColumnModel().getColumn(1).setPreferredWidth(194);
		tablePhoto.getColumnModel().getColumn(2).setPreferredWidth(348);
		tablePhoto.setRowHeight(20);
		scrollPanePhoto.setViewportView(tablePhoto);
		panel.setLayout(gl_panel);
		this.setSize(695, 700);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 350,Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 350);
		this.setVisible(true);
	}
	public JTextField getTextFieldPhotoPath() {
		return textFieldPhotoPath;
	}
	public void setTextFieldPhotoPath(JTextField textFieldPhotoPath) {
		this.textFieldPhotoPath = textFieldPhotoPath;
	}
	public JTextField getTextFieldVideoPath() {
		return textFieldVideoPath;
	}
	public void setTextFieldVideoPath(JTextField textFieldVideoPath) {
		this.textFieldVideoPath = textFieldVideoPath;
	}
	public JTable getTablePhoto() {
		return tablePhoto;
	}
	public void setTablePhoto(JTable tablePhoto) {
		this.tablePhoto = tablePhoto;
	}
	public JTable getTableVideo() {
		return tableVideo;
	}
	public void setTableVideo(JTable tableVideo) {
		this.tableVideo = tableVideo;
	}
	public JTextField getTextFieldSave() {
		return textFieldSave;
	}
	public void setTextFieldSave(JTextField textFieldSave) {
		this.textFieldSave = textFieldSave;
	}
	public JLabel getLblfps() {
		return lblfps;
	}
	public void setLblfps(JLabel lblfps) {
		this.lblfps = lblfps;
	}
	public JLabel getLabelThreshold() {
		return labelThreshold;
	}
	public void setLabelThreshold(JLabel labelThreshold) {
		this.labelThreshold = labelThreshold;
	}
	public JSlider getSliderFPS() {
		return sliderFPS;
	}
	public void setSliderFPS(JSlider sliderFPS) {
		this.sliderFPS = sliderFPS;
	}
	public JSlider getSliderThreshold() {
		return sliderThreshold;
	}
	public void setSliderThreshold(JSlider sliderThreshold) {
		this.sliderThreshold = sliderThreshold;
	}
}
