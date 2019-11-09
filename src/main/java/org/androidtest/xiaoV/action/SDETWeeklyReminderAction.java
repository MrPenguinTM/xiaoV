package org.androidtest.xiaoV.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.WeekHelper;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 部门例行事件提醒组件
 * 
 * @author caipeipei
 *
 */
public class SDETWeeklyReminderAction extends Action {

	private static final String actionName = "部门例行事件提醒";

	/**
	 * SDETWeeklyReminderAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> SDET_WEEKLY_REMINDER_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("部门例行事件提醒", MsgTypeEnum.TEXT);
		}
	};

	public SDETWeeklyReminderAction() {
		super(actionName, SDET_WEEKLY_REMINDER_VAILD_KEYWORD_LIST);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		return null;
	}

	@Override
	public boolean notify(Group currentGroup) {
		Date currentDate = new Date();
		int currentTime = Integer.parseInt(new SimpleDateFormat("HHmm")
				.format(currentDate));
		int dayOfWeek = WeekHelper.getCurrentDayOfWeek();
		boolean result = false;

		if (dayOfWeek == Calendar.WEDNESDAY) {
			switch (currentTime) {
			case 1845:
				MessageTools.sendGroupMsgByNickName(
						"19:00周会时间将至，大家可以过来了~守时惜时，你我同行。",
						currentGroup.getGroupNickName());
				result = true;// 传false表示允许轮询，不阻塞消息线程
				LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + result);
				break;
			case 1700:
				MessageTools.sendGroupMsgByNickName(
						"大家把本周任务的进展在JIRA更新下。工作到位、任务量化、登记及时，说明是靠谱之人哦！",
						currentGroup.getGroupNickName());
				result = true;// 传false表示允许轮询，不阻塞消息线程
				LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + result);
				break;
			default:
				break;
			}
		} else if (dayOfWeek == Calendar.FRIDAY) {
			switch (currentTime) {
			case 1700:
				MessageTools
						.sendGroupMsgByNickName(
								"明日复明日,明日何其多。大家今天把本周待验收任务验收下。每周定期梳理，要保质保量地确定已经完成，才能关闭任务。",
								currentGroup.getGroupNickName());
				result = true;// 传false表示允许轮询，不阻塞消息线程
				LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + result);
				break;
			default:
				break;
			}
		} else if (dayOfWeek == Calendar.SUNDAY) {
			switch (currentTime) {
			case 1900:
				MessageTools.sendGroupMsgByNickName(
						"如果没有特殊说明，按之前的安排参加软件一二部的周早会。",
						currentGroup.getGroupNickName());
				result = true;// 传false表示允许轮询，不阻塞消息线程
				LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + result);
				break;
			default:
				break;
			}
		}
		return result;
	}

	@Override
	public String report(Group group) {
		return null;
	}

}