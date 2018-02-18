package Kernel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

//联系人列表中的好友树渲染器,实现TreeCellRenderer接口
//继承JLabel,使这个对象自己成为渲染的组件
public class FrndTrCllRndrr extends JLabel implements TreeCellRenderer {
	// int mouseRow;// 当前鼠标所在的行,用来和下面row参数做判定实现悬停变色

	// getTreeCellRendererComponent设置当前树单元的值
	// 如果selected为真,则单元格将被绘制为选中的
	// 如果expanded为真,则该节点当前展开
	// 如果leaf为真,节点表示一片叶子
	// 如果hasFocus为真,则节点当前具有焦点
	// tree是接收方正在配置的JTree
	// 返回渲染器用来绘制值的组件(Component对象)
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		// 把Object对象转换为好友树的结点
		FrndNode fn = (FrndNode) value;
		// 是叶子结点,而且不是某个分组里没有人这样的情形造成的临时叶子
		// 这样的结点才是真正的ChatCat用户所在的结点
		if (leaf == true && fn.getParent() != tree.getModel().getRoot()) {
			// 继承自JLabel的文字属性的set方法,JLabel的文字不能换行,使用HTML标签换行
			this.setText("<HTML>" + fn.Name + "<br>" + fn.Signature + "</HTML>");
			// 两步缩放这个好友结点的头像
			Image img = fn.ii_head.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
			// 继承自JLabel的图片属性的set方法,转回ImageIcon对象传入
			this.setIcon(new ImageIcon(img));
			this.setIconTextGap(15);// 设置JLabel文字图片间距
			// this.setSize(270, 50);// 设置大小,似乎没用
			this.setOpaque(true);// 设置不透明
			// TODO 通过mouseRow来判断是否悬停在当前行
			// 通过选择与否设置颜色
			if (selected) {
				this.setBackground(new Color(200, 200, 255));
			} else {
				this.setBackground(new Color(220, 220, 255));
			}
		}
		// 如果不是用户结点
		else {
			// 将结点名字拿出来放到JLabel
			this.setOpaque(false);
			this.setText(fn.Name);
			// 当这个结点展开时
			if (expanded == true) {
				// 设置箭头向下的图标
				this.setIcon(new ImageIcon("krnl_pic/dwn_arrw.png"));
			}
			// 当这个结点收敛时
			else {
				// 设置箭头向右的图标
				this.setIcon(new ImageIcon("krnl_pic/rgt_arrw.png"));
			}
			this.setIconTextGap(15);// 设置JLabel文字图片间距
		}
		return this;// 返回绘制的组件,正是继承了JLabel的自己这个对象
	}

}
