package Kernel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import Other.AddFrndFrame;
import Other.DataCardFrame;
import Other.OthrMouseAdapter;
import Other.SndErrFrame;

//主要界面,同时作为通信接收线程的目标对象
public class KernelFrame extends JFrame implements Runnable {
	// 组件
	public static String str_nmbr;// 账户名(账号),从登录时传入
	String str_name;// 用户名Name
	String str_id;// 头像的编号HeadID
	JLabel jl_myhd;// 存放自己头像的JLabel
	ImageIcon ii_myhd;// 头像图像
	JLabel jl_mynm;// 存放自己用户名的JLabel
	JButton jb_ppl, jb_grp, jb_othr;// 联系人,群聊,其它
	JPanel jp_ppl, jp_grp, jp_othr;// 联系人,群聊,其它
	// 添加好友,报告错误,处理事务,批量删除,创建群聊
	JButton jb_frnd, jb_err, jb_msg, jb_del, jb_crtgrp;
	// 滚动条:其它面板
	JScrollPane jsp_othr;
	// JTree
	JTree jt_frnd;

	// 资源
	// 存放解析后的好友信息串
	LinkedList<String> ll_ppl = new LinkedList<String>();
	// 存放打开的<联系人账号,联系人聊天窗口FrndChatFrame的引用>映射
	// 用static静态类型方便在聊天窗口关闭时操作之
	// (在另一个类里难以拿到这个类对象的引用,而这个类只创建这一个对象)
	public static HashMap<String, FrndChatFrame> hm_usrTOfcf = new HashMap<String, FrndChatFrame>();
	// 存放<联系人账户,联系人结点的引用>的哈希表
	public static HashMap<String, FrndNode> hm_usrTOfn = new HashMap<String, FrndNode>();
	// 个人资料卡,只能打开一张
	public static DataCardFrame dcf = null;
	// 提交错误窗体,只能打开一张
	public static SndErrFrame srf = null;
	// 添加好友窗体,只能打开一张
	public static AddFrndFrame aff = null;

	// 其它
	// 联系人树的渲染器
	FrndTrCllRndrr ftcr = new FrndTrCllRndrr();
	// Socket对象
	Socket sckt = null;
	// 输入流
	DataInputStream dis = null;
	// 输出流写成static类型,方便在FrndChatFrame里发送时使用
	public static DataOutputStream dos = null;
	// 通信接收线程对象
	Thread thrd = null;
	// 其它面板的颜色,即其它组件的未选中颜色
	public static Color clr_othr = new Color(230, 240, 230);
	// 鼠标在其它组件上时变成的颜色
	public static Color clr_othr2 = new Color(210, 240, 210);
	// 联系人面板的颜色
	public static Color clr_ppl = new Color(220, 220, 255);

