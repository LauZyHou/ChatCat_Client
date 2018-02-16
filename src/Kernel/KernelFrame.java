package Kernel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

//主要界面
public class KernelFrame extends JFrame {
	String str_name;// 用户名Name
	String str_id;// 头像的编号HeadID
	JLabel jl_myhd;// 存放自己头像的JLabel
	ImageIcon ii_myhd;// 头像图像
	JLabel jl_mynm;// 存放自己用户名的JLabel

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
	}

	// 窗体初始化
	private void myInit() {
		// 设定自己的头像
		// 头像的ImageIcon对象
		ii_myhd = new ImageIcon("./pic/cat" + str_id + ".jpeg");
		// 对头像两步缩放
		ii_myhd.setImage(ii_myhd.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
		// 用这个ImageIcon对象设置头像入JLabel
		jl_myhd = new JLabel(ii_myhd);
		jl_myhd.setOpaque(true);// 设置JLabel为不透明才能看见底色(作边框)
		jl_myhd.setBackground(Color.WHITE);// 底色白色作边框
		jl_myhd.setBounds(10, 10, 90, 90);// 大小稍多一截作边框
		// 设定鼠标悬停特效
		jl_myhd.addMouseListener(new MouseAdapter() {
			// 鼠标进入
			@Override
			public void mouseEntered(MouseEvent e) {
				jl_myhd.setBackground(new Color(120, 120, 200));
			}

			// 鼠标退出
			@Override
			public void mouseExited(MouseEvent e) {
				jl_myhd.setBackground(Color.WHITE);
			}
		});
		this.add(jl_myhd);

		// 用户名
		jl_mynm = new JLabel(str_name);
		jl_mynm.setFont(new Font("黑体", 1, 14));
		jl_mynm.setBounds(110, 15, 50, 20);
		this.add(jl_mynm);

		// 有关窗体
		this.setLayout(null);
		this.setBounds(700, 0, 250, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

}