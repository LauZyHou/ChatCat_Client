package Kernel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

//主要界面
public class KernelFrame extends JFrame {
	// 组件
	String str_name;// 用户名Name
	String str_id;// 头像的编号HeadID
	JLabel jl_myhd;// 存放自己头像的JLabel
	ImageIcon ii_myhd;// 头像图像
	JLabel jl_mynm;// 存放自己用户名的JLabel
	JButton jl_ppl, jl_grp, jl_othr;// 联系人,群聊,其它
	JPanel jp_ppl, jp_grp, jp_othr;// 联系人,群聊,其它

	// 资源
	// 存放解析后的好友信息串
	LinkedList<String> ll_ppl = new LinkedList<String>();

	// 其它
	FrndTrCllRndrr ftcr = new FrndTrCllRndrr();// 联系人树的渲染器

	// 构造器
	public KernelFrame(String s) {
		myResolve(s);// 解析服务器返回的信息
		myInit();// 整体窗体初始化
		pplDraw();// 联系人面板jp_ppl的绘制

		this.setVisible(true);// 全部绘制好后再设置为可见
	}

	// 解析服务器返回的信息
	private void myResolve(String s) {
		// 解析用户名Name
		this.str_name = s.substring(s.indexOf("#用户名:") + 5, s.indexOf("#头像ID:"));
		// 解析头像的编号HeadID
		this.str_id = s.substring(s.indexOf("#头像ID:") + 6, MyTools.indexOf(s, 3, "#"));
		// 解析好友信息,前两个#携带的是用户名和头像ID,所以从3开始
		int nowIndex = MyTools.indexOf(s, 3, "#");// 存储当前"#"位置
		int nextIndex;// 存储下一"#"位置
		String str_frnd;// 存储当前得到的好友信息串
		this.ll_ppl.clear();// 清空好友信息串,这是每次解析信息完全重写
		for (int i = 3; nowIndex != -1; i++) {
			nextIndex = MyTools.indexOf(s, i + 1, "#");// 计算下一#位置
			// 如果下一个"#"还存在,依靠它限界
			if (nextIndex != -1) {
				str_frnd = s.substring(nowIndex + 1, nextIndex);
			}
			// 如果下一个"#"不存在,默认以结尾限界就行
			else {
				str_frnd = s.substring(nowIndex + 1);
			}
			// System.out.println(str_frnd);// 调试输出
			this.ll_ppl.add(str_frnd);// 添加到好友信息链表中
			nowIndex = nextIndex;// 下次的"当前"即是当前的"下次"
		}
		System.out.println(this.ll_ppl);// 调试输出
	}

	// 整体窗体初始化
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
		jp_ppl.setLayout(null);
		jp_ppl.setBounds(0, 150, 270, 480);
		jp_ppl.setBackground(new Color(200, 200, 255));
		jp_ppl.setVisible(true);
		this.add(jp_ppl);

		// 群聊面板
		jp_grp = new JPanel();
		jp_grp.setLayout(null);
		jp_grp.setBounds(0, 150, 270, 480);
		jp_grp.setBackground(new Color(225, 200, 200));
		jp_grp.setVisible(false);// 默认不显示
		this.add(jp_grp);

		// 其它面板
		jp_othr = new JPanel();
		jp_othr.setLayout(null);
		jp_othr.setBounds(0, 150, 270, 480);
		jp_othr.setBackground(new Color(200, 230, 200));
		jp_othr.setVisible(false);// 默认不显示
		this.add(jp_othr);

		// 有关窗体
		this.getContentPane().setBackground(Color.BLACK);
		this.setLayout(null);
		this.setBounds(700, 0, 270, 600);
		this.setVisible(false);// 在全部绘制好前不可见
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	// 联系人面板jp_ppl的绘制
	private void pplDraw() {
		FrndNode fn_root = new FrndNode("fn_root");// JTree根结点
		FrndNode fn_clsmt = new FrndNode("同学");// 一级结点
		FrndNode fn_blk = new FrndNode("黑名单");// 一级结点
		// 绘制"同学"里的用户结点
		for (String s : ll_ppl) {
			// 解析成String类型,它们是按逗号分隔的
			String usr = s.substring(0, s.indexOf(","));
			String nm = s.substring(s.indexOf(",") + 1, MyTools.indexOf(s, 2, ","));
			String hd = s.substring(MyTools.indexOf(s, 2, ",") + 1);
			// 新建这个用户结点,传入解析好的必要的信息
			FrndNode fn_etc = new FrndNode(usr, nm, Integer.parseInt(hd));
			// 添加到一级结点上
			fn_clsmt.add(fn_etc);
		}
		// 一级结点和根节点之间的嵌套
		fn_root.add(fn_clsmt);
		fn_root.add(fn_blk);
		// 用实现了TreeModel接口的DefaultTreeModel类指定树的根结点
		DefaultTreeModel dtm_frnd = new DefaultTreeModel(fn_root);
		// 建立JTree树
		JTree jt_frnd = new JTree(dtm_frnd);
		// 为这棵树设定渲染器
		jt_frnd.setCellRenderer(this.ftcr);
		// 当JPanel使用空布局时,这里需要指明位置和大小
		jt_frnd.setBounds(0, 0, jp_ppl.getWidth(), jp_ppl.getHeight());
		// 设置树结点的高度,以实现间距
		jt_frnd.setRowHeight(55);
		// 设置为水平分割风格
		jt_frnd.putClientProperty("JTree.lineStyle", "Horizontal");
		// 设置根节点不可见
		jt_frnd.setRootVisible(false);
		// 设置结点单击展开所需次数:1
		jt_frnd.setToggleClickCount(1);
		// 添加到联系人面板jp_ppl
		jp_ppl.add(jt_frnd);
		this.validate();// 重绘

		// 下面是测试区
		// JButton jb = new JButton("测试1");
		// jb.setBounds(0, 0, 100, 200);
		// jp_ppl.add(jb);
	}

}