	// 构造器(传入登录成功发来的信息,建立好连接的Socket对象,账户名)
	public KernelFrame(String s, Socket sckt, String nmbr) {
		super("ChatCat[v]TCP连接保持中");
		str_nmbr = nmbr;// 保留账户名,为了在关闭该窗体时返回给服务器
		this.sckt = sckt;// 保留连接好的Socket对象
		try {
			// 用连接好的Socket对象重建输入输出流
			dis = new DataInputStream(sckt.getInputStream());
			dos = new DataOutputStream(sckt.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		myResolve(s);// 解析服务器返回的信息
		myInit();// 整体窗体初始化
		pplDraw();// 联系人面板jp_ppl的绘制
		// TODO 群聊面板绘制
		othrDraw();
		this.setVisible(true);// 全部绘制好后再设置为可见

		// 单开一个线程用来接收服务器发来的信息,因为接收可能阻塞
		thrd = new Thread(this);
		thrd.start();
	}

	// 解析服务器返回的信息
	private void myResolve(String s) {
		// 解析用户名Name
		this.str_name = s.substring(s.indexOf("#用户名:") + 5, s.indexOf("#头像ID:"));
		// 解析头像的编号HeadID,没有好友时是没有第三个"#"的
		if (MyTools.indexOf(s, 3, "#") != -1)// 有好友
			this.str_id = s.substring(s.indexOf("#头像ID:") + 6, MyTools.indexOf(s, 3, "#"));
		else// 没好友
			this.str_id = s.substring(s.indexOf("#头像ID:") + 6);
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

			// 鼠标单击
			@Override
			public void mouseClicked(MouseEvent e) {
				// 如果还没有创建,或者已经关闭,要向服务器发消息来创建
				// 这样才能保证资料卡是最新的
				if (dcf == null) {
					try {
						// 发送获取资料卡请求给服务器
						// 在接收消息处new而不需要在这里new
						dos.writeUTF("[mycard]");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				// 如果仅仅是最小化了,恢复正常
				else {
					dcf.setExtendedState(JFrame.NORMAL);
					dcf.requestFocus();
				}
			}

		});
		this.add(jl_myhd);

		// 用户名
		jl_mynm = new JLabel(str_name);
		jl_mynm.setFont(new Font("黑体", 1, 14));
		jl_mynm.setForeground(Color.WHITE);
		jl_mynm.setBounds(110, 15, 160, 20);
		this.add(jl_mynm);

		// 联系人按钮
		jb_ppl = new JButton("联系人");
		jb_ppl.setBackground(new Color(220, 220, 255));
		jb_ppl.setBounds(0, 120, 90, 30);
		jb_ppl.setEnabled(false);// 不可用
		jb_ppl.addActionListener(new MyActionAdapter() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp_othr.setVisible(false);
				jp_grp.setVisible(false);
				jp_ppl.setVisible(true);
				jb_othr.setEnabled(true);
				jb_grp.setEnabled(true);
				jb_ppl.setEnabled(false);
			}
		});
		jb_ppl.setFocusable(false);
		this.add(jb_ppl);

		// 群聊按钮
		jb_grp = new JButton("群聊");
		jb_grp.setBackground(new Color(240, 230, 230));
		jb_grp.setBounds(90, 120, 90, 30);
		jb_grp.addActionListener(new MyActionAdapter() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp_ppl.setVisible(false);
				jp_othr.setVisible(false);
				jp_grp.setVisible(true);
				jb_ppl.setEnabled(true);
				jb_othr.setEnabled(true);
				jb_grp.setEnabled(false);
				// 让其它面板的滚动条不可用
				jsp_othr.setEnabled(false);
			}
		});
		jb_grp.setFocusable(false);
		this.add(jb_grp);

		// 其它按钮
		jb_othr = new JButton("其它");
		jb_othr.setBackground(new Color(230, 240, 230));
		jb_othr.setBounds(180, 120, 90, 30);
		jb_othr.addActionListener(new MyActionAdapter() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jp_ppl.setVisible(false);
				jp_grp.setVisible(false);
				jp_othr.setVisible(true);
				jb_ppl.setEnabled(true);
				jb_grp.setEnabled(true);
				jb_othr.setEnabled(false);
				// 让其它面板的滚动条恢复可用
				jsp_othr.setEnabled(true);
			}
		});
		jb_othr.setFocusable(false);
		this.add(jb_othr);

		// 联系人面板
		jp_ppl = new JPanel();
		jp_ppl.setLayout(null);
		jp_ppl.setBounds(0, 150, 270, 480);
		jp_ppl.setBackground(clr_ppl);
		jp_ppl.setVisible(true);
		this.add(jp_ppl);

		// 群聊面板
		jp_grp = new JPanel();
		jp_grp.setLayout(null);
		jp_grp.setBounds(0, 150, 270, 500);// 这里要多高才能遮挡下面的
		jp_grp.setBackground(new Color(240, 230, 230));
		jp_grp.setVisible(false);// 默认不显示
		this.add(jp_grp);

		// 其它面板
		jp_othr = new JPanel();
		jp_othr.setLayout(null);
		// 这个x,y是相对于JScrollPane的,似乎乱设也没关系
		jp_othr.setBounds(0, 0, 270, 500);// 500放5个100高的按钮
		jp_othr.setBackground(clr_othr);
		jp_othr.setVisible(false);// 默认不显示
		// 为JPanel设定滚动条一定要先setPreferredSize而且要比滚动条大
		jp_othr.setPreferredSize(new Dimension(270, 500));
		jsp_othr = new JScrollPane(jp_othr);
		// 设置滚动速度快一些
		JScrollBar jsb = jsp_othr.getVerticalScrollBar();
		jsb.setUnitIncrement(10);
		// 设置取消横向滚动
		jsp_othr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp_othr.setBounds(0, 150, 270, 425);// 滚动条高度=实际算出来的-25
		this.add(jsp_othr);

		// 有关窗体
		this.getContentPane().setBackground(Color.BLACK);
		this.setLayout(null);
		this.setBounds(700, 0, 270, 600);
		this.setVisible(false);// 在全部绘制好前不可见
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		// 设置总在最前面,因为后面弹出的窗口也是这样
		// this.setAlwaysOnTop(true);
		// 窗体关闭时向服务器发送一个关闭信息
		// this.addWindowListener(new WindowAdapter() {
		// @Override
		// public void windowClosing(WindowEvent e) {
		// try {
		// // 告诉服务器要下线的账户名,服务器查哈希表删除这项
		// dos.writeUTF("[bye]" + str_nmbr);
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// }
		// });
	}

	// 联系人面板jp_ppl的绘制
	private void pplDraw() {
		FrndNode fn_root = new FrndNode("fn_root");// JTree根结点
		FrndNode fn_clsmt = new FrndNode("我的好友");// 一级结点
		FrndNode fn_blk = new FrndNode("黑名单");// 一级结点
		// 绘制"我的好友"里的用户结点
		for (String s : ll_ppl) {
			// 解析成String类型,它们是按逗号分隔的
			String usr = s.substring(0, s.indexOf(","));
			String nm = s.substring(s.indexOf(",") + 1, MyTools.indexOf(s, 2, ","));
			String hd = s.substring(MyTools.indexOf(s, 2, ",") + 1);
			// 新建这个用户结点,传入解析好的必要的信息
			FrndNode fn_etc = new FrndNode(usr, nm, Integer.parseInt(hd));
			// 将这个结点放入存<用户账户,用户结点引用>的哈希表中
			hm_usrTOfn.put(usr, fn_etc);
			// 挂载到一级结点上
			fn_clsmt.add(fn_etc);
		}
		// 一级结点和根节点之间的嵌套
		fn_root.add(fn_clsmt);
		fn_root.add(fn_blk);
		// 用实现了TreeModel接口的DefaultTreeModel类指定树的根结点
		DefaultTreeModel dtm_frnd = new DefaultTreeModel(fn_root);
		// 建立JTree树
		jt_frnd = new JTree(dtm_frnd);
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
		// 设置背景色为下层面板的背景色
		jt_frnd.setBackground(jp_ppl.getBackground());
		// 为这棵树注册监听器,用匿名的适配器覆写鼠标点击方法
		jt_frnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 如果在这棵树上点击了2次,即双击
				if (e.getClickCount() == 2) {
					// 按照鼠标点击的坐标点获取路径
					TreePath tp_clk = jt_frnd.getPathForLocation(e.getX(), e.getY());
					// 谨防单击空白处发生的空指针异常
					if (tp_clk != null) {
						// 获取这个路径上的最后一个组件引用,即双击的那个组件
						FrndNode fn_end = (FrndNode) tp_clk.getLastPathComponent();
						// 只要有账号,说明是ChatCat用户
						if (fn_end.UsrNum != null) {
							// 打开聊天窗口,传入这个用户节点,同时这个聊天窗口的引用存进哈希表
							// 特判一下:窗口尚未打开过,即这个窗口没有用new创建过,即不包含这个键值对
							if (!hm_usrTOfcf.containsKey(fn_end.UsrNum)) {
								hm_usrTOfcf.put(fn_end.UsrNum, new FrndChatFrame(fn_end));
							}
							// 如果窗口已经打开过了,它也许是点击x被隐藏了,也许已经打开而且没隐藏
							else {
								// 让其可见,处于正常扩展状态,申请焦点
								hm_usrTOfcf.get(fn_end.UsrNum).setVisible(true);
								hm_usrTOfcf.get(fn_end.UsrNum).setExtendedState(JFrame.NORMAL);
								hm_usrTOfcf.get(fn_end.UsrNum).requestFocus();
								// 下面是尝试暂时置顶的失败代码
								// hm_usrTOfcf.get(fn_end.UsrNum).requestFocus();
								// 没找到临时设置最前面的方法,不妨让每个窗体都始终在最前面,则他们有先后
								// hm_usrTOfcf.get(fn_end.UsrNum).setAlwaysOnTop(true);
							}
							// try {
							// dos.writeUTF(fn_end.Name);
							// } catch (IOException e1) {
							// e1.printStackTrace();
							// }
							// System.out.println(fn_end.Name);//测试输出
						}
					}
				}
			}

			// 鼠标进入
			// @Override
			// public void mouseEntered(MouseEvent e) {
			// // 按照鼠标进入的坐标点获取路径
			// TreePath tp_clk = jt_frnd.getPathForLocation(e.getX(), e.getY());
			// // 谨防进入空白处发生的空指针异常
			// if (tp_clk != null) {
			// // 获取这个路径上的最后一个组件引用,即事件发生所在的那个组件
			// FrndNode fn_end = (FrndNode) tp_clk.getLastPathComponent();
			// // 只要有账号,说明是ChatCat用户
			// if (fn_end.UsrNum != null) {
			// // 根据路径获知选中的是哪行,传递给这棵树的渲染器
			// ftcr.mouseRow = jt_frnd.getRowForLocation(e.getX(), e.getY());
			// jt_frnd.repaint();
			// }
			// }
			//
			// }
		});
		// 添加到联系人面板jp_ppl
		jp_ppl.add(jt_frnd);
		this.validate();// 重绘

		// 下面是测试区
		// JButton jb = new JButton("测试1");
		// jb.setBounds(0, 0, 100, 200);
		// jp_ppl.add(jb);
	}

	// 其它面板jp_othr的绘制
	private void othrDraw() {
		// "其它"面板上鼠标事件的适配器,实现悬停变色
		OthrMouseAdapter oma = new OthrMouseAdapter();

		// 添加好友
		jb_frnd = new JButton("添加好友");
		jb_frnd.setBounds(0, 0, jp_othr.getWidth(), 100);
		jb_frnd.setIcon(new ImageIcon("./krnl_pic/add.png"));
		jb_frnd.setFocusable(false);// 不获得焦点,不然焦点框很丑
		jb_frnd.setBackground(clr_othr);
		jb_frnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (aff == null)
					aff = new AddFrndFrame(dos);
				else {
					// aff窗口关闭时会析构自己,因此这里只是最小化
					aff.setExtendedState(JFrame.NORMAL);
					aff.requestFocus();
				}
			}
		});
		jb_frnd.addMouseListener(oma);
		jp_othr.add(jb_frnd);

		// 处理事务
		jb_msg = new JButton("处理事务");
		jb_msg.setBounds(0, 100, jp_othr.getWidth(), 100);
		jb_msg.setIcon(new ImageIcon("./krnl_pic/msg.png"));
		jb_msg.setFocusable(false);
		jb_msg.setBackground(clr_othr);
		jb_msg.addMouseListener(oma);
		jp_othr.add(jb_msg);

		// 批量删除
		jb_del = new JButton("批量删除");
		jb_del.setBounds(0, 200, jp_othr.getWidth(), 100);
		jb_del.setIcon(new ImageIcon("./krnl_pic/del.png"));
		jb_del.setFocusable(false);
		jb_del.setBackground(clr_othr);
		jb_del.addMouseListener(oma);
		jp_othr.add(jb_del);

		// 创建群聊
		jb_crtgrp = new JButton("创建群聊");
		jb_crtgrp.setBounds(0, 300, jp_othr.getWidth(), 100);
		jb_crtgrp.setIcon(new ImageIcon("./krnl_pic/crtgrp.png"));
		jb_crtgrp.setFocusable(false);
		jb_crtgrp.setBackground(clr_othr);
		jb_crtgrp.addMouseListener(oma);
		jp_othr.add(jb_crtgrp);

		// 错误提交
		jb_err = new JButton("错误提交");
		jb_err.setBounds(0, 400, jp_othr.getWidth(), 100);
		jb_err.setIcon(new ImageIcon("./krnl_pic/err.png"));
		jb_err.setFocusable(false);
		jb_err.setBackground(clr_othr);
		jb_err.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (srf == null)
					srf = new SndErrFrame(dos);
				else {
					// 窗口关闭时默认只是隐藏了
					srf.setVisible(true);
					srf.setExtendedState(JFrame.NORMAL);
					srf.requestFocus();
				}
			}
		});
		jb_err.addMouseListener(oma);
		jp_othr.add(jb_err);
	}

	// 通信接收线程
	@Override
	public void run() {
		String s = null;// 存储接收来的消息
		try {
			// while写在try块里时,只要有一次异常就跳出来了结束线程
			while (true) {
				s = dis.readUTF();// 不停地从输入流接收
				System.out.println(s);// 调试输出
				/* 针对不同类型的消息做不同的事情 */
				// 如果是服务器回送的用于创建资料卡的消息
				// 为了防止快速点击造成的多窗体bug,要判空
				if (s.startsWith("[mycard]") && dcf == null) {
					// 传入输出流用于修改资料卡时向服务器发消息
					// 传入返回来的字符串用来构造窗体
					dcf = new DataCardFrame(dos, s);
				}
				// 如果是联系人发送给自己的消息
				else if (s.contains("\n\n")) {
					// 解析:消息来源的UsrNum
					String str_frm = s.substring(0, s.indexOf("\n\n"));
					// 解析:消息的内容
					String str_msg = s.substring(s.indexOf("\n\n") + 2);
					// 如果窗体没有创建,即在第一张哈希表中查不到
					if (hm_usrTOfcf.get(str_frm) == null) {
						// TODO 头像闪烁
						// 根据消息来源,在第二章哈希表里查找来源用户的JTree结点引用
						FrndNode fn_ext = hm_usrTOfn.get(str_frm);
						// 不等用户双击,立即为没创建的窗体创建并写入哈希表
						hm_usrTOfcf.put(str_frm, new FrndChatFrame(fn_ext));
					}
					// 如果窗体创建了,可能是最小化了,可能是隐藏了
					else {
						// 让其可见,处于正常扩展状态,申请焦点
						hm_usrTOfcf.get(str_frm).setVisible(true);
						hm_usrTOfcf.get(str_frm).setExtendedState(JFrame.NORMAL);
						hm_usrTOfcf.get(str_frm).requestFocus();
					}
					// 将消息的内容展示到对应的对话框上
					hm_usrTOfcf.get(str_frm).jta_rcv.append("[对方]:" + str_msg + "\n");
					// 选中所有,从而让滚动条始终在最下边
					hm_usrTOfcf.get(str_frm).jta_rcv.selectAll();
				}
				// 如果是服务器回送的修改资料卡的成败消息
				else if (s.startsWith("[changecard]")) {
					// 修改成功
					if (s.contains("success")) {
						// 如果资料卡还开着
						if (dcf != null) {
							dcf.jtf_nm.setEditable(false);
							dcf.jta_sgntr.setEditable(false);
							dcf.jb_lck.setEnabled(true);
							// TODO 头像同步修改
							// 昵称同步修改
							jl_mynm.setText(dcf.jtf_nm.getText());
							// TODO 签名档同步修改
						}
						JOptionPane.showMessageDialog(null, "[v]修改成功");
					}
					// 修改失败
					else {
						// 如果资料卡还开着
						if (dcf != null) {
							dcf.jb_snd.setEnabled(true);// 发送又可用了
						}
						JOptionPane.showMessageDialog(null, "[x]修改失败");
					}
				}
				// 如果是服务器回送的加好友查询非空结果
				else if (s.startsWith("[addFrnd]")) {
					// 确保加好友窗体未析构
					if (aff != null) {
						// 先清空数据模型
						aff.dlm.clear();
						// 由双#的下标控制循环,在循环中变化
						int i = 1;
						int idx_dblShrp = MyTools.indexOf(s, i++, "##");
						// 截取的开始位置,在循环中变化
						int idx_now = s.indexOf("]") + 1;
						// 当没有下一个双sharp时结束循环
						while (idx_dblShrp != -1) {
							// 截取出查找到的这个用户的那部分
							String nowUsr = s.substring(idx_now, idx_dblShrp);
							// 解析这部分,性别从1/0转成男/女
							String nowUsrNum = nowUsr.substring(0, nowUsr.indexOf("#"));
							String nowName = nowUsr.substring(nowUsr.indexOf("#") + 1, nowUsr.lastIndexOf("#"));
							String Sex = nowUsr.substring(nowUsr.lastIndexOf("#") + 1).equals("1") ? "男" : "女";
							// System.out.println(nowUsrNum + nowName + Sex);
							// 重排写入加好友窗体的JList模型中
							aff.dlm.addElement(nowUsrNum + "      " + Sex + "      " + nowName);
							// 下标转移:下一个开始位置是双#之后
							idx_now = idx_dblShrp + 2;
							// 下标转移:i在上个寻下标中已经跳变
							idx_dblShrp = MyTools.indexOf(s, i++, "##");
						}
						aff.validate();
					} else {
						// 如果加好友窗体已经析构了,不需做什么
					}
				}
				// 如果是服务器回送的加好友查询为空结果
				else if (s.equals("[addFrnd_None]")) {
					if (aff != null)// 如果没有析构窗体,JList的数据模型清空
						aff.dlm.clear();
					// 不论是否析构了窗体都通知一下
					JOptionPane.showMessageDialog(null, "[!]查询结果为空");
				}
				// 如果是服务器回送的加好友存到服务器内存的返回结果
				else if (s.startsWith("[youradd]")) {
					// 不论是否析构了窗体都通知一下
					JOptionPane.showMessageDialog(null, s.substring(s.indexOf("]") + 1));
				}
				// 如果服务器要求自己更新界面
				else if (s.startsWith("[refresh]")) {
					// 用第一次构造界面的方式,先构造一个伪的消息头
					String str = "#用户名:" + str_name + "#头像ID:" + str_id + s.substring(s.indexOf("]") + 1);
					// 用这个伪的消息给自己解析
					myResolve(str);
					// 移除联系人面板上面的所有组件
					jp_ppl.removeAll();
					// 绘制联系人面板
					pplDraw();
				}
				// 如果服务器要求显示提示
				else if (s.startsWith("[!]")) {
					JOptionPane.showMessageDialog(null, s);
				}
				// TODO 服务器回送的显示好友信息
				// System.out.println(s);// 测试输出
			}
		} catch (IOException e) {
			// 在客户端成功登录并保持连接的情况下服务器关闭会发生此异常
			this.setTitle("ChatCat[x]连接已断开");
			JOptionPane.showMessageDialog(this, "与服务器断开连接\n客户端程序将退出");
			System.exit(0);// 结束程序
		}
	}

}