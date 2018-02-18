package Login;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

//注册的客户端窗口,用3939端口
public class SignUpClient extends JFrame {
	// 组件
	// 标签提示:帐号,密码第一次,密码第二次,网名
	JLabel jl_usrnm, jl_pswd1, jl_pswd2, jl_nm;
	// 文本框:帐号,网名
	JTextField jtf_usrnm, jtf_nm;
	// 密码框:密码第一次,密码第二次,
	JPasswordField jpf_pswd1, jpf_pswd2;
	// 按钮:注册
	JButton jb_log;
	// 标签数组:6个默认头像
	JLabel[] jl_hd = new JLabel[6];
	// 单选框数组:6个单选框
	JRadioButton[] jrb_hd = new JRadioButton[6];
	// 按钮组:用于联结单选框
	ButtonGroup bg;
	// 标签:存yes图标
	JLabel jl_yes;

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

		// 密码框:密码1次
		jpf_pswd1 = new JPasswordField(10);
		jpf_pswd1.setFont(fnt_jl);
		jpf_pswd1.setBounds(170, 98, 150, 30);
		this.add(jpf_pswd1);

		// 密码框:密码2次
		jpf_pswd2 = new JPasswordField(10);
		jpf_pswd2.setFont(fnt_jl);
		jpf_pswd2.setBounds(170, 168, 150, 30);
		this.add(jpf_pswd2);

		// 文本框:昵称
		jtf_nm = new JTextField(10);
		jtf_nm.setFont(fnt_jl);
		jtf_nm.setBounds(170, 238, 150, 30);
		this.add(jtf_nm);

		// 按钮:注册
		jb_log = new JButton("立即注册");
		jb_log.setBounds(160, 500, 90, 40);
		this.add(jb_log);

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
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getSource() == jrb) {
						jl_yes.setBounds(jrb.getX() + 50, jrb.getY() + 10, 30, 30);
					}
				}
			});
			bg.add(jrb_hd[i]);
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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 账号(20000~30000)的数字
	}
}
