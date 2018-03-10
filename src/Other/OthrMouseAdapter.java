package Other;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import Kernel.KernelFrame;

//"其它"面板上的组件的鼠标事件的适配器
public class OthrMouseAdapter extends MouseAdapter {
	// 鼠标悬停(进入)变色
	@Override
	public void mouseEntered(MouseEvent e) {
		((JButton) e.getSource()).setBackground(KernelFrame.clr_othr2);
	}

	// 鼠标退出变色
	@Override
	public void mouseExited(MouseEvent e) {
		((JButton) e.getSource()).setBackground(KernelFrame.clr_othr);
	}
}
