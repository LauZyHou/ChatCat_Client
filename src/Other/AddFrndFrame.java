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
		super("ChatCat添加好友");
		this.dos = dos;
		myInit();// 窗体初始化
	}

	// 窗体初始化
	private void myInit() {
		// 提示:id

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
		this.setVisible(true);
	}
}
