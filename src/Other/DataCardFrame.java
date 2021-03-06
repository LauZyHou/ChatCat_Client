package Other;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Kernel.KernelFrame;
import Kernel.MyTools;

//个人资料卡
public class DataCardFrame extends JFrame {
	// 存用户的信息
	String Name, Signature;// 昵称,签名档
	int HeadID, Sex;// 头像号,性别0女1男

	// 组件
	JLabel jl_myhd;// 放头像
	public JTextField jtf_nm;// 昵称
	public JTextArea jta_sgntr;// 个性签名
	JLabel jl_pswd1, jl_pswd2;// 新密码两个标签
	JPasswordField jpf_pswd1, jpf_pswd2;// 新密码两个密码框
	JLabel jl_ml, jl_fml;// 男女两个标签
	public JButton jb_lck, jb_snd;// 解锁,发送

	// 资源
	ImageIcon ii_myhd;// 头像ImageIcon
	ImageIcon ii_ml = new ImageIcon("./krnl_pic/male.png");
	ImageIcon ii_fml = new ImageIcon("./krnl_pic/female.png");
	Font fnt_all = new Font("黑体", 1, 20);// 通用字体
	Color clr_all = new Color(225, 220, 240);
	public static Color clr_ml = new Color(120, 200, 230);// 男性颜色
	public static Color clr_fml = new Color(230, 120, 230);// 女性颜色

	// 其它
	DataOutputStream dos;// KernelFrame传进来的输出流
	boolean isExtnd = false;// 展开情况

	// 构造器,传入数据输出流(只向服务器写)
	public DataCardFrame(DataOutputStream dos, String s) {
		super("ChatCat个人资料卡");
		this.dos = dos;
		myResolve(s);// 解析字符串
		myInit();// 初始化窗体
	}

	// 解析字符串
	private void myResolve(String s) {
		Name = s.substring(s.indexOf("]") + 1, s.indexOf("#"));
		HeadID = Integer.parseInt(s.substring(s.indexOf("#") + 1, MyTools.indexOf(s, 2, "#")));
		Sex = Integer.parseInt(s.substring(MyTools.indexOf(s, 2, "#") + 1, s.lastIndexOf("#")));
		Signature = s.substring(s.lastIndexOf("#") + 1);
	}

	// 初始化窗体
	private void myInit() {
		// 标签:头像
		// 头像的ImageIcon对象
		ii_myhd = new ImageIcon("./pic/cat" + HeadID + ".jpeg");
		// 对头像两步缩放
		ii_myhd.setImage(ii_myhd.getImage().getScaledInstance(125, 125, Image.SCALE_DEFAULT));
		// 用这个ImageIcon对象设置头像入JLabel
		jl_myhd = new JLabel(ii_myhd);
		jl_myhd.setOpaque(true);// 设置JLabel为不透明才能看见底色(作边框)
		// jl_myhd.setBackground(Color.WHITE);// 底色作边框
		jl_myhd.setBounds(20, 20, 140, 140);// 大小稍多一截作边框
		this.add(jl_myhd);

		// 文本框:昵称
		jtf_nm = new JTextField(10);
		jtf_nm.setEditable(false);
		jtf_nm.setText(Name);
		jtf_nm.setFont(fnt_all);
		jtf_nm.setBounds(180, 20, 180, 40);
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

		// 标签:性别男
		ii_ml.setImage(ii_ml.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
		jl_ml = new JLabel(ii_ml);
		jl_ml.setBounds(170, 70, 100, 100);
		// jl_ml.setOpaque(true);
		// jrb_ml.setBackground(clr_all);
		jl_ml.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ii_ml = new ImageIcon("./krnl_pic/male.png");
				jl_ml.setIcon(ii_ml);
				ii_fml = new ImageIcon("./krnl_pic/female.png");
				ii_fml.setImage(ii_fml.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
				jl_fml.setIcon(ii_fml);
				// 更改男女,头像边框颜色
				Sex = 1;
				jl_myhd.setBackground(clr_ml);
			}
		});
		this.add(jl_ml);

