package Login;

import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

//注册的客户端窗口,用3939端口
public class SignUpClient extends JFrame {
	// 组件
	// 标签提示:帐号,密码第一次,密码第二次,网名
	JLabel jl_usrnm, jl_pswd1, jl_pswd2, jl_nm;
	// 文本框:帐号,密码第一次,密码第二次,网名
	JTextField jtf_usrnm, jtf_pswd1, jtf_pswd2, jtf_nm;
	// 按钮:注册
	JButton jb_log;

	// 其它
	// 通用文本提示字体
	Font fnt_jl = new Font("黑体", 1, 20);
	// 客户端Socket对象
	Socket sckt = null;
	// 输入流,输出流
	DataInputStream dis = null;
	DataOutputStream dos = null;
	// 通信接收线程对象

	// 构造器
	SignUpClient() {
		super("ChatCat注册[正在连接到服务器]");
		myInit();// 窗体组件初始化
	}

	// 窗体组件初始化
	private void myInit() {
		// 标签:帐号
		jl_usrnm = new JLabel("请输入帐号:");
		jl_usrnm.setFont(fnt_jl);
		jl_usrnm.setBounds(30, 30, 150, 20);
		this.add(jl_usrnm);

		// 标签:密码1次
		jl_pswd1 = new JLabel("请输入密码:");
		jl_pswd1.setFont(fnt_jl);
		jl_pswd1.setBounds(30, 100, 150, 20);
		this.add(jl_pswd1);

		// 标签:密码2次
		jl_pswd2 = new JLabel("再次输入密码:");
		jl_pswd2.setFont(fnt_jl);
		jl_pswd2.setBounds(20, 170, 150, 20);
		this.add(jl_pswd2);

		// 标签:昵称
		jl_nm = new JLabel("请输入昵称:");
		jl_nm.setFont(fnt_jl);
		jl_nm.setBounds(30, 240, 150, 20);
		this.add(jl_nm);

		// 文本框:帐号
		jtf_usrnm = new JTextField(10);
		jtf_usrnm.setFont(fnt_jl);
		jtf_usrnm.setBounds(170, 28, 150, 30);
		this.add(jtf_usrnm);

		// 文本框:密码1次
		jtf_pswd1 = new JTextField(10);
		jtf_pswd1.setFont(fnt_jl);
		jtf_pswd1.setBounds(170, 98, 150, 30);
		this.add(jtf_pswd1);

		// 文本框:密码2次
		jtf_pswd2 = new JTextField(10);
		jtf_pswd2.setFont(fnt_jl);
		jtf_pswd2.setBounds(170, 168, 150, 30);
		this.add(jtf_pswd2);

		// 文本框:昵称
		jtf_nm = new JTextField(10);
		jtf_nm.setFont(fnt_jl);
		jtf_nm.setBounds(170, 238, 150, 30);
		this.add(jtf_nm);

		// 按钮:注册
		jb_log = new JButton("立即注册");
		jb_log.setBounds(160, 500, 90, 40);
		this.add(jb_log);

		// 窗体相关
		this.setLayout(null);
		this.setBounds(300, 100, 400, 600);
		this.setVisible(true);

		// 账号(20000~30000)的数字
	}
}
