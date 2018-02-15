package Kernel;

import javax.swing.JFrame;

//主要界面
public class KernelFrame extends JFrame {
	String str_name;// 用户名Name
	String str_id;// 头像的编号HeadID

	// 构造器
	public KernelFrame(String s) {
		myResolve(s);// 解析服务器返回的信息
		myInit();// 窗体初始化
	}

	// 解析服务器返回的信息
	private void myResolve(String s) {
		// 解析用户名Name
		this.str_name = s.substring(s.indexOf("#用户名:") + 5, s.indexOf("#头像ID:"));
		// 解析头像的编号HeadID
		this.str_id = s.substring(s.indexOf("#头像ID:") + 6);
		System.out.println(str_name + " " + str_id);
	}

	// 窗体初始化
	private void myInit() {
		this.setBounds(700, 0, 250, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

}