		// 标签:性别女
		ii_fml.setImage(ii_fml.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
		jl_fml = new JLabel(ii_fml);
		jl_fml.setBounds(280, 70, 100, 100);
		// jl_fml.setOpaque(true);
		// jrb_fml.setBackground(clr_all);
		jl_fml.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ii_fml = new ImageIcon("./krnl_pic/female.png");
				jl_fml.setIcon(ii_fml);
				ii_ml = new ImageIcon("./krnl_pic/male.png");
				ii_ml.setImage(ii_ml.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
				jl_ml.setIcon(ii_ml);
				// 更改男女,头像边框颜色
				Sex = 0;
				jl_myhd.setBackground(clr_fml);
			}
		});
		this.add(jl_fml);

		// 判断男女以决定让哪个JLabel里的图像变大
		if (Sex == 1) {
			ii_ml = new ImageIcon("./krnl_pic/male.png");
			jl_ml.setIcon(ii_ml);
			jl_myhd.setBackground(clr_ml);
		} else {
			ii_fml = new ImageIcon("./krnl_pic/female.png");
			jl_fml.setIcon(ii_fml);
			jl_myhd.setBackground(clr_fml);
		}

		// 文本提示:个性签名
		JLabel jl = new JLabel("个性签名:");
		jl.setFont(new Font("黑体", Font.BOLD, 16));
		jl.setBounds(40, 180, 80, 20);
		this.add(jl);

		// 文本区域:个性签名
		jta_sgntr = new JTextArea(5, 10);
		jta_sgntr.setEditable(false);
		// jta_sgntr.setEnabled(false);
		jta_sgntr.setLineWrap(true);
		jta_sgntr.setFont(fnt_all);
		jta_sgntr.setText(Signature);
		jta_sgntr.setBounds(20, 200, 360, 200);
		this.add(jta_sgntr);

		// 按钮:解锁
		jb_lck = new JButton("解锁");
		jb_lck.setBounds(60, 420, 110, 30);
		jb_lck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jtf_nm.setEditable(true);
				jta_sgntr.setEditable(true);
				if (jta_sgntr.getText().equals("null")) {
					jta_sgntr.setText("");
				}
				jb_snd.setEnabled(true);
				jb_snd.grabFocus();
				jb_lck.setEnabled(false);// 自己暂时不可用
			}
		});
		jb_lck.setBackground(new Color(210, 200, 250));
		jb_lck.setFocusable(false);
		this.add(jb_lck);

		// 按钮:发送
		jb_snd = new JButton("保存修改");
		jb_snd.setBounds(230, 420, 110, 30);
		jb_snd.setEnabled(false);
		jb_snd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO HeadID头像的更新
				Name = jtf_nm.getText();
				Signature = jta_sgntr.getText();
				// 个签的长度在客户端拦截
				if (Signature.length() >= 40) {
					JOptionPane.showMessageDialog(null, "[x]个性签名太长了");
					jta_sgntr.grabFocus();
					return;
				}
				try {
					jb_snd.setEnabled(false);// 在服务器回送前关闭使用
					// 拼起来发送给服务器,因为服务器单开一个[消息处理线程],会知道帐号
					dos.writeUTF("[changecard]" + Name + "#" + HeadID + "#" + Sex + "#" + Signature);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		jb_snd.setBackground(Color.WHITE);
		jb_snd.setFocusable(false);
		this.add(jb_snd);

		// 窗体相关
		this.setBounds(200, 100, 400, 500);
		this.setLayout(null);
		// 窗体颜色
		this.getContentPane().setBackground(clr_all);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// 把这行注释!然后setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
				// 否则有时会出现运行时错误
				// dispose();// 释放自己窗体资源
				KernelFrame.dcf = null;// 使这个窗体不被引用
				System.gc();// 立即要求JVM进行垃圾回收
			}
		});
	}

}
