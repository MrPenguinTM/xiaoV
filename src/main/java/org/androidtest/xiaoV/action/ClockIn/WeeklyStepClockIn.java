package org.androidtest.xiaoV.action.ClockIn;

import static org.androidtest.xiaoV.data.Constant.SIMPLE_DAY_FORMAT_FILE;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;
import org.androidtest.xiaoV.publicutil.WeekHelper;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 每日步数打卡组件
 * 
 * @author caipeipei
 *
 */
public class WeeklyStepClockIn extends ClockIn {

	/**
	 * DailyStepClockIn类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> DAILY_STEP_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("步数打卡", MsgTypeEnum.TEXT);
			// put("打卡步数", MsgTypeEnum.TEXT);
		}
	};

	private static final String actionName = "每周步数打卡(day)";

	public WeeklyStepClockIn() {
		this(7);
	}

	public WeeklyStepClockIn(int weeklyLimitTimes) {
		this(weeklyLimitTimes, false);
	}

	public WeeklyStepClockIn(int weeklyLimitTimes, boolean isDiff) {
		super(actionName, DAILY_STEP_VAILD_KEYWORD_LIST, weeklyLimitTimes,
				isDiff);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());

		String currentGroupNickName = WechatTools
				.getGroupNickNameByGroupUserName(msg.getFromUserName());
		String senderNickName = WechatTools
				.getMemberDisplayOrNickNameByGroupNickName(
						currentGroupNickName, msg.getStatusNotifyUserName());
		String result = null;
		String fileUserName = StringUtil.filter(senderNickName);
		if (StringUtil.ifNullOrEmpty(fileUserName)) {
			result = "@" + senderNickName + " 你的名字有无法识别的字符，无法处理！请改昵称。";
			LogUtil.MSG.warn("action: " + result);
			return result;
		}

		String stepfilename = Constant.SIMPLE_DAY_FORMAT_FILE
				.format(new Date()) + "-" + fileUserName + ".step";
		File stepfile = new File(Constant.getCurrentWeekSavePath()
				+ File.separator + stepfilename);
		String sportfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
				+ fileUserName + ".sport";
		File sportfile = new File(Constant.getCurrentWeekSavePath()
				+ File.separator + sportfilename);
		try {
			boolean isExist = false;
			boolean isDiffExist = false;
			if (stepfile.exists()) {
				isExist = true;
			} else if (isDiff() && sportfile.exists()) {
				isDiffExist = true;
			} else {
				stepfile.createNewFile();
			}
			File dir = new File(Constant.getCurrentWeekSavePath());
			if (dir.isDirectory()) {
				File[] array = dir.listFiles();
				int count = 0;
				for (int i = 0; i < array.length; i++) {
					if (array[i].isFile()
							&& array[i].getName().endsWith(".step")
							&& array[i].getName().contains(
									"-" + fileUserName + ".")) {
						count++;
					}
				}
				if (isExist) {
					result = "@" + senderNickName + " 你今天已经步数打卡过，无需重复打卡。"
							+ WeekHelper.getCurrentWeek() + "步数打卡已完成了" + count
							+ "次，再接再励！";
				} else if (isDiff() && isDiffExist) {
					result = "@" + senderNickName + " 你今天已经运动打卡过，不能步数打卡。";
				} else {
					result = "@" + senderNickName + " 今天步数打卡成功！"
							+ WeekHelper.getCurrentWeek() + "步数打卡已完成了" + count
							+ "次，继续保持。";
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
		LogUtil.MSG.debug("report: " + this.getClass().getSimpleName() + ", "
				+ group.getGroupNickName());
		String currentGroupNickName = group.getGroupNickName();
		String result = null;
		File dir = new File(Constant.getCurrentWeekSavePath());
		List<String> list = WechatTools
				.getMemberListByGroupNickName2(currentGroupNickName);
		LogUtil.MSG.debug("report: " + currentGroupNickName + "群成员:"
				+ list.toString());
		String errorStep = null;
		String error404Name = "";
		String todaystepkeyword = Constant.SIMPLE_DAY_FORMAT_FILE
				.format(new Date());
		boolean isCurrentUserExistTodayStep = false;
		if (dir.isDirectory()) {
			File[] array = dir.listFiles();
			for (int i = 0; i < list.size(); i++) {
				isCurrentUserExistTodayStep = false;
				String name = StringUtil.filter(list.get(i));
				if (StringUtil.ifNullOrEmpty(name)) {
					error404Name = error404Name + "@" + list.get(i) + " ";
					continue;
				}
				int count = 0;
				for (int j = 0; j < array.length; j++) {

					if ((array[j].isFile()
							&& array[j].getName().endsWith(".step") && array[j]
							.getName().contains("-" + list.get(i) + "."))
							|| (array[j].isFile()
									&& array[j].getName().endsWith(".step") && array[j]
									.getName().contains("-" + name + "."))) {
						if (getWeeklyLimitTimes() >= 7) {
							if (array[j].getName().contains(todaystepkeyword)) {
								isCurrentUserExistTodayStep = true;
								break;
							}
						} else {
							count++;
						}
					}

				}
				if (getWeeklyLimitTimes() >= 7) {
					if (!isCurrentUserExistTodayStep) {
						if (errorStep == null) {
							errorStep = "@" + list.get(i) + " ";
						} else {
							errorStep = errorStep + "@" + list.get(i) + " ";
						}
					}
				} else {
					if (count < getWeeklyLimitTimes()) {
						if (errorStep == null) {
							errorStep = "@" + list.get(i) + " ";
						} else {
							errorStep = errorStep + "@" + list.get(i) + " ";
						}
					}

				}
			}
			if (errorStep == null) {
				errorStep = "无";
			}
			if (getWeeklyLimitTimes() >= 7) {
				result = "------今日步数未完成：-------\n" + errorStep;
			} else {
				result = "------本周步数未完成：-------\n" + errorStep;
			}
			if (!StringUtil.ifNullOrEmpty(error404Name)) {
				result = result + "\n如下（微信名含非法字符无法统计: " + error404Name + "）";
			}
		} else {
			LogUtil.MSG.error("report: " + "reportProcess: " + "error:"
					+ dir.getAbsolutePath() + "非文件夹路径！");
		}
		return result;
	}
}
