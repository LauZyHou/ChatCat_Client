package Other;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Kernel.KernelFrame;

//个人资料卡
public class DataCardFrame extends JFrame {
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
	// 传进来的Socket连接对象
	Socket sckt;
	// 输入输出流
	DataInputStream dis;
	DataOutputStream dos;
	boolean isExtnd = false;// 展开情况
	boolean male;// fale女true男

	// 构造器,传入数据输出流(只向服务器写)
	public DataCardFrame(Socket sckt) {
		super("ChatCat个人资料卡");
		this.sckt = sckt;
		query();// 查询数据库
		myInit();// 初始化窗体
	}

	// 查询数据库
	private void query() {
		try {
			dis = new DataInputStream(sckt.getInputStream());
			dos = new DataOutputStream(sckt.getOutputStream());
			// 发送请求给服务器
			dos.writeUTF("[mycard]" + KernelFrame.str_nmbr);
			// 单开一个线程,从服务器拿回结果,防止阻塞影响其它线程
			Thread thrd = new Thread() {
				@Override
				public void run() {
					String result;
					try {
						result = dis.readUTF();
						System.out.println(result);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			// thrd.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 初始化窗体
	private void myInit() {
		// 标签:头像
		// 头像的ImageIcon对象
		ii_myhd = new ImageIcon("./pic/cat1.jpeg");
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
			}
		});
		this.add(jl_fml);

		ii_fml = new ImageIcon("./krnl_pic/female.png");
		jl_fml.setIcon(ii_fml);

		// 窗体相关
		this.setBounds(200, 100, 400, 500);
		this.setLayout(null);
		// 窗体颜色
		this.getContentPane().setBackground(clr_all);
		this.setResizable(false);
		this.setVisible(true);
	}

}
