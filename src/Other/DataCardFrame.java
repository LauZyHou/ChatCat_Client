package Other;

import java.io.DataOutputStream;

import javax.swing.JFrame;

//个人资料卡
public class DataCardFrame extends JFrame {
	DataOutputStream dos;

	// 构造器,传入数据输出流(只向服务器写)
	public DataCardFrame(DataOutputStream dos) {
		super("ChatCat个人资料卡");
		this.dos = dos;

		// 窗体相关
		this.setBounds(200, 100, 400, 500);
		this.setVisible(true);
	}

}
