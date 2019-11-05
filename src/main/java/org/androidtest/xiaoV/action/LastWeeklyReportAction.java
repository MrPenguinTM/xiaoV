package org.androidtest.xiaoV.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidtest.xiaoV.action.ClockIn.WeeklySportClockIn;
import org.androidtest.xiaoV.action.ClockIn.WeeklyStepClockIn;
import org.androidtest.xiaoV.action.ClockIn.WholeWeekStepClockIn;
import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;
import org.androidtest.xiaoV.publicutil.WeekHelper;
import org.apache.log4j.Logger;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 周报组件
 * 
 * @author caipeipei
 *
 */
public class LastWeeklyReportAction extends Action {

	/**
	 * LastWeeklyReportAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> LAST_WEEKLY_REPORT_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("上周周报", MsgTypeEnum.TEXT);
		}
	};

	protected Logger LOG = Logger.getLogger(LastWeeklyReportAction.class);

	private static final String actionName = "上周统计重报";

	private int dailyStep_weeklyLimitTimes = -1;

	private int weeklySport_weeklyLimitTimes = -1;

	private int wholeweeklyStep_weeklyLimitTimes = -1;

	private boolean supportStepAction = true;
	private boolean supportSportAction = true;
	private boolean supportWStepAction = true;

	public LastWeeklyReportAction(int dailyStep_weeklyLimitTimes,
			int weeklySport_weeklyLimitTimes,
			int wholeweeklyStep_weeklyLimitTimes) {
		super(actionName, LAST_WEEKLY_REPORT_VAILD_KEYWORD_LIST);
		setDailyStep_weeklyLimitTimes(dailyStep_weeklyLimitTimes);
		setWeeklySport_weeklyLimitTimes(weeklySport_weeklyLimitTimes);
		setWholeWeekStep_weeklyLimitTimes(wholeweeklyStep_weeklyLimitTimes);
	}

	private void setWholeWeekStep_weeklyLimitTimes(int i) {
		LogUtil.MSG.debug("setWholeWeekStep_weeklyLimitTimes: " + i);
		if (i >= 0) {
			this.wholeweeklyStep_weeklyLimitTimes = 1;
			supportWStepAction = true;
		} else {
			supportWStepAction = false;
		}
	}

	private int getWholeWeekStep_weeklyLimitTimes() {
		return wholeweeklyStep_weeklyLimitTimes;
	}

	@Override
	public String action(Group g, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
				+ g.getGroupNickName());
		String result = null;
		String currentGroupNickName = g.getGroupNickName();
		File dir = new File(Constant.getLastWeekSavePath());
		List<String> list = WechatTools
				.getMemberListByGroupNickName2(currentGroupNickName);
		LogUtil.MSG.debug("action: " + currentGroupNickName + "群成员:"
				+ list.toString());
		String error404Name = "";
		Group group = null;
		for (Group gr : Constant.groupList) {
			if (gr.getGroupNickName().equals(currentGroupNickName)) {
				group = gr;
				break;
			}
		}
		if (group != null) {
			boolean supportSport = false;
			boolean supportStep = false;
			boolean supportWStep = false;
			if (group.containsAction(WeeklySportClockIn.class)) {
				supportSport = true;
			}
			if (group.containsAction(WeeklyStepClockIn.class)) {
				supportStep = true;
			}
			if (group.containsAction(WholeWeekStepClockIn.class)) {
				supportWStep = true;
			}
			if (supportSport == true || supportStep == true
					|| supportWStep == true) {
				if (dir.isDirectory()) {
					File[] array = dir.listFiles();
					for (int i = 0; i < list.size(); i++) {
						int countstep = 0;
						int countsport = 0;
						int countwstep = 0;
						String name = StringUtil.filter(list.get(i));
						if (StringUtil.ifNullOrEmpty(name)) {
							error404Name = error404Name + "@" + list.get(i)
									+ " ";
							continue;
						}
						for (int j = 0; j < array.length; j++) {

							if ((array[j].isFile()
									&& array[j].getName().endsWith(".step") && array[j]
									.getName()
									.contains("-" + list.get(i) + "."))
									|| (array[j].isFile()
											&& array[j].getName().endsWith(
													".step") && array[j]
											.getName().contains(
													"-" + name + "."))) {
								countstep++;
							}
							if ((array[j].isFile()
									&& array[j].getName().endsWith(".sport") && array[j]
									.getName()
									.contains("-" + list.get(i) + "."))
									|| (array[j].isFile()
											&& array[j].getName().endsWith(
													".sport") && array[j]
											.getName().contains(
													"-" + name + "."))) {
								countsport++;
							}

							if ((array[j].isFile()
									&& array[j].getName().endsWith(".wstep") && array[j]
									.getName()
									.contains("-" + list.get(i) + "."))
									|| (array[j].isFile()
											&& array[j].getName().endsWith(
													".wstep") && array[j]
											.getName().contains(
													"-" + name + "."))) {
								countwstep++;
							}

						}
						if (result == null) {
							result = "=== 上周数据重新报告 ===\n";
							result = result + WeekHelper.getLastWeek() + "\n";
							result = result + list.get(i) + ":";
							if (supportSportAction) {
								if (supportSport) {
									result = result + "	运动" + countsport + "/"
											+ getWeeklySport_weeklyLimitTimes();
								}
							}
							if (supportStepAction) {
								if (supportStep) {
									result = result + "	步数" + countstep + "/"
											+ getDailyStep_weeklyLimitTimes();
								}
							}
							if (supportWStepAction) {
								if (supportWStep) {
									result = result
											+ "	周步数"
											+ countwstep
											+ "/"
											+ getWholeWeekStep_weeklyLimitTimes();
								}
							}
							result = result + "；\n";
						} else {
							result = result + list.get(i) + ":";
							if (supportSportAction) {
								if (supportSport) {
									result = result + "	运动" + countsport + "/"
											+ getWeeklySport_weeklyLimitTimes();
								}
							}
							if (supportStepAction) {
								if (supportStep) {
									result = result + "	步数" + countstep + "/"
											+ getDailyStep_weeklyLimitTimes();
								}
							}
							if (supportWStepAction) {
								if (supportWStep) {
									result = result
											+ "	周步数"
											+ countwstep
											+ "/"
											+ getWholeWeekStep_weeklyLimitTimes();
								}
							}
							result = result + "；\n";
						}
					}
					if (!StringUtil.ifNullOrEmpty(error404Name)) {
						result = result + "\n如下（微信名含非法字符无法统计: " + error404Name
								+ "）";
					}
					// List<Action> actions = group.getActionList();
					// for (Action a : actions) {
					// if (a.getClass().getGenericSuperclass() == ClockIn.class)
					// {
					// if (!supportStepAction
					// && a.getClass() == WeeklyStepClockIn.class) {
					// continue;
					// }
					// if (!supportSportAction
					// && a.getClass() == WeeklySportClockIn.class) {
					// continue;
					// }
					// if (!supportWStepAction
					// && a.getClass() == WholeWeekStepClockIn.class) {
					// continue;
					// }
					// String content = a.report(group);
					// if (StringUtil.ifNotNullOrEmpty(content)) {
					// content = "\n" + content;
					// result = result + content;
					// }
					// }
					// }
				} else {
					LogUtil.MSG.error("action: " + "error:"
							+ dir.getAbsolutePath() + "非文件夹路径！");
				}
			}
		}

		return result;
	}

	private int getDailyStep_weeklyLimitTimes() {
		return dailyStep_weeklyLimitTimes;
	}

	private int getWeeklySport_weeklyLimitTimes() {
		return weeklySport_weeklyLimitTimes;
	}

	@Override
	public boolean notify(Group currentGroup) {
		return false;
	}

	@Override
	public String report(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setDailyStep_weeklyLimitTimes(int dailyStep_weeklyLimitTimes) {
		LogUtil.MSG.debug("setDailyStep_weeklyLimitTimes: "
				+ dailyStep_weeklyLimitTimes);
		if (dailyStep_weeklyLimitTimes >= 0) {
			this.dailyStep_weeklyLimitTimes = dailyStep_weeklyLimitTimes;
			supportStepAction = true;
		} else {
			supportStepAction = false;
			// throw new RuntimeException(
			// "setDailyStep_weeklyLimitTimes: out of range: "
			// + dailyStep_weeklyLimitTimes);
		}

	}

	private void setWeeklySport_weeklyLimitTimes(
			int weeklySport_weeklyLimitTimes) {
		LogUtil.MSG.debug("setWeeklySport_weeklyLimitTimes: "
				+ weeklySport_weeklyLimitTimes);
		if (weeklySport_weeklyLimitTimes >= 0) {
			this.weeklySport_weeklyLimitTimes = weeklySport_weeklyLimitTimes;
			supportSportAction = true;
		} else {
			supportSportAction = false;
			// throw new RuntimeException(
			// "setWeeklySport_weeklyLimitTimes: out of range: "
			// + weeklySport_weeklyLimitTimes);
		}

	}
}