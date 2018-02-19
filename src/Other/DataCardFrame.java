package Other;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Kernel.KernelFrame;
import Kernel.MyTools;

//个人资料卡
public class DataCardFrame extends JFrame {
	// 存用户的信息
	String Name, Signature;// 姓名,签名档
	int HeadID, Sex;// 头像号,性别0女1男

	// 组件
	JLabel jl_myhd;// 放头像
	JTextField jtf_nm, jtf_sig;// 昵称,个性签名
	JLabel jl_pswd1, jl_pswd2;// 新密码两个标签
	JPasswordField jpf_pswd1, jpf_pswd2;// 新密码两个密码框
	JLabel jl_ml, jl_fml;// 男女两个标签

	// 资源
	ImageIcon ii_myhd;// 头像ImageIcon
	ImageIcon ii_ml = new ImageIcon("./krnl_pic/male.png");
	ImageIcon ii_fml = new ImageIcon("./krnl_pic/female.png");
	Font fnt_all = new Font("黑体", 1, 20);// 通用字体
	Color clr_all = new Color(230, 240, 230);

	// 其它
	DataOutputStream dos;// KernelFrame传进来的输出流
	boolean isExtnd = false;// 展开情况

	// 构造器,传入数据输出流(只向服务器写)
	public DataCardFrame(DataOutputStream dos, String s) {
		super("ChatCat个人资料卡");
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
		jl_myhd.setBackground(Color.WHITE);// 底色白色作边框
		jl_myhd.setBounds(20, 20, 140, 140);// 大小稍多一截作边框
		this.add(jl_myhd);

		// 文本区:昵称
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
				// 更改男女
				Sex = 1;
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
				// 更改男女
				Sex = 0;
			}
		});
		this.add(jl_fml);

		// 判断男女以决定让哪个JLabel里的图像变大
		if (Sex == 1) {
			ii_ml = new ImageIcon("./krnl_pic/male.png");
			jl_ml.setIcon(ii_ml);
		} else {
			ii_fml = new ImageIcon("./krnl_pic/female.png");
			jl_fml.setIcon(ii_fml);
		}

		// 窗体相关
		this.setBounds(200, 100, 400, 500);
		this.setLayout(null);
		// 窗体颜色
		this.getContentPane().setBackground(clr_all);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();// 释放自己窗体资源
				KernelFrame.dcf = null;// 使这个窗体不被引用
				System.gc();// 立即要求JVM进行垃圾回收
			}
		});
	}

}
