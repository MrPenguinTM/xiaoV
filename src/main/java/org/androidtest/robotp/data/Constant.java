package org.androidtest.robotp.data;

import java.text.SimpleDateFormat;

/**
 * 全局常量类
 *
 * @Author: Vince蔡培培
 */
public class Constant {
	/**
	 * 时间处理
	 */
	public final static SimpleDateFormat SIMPLE_DATE_FORMAT_FILE = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	public final static SimpleDateFormat SIMPLE_DAY_FORMAT_FILE = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat SIMPLE_DATE_FORMAT_LOG = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat CRON_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	/**
	 * 字符串处理
	 */
	public static final String CMD_RESULT_SPLIT = "\r\n";
	public static final String BLANK_SPLIT = "\\s+";
	public static final String COLON_SPLIT = ":";
	public static final String BACKSLASH_SPILT = "/";
	public static final String SEMICOLON_SPILT = ";";
	public static final String BlANK_LINE = " ";
	public static final String AT_SPILT = "@";
}
