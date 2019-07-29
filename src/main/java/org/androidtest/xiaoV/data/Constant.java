package org.androidtest.xiaoV.data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.androidtest.xiaoV.publicutil.FileOperatorUtil;
import org.androidtest.xiaoV.publicutil.WeekHelper;

/**
 * 常量类
 *
 * @author caipeipei
 */
public class Constant {

	/**
	 * 输出路径相关
	 */
	public static String getOutPutPath() {
		return FileOperatorUtil.mkdirs(System.getProperty("user.dir"))
				.getAbsolutePath();

	}

	public static String getDataSavePath() {
		return FileOperatorUtil.mkdirs(
				getOutPutPath() + File.separator + "data").getAbsolutePath();

	}

	public static String getCurrentWeekSavePath() {
		return FileOperatorUtil.mkdirs(
				getDataSavePath() + File.separator
						+ WeekHelper.getCurrentWeek()).getAbsolutePath();

	}

	/**
	 * 时间处理
	 */
	public final static SimpleDateFormat SIMPLE_DATE_FORMAT_FILE = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");
	public final static SimpleDateFormat SIMPLE_DAY_FORMAT_FILE = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat SIMPLE_DATE_FORMAT_LOG = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 字符串处理
	 */
	public static final String CMD_RESULT_SPLIT = "\r\n";
	public static final String BLANK_SPLIT = "\\s+";
	public static final String COLON_SPLIT = ":";
	public static final String BACKSLASH_SPILT = "/";
	public static final String BlANK_LINE = " ";

	public static final String DAILY_SELF_REFLECTION_DEFAULT_FILENAME_TEMPLATES = "DAILY CHECK LIST";
	public static final String GROUP_RULE_DEFAULT_FILENAME_TEMPLATES = "GROUP RULE-";

	/**
	 * 预设默认值
	 */
	public static final int DEFAULT_START_SLEEP_RED_PACKET_COUNT_TIME = 2100;
	public static final String DEFAULT_ADMIN = "Vince蔡培培";

	/**
	 * 全局参数
	 */
	public static List<Group> groupList = new ArrayList<>();

}
