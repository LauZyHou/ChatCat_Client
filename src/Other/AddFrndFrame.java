package Other;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import Kernel.KernelFrame;

//添加好友界面
public class AddFrndFrame extends JFrame {
	// 组件
	// 文本框:帐号,昵称
	JTextField jtf_usrnm, jtf_nm;
	// 按钮:清空,查找,添加
	JButton jb_clr, jb_qry, jb_add;
	// JList相关
	JList<String> jl;
	public DefaultListModel<String> dlm;// JList数据模型

	// 其它
	DataOutputStream dos;
	Font fnt_jl = new Font("黑体", 1, 20);
	Color clr_jb = KernelFrame.clr_othr2;

	// 构造器
	public AddFrndFrame(DataOutputStream dos) {
		super("ChatCat添加好友");
		this.dos = dos;
		myInit();// 窗体初始化
	}

	// 窗体初始化
	private void myInit() {
		// 提示:帐号
		JLabel jl_usrnm = new JLabel("帐号:");
		jl_usrnm.setFont(fnt_jl);
		jl_usrnm.setBounds(10, 10, 60, 30);
		this.add(jl_usrnm);

		// 文本框:账号
		jtf_usrnm = new JTextField(5);
		jtf_usrnm.setFont(fnt_jl);
		// 帐号在客户端检查只允许输入数字
		jtf_usrnm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int ascii = e.getKeyChar();// 获得ASCII码
				// 退格和TAB是允许的
				if (ascii == 8 && ascii == 9) {
					return;
				}
				// 超过5字符时不能再输入
				else if (jtf_usrnm.getText().length() >= 5) {
					e.consume();
				}
				// 不超过5字符的数字是允许的
				else if (ascii >= 48 && ascii <= 57) {
					return;
				}
				// 其它都不允许
				else {
					e.consume();// 消除键盘事件
				}
			}
		});
		jtf_usrnm.setBounds(80, 10, 100, 30);
		this.add(jtf_usrnm);

		// 提示:昵称
		JLabel jl_nm = new JLabel("昵称:");
		jl_nm.setFont(fnt_jl);
		jl_nm.setBounds(190, 10, 60, 30);
		this.add(jl_nm);

		// 文本框:昵称
		jtf_nm = new JTextField(8);
		jtf_nm.setFont(fnt_jl);
		jtf_nm.setBounds(260, 10, 100, 30);
		this.add(jtf_nm);

		// 按钮:清空
		jb_clr = new JButton("清空");
		jb_clr.setBounds(40, 50, 80, 30);
		jb_clr.setBackground(clr_jb);
		jb_clr.setFocusable(false);
		jb_clr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jtf_nm.setText("");
				jtf_usrnm.setText("");
				dlm.clear();// 清空要在这里清空
				jtf_usrnm.grabFocus();
			}
		});
		this.add(jb_clr);

		// 按钮:查找
		jb_qry = new JButton("查找");
		jb_qry.setBounds(160, 50, 80, 30);
		jb_qry.setBackground(clr_jb);
		jb_qry.setFocusable(false);
		jb_qry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// 有帐号时按帐号查找
					if (jtf_usrnm.getText().length() > 0) {
						dos.writeUTF("[addFrnd_U]" + jtf_usrnm.getText());
						jtf_nm.setText("");// 设空告诉用户没有使用
					}
					// 没有帐号时按名称LIKE匹配
					else if (jtf_nm.getText().length() > 0) {
						dos.writeUTF("[addFrnd_N]" + jtf_nm.getText());
					}
					// 两个都没有,提示输入
					else {
						JOptionPane.showMessageDialog(null, "[x]缺少输入");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(jb_qry);

		// 按钮:添加
		jb_add = new JButton("添加");
		jb_add.setBounds(280, 50, 80, 30);
		jb_add.setBackground(clr_jb);
		jb_add.setFocusable(false);
		jb_add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		this.add(jb_add);

		// JList数据模型
		dlm = new DefaultListModel<String>();

		// JList
		jl = new JList<String>();
		jl.setModel(dlm);// 设置渲染器
		jl.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jl.setBounds(0, 0, 340, 160);
		// // 选择事件
		// jl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		// @Override
		// public void valueChanged(ListSelectionEvent e) {
		// // 按住最后松手时发生的事件在这返回false
		// // 按下一瞬间发生的时间在这返回true
		// if (e.getValueIsAdjusting() == false) {
		// System.out.println(jl.getSelectedValue());
		// }
		// }
		// });
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(jl);// 设置视图(V)
		jsp.setBounds(30, 100, 340, 160);
		// 禁止横向滚动
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(jsp);

		// 窗体相关
		this.setBounds(300, 200, 400, 300);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// dispose();
				KernelFrame.aff = null;
				System.gc();
			}
		});
		this.getContentPane().setBackground(KernelFrame.clr_othr);
		this.setLayout(null);
		this.setVisible(true);
	}
}
