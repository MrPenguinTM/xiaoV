package org.androidtest.xiaoV.action.ClockIn;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.DateUtil;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;
import org.androidtest.xiaoV.publicutil.WeekHelper;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 周步数打卡组件
 * 
 * @author caipeipei
 *
 */
public class WholeWeekStepClockIn extends ClockIn {

	/**
	 * WholeWeekStepClockIn类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> DAILY_STEP_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("周步数打卡", MsgTypeEnum.TEXT);
		}
	};

	private static final String actionName = "周步数打卡(week)";

	public WholeWeekStepClockIn() {
		super(actionName, DAILY_STEP_VAILD_KEYWORD_LIST, 1, false);
		autoReportTime = new ArrayList<Integer>();
		autoReportTime.add(1000);
		autoReportTime.add(1500);
		autoReportTime.add(2100);
	}

	private List<Integer> autoReportTime;

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());
		String result = null;

		String currentGroupNickName = WechatTools
				.getGroupNickNameByGroupUserName(msg.getFromUserName());
		String senderNickName = WechatTools
				.getMemberDisplayOrNickNameByGroupNickName(
						currentGroupNickName, msg.getStatusNotifyUserName());

		String fileUserName = StringUtil.filter(senderNickName);
		if (StringUtil.ifNullOrEmpty(fileUserName)) {
			result = "@" + senderNickName + " 你的名字有无法识别的字符，无法处理！请改昵称。";
			LogUtil.MSG.warn("action: " + result);
			return result;
		}
		if (!DateUtil.isSunday()) {
			result = "@"
					+ senderNickName
					+ " 周步数打卡请在周天0:00-24:00之间打卡。符合群规达标才打卡，不符群规不达标不打卡。今天非周天，打卡失败。";
			LogUtil.MSG.warn("action: " + result);
			return result;
		}
		String wstepfilename = Constant.SIMPLE_DAY_FORMAT_FILE
				.format(new Date()) + "-" + fileUserName + ".wstep";
		File wstepfile = new File(Constant.getCurrentWeekSavePath()
				+ File.separator + wstepfilename);
		try {
			boolean isExist = false;
			if (wstepfile.exists()) {
				isExist = true;
			} else {
				wstepfile.createNewFile();
			}
			if (isExist) {
				result = "@" + senderNickName + " 你本周已经周步数打卡过，无需重复打卡。"
						+ WeekHelper.getCurrentWeek() + "周步数打卡已完成，再接再励！";
			} else {
				result = "@" + senderNickName + " 本周步数打卡成功！"
						+ WeekHelper.getCurrentWeek() + "步数打卡已完成，继续保持。";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean notify(Group currentGroup) {
		if (DateUtil.isSunday()) {
			Date currentDate = new Date();
			int currentTime = Integer.parseInt(new SimpleDateFormat("HHmm")
					.format(currentDate));
			String currentTimeString = new SimpleDateFormat("HH:mm")
					.format(currentDate);
			for (Integer time : autoReportTime) {
				if (currentTime == time) {
					String currentGroupNickName = currentGroup
							.getGroupNickName();
					MessageTools
							.sendGroupMsgByNickName(
									"今天需要进行本周步数打卡，符合群规达标才打卡，不符群规不达标不打卡。还没打卡的小伙伴记得及时打卡噢！",
									currentGroupNickName);
					LogUtil.MSG.info("notify: "
							+ currentGroup.getGroupNickName() + ": report "
							+ true);
					return true;
				}
			}
			int startTime = 0;
			int endTime = 2330;
			if (currentTime == startTime) {
				String currentGroupNickName = currentGroup.getGroupNickName();
				MessageTools
						.sendGroupMsgByNickName(
								WeekHelper.getCurrentWeek()
										+ "周步数打卡功能已经开启！今天需要进行本周步数打卡，还没打卡的小伙伴记得及时打卡噢！(符合群规达标才打卡，不符群规不达标不打卡)",
								currentGroupNickName);
				LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + true);
				return true;
			} else if (currentTime == endTime) {
				String currentGroupNickName = currentGroup.getGroupNickName();
				MessageTools
						.sendGroupMsgByNickName(
								WeekHelper.getCurrentWeek()
										+ "周步数打卡功能将在0点关闭！还没打卡的小伙伴赶紧及时打卡噢！(符合群规达标才打卡，不符群规不达标不打卡)",
								currentGroupNickName);
				LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + true);
				return true;
			}
		}
		return false;
	}

	@Override
	public String report(Group group) {
		String result = null;
		if (DateUtil.isSunday()) {
			LogUtil.MSG.debug("report: " + this.getClass().getSimpleName()
					+ ", " + group.getGroupNickName());
			String currentGroupNickName = group.getGroupNickName();

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
					int countwstep = 0;
					String name = StringUtil.filter(list.get(i));
					if (StringUtil.ifNullOrEmpty(name)) {
						error404Name = error404Name + "@" + list.get(i) + " ";
						continue;
					}
					for (int j = 0; j < array.length; j++) {
						if ((array[j].isFile()
								&& array[j].getName().endsWith(".wstep") && array[j]
								.getName().contains("-" + list.get(i) + "."))
								|| (array[j].isFile()
										&& array[j].getName()
												.endsWith(".wstep") && array[j]
										.getName().contains("-" + name + "."))) {
							countwstep++;
						}
					}
					if (countwstep < getWeeklyLimitTimes()) {
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
				result = "------本周步数未达标：-------\n" + errorSport;
				if (!StringUtil.ifNullOrEmpty(error404Name)) {
					result = result + "\n如下（微信名含非法字符无法统计: " + error404Name
							+ "）";
				}
			} else {
				LogUtil.MSG.error("report: " + "error:" + dir.getAbsolutePath()
						+ "非文件夹路径！");
			}
		}
		return result;
	}

}
