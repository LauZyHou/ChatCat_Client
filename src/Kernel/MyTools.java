package Kernel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//工具类,存放一些自定义的方法
public class MyTools {
	// 求字符串str中第k次出现子串s的位置,使用正则
	public static int indexOf(String str, int k, String s) {
		Matcher slashMatcher = Pattern.compile(s).matcher(str);
		int mIdx = 0;
		while (slashMatcher.find()) {
			mIdx++;
			if (mIdx == k)// 如果确实能找到第k个
				return slashMatcher.start();
		}
		// 找不到第k个时候返回-1
		return -1;
	}
}
