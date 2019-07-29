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
 * 每周运动打卡组件
 * 
 * @author caipeipei
 *
 */
public class WeeklySportClockIn extends ClockIn {

	/**
	 * WeeklySportClockIn类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> WEEKLY_SPORT_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("运动打卡", MsgTypeEnum.TEXT);
			// put("打卡运动", MsgTypeEnum.TEXT);
		}
	};

	private static final String actionName = "每周运动打卡";

	public WeeklySportClockIn(int weeklyLimitTimes) {
		this(weeklyLimitTimes, false);
	}

	public WeeklySportClockIn(int weeklyLimitTimes, boolean isDiff) {
		super(actionName, WEEKLY_SPORT_VAILD_KEYWORD_LIST, weeklyLimitTimes,
				isDiff);
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
		String sportfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
				+ fileUserName + ".sport";
		String stepfilename = Constant.SIMPLE_DAY_FORMAT_FILE
				.format(new Date()) + "-" + fileUserName + ".step";
		File sportfile = new File(Constant.getCurrentWeekSavePath()
				+ File.separator + sportfilename);
		File stepfile = new File(Constant.getCurrentWeekSavePath()
				+ File.separator + stepfilename);
		try {
			boolean isExist = false;
			boolean isDiffExist = false;
			if (sportfile.exists()) {
				isExist = true;
			} else if (isDiff() && stepfile.exists()) {
				isDiffExist = true;
			} else {
				sportfile.createNewFile();
			}
			File dir = new File(Constant.getCurrentWeekSavePath());
			if (dir.isDirectory()) {
				File[] array = dir.listFiles();
				int count = 0;
				for (int i = 0; i < array.length; i++) {
					if (array[i].isFile()
							&& array[i].getName().endsWith(".sport")
							&& array[i].getName().contains(
									"-" + fileUserName + ".")) {
						count++;
					}
				}
				if (isExist) {
					if (week_sport_limit_times > count) {
						result = "@" + senderNickName + " 你今天已经运动打卡过，无需重复打卡。"
								+ WeekHelper.getCurrentWeek() + "打卡运动第" + count
								+ "次，本周还差" + (week_sport_limit_times - count)
								+ "次";
					} else {
						result = "@" + senderNickName + " 你今天已经运动打卡过，无需重复打卡。"
								+ WeekHelper.getCurrentWeek() + "打卡运动第" + count
								+ "次，本周已经达标。已经运动了" + count + "次";
					}
				} else if (isDiff() && isDiffExist) {
					result = "@" + senderNickName + " 你今天已经步数打卡过，不能运行打卡。";

				} else {
					if (week_sport_limit_times > count) {
						result = "@" + senderNickName + " 于"
								+ WeekHelper.getCurrentWeek() + "打卡运动第" + count
								+ "次，本周还差" + (week_sport_limit_times - count)
								+ "次";
					} else {
						result = "@" + senderNickName + " 于"
								+ WeekHelper.getCurrentWeek() + "打卡运动第" + count
								+ "次，本周已经达标。已经运动了" + count + "次";
					}
				}

			} else {
				LogUtil.MSG.error("action: " + "error:" + dir.getAbsolutePath()
						+ "非文件夹路径！");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean notify(Group group) {
		// TODO Auto-generated method stub
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
		String errorSport = null;
		String error404Name = "";
		if (dir.isDirectory()) {
			File[] array = dir.listFiles();
			for (int i = 0; i < list.size(); i++) {
				int countsport = 0;
				String name = StringUtil.filter(list.get(i));
				if (StringUtil.ifNullOrEmpty(name)) {
					error404Name = error404Name + "@" + list.get(i) + " ";
					continue;
				}
				for (int j = 0; j < array.length; j++) {
					if ((array[j].isFile()
							&& array[j].getName().endsWith(".sport") && array[j]
							.getName().contains("-" + list.get(i) + "."))
							|| (array[j].isFile()
									&& array[j].getName().endsWith(".sport") && array[j]
									.getName().contains("-" + name + "."))) {
						countsport++;
					}
				}
				if (countsport < getWeeklyLimitTimes()) {
					if (errorSport == null) {
						errorSport = "@" + list.get(i) + " ";
					} else {
						errorSport = errorSport + "@" + list.get(i) + " ";
					}
				}
			}
			if (errorSport == null) {
				errorSport = "无";
			}
			result = "------本周运动未达标：-------\n" + errorSport;
			if (!StringUtil.ifNullOrEmpty(error404Name)) {
				result = result + "\n如下（微信名含非法字符无法统计: " + error404Name + "）";
			}
		} else {
			LogUtil.MSG.error("report: " + "error:" + dir.getAbsolutePath()
					+ "非文件夹路径！");
		}
		return result;
	}

}
