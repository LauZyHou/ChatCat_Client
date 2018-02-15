package Login;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//既是登录窗口界面,又是其ActionEvent的监听器,又是线程的目标对象
public class LoginClient extends JFrame implements ActionListener, Runnable {
	// 文本框:帐号
	JTextField jtf_nmbr = null;
	// 密码框:密码
	JPasswordField jpf_pswd = null;
	// 按钮:登录
	JButton jb_lgn = null;
	// 通用文本提示字体
	Font fnt_jl = new Font("黑体", 1, 20);
	// 客户端Socket对象
	Socket sckt = null;
	// 输入流,输出流
	DataInputStream dis = null;
	DataOutputStream dos = null;
	// 线程对象
	Thread thrd = null;

	// 构造器
	LoginClient() {
		super("ChatCat登录[正在连接到服务器...]");
		myInit();// 窗体组件初始化
		myConnect();// 连接到后端程序服务器
	}

	// 窗体组件初始化
	private void myInit() {
		// 文本提示:帐号
		JLabel jl_nmbr = new JLabel("帐号:");
		jl_nmbr.setBounds(50, 40, 90, 20);
		jl_nmbr.setFont(fnt_jl);
		jl_nmbr.setForeground(Color.BLACK);// 设置前景色即设置了文字颜色
		this.add(jl_nmbr);

		// 文本提示:密码
		JLabel jl_pswd = new JLabel("密码:");
		jl_pswd.setBounds(50, 80, 90, 20);
		jl_pswd.setFont(fnt_jl);
		jl_pswd.setForeground(Color.BLACK);
		this.add(jl_pswd);

		// 密码框:帐号
		jtf_nmbr = new JTextField(10);
		jtf_nmbr.setBounds(120, 40, 110, 20);
		this.add(jtf_nmbr);

		// 密码框:密码
		jpf_pswd = new JPasswordField(10);
		jpf_pswd.setBounds(120, 80, 110, 20);
		this.add(jpf_pswd);

		// 按钮:登录
		jb_lgn = new JButton("登录");
		jb_lgn.setEnabled(false);// 在连接到服务器之前不可用
		jb_lgn.setBounds(110, 130, 80, 30);
		jb_lgn.addActionListener(this);
		this.add(jb_lgn);

		// 关于窗体本身
		this.setLayout(null);
		this.setBounds(350, 250, 300, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.validate();
	}

	// 连接到后端程序服务器
	private void myConnect() {
		sckt = new Socket();// 无参构造,后面再指定ip地址和端口号
		// 确保没连接,做个判断
		if (sckt.isConnected() == true)
			return;
		try {
			// 网络地址对象:后端程序服务器
			InetAddress ina = InetAddress.getByName("192.168.0.108");
			// 网络地址组合端口的套接字对象:后端程序网络地址对象,3838端口
			InetSocketAddress isa = new InetSocketAddress(ina, 3838);
			// 尝试连接到服务器,可能发生阻塞
			sckt.connect(isa, 10000);// 超时时间10s
			// 用建立好的Socket对象初始化输入输出流,与服务器输出输入流关联
			dis = new DataInputStream(sckt.getInputStream());
			dos = new DataOutputStream(sckt.getOutputStream());
			// 连接完全建立好以后,可以允许登录了
			jb_lgn.setEnabled(true);
			this.setTitle("ChatCat登录[v]已连接到服务器");
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this, "无法解析服务器域名");
			this.setTitle("ChatCat登录[x]无法解析服务器域名");
			// System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "连接服务器超时");
			this.setTitle("ChatCat登录[x]连接服务器超时");
			// System.exit(0);
		}
	}

	// 用本目标对象创建的线程启动时
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	// 注册了本监听器的控件发生ActionEvent事件时
	@Override
	public void actionPerformed(ActionEvent e) {
		// 按下了登录按钮
		if (e.getSource() == jb_lgn) {
			// 获取用户名和密码
			String str_nmbr = jtf_nmbr.getText();
			String str_pswd = new String(jpf_pswd.getPassword());
			try {
				// 加上字符串头表示要登录,发送给后端程序服务器
				dos.writeUTF("[login]" + str_nmbr + "#" + str_pswd);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "与服务器断开");
				this.setTitle("ChatCat登录[x]与服务器断开");
				// System.exit(0);
			}
		}

	}
}
