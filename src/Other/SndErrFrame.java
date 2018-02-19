package Other;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class SndErrFrame extends JFrame {
	JLabel jl;
	JButton jb;
	JTextArea jta;
	DataOutputStream dos;

	// 构造器
	public SndErrFrame(DataOutputStream dos) {
		super("ChatCat错误提交");
		this.dos = dos;
		myInit();
	}

	// 窗体组件初始化
	private void myInit() {
		// 文本提示
		jl = new JLabel("描述发生的BUG并提交:");
		jl.setFont(new Font("黑体", 1, 22));
		this.add(jl);

		// 文本区域
		jta = new JTextArea(8, 18);
		jta.setFont(new Font("黑体", 1, 20));
		jta.setLineWrap(true);
		this.add(jta);

		// 发送按钮
		jb = new JButton("提交");
		jb.setFocusable(false);
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jta.getText().length() < 10) {
					JOptionPane.showMessageDialog(null, "[x]您的描述太短了");
					return;
				} else if (jta.getText().length() > 200) {
					JOptionPane.showMessageDialog(null, "[x]超过200字了");
					return;
				}
				try {
					dos.writeUTF("[err]" + jta.getText());
					jta.setEnabled(false);
					jl.setText("[v]您的提交已经送达");
					jb.setEnabled(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(jb);

		this.setLayout(new FlowLayout());// 流式布局
		this.setBounds(300, 200, 400, 300);
		this.setResizable(false);
		this.setVisible(true);
	}
}
