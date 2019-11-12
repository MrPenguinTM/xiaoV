package org.androidtest.robotp.publicutils;

import org.androidtest.robotp.data.Constant;

import java.util.Date;
import java.util.List;

import static org.androidtest.robotp.data.Constant.SIMPLE_DATE_FORMAT_FILE;

/**
 * 字符串处理工具类
 *
 * @author Vince蔡培培
 */
public class StringUtil {
	public static String filter(String character) {
		character = character.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
		return character;
	}

	/**
	 * 获取当前系统时间，并按指定格式返回
	 *
	 * @return
	 */
	public static String getCurrentSystemTimeWithFileFormat() {
		return SIMPLE_DATE_FORMAT_FILE.format(new Date());
	}

	public static boolean ifNotNullOrEmpty(Object object) {
		boolean isTrue = false;
		if (object != null) {
			if (!object.equals("")) {
				isTrue = true;

			}
		}
		return isTrue;
	}

	public static boolean ifNullOrEmpty(Object object) {
		boolean isTrue = object == null || object.equals("");
		return isTrue;
	}

	public static boolean ifNullOrEmpty(List object) {
		boolean isTrue = object == null || object.isEmpty();
		return isTrue;
	}

	public static String ifNullOrEmptyReturnUnknown(String data) {
		if (data == null || data.equals("")) {
			data = "unknown";
		}
		return data;
	}

	public static String returnOnlyString(String data) {
		return data.split(Constant.CMD_RESULT_SPLIT)[0];
	}

	public static String returnUnknown() {
		return "unknown";
	}

	/**
	 * 去除首尾指定字符
	 *
	 * @param str
	 *            字符串
	 * @param element
	 *            指定字符
	 * @return
	 */
	public static String trimFirstAndLastChar(String str, String element) {
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do {
			int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
			int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str
					.lastIndexOf(element) : str.length();
			str = str.substring(beginIndex, endIndex);
			beginIndexFlag = (str.indexOf(element) == 0);
			endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
		} while (beginIndexFlag || endIndexFlag);
		return str;
	}
}
