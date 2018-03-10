package Login;

public class Main {
	// 服务器ip
	public static String ServerIp = "192.168.1.104";

	// 客户端程序入口
	public static void main(String[] args) {
		LoginClient lc = new LoginClient();// 新建客户端登录窗口
		lc.jtf_nmbr.setText("10001");
		lc.jpf_pswd.setText("3838438");
		lc.jpf_pswd.grabFocus();

		// 下面是测试区
		// String s = "[v]#用户名:刘知昊#头像ID:2#10002,小贤,1#10003,马爷,3#10004,李喆雯,4";
		// new KernelFrame(s, null, null);
		// new FrndChatFrame(new FrndNode("10000", "flora", 3));
		// new SignUpClient();
		// new FrndCardFrame("TODO");
		// new BatchDltFrame();

	}

}
