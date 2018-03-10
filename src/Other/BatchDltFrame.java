package Other;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Kernel.KernelFrame;

//批量删除好友
public class BatchDltFrame extends JFrame {
	// 组件
	JLabel jl = new JLabel("按住Ctrl以选择多个要删除的好友");
	JTable jt = null;
	JScrollPane jsp = null;
	JButton jb_dlt = new JButton("删除TA们");
	JButton jb_bye = new JButton("到此为止");

	// 表头
	String[] headers = { "帐号", "昵称", "个性签名" };

	// 其它
	Font myft = new Font("微软雅黑", 1, 20);;
	String[][] frndMsg = { { "AA", "bb", "CC" }, { "cc" }, { "cc" }, { "cc" }, { "cc" } };// 存表的内容
	DefaultTableModel dtm = null;// JTable模型
	BatchDltFrame mypgl = this;// 自己的引用,用于监听器内部

	// 构造器
	public BatchDltFrame() {
		super("ChatCat好友批量删除");
		// 标题
		jl.setFont(myft);
		jl.setBounds(40, 10, 350, 30);
		this.add(jl);

		// JTabel相关
		dtm = new DefaultTableModel(frndMsg, headers);
		jt = new JTable(dtm);
		jt.setFont(myft);
		// jt.setBackground(KernelFrame.clr_othr2);
		jt.getTableHeader().setFont(myft);
		jt.getTableHeader().setBackground(new Color(220, 255, 150));
		jt.getTableHeader().setReorderingAllowed(false);// 表头不可拖动
		jt.setFocusable(false);
		JScrollPane jsp = new JScrollPane();
		// 放在JScrollPane里才能显示表头
		jsp.setViewportView(jt);
		jsp.getViewport().setBackground(new Color(230, 230, 255));
		// 禁止横向滚动
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBounds(30, 60, 340, 160);
		this.add(jsp);

		// 到此为止
		jb_bye.setBounds(70, 230, 100, 30);
		jb_bye.setFocusable(false);
		jb_bye.setBackground(KernelFrame.clr_othr2);
		jb_bye.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		this.add(jb_bye);

		// 删除TA们
		jb_dlt.setBounds(230, 230, 100, 30);
		jb_dlt.setFocusable(false);
		jb_dlt.setBackground(Color.WHITE);
		jb_dlt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jt.getSelectedRowCount() <= 0) {
					JOptionPane.showMessageDialog(null, "缺少选择!");
					return;
				} else {
					String slctFrndNm = "";
					LinkedList<String> ll = new LinkedList<>();
					for (int i : jt.getSelectedRows()) {
						slctFrndNm += "\n";
						slctFrndNm += frndMsg[i][0];
						ll.add("" + frndMsg[i][0]);// 顺便加到链表中,为后面发消息做准备
						slctFrndNm += " ";
						slctFrndNm += frndMsg[i][0];
					}
					int n = JOptionPane.showConfirmDialog(null, "注意!该操作会使您失去以下好友:" + slctFrndNm, "批量删除",
							JOptionPane.YES_NO_OPTION);
					// 选择了是
					if (n == JOptionPane.YES_OPTION) {
						try {
							// 向服务器发送删除消息
							KernelFrame.dos.writeUTF("[dltManyFrnd]" + String.join("#", ll));
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {
							// FIXME
						}
						System.gc();
					} else {
						// 选择了否,啥也不做
					}
				}
			}
		});
		this.add(jb_dlt);

		// 窗体相关
		this.setBounds(300, 200, 400, 300);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				KernelFrame.bdf = null;
				System.gc();
			}
		});
		this.getContentPane().setBackground(KernelFrame.clr_othr);
		this.setLayout(null);
		this.setVisible(true);
	}

	// 按照服务器返回的信息做刷新
	void flushMsg() {
		if (jsp != null)
			jsp.removeAll();
		this.removeAll();
		dtm = new DefaultTableModel(frndMsg, headers);
		jt = new JTable(dtm);
		// 放在JScrollPane里才能显示表头
		jsp = new JScrollPane(jt);
		jsp.setBounds(30, 110, 800, 300);
		this.add(jsp);
	}

}
