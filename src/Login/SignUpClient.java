package Login;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

//注册的客户端窗口,用3939端口和服务端[注册请求管理/处理线程]交互
public class SignUpClient extends JFrame implements Runnable {
	// 组件
	// 标签提示:帐号,密码第一次,密码第二次,网名
	JLabel jl_usrnm, jl_pswd1, jl_pswd2, jl_nm;
	// 文本框:帐号,网名
	JTextField jtf_usrnm, jtf_nm;
	// 密码框:密码第一次,密码第二次,
	JPasswordField jpf_pswd1, jpf_pswd2;
	// 按钮:注册
	JButton jb_sgn;
	// 标签数组:6个默认头像
	JLabel[] jl_hd = new JLabel[6];
	// 单选框数组:6个单选框
	JRadioButton[] jrb_hd = new JRadioButton[6];
	// 按钮组:用于联结单选框
	ButtonGroup bg;
	// 标签:存yes图标
	JLabel jl_yes;

	// 资源
	// 当前选择的头像号
	int nowHd = 1;
	// 每个<JRadioButton引用,对应的头像号>哈希表
	HashMap<JRadioButton, Integer> hm_jrbTOhd = new HashMap<JRadioButton, Integer>();

	// 其它
	// 通用文本提示字体
	Font fnt_jl = new Font("黑体", 1, 20);
	// 客户端Socket对象
	Socket sckt = null;
	// 输入流,输出流
	DataInputStream dis = null;
	DataOutputStream dos = null;
	// 通信接收线程对象
	Thread thrd;

	// 构造器
	SignUpClient() {
		super("ChatCat注册[正在连接到服务器]");
		myInit();// 窗体组件初始化
		myConnect();// 连接到后端程序服务器
		// 连接建立好以后,可以注册了,单开一个线程用于接收注册结果
		// 而本线程用来向服务器发送账户名和密码,不受子线程的接收时阻塞影响
		thrd = new Thread(this);
		thrd.start();
	}

