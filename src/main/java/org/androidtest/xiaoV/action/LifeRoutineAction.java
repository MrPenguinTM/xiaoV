package org.androidtest.xiaoV.action;

import static org.androidtest.xiaoV.data.Constant.groupList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.apache.log4j.Logger;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 作息打卡组件
 * 
 * @author caipeipei
 *
 */
public class LifeRoutineAction extends Action {
	/**
	 * LifeRoutineClockInAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> LIFE_ROUTINE_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("收到红包，请在手机上查看", MsgTypeEnum.SYS);
			put("Red packet received. View on phone.", MsgTypeEnum.SYS);
		}
	};

	private static final String actionName = "每日作息打卡";

	protected Logger LOG = Logger.getLogger(LifeRoutineAction.class);

	private int life_routine_redpacket_count = -1;
	private boolean life_routine_monrning_call = false;
	private boolean life_routine_sleep_remind = false;
	private int morning_call_time = -1;
	private int sleep_remind_time = -1;

	private final int startSleepListenerTime = 2100;

	private final int endSleepListenerTime = 400;

	public LifeRoutineAction(boolean life_routine_monrning_call,
			int morning_call_time, boolean life_routine_sleep_remind,
			int sleep_remind_time) {
		super(actionName, LIFE_ROUTINE_VAILD_KEYWORD_LIST);
		setLife_routine_redpacket_count(0);
		setLife_routine_monrning_call(life_routine_monrning_call,
				morning_call_time);
		setLife_routine_sleep_remind(life_routine_sleep_remind,
				sleep_remind_time);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
				+ group.getGroupNickName());
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (hour <= getEndSleepListenerTime() / 100
				|| hour >= getStartSleepListenerTime() / 100) {// 21:00 ~ 4:00
			int currentGroupRedPacketCount = -1;
			String currentGroupNickName = group.getGroupNickName();
			for (int i = 0; i < groupList.size(); i++) {
				if (groupList.get(i).getGroupNickName()
						.equals(currentGroupNickName)) {
					currentGroupRedPacketCount = getLife_routine_redpacket_count();
					currentGroupRedPacketCount++;
					setLife_routine_redpacket_count(currentGroupRedPacketCount);
					String message = "睡前打卡人数:" + currentGroupRedPacketCount
							+ "人。";
					if (currentGroupRedPacketCount >= WechatTools
							.getMemberListByGroupNickName2(currentGroupNickName)
							.size()) {
						message = message + "所有人已经完成睡前打卡。";
					} else {
						message = message
								+ "还差"
								+ (WechatTools.getMemberListByGroupNickName2(
										currentGroupNickName).size() - currentGroupRedPacketCount)
								+ "人。";
					}
					MessageTools.sendGroupMsgByNickName(message,
							currentGroupNickName);
					LogUtil.MSG.debug("action: " + "红包总数量: "
							+ currentGroupRedPacketCount);
					break;
				}
			}
		}
		return null;
	}

	private int getEndSleepListenerTime() {
		return endSleepListenerTime;
	}

	private int getLife_routine_redpacket_count() {
		return life_routine_redpacket_count;
	}

	private int getMorningCallTime() {
		return morning_call_time;
	}

	private int getSleepRemindTime() {
		return sleep_remind_time;
	}

	private int getStartSleepListenerTime() {
		return startSleepListenerTime;
	}

	private boolean isLife_routine_monrning_call() {
		return life_routine_monrning_call;
	}

	private boolean isLife_routine_sleep_remind() {
		return life_routine_sleep_remind;
	}

	@Override
	public boolean notify(Group currentGroup) {
		Date currentDate = new Date();
		int currentTime = Integer.parseInt(new SimpleDateFormat("HHmm")
				.format(currentDate));
		String currentTimeString = new SimpleDateFormat("HH:mm")
				.format(currentDate);
		if (isLife_routine_monrning_call()) {// 响应morningcall功能
			if (currentTime == getMorningCallTime()) {
				String currentGroupNickName = currentGroup.getGroupNickName();
				MessageTools.sendGroupMsgByNickName(currentTimeString
						+ "已过，按规则，晚起的不能领取红包噢。", currentGroupNickName);
				int currentGroupRedPacketSize = getLife_routine_redpacket_count();
				String message = "昨晚睡前打卡人数:" + currentGroupRedPacketSize + "人。";
				int size = WechatTools.getMemberListByGroupNickName2(
						currentGroupNickName).size();
				if (currentGroupRedPacketSize >= size) {
					message = message + "所有人已经完成睡前打卡。真棒！";
				} else {
					message = message + "还差"
							+ (size - currentGroupRedPacketSize) + "人。有人得发红包咯！";
				}
				MessageTools.sendGroupMsgByNickName(message,
						currentGroupNickName);
				LOG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + true);
				return true;
			}
		}
		if (isLife_routine_sleep_remind()) {// 响应sleepremind功能
			if (currentTime == getSleepRemindTime()) {
				MessageTools.sendGroupMsgByNickName(currentTimeString
						+ "了，进入睡前时间，一天该收尾咯，大家睡前记得打卡噢。",
						currentGroup.getGroupNickName());
				LOG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + true);
				return true;
			}
		}
		// int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (currentTime == getStartSleepListenerTime()) {
			setLife_routine_redpacket_count(0);
			MessageTools.sendGroupMsgByNickName("[进入睡前打卡红包计数监听模式]\n监听时间"
					+ String.format("%04d", getStartSleepListenerTime())
					+ " ~ " + String.format("%04d", getEndSleepListenerTime()),
					currentGroup.getGroupNickName());
			LOG.info("notify: " + currentGroup.getGroupNickName() + ": report "
					+ true);
			return true;
		}
		return false;
	}

	@Override
	public String report(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setLife_routine_monrning_call(
			boolean life_routine_monrning_call, int morning_call_time) {
		LogUtil.MSG.debug("setLife_routine_monrning_call: args: "
				+ life_routine_monrning_call + ", " + morning_call_time);
		this.life_routine_monrning_call = life_routine_monrning_call;
		if (life_routine_monrning_call) {
			if (0 <= morning_call_time && morning_call_time <= 2400) {
				this.morning_call_time = morning_call_time;
			} else {
				// LOG.error("setLife_routine_monrning_call: value is out of range: "
				// + morning_call_time);
				throw new RuntimeException(
						"setLife_routine_monrning_call: value is out of range: "
								+ morning_call_time);
			}

		}
	}

	private void setLife_routine_redpacket_count(
			int life_routine_redpacket_count) {
		this.life_routine_redpacket_count = life_routine_redpacket_count;
	}

	private void setLife_routine_sleep_remind(
			boolean life_routine_sleep_remind, int sleep_remind_time) {
		LogUtil.MSG.debug("setLife_routine_sleep_remind: args: "
				+ life_routine_sleep_remind + ", " + sleep_remind_time);
		this.life_routine_sleep_remind = life_routine_sleep_remind;
		if (life_routine_sleep_remind) {
			if (0 <= sleep_remind_time && sleep_remind_time <= 2400) {
				this.sleep_remind_time = sleep_remind_time;
			} else {
				throw new RuntimeException(
						"setLife_routine_sleep_remind: value is out of range: "
								+ sleep_remind_time);
			}

		}
	}
}
