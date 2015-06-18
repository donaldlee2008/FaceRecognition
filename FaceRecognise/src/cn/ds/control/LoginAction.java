/**
 * Project             :FaceRecognise project
 * Comments            :登陆控制类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 * 2 | 2013-8-16 | 添加回车键登陆功能；加载界面时出现动态提示效果 | jxm 
 */
package cn.ds.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.ds.domain.User;
import cn.ds.model.LoginModel;
import cn.ds.service.RecogniseService;
import cn.ds.view.LoadingView;
import cn.ds.view.LoginView;

public class LoginAction implements ActionListener, ChangeListener, KeyListener {

	// 登陆界面
	private LoginView loginView;
	// RecogniseService服务
	private RecogniseService recogniseService;
	// 存放RecogniseService服务结果
	private BlockingQueue<Integer> recogniseResult = new LinkedBlockingDeque<Integer>();
	// 登陆模型
	private LoginModel userModel;

	/**
	 * Description :构造函数
	 * 
	 * @return LoginAction
	 */
	public LoginAction() {

		loginView = new LoginView(this);

		// 重写loginView窗口的关闭方法
		loginView.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (RecogniseService.getInstance() != null) {
					// 结束RecogniseService线程
					RecogniseService.getInstance().setFlag(false);

					// 释放摄像头capture资源
					RecogniseService.getInstance().releaseCapture();
				}

				System.exit(0);
			}
		});
		userModel = new LoginModel();
	}

	/**
	 * Description :登陆界面的按钮响应函数
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("buttonLogin")) {// 登陆按钮响应
			// if (this.loginView.getTabbedPane().getSelectedIndex() == 0) {//
			// 用户名加密码登陆
			//
			// User tempUser = new User();
			// tempUser.setName(this.loginView.getTextFieldAccount().getText());
			// tempUser.setPwd(new String(this.loginView.getPasswordField()
			// .getPassword()));
			//
			// // 检查用户是否存在
			// boolean result = userModel.checkUserByPwd(tempUser);
			//
			// if (!result) {// 不存在，弹出提示对话框
			// JOptionPane.showMessageDialog(loginView, "密码或是账号错误");
			// } else {// 存在
			// loginView.dispose();
			//
			// if (tempUser.getId() == 0) {// ID号为0，管理员用户
			//
			// // 非管理模式登陆，参数为1
			// // new ManageAction(1);
			// openManage(1);
			// } else {// ID号不为0，普通用户
			// // 非管理模式登陆，参数为1
			// new CommonUserAction(tempUser.getId(), 1);
			// }
			// }
			// } else if (this.loginView.getTabbedPane().getSelectedIndex() ==
			// 1)
			// {// 人脸识别登陆模式
			//
			// // 将RecogniseService，设置为人脸识别模式
			// RecogniseService.getInstance(recogniseResult).recognitionMode();
			//
			// try {
			// // 取出结果
			// int r = recogniseResult.take();
			// if (r == 0) {// ID为0，管理员
			//
			// // 非管理模式登陆，参数为1
			// // new ManageAction(1);
			// openManage(1);
			//
			// loginView.dispose();
			//
			// RecogniseService.getInstance(null).waitMode();
			// } else if (r == -1) {// 不存在该人，弹出提示对话框
			// JOptionPane.showMessageDialog(loginView, "未注册用户！");
			// } else if (r == -2) {
			// JOptionPane.showMessageDialog(loginView,
			// "还未进行人脸库更新，人脸识别功能不可用");
			// } else {// ID号非0，普通用户
			//
			// // 非管理模式登陆，参数为1
			// new CommonUserAction(r, 1);
			//
			// loginView.dispose();
			//
			// RecogniseService.getInstance(null).waitMode();
			// }
			//
			// } catch (Exception e2) {
			// e2.printStackTrace();
			// }
			// } else if (this.loginView.getTabbedPane().getSelectedIndex() ==
			// 2)
			// {// 管理模式登陆
			//
			// User tempUser = new User();
			// tempUser.setName(this.loginView.getTextFieldMAccount()
			// .getText());
			// tempUser.setPwd(new String(this.loginView.getPasswordFieldM()
			// .getPassword()));
			//
			// // 先检查用户名加密码是否正确
			// boolean result = userModel.checkUserByPwd(tempUser);
			//
			// if (result) {// 用户名加密码正确
			//
			// int r = -1;
			//
			// // 人脸识别
			// RecogniseService.getInstance(recogniseResult)
			// .recognitionMode();
			//
			// try {
			// // 取得结果
			// r = recogniseResult.take();
			// } catch (Exception e2) {
			// e2.printStackTrace();
			// }
			//
			// if (r == tempUser.getId()) {// 用户名加密码和人脸识别结果一致
			// if (r == 0) {// 管理员
			//
			// // 管理模式，参数为0
			// // new ManageAction(0);
			// openManage(0);
			// } else {// 普通用户
			//
			// // 管理模式，参数为0
			// new CommonUserAction(r, 0);
			// }
			//
			// loginView.dispose();
			// RecogniseService.getInstance(null).waitMode();
			// } else if (r == -1) {// 人脸识别未通过
			//
			// if ("是".equals(tempUser.getHavePhoto())) {
			// JOptionPane.showMessageDialog(loginView, "人脸识别未通过");
			// } else if ("否".equals(tempUser.getHavePhoto())) {
			// /**
			// * 若为管理员，则允许其进入到管理模式，本系统的安全责任由管理员承担，
			// * 所以在其第一次进入系统后建议立即录入照片
			// */
			//
			// if (tempUser.getId() == 0) {
			// // 提示信息，提示管理员进入后录入照片
			// JOptionPane.showMessageDialog(loginView,
			// "管理员身份，登录后请务必录入照片并训练");
			//
			// // 管理模式进入，参数为0
			// // new ManageAction(0);
			// openManage(0);
			//
			// loginView.dispose();
			// RecogniseService.getInstance(null).waitMode();
			// } else {
			// /**
			// * 若为普通用户，在没有进行人脸识别的情况下，为了提高安全性，不允许进入管理模式，
			// * 如果需要更改密码等信息的话， 可以去管理员处更改
			// */
			// JOptionPane.showMessageDialog(loginView,
			// "未录入照片，不允许进入管理模式");
			//
			// // 如果设定普通用户可以登陆，可以讲下面的注释去掉
			// // JOptionPane.showMessageDialog(loginView,
			// // "为增强安全性，建议录入照片");
			// // new CommonUserAction(r, 0);
			// }
			// }
			//
			// } else if (r == -2) {
			//
			// if (tempUser.getId() == 0) {
			// // 提示信息，提示管理员进入后录入照片
			// JOptionPane.showMessageDialog(loginView,
			// "管理员：还未进行人脸库更新，人脸识别功能不可用，请尽快更新人脸库");
			//
			// // 管理模式进入，参数为0
			// // new ManageAction(0);
			// openManage(0);
			//
			// loginView.dispose();
			// RecogniseService.getInstance(null).waitMode();
			// } else {
			// JOptionPane.showMessageDialog(loginView,
			// "还未进行人脸库更新，人脸识别功能不可用");
			// }
			// } else {// 人脸识别通过，但是和用户名加密码的结果不一致
			// JOptionPane.showMessageDialog(loginView,
			// "人脸识别与登录账号不是同一人");
			// }
			//
			// } else {// 账号加密码错误
			// JOptionPane.showMessageDialog(loginView, "密码或是账号错误");
			// }
			// }
			login();

		} else if (e.getActionCommand().equals("buttonCancle")) {// 取消登陆
			if (RecogniseService.getInstance() != null) {

				// 结束RecogniseService线程
				RecogniseService.getInstance().setFlag(false);

				// 释放摄像头capture资源
				RecogniseService.getInstance().releaseCapture();

				// System.exit(0);
			}
			System.exit(0);

		}
	}

	/**
	 * Description :响应TabbedPane标签改变
	 * 
	 * @param e
	 *            :响应事件
	 * @return void
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (recogniseService == null) {// 如果RecogniseService为空，则创建RecogniseService
					recogniseService = RecogniseService.getInstance(null);

				}

				if (LoginAction.this.loginView.getTabbedPane()
						.getSelectedIndex() > 0) {// 在人脸识别登陆模式和管理模式下需要人脸识别服务，如果等待则唤醒
					if (recogniseService.getState() == State.WAITING) {
						RecogniseService.getInstance(null).Resume();
					}
				} else if (LoginAction.this.loginView.getTabbedPane()
						.getSelectedIndex() == 0) {// 若切换到用户名加密码模式，不需要人脸识别，则让其处于等待状态
					if (recogniseService.getState() != State.WAITING) {
						// recogniseService.setM_mode(MODES.MODE_WAIT);
						recogniseService.waitMode();
					}
				}

			}
		}).start();

	}

	public void login() {
		if (this.loginView.getTabbedPane().getSelectedIndex() == 0) {// 用户名加密码登陆

			User tempUser = new User();
			tempUser.setName(this.loginView.getTextFieldAccount().getText());
			tempUser.setPwd(new String(this.loginView.getPasswordField()
					.getPassword()));

			// 检查用户是否存在
			boolean result = userModel.checkUserByPwd(tempUser);

			if (!result) {// 不存在，弹出提示对话框
				JOptionPane.showMessageDialog(loginView, "密码或是账号错误");
			} else {// 存在
				loginView.dispose();

				if (tempUser.getId() == 0) {// ID号为0，管理员用户

					// 非管理模式登陆，参数为1
					// new ManageAction(1);
					openManage(1);
				} else {// ID号不为0，普通用户
					// 非管理模式登陆，参数为1
					//new CommonUserAction(tempUser.getId(), 1);
					openCommonUser(tempUser.getId(), 1);
				}
			}
		} else if (this.loginView.getTabbedPane().getSelectedIndex() == 1) {// 人脸识别登陆模式

			// 将RecogniseService，设置为人脸识别模式
			RecogniseService.getInstance(recogniseResult).recognitionMode();

			try {
				// 取出结果
				int r = recogniseResult.take();
				if (r == 0) {// ID为0，管理员

					// 非管理模式登陆，参数为1
					// new ManageAction(1);
					openManage(1);

					loginView.dispose();

					RecogniseService.getInstance(null).waitMode();
				} else if (r == -1) {// 不存在该人，弹出提示对话框
					JOptionPane.showMessageDialog(loginView, "未注册用户！");
				} else if (r == -2) {
					JOptionPane.showMessageDialog(loginView,
							"还未进行人脸库更新，人脸识别功能不可用");
				} else {// ID号非0，普通用户

					// 非管理模式登陆，参数为1
					//new CommonUserAction(r, 1);
					openCommonUser(r, 1);

					loginView.dispose();

					RecogniseService.getInstance(null).waitMode();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else if (this.loginView.getTabbedPane().getSelectedIndex() == 2) {// 管理模式登陆

			User tempUser = new User();
			tempUser.setName(this.loginView.getTextFieldMAccount().getText());
			tempUser.setPwd(new String(this.loginView.getPasswordFieldM()
					.getPassword()));

			// 先检查用户名加密码是否正确
			boolean result = userModel.checkUserByPwd(tempUser);

			if (result) {// 用户名加密码正确

				int r = -1;

				// 人脸识别
				RecogniseService.getInstance(recogniseResult).recognitionMode();

				try {
					// 取得结果
					r = recogniseResult.take();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				if (r == tempUser.getId()) {// 用户名加密码和人脸识别结果一致
					if (r == 0) {// 管理员

						// 管理模式，参数为0
						// new ManageAction(0);
						openManage(0);
					} else {// 普通用户

						// 管理模式，参数为0
						//new CommonUserAction(r, 0);
						openCommonUser(r, 0);
					}

					loginView.dispose();
					RecogniseService.getInstance(null).waitMode();
				} else if (r == -1) {// 人脸识别未通过

					if ("是".equals(tempUser.getHavePhoto())) {
						JOptionPane.showMessageDialog(loginView, "人脸识别未通过");
					} else if ("否".equals(tempUser.getHavePhoto())) {
						/**
						 * 若为管理员，则允许其进入到管理模式，本系统的安全责任由管理员承担，
						 * 所以在其第一次进入系统后建议立即录入照片
						 */

						if (tempUser.getId() == 0) {
							// 提示信息，提示管理员进入后录入照片
							JOptionPane.showMessageDialog(loginView,
									"管理员身份，登录后请务必录入照片并训练");

							// 管理模式进入，参数为0
							// new ManageAction(0);
							openManage(0);

							loginView.dispose();
							RecogniseService.getInstance(null).waitMode();
						} else {
							/**
							 * 若为普通用户，在没有进行人脸识别的情况下，为了提高安全性，不允许进入管理模式，
							 * 如果需要更改密码等信息的话， 可以去管理员处更改
							 */
							JOptionPane.showMessageDialog(loginView,
									"未录入照片，不允许进入管理模式");

							// 如果设定普通用户可以登陆，可以讲下面的注释去掉
							// JOptionPane.showMessageDialog(loginView,
							// "为增强安全性，建议录入照片");
							// new CommonUserAction(r, 0);
						}
					}

				} else if (r == -2) {

					if (tempUser.getId() == 0) {
						// 提示信息，提示管理员进入后录入照片
						JOptionPane.showMessageDialog(loginView,
								"管理员：还未进行人脸库更新，人脸识别功能不可用，请尽快更新人脸库");

						// 管理模式进入，参数为0
						// new ManageAction(0);
						openManage(0);

						loginView.dispose();
						RecogniseService.getInstance(null).waitMode();
					} else {
						JOptionPane.showMessageDialog(loginView,
								"还未进行人脸库更新，人脸识别功能不可用");
					}
				} else {// 人脸识别通过，但是和用户名加密码的结果不一致
					JOptionPane.showMessageDialog(loginView, "人脸识别与登录账号不是同一人");
				}

			} else {// 账号加密码错误
				JOptionPane.showMessageDialog(loginView, "密码或是账号错误");
			}
		}
	}

	public void openManage(int mode) {
		new OpenManageThread(mode).start();
	}

	class OpenManageThread extends Thread {
		private int mode;
		private BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();

		public OpenManageThread(int mode) {
			this.mode = mode;
		}

		@Override
		public void run() {
			String content = "<html>管理员用户，您好：<br/>已经登录成功，现在正在加载管理界面，请稍等。</html>";

			LoadingView loadingView = new LoadingView(content);

			new ManageAction(mode, result);
			try {
				result.take();
			} catch (Exception e) {
				// TODO: handle exception
			}
			loadingView.dispose();
		}
	}

	public void openCommonUser(int id, int mode) {
		new OpenCommonUserThread(id, mode).start();
	}

	class OpenCommonUserThread extends Thread {
		private int mode;
		private int id;
		private BlockingQueue<Boolean> result = new LinkedBlockingQueue<Boolean>();

		public OpenCommonUserThread(int id, int mode) {
			this.mode = mode;
			this.id = id;
		}

		@Override
		public void run() {
			String content = "<html>您好：<br/>已经登录成功，现在正在加载管理界面，请稍等。</html>";

			LoadingView loadingView = new LoadingView(content);

			new CommonUserAction(id, mode,result);

			try {
				result.take();
			} catch (Exception e) {
				// TODO: handle exception
			}
			loadingView.dispose();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			login();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