	// 窗体组件初始化
	private void myInit() {
		// 标签:帐号
		jl_usrnm = new JLabel("请输入帐号:");
		jl_usrnm.setFont(fnt_jl);
		jl_usrnm.setBounds(40, 30, 150, 20);
		this.add(jl_usrnm);

		// 标签:密码1次
		jl_pswd1 = new JLabel("请输入密码:");
		jl_pswd1.setFont(fnt_jl);
		jl_pswd1.setBounds(40, 100, 150, 20);
		this.add(jl_pswd1);

		// 标签:密码2次
		jl_pswd2 = new JLabel("再次输入密码:");
		jl_pswd2.setFont(fnt_jl);
		jl_pswd2.setBounds(20, 170, 150, 20);
		this.add(jl_pswd2);

		// 标签:昵称
		jl_nm = new JLabel("请输入昵称:");
		jl_nm.setFont(fnt_jl);
		jl_nm.setBounds(40, 240, 150, 20);
		this.add(jl_nm);

		// 文本框:帐号
		jtf_usrnm = new JTextField(10);
		jtf_usrnm.setFont(fnt_jl);
		jtf_usrnm.setBounds(170, 28, 150, 30);
		// 帐号在客户端检查只允许输入数字
		jtf_usrnm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int ascii = e.getKeyChar();// 获得ASCII码
				// 退格和TAB是允许的
				if (ascii == 8 && ascii == 9) {
					return;
				}
				// 超过5字符时不能再输入
				else if (jtf_usrnm.getText().length() >= 5) {
					e.consume();
				}
				// 不超过5字符的数字是允许的
				else if (ascii >= 48 && ascii <= 57) {
					return;
				}
				// 其它都不允许
				else {
					e.consume();// 消除键盘事件
				}

			}
		});
		this.add(jtf_usrnm);

		// 密码框:密码1次
		jpf_pswd1 = new JPasswordField(10);
		jpf_pswd1.setFont(fnt_jl);
		jpf_pswd1.setBounds(170, 98, 150, 30);
		// 密码框检查不能超过10字符
		jpf_pswd1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (new String(jpf_pswd1.getPassword()).length() >= 10) {
					e.consume();// 超过10字符时取消键盘事件
				}
			}
		});
		this.add(jpf_pswd1);

		// 密码框:密码2次
		jpf_pswd2 = new JPasswordField(10);
		jpf_pswd2.setFont(fnt_jl);
		jpf_pswd2.setBounds(170, 168, 150, 30);
		// 密码框检查不能超过10字符
		jpf_pswd2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (new String(jpf_pswd2.getPassword()).length() >= 10) {
					e.consume();// 超过10字符时取消键盘事件
				}
			}
		});
		this.add(jpf_pswd2);

		// 文本框:昵称
		jtf_nm = new JTextField(10);
		jtf_nm.setFont(fnt_jl);
		jtf_nm.setBounds(170, 238, 150, 30);
		// 昵称在客户端检查不能超过10字符
		jtf_nm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (jtf_nm.getText().length() >= 10) {
					e.consume();// 超过10字符时取消键盘事件
				}
			}
		});
		this.add(jtf_nm);

		// 按钮:注册
		jb_sgn = new JButton("立即注册");
		jb_sgn.setBounds(160, 500, 90, 40);
		jb_sgn.setEnabled(false);// 连接服务器成功前不可用
		jb_sgn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trySignUp();// 尝试注册
			}
		});
		this.add(jb_sgn);

		// // 标签:头像
		// ImageIcon ii_myhd;
		// for (int i = 0; i < jl_hd.length; i++) {
		// // 设定自己的头像
		// // 头像的ImageIcon对象
		// ii_myhd = new ImageIcon("./pic/cat" + (i + 1) + ".jpeg");
		// // 对头像两步缩放
		// ii_myhd.setImage(ii_myhd.getImage().getScaledInstance(50, 50,
		// Image.SCALE_DEFAULT));
		// // 用头像去构造这个Jlabel
		// jl_hd[i] = new JLabel(ii_myhd);
		// jl_hd[i].setOpaque(true);// 设置JLabel为不透明才能看见底色(作边框)
		// jl_hd[i].setBackground(Color.WHITE);// 底色白色作边框
		// // 大小稍多一截作边框
		// jl_hd[i].setBounds(60 + (i % 3) * 100, 300 + (i / 3) * 100, 60, 60);
		// // 设定鼠标悬停特效,这里没法访问到jl_hd[i],拿其引用出来
		// JLabel jl_cp = jl_hd[i];
		// jl_hd[i].addMouseListener(new MouseAdapter() {
		// // 鼠标进入
		// @Override
		// public void mouseEntered(MouseEvent e) {
		// jl_cp.setBackground(new Color(160, 160, 240));
		// }
		//
		// // 鼠标退出
		// @Override
		// public void mouseExited(MouseEvent e) {
		// jl_cp.setBackground(Color.WHITE);
		// }
		// });
		// this.add(jl_hd[i]);
		// }

		// yes标签
		ImageIcon ii_yes = new ImageIcon("./krnl_pic/yes.png");
		ii_yes.setImage(ii_yes.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		jl_yes = new JLabel(ii_yes);
		this.add(jl_yes);

		// 按钮组和单选框:头像下
		bg = new ButtonGroup();
		ImageIcon ii_myhd;
		for (int i = 0; i < jrb_hd.length; i++) {
			// 头像的ImageIcon对象
			ii_myhd = new ImageIcon("./pic/cat" + (i + 1) + ".jpeg");
			// 对头像两步缩放
			ii_myhd.setImage(ii_myhd.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
			jrb_hd[i] = new JRadioButton(ii_myhd, false);
			jrb_hd[i].setBounds(60 + (i % 3) * 100, 300 + (i / 3) * 100, 50, 50);
			// jrb_hd[i]对内部类(接口)不可见,取其引用
			JRadioButton jrb = jrb_hd[i];
			jrb.addItemListener(new ItemListener() {
				// 先发生的是"失去选中",再发生"获得选中",都引起这个方法调用
				@Override
				public void itemStateChanged(ItemEvent e) {
					// 如果是"获得选中"
					if (jrb.isSelected()) {
						// yes标签位置改变
						jl_yes.setBounds(jrb.getX() + 50, jrb.getY() + 10, 30, 30);
						nowHd = hm_jrbTOhd.get(jrb);
						// System.out.println(nowHd);// 测试输出
					}
				}
			});
			bg.add(jrb_hd[i]);// 添加到按钮组
			hm_jrbTOhd.put(jrb_hd[i], i + 1);// 添加到哈希表,注意+1关系
			this.add(jrb_hd[i]);
		}
		// 默认是第0个被选中
		jrb_hd[0].setSelected(true);
		// yes标签默认在第0个头像旁
		jl_yes.setBounds(jrb_hd[0].getX() + 50, jrb_hd[0].getY() + 10, 30, 30);

		// 窗体相关
		this.setLayout(null);
		this.setResizable(false);
		this.setBounds(300, 100, 400, 600);
		this.setVisible(true);
		// 在后面覆写关闭时的方法,否则不能正确识别[放弃注册]
		// 因为关闭连接时一定发生放弃注册,所以不用考虑直接结束整个进程的情况
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					sckt.close();// 关闭Socket连接,告诉服务器已断开
				} catch (IOException e1) {
					// 可能会发生异常,发生异常时还是要确保连接关闭
					// 首先让这个Socket连接不再被引用
					sckt = null;
					// JVM立即进行垃圾回收,销毁这个变量以告诉服务器断开
					System.gc();
				} finally {
					dispose();// 关闭窗口
				}
			}
		});
		this.getContentPane().setBackground(new Color(220, 240, 230));
	}

	// 连接到后端程序服务器
	private void myConnect() {
		sckt = new Socket();// 无参构造,后面再指定ip地址和端口号
		// 确保没连接,做个判断
		if (sckt.isConnected() == true)
			return;
		try {
			// 网络地址对象:后端程序服务器
			InetAddress ina = InetAddress.getByName(Main.ServerIp);
			// 网络地址组合端口的套接字对象:后端程序网络地址对象,3939端口
			InetSocketAddress isa = new InetSocketAddress(ina, 3939);
			// 尝试连接到服务器,可能发生阻塞
			sckt.connect(isa, 10000);// 超时时间10s
			// 用建立好的Socket对象初始化输入输出流,与服务器输出输入流关联
			dis = new DataInputStream(sckt.getInputStream());
			dos = new DataOutputStream(sckt.getOutputStream());
			// 连接完全建立好以后,可以尝试注册了
			jb_sgn.setEnabled(true);// 按钮使用开放
			this.setTitle("ChatCat注册[v]已连接到服务器");
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this, "无法解析服务器域名");
			this.setTitle("ChatCat注册[x]无法解析服务器域名");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "连接服务器超时");
			this.setTitle("ChatCat注册[x]连接服务器超时");
		}
	}

	// 因为从数据流中读可能发生阻塞,所以放在子线程中
	@Override
	public void run() {
		String s = null;// 存储返回来的成败信息
		try {
			// 不停地尝试读入返回来的注册成败信息,只要发生异常就跳出循环
			while (true) {
				// 从输入流读入注册结果
				s = dis.readUTF();// 子线程阻塞之处
				// 注册成功/失败信息都用警告框展示
				JOptionPane.showMessageDialog(this, s);
				// 如果以"[v]"开头,说明注册成功了
				if (s.startsWith("[v]")) {
					this.dispose();// 销毁注册框
					break;// 不用继续尝试读信息了,退出循环
				}
			}
		} catch (IOException e) {
			// 注册框直接关闭(放弃注册)时会发生此异常
		}

	}

	// 尝试注册
	private void trySignUp() {
		// 判空
		if (jpf_pswd1.getPassword().length == 0 || jpf_pswd2.getPassword().length == 0 || jtf_nm.getText().isEmpty()
				|| jtf_usrnm.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "[x]每一项都不能为空");
			return;
		}
		// 拿出两次输入的密码并在客户端判断
		String Passwd1 = new String(jpf_pswd1.getPassword());
		String Passwd2 = new String(jpf_pswd2.getPassword());
		if (!Passwd1.equals(Passwd2)) {
			JOptionPane.showMessageDialog(this, "[x]两次密码不一致");
			// 清空
			jpf_pswd1.setText("");
			jpf_pswd2.setText("");
			// 焦点到第一个密码框
			jpf_pswd1.grabFocus();
			return;// 结束这个方法
		}
		// 运行至此,客户端端没有要判别的输入了,拿出其它信息并发给服务端
		String UsrNum = jtf_usrnm.getText();
		String Name = jtf_nm.getText();
		try {
			// 加上字符串头表示要注册,发送给后端程序服务器
			dos.writeUTF("[sign]" + UsrNum + "#" + Passwd1 + "#" + Name + "#" + nowHd);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "与服务器断开");
			this.setTitle("ChatCat注册[x]与服务器断开");
		}
	}
}
