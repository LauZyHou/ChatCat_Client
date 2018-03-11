package Kernel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Other.FrndCardFrame;

public class FrndChatFrame extends JFrame {
	// 对方信息资源
	FrndNode fn_ctpt;// 对方的信息结点,注意复制来的是引用
	String ctptUsr;// 对方的帐号
	String ctptNm;// 对方的网名
	int ctptHID;// 对方的头像号
	String ctptSig;// 对方的个性签名
	ImageIcon ii_ctptHd;// 对方的头像

	// 组件
	JLabel jl_hd;// 存放对方头像,用户名等的左上角标签
	JButton jb_snd, jb_ext;// 发送按钮,关闭按钮
	JTextField jtf_inpt;// 输入框
	JTextArea jta_rcv;// 接收框,可以改成JPanel
	// 发送图片,资料卡,清屏,保存聊天记录,修改背景颜色
	JButton jb_pic, jb_card, jb_cln, jb_hstry, jb_clr;

	// 其它
	private Font ft_jt = new Font("黑体", 1, 20);// 发送/接收框字体
	public FrndCardFrame fcrdf = null;// 该好友的资料卡,仅能通过单击头像打开
	Color clr_jb = Color.WHITE;

	// 构造器,传入双击的那个联系人结点
	public FrndChatFrame(FrndNode fn) {
		super("正在和" + fn.Name + "聊天");
		this.fn_ctpt = fn;// 保留一份引用,但不要随意修改
		myInit();// 窗体初始化
		jtf_inpt.grabFocus();// 输入栏获取焦点
	}

