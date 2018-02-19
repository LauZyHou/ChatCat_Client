package Other;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;

import javax.swing.JFrame;

import Kernel.KernelFrame;

//添加好友界面
public class AddFrndFrame extends JFrame {
	DataOutputStream dos;

	// 构造器
	public AddFrndFrame(DataOutputStream dos) {
		this.dos = dos;
		myInit();// 窗体初始化
	}

	// 窗体初始化
	private void myInit() {

		this.setBounds(250, 150, 300, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				KernelFrame.aff = null;
				System.gc();
			}
		});
	}
}
