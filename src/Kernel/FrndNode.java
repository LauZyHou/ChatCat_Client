package Kernel;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

//联系人列表中的好友树结点,实现TreeNode接口
public class FrndNode implements TreeNode {
	String UsrNum = null;// 账号
	String Name = null;// 网名
	int HeadID;// 头像号码
	ImageIcon ii_head = null;// 头像图像
	String Signature = null;// 个性签名

	private ArrayList<TreeNode> al_chldrn = null;// 子结点数组,泛型
	private TreeNode tn_prnt = null;// 父结点,对象上转型

	// 为用户结点使用的构造器
	public FrndNode(String usr, String nm, int hd, String sgntr) {
		this.UsrNum = usr;
		this.Name = nm;
		this.HeadID = hd;
		// 头像图像按头像号码生成,不必传入
		this.ii_head = new ImageIcon("./pic/cat" + hd + ".jpeg");
		this.Signature = sgntr;
	}

	// 为分组结点使用的构造器
	FrndNode(String nm) {
		this.Name = nm;
	}

	// 判断参数node结点是否是当前这个结点的子结点
	public boolean hasChild(TreeNode node) {
		// 传入null时抛出异常
		if (node == null) {
			throw new IllegalArgumentException("传入了null");
		}
		return node.getParent() == this;
	}

	// 为当前结点添加子结点
	public void add(FrndNode mtn_child) {
		// 判断一下当前结点是否没有创建过存子结点的ArrayList
		if (this.al_chldrn == null) {
			this.al_chldrn = new ArrayList<TreeNode>();// 创建之
		}
		// 添加进来,泛型
		this.al_chldrn.add(mtn_child);
		// 同时子结点方面与本结点建立父亲关系
		mtn_child.tn_prnt = this;
	}

	// 按下标去获得子结点ArrayList中的一个结点
	@Override
	public TreeNode getChildAt(int childIndex) {
		// 没有子结点时抛出异常
		if (al_chldrn == null) {
			throw new ArrayIndexOutOfBoundsException("该结点无子结点");
		}
		// 直接用ArrayList的get方法
		return al_chldrn.get(childIndex);
	}

	// 获得子结点的数目
	@Override
	public int getChildCount() {
		// 如果子结点ArrayList未定义,说明是希望这个结点是叶子结点
		// 否则只要设定这个ArrayList定义了但为空
		if (al_chldrn == null)
			return -1;// 标识这个结点不希望成为非叶结点
		// 直接用ArrayList的size()方法
		return al_chldrn.size();
	}

	// 获得父结点
	@Override
	public TreeNode getParent() {
		return tn_prnt;// 不论是否为空,可以直接返回
	}

	// 获得传入的结点在子结点ArrayList中的下标
	@Override
	public int getIndex(TreeNode node) {
		// 传入null时抛出异常
		if (node == null) {
			throw new IllegalArgumentException("传入了null");
		} else if (this.hasChild(node)) {
			return -1;// 传入的结点不是本结点的子结点
		}
		// 直接用ArrayList的indexOf方法
		return al_chldrn.indexOf(node);
	}

	// 是否允许子结点
	@Override
	public boolean getAllowsChildren() {
		return true;// 允许
	}

	// 判断本结点是否是叶子结点
	@Override
	public boolean isLeaf() {
		// 1:叶子结点不希望被定义子节点ArrayList
		// 2:叶子结点一定有父结点,否则是一个空根结点
		if (this.getChildCount() == -1 && this.getParent() != null) {
			return true;
		}
		return false;
	}

	// 将接收方的子结点作为枚举类型返回,没用到
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration children() {
		return null;
	}

}