	// 窗体初始化
	private void myInit() {
		// 从对方的信息结点中把信息读出来
		this.ctptUsr = fn_ctpt.UsrNum;
		this.ctptNm = fn_ctpt.Name;
		this.ctptSig = fn_ctpt.Signature;
		this.ctptHID = fn_ctpt.HeadID;
		this.ii_ctptHd = fn_ctpt.ii_head;

		// 左上角标签
		jl_hd = new JLabel();
		// 两步缩放对方的头像
		Image img = ii_ctptHd.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		jl_hd.setIcon(new ImageIcon(img));// 头像放入
		jl_hd.setText("<HTML><font size=\"6\" color=\"#330066\">" + ctptNm + "</font><br>" + ctptSig + "</HTML>");// 名字和个性签名放入
		jl_hd.setIconTextGap(15);// 设置JLabel文字图片间距
		jl_hd.addMouseListener(new MouseAdapter() {
			// 单击头像
			@Override
			public void mouseClicked(MouseEvent e) {
				// 发送给服务器消息以获取好友资料
				try {
					KernelFrame.dos.writeUTF("[msg]" + ctptUsr);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		jl_hd.setBounds(20, 10, 500, 60);
		jl_hd.setLayout(null);
		jl_hd.setBackground(new Color(200, 200, 240));
		jl_hd.setForeground(Color.BLACK);
		jl_hd.setOpaque(true);
		this.add(jl_hd);

		// 接收框
		jta_rcv = new JTextArea(6, 20);
		jta_rcv.setFont(ft_jt);
		jta_rcv.setEditable(false);
		jta_rcv.setLineWrap(true);// 自动换行
		JScrollPane jsp = new JScrollPane(jta_rcv);// 放入滚动条
		jsp.setBounds(20, 80, 455, 200);
		this.add(jsp);

		// 输入栏
		jtf_inpt = new JTextField(20);
		jtf_inpt.setFont(ft_jt);
		jtf_inpt.setBounds(20, 320, 370, 40);
		jtf_inpt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// 按下了回车,并且有内容时允许发送
				if (e.getKeyChar() == '\n' && jtf_inpt.getText().length() > 0) {
					try {
						send();// 发送给服务器
					} catch (IOException e1) {
						e1.printStackTrace();// 发送失败
					}
				}
				// 按完回车焦点一定还在这个发送栏里,不必重新取回
			}
		});
		this.add(jtf_inpt);

		// 清屏按钮
		jb_cln = new JButton();
		jb_cln.setIcon(new ImageIcon("./krnl_pic/cln.png"));
		jb_cln.setFocusable(false);
		jb_cln.setBounds(20, 285, 30, 30);
		jb_cln.setBackground(clr_jb);
		jb_cln.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				jta_rcv.setText("");
				jtf_inpt.grabFocus();
			}
		});
		this.add(jb_cln);

		// 发送图片按钮
		jb_pic = new JButton();
		jb_pic.setIcon(new ImageIcon("./krnl_pic/pic.png"));
		jb_pic.setFocusable(false);
		jb_pic.setBounds(60, 285, 30, 30);
		jb_pic.setBackground(clr_jb);
		this.add(jb_pic);

		// 资料卡按钮
		jb_card = new JButton();
		jb_card.setIcon(new ImageIcon("./krnl_pic/card.png"));
		jb_card.setFocusable(false);
		jb_card.setBounds(100, 285, 30, 30);
		jb_card.setBackground(clr_jb);
		jb_card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 发送给服务器消息以获取好友资料
				try {
					KernelFrame.dos.writeUTF("[msg]" + ctptUsr);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(jb_card);

		// 保存历史按钮
		jb_hstry = new JButton();
		jb_hstry.setIcon(new ImageIcon("./krnl_pic/hstry.png"));
		jb_hstry.setFocusable(false);
		jb_hstry.setBounds(140, 285, 30, 30);
		jb_hstry.setBackground(clr_jb);
		jb_hstry.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// FIXME
			}
		});
		this.add(jb_hstry);

		// 修改背景颜色按钮
		jb_clr = new JButton();
		jb_clr.setIcon(new ImageIcon("./krnl_pic/clr.png"));
		jb_clr.setFocusable(false);
		jb_clr.setBounds(180, 285, 30, 30);
		jb_clr.setBackground(clr_jb);
		jb_clr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 颜色对话框
				Color newClr = JColorChooser.showDialog(null, "修改聊天窗体背景色", KernelFrame.clr_ppl);
				if (newClr != null) {
					getContentPane().setBackground(newClr);
				}
			}
		});
		this.add(jb_clr);

		// 发送按钮
		jb_snd = new JButton("发送");
		jb_snd.setBounds(405, 320, 70, 40);
		jb_snd.addActionListener(new MyActionAdapter() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 仅当文本框内有内容时允许发送
				if (jtf_inpt.getText().length() > 0) {
					try {
						send();// 发送给服务器
					} catch (IOException e1) {
						e1.printStackTrace();// 发送失败
					}
				}
				jtf_inpt.grabFocus();// 取回焦点
			}
		});
		jb_snd.setFocusable(false);
		jb_snd.setBackground(new Color(210, 200, 250));
		this.add(jb_snd);

		// 有关窗体
		this.setLayout(null);
		// 关闭时什么都不做,而是在后面覆写windowClosing里隐藏窗体
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBounds(250, 150, 500, 400);
		this.setResizable(false);
		this.setVisible(true);
		// 窗体关闭时只是隐藏它,不析构存窗体引用HashMap里对应的窗体
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);// 隐藏本窗体
				// KernelFrame.hm_usrTOfcf.remove(ctptUsr);
			}
		});
		this.getContentPane().setBackground(KernelFrame.clr_ppl);
	}

	// 发送时做的事情,将发送时的异常抛出去,在调用send()时捕获
	// 这样一旦发生异常,整个send()立即停止执行
	// 所以只要自己窗口内看到了发出去消息,而且发送框清空,消息就发出去了
	private void send() throws IOException {
		// TODO
		// 构造要发送给服务器的消息,格式"[to对方帐号][im自己帐号]消息内容"
		String s = "[to" + ctptUsr + "][im" + KernelFrame.str_nmbr + "]" + jtf_inpt.getText();
		KernelFrame.dos.writeUTF(s);// 发给服务器,此处抛异常
		jta_rcv.append("[自己]:" + jtf_inpt.getText() + "\n");// 成功发送后在文本区显示
		jta_rcv.selectAll();// 选中所有,从而让滚动条始终在最下边
		jtf_inpt.setText("");// 同时清空发送框
		// 获得焦点在不同的发送方式下未必都需要,所以不写在send()里
	}
}
