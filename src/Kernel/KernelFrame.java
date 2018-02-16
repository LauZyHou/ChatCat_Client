package Kernel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//主要界面
public class KernelFrame extends JFrame {
	String str_name;// 用户名Name
	String str_id;// 头像的编号HeadID
	JLabel jl_myhd;// 存放自己头像的JLabel
	ImageIcon ii_myhd;// 头像图像
	JLabel jl_mynm;// 存放自己用户名的JLabel
	JButton jl_ppl, jl_grp, jl_othr;// 联系人,群聊,其它
	JPanel jp_ppl, jp_grp, jp_othr;// 联系人,群聊,其它

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
				jl_myhd.setBackground(new Color(160, 160, 240));
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
		jl_mynm.setForeground(Color.WHITE);
		jl_mynm.setBounds(110, 15, 50, 20);
		this.add(jl_mynm);

		// 联系人按钮
		jl_ppl = new JButton("联系人");
		jl_ppl.setBounds(0, 120, 90, 30);
		jl_ppl.setEnabled(false);// 不可用
		jl_ppl.addActionListener(new MyActionAdapter() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp_othr.setVisible(false);
				jp_grp.setVisible(false);
				jp_ppl.setVisible(true);
				jl_othr.setEnabled(true);
				jl_grp.setEnabled(true);
				jl_ppl.setEnabled(false);
			}
		});
		this.add(jl_ppl);

		// 群聊按钮
		jl_grp = new JButton("群聊");
		jl_grp.setBounds(90, 120, 90, 30);
		jl_grp.addActionListener(new MyActionAdapter() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp_ppl.setVisible(false);
				jp_othr.setVisible(false);
				jp_grp.setVisible(true);
				jl_ppl.setEnabled(true);
				jl_othr.setEnabled(true);
				jl_grp.setEnabled(false);
			}
		});
		this.add(jl_grp);

		// 其它按钮
		jl_othr = new JButton("其它");
		jl_othr.setBounds(180, 120, 90, 30);
		jl_othr.addActionListener(new MyActionAdapter() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp_ppl.setVisible(false);
				jp_grp.setVisible(false);
				jp_othr.setVisible(true);
				jl_ppl.setEnabled(true);
				jl_grp.setEnabled(true);
				jl_othr.setEnabled(false);
			}
		});
		this.add(jl_othr);

		// 联系人面板
		jp_ppl = new JPanel();
		jp_ppl.setBounds(0, 150, 270, 480);
		jp_ppl.setBackground(new Color(200, 200, 255));
		jp_ppl.setVisible(true);
		this.add(jp_ppl);

		// 群聊面板
		jp_grp = new JPanel();
		jp_grp.setBounds(0, 150, 270, 480);
		jp_grp.setBackground(new Color(225, 200, 200));
		jp_grp.setVisible(false);// 默认不显示
		this.add(jp_grp);

		// 其它面板
		jp_othr = new JPanel();
		jp_othr.setBounds(0, 150, 270, 480);
		jp_othr.setBackground(new Color(200, 230, 200));
		jp_othr.setVisible(false);// 默认不显示
		this.add(jp_othr);

		// 有关窗体
		this.getContentPane().setBackground(Color.BLACK);
		this.setLayout(null);
		this.setBounds(700, 0, 270, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

}