package org.androidtest.xiaoV.action.ClockIn;

import static org.androidtest.xiaoV.data.Constant.SIMPLE_DAY_FORMAT_FILE;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.FileOperatorUtil;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 吐槽合景组件
 * 
 * @author caipeipei
 *
 */
public class ComplainingClockIn extends ClockIn {

	private static File complainingWordFile = new File(
			Constant.getDataSavePath() + File.separator + "ComplainingWord.txt");
	/**
	 * ComplainingClockIn类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> COMPLAINING_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("合景", MsgTypeEnum.TEXT);
			put("五区", MsgTypeEnum.TEXT);
			put("物业", MsgTypeEnum.TEXT);
			put("变电站", MsgTypeEnum.TEXT);
			put("换流站", MsgTypeEnum.TEXT);
			put("垃圾站", MsgTypeEnum.TEXT);
			put("学校", MsgTypeEnum.TEXT);
			put("花漫里", MsgTypeEnum.TEXT);
			put("车位", MsgTypeEnum.TEXT);
			put("马桶", MsgTypeEnum.TEXT);
			put("背景墙", MsgTypeEnum.TEXT);
			put("双合同", MsgTypeEnum.TEXT);
			put("誉山", MsgTypeEnum.TEXT);
		}
	};

	private static final String actionName = "日常吐槽合景";

	public ComplainingClockIn(int weeklyLimitTimes) {
		super(actionName, COMPLAINING_VAILD_KEYWORD_LIST, weeklyLimitTimes,
				false);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());
		String result = null;
		String currentGroupNickName = WechatTools
				.getGroupNickNameByGroupUserName(msg.getFromUserName());
		String senderNickName = WechatTools
				.getMemberDisplayOrNickNameByGroupNickName(
						currentGroupNickName, msg.getStatusNotifyUserName());
		LogUtil.MSG.info("action: senderNickName: " + senderNickName);
		String fileUserName = StringUtil.filter(senderNickName);
		LogUtil.MSG.info("action: fileUserName: " + fileUserName);
		int week_sport_limit_times = getWeeklyLimitTimes();
		if (StringUtil.ifNullOrEmpty(fileUserName)) {
			return "@" + senderNickName + " 你的名字有无法识别的字符，无法处理！请改昵称。";
		}
		String filename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
				+ fileUserName + ".complaining";
		File file = new File(Constant.getCurrentWeekSavePath() + File.separator
				+ filename);
		try {
			boolean isExist = false;
			if (file.exists()) {
				isExist = true;
			} else {
				file.createNewFile();
			}
			File dir = new File(Constant.getCurrentWeekSavePath());
			if (dir.isDirectory()) {
				File[] array = dir.listFiles();
				int count = 0;
				for (int i = 0; i < array.length; i++) {
					if (array[i].isFile()
							&& array[i].getName().endsWith(".complaining")
							&& array[i].getName().contains(
									"-" + fileUserName + ".")) {
						count++;
					}
				}
				if (!isExist) {
					if (week_sport_limit_times > count) {
						String content[] = FileOperatorUtil.readFromFile(
								complainingWordFile).split("\n");
						Random ran1 = new Random();
						int index = ran1.nextInt(content.length);
						String keyword = msg.getText();
						Iterator iter = COMPLAINING_VAILD_KEYWORD_LIST
								.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							String vaildKeyword = (String) entry.getKey();
							if (keyword.contains(vaildKeyword)) {
								result = "@" + senderNickName
										+ " 此时你是不是想说：合景！看看这" + vaildKeyword
										+ "！" + content[index];
								break;
							}
						}
					}
				}
			} else {
				LogUtil.MSG.error("action: " + "error:" + dir.getAbsolutePath()
						+ "非文件夹路径！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean notify(Group group) {
		return false;
	}

	@Override
	public String report(Group group) {
		return null;
	}

}
