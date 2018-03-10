package Other;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// 好友资料卡
public class FrndCardFrame extends JFrame {
	// 组件
	JLabel jl_hd;// 头像
	JLabel jl_sex;// 性别图
	JTextField jtf_nm;// 昵称
	JTextArea jta_sgntr;// 个性签名
	JButton jb_ok, jb_del;// 确认,删除

	// 资源
	String str_id = "2";
	int Sex = 1;// 0女1男
	ImageIcon ii_hd;
	String Name = "刘傻逼", Signature = "喵喵";// 昵称,签名档

	// 颜色
	// 背景颜色
	Color[] clr_bck = { new Color(240, 225, 230), new Color(225, 230, 240) };
	// 按钮颜色
	Color[] clr_jb = { new Color(220, 190, 220), new Color(190, 220, 220) };
	// 头像框颜色
	Color[] clr_hd = { DataCardFrame.clr_fml, DataCardFrame.clr_ml };

	public FrndCardFrame(String str) {
		myResolve(str);// 解析传来的好友详细信息
		myInit();// 根据好友详细信息,初始化窗体内组件
		this.setBounds(400, 150, 200, 300);

		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	// 解析传来的好友详细信息
	private void myResolve(String str) {

	}

	// 窗体初始化
	private void myInit() {
		// 设定自己的头像
		// 头像的ImageIcon对象
		ii_hd = new ImageIcon("./pic/cat" + str_id + ".jpeg");
		// 对头像两步缩放
		ii_hd.setImage(ii_hd.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
		// 用这个ImageIcon对象设置头像入JLabel
		jl_hd = new JLabel(ii_hd);
		jl_hd.setOpaque(true);// 设置JLabel为不透明才能看见底色(作边框)
		jl_hd.setBackground(clr_hd[Sex]);// 底色白色作边框
		jl_hd.setBounds(10, 10, 90, 90);// 大小稍多一截作边框
		this.add(jl_hd);

		// 昵称
		jtf_nm = new JTextField(this.Name);
		jtf_nm.setEditable(false);
		jtf_nm.setBounds(110, 10, 80, 40);
		this.add(jtf_nm);

		// 个性签名
		jta_sgntr = new JTextArea(this.Signature);
		jta_sgntr.setEditable(false);
		// jta_sgntr.setEnabled(false);
		jta_sgntr.setBounds(8, 110, 180, 110);
		this.add(jta_sgntr);

		// 确认
		jb_ok = new JButton("知道了");
		jb_ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		jb_ok.setBackground(clr_jb[Sex]);
		jb_ok.setFocusable(false);
		jb_ok.setBounds(10, 230, 80, 30);
		this.add(jb_ok);

		// 删除
		String str_sex = (Sex == 1 ? "他" : "她");
		jb_del = new JButton("删除" + str_sex);
		jb_del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "确定要删除好友" + Name + "吗?", "删除好友", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					// TODO 发送删除消息
					System.out.println("删除测试");
				} else {
					// 啥也不做
				}
			}
		});
		jb_del.setBackground(Color.WHITE);
		jb_del.setFocusable(false);
		jb_del.setBounds(105, 230, 80, 30);
		this.add(jb_del);

		this.setTitle(Name + "的资料卡");
		this.getContentPane().setBackground(clr_bck[Sex]);
	}
}
