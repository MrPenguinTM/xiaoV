package org.androidtest.xiaoV.action.ClockIn;

import java.util.Map;

import org.androidtest.xiaoV.action.Action;
import org.androidtest.xiaoV.publicutil.LogUtil;

import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

public abstract class ClockIn extends Action {
	protected int weeklyLimitTimes = -1;
	protected boolean isDiff = false;

	public ClockIn(String actionName, Map<String, MsgTypeEnum> vailKeywordMap,
			int weeklyLimitTimes) {
		this(actionName, vailKeywordMap, weeklyLimitTimes, false);
	}

	public ClockIn(String actionName, Map<String, MsgTypeEnum> vailKeywordMap,
			int weeklyLimitTimes, boolean isDiff) {
		super(actionName, vailKeywordMap);
		if (weeklyLimitTimes >= 0) {
			this.weeklyLimitTimes = weeklyLimitTimes;
		} else {
			throw new RuntimeException(
					"ClockIn: weeklyLimitTimes is out of range: "
							+ weeklyLimitTimes);
		}
		setDiff(isDiff);
	}

	protected int getWeeklyLimitTimes() {
		return weeklyLimitTimes;
	}

	protected boolean isDiff() {
		return isDiff;
	}

	protected void setDiff(boolean isDiff) {
		this.isDiff = isDiff;
	}

	protected void setWeeklyLimitTimes(int weeklyLimitTimes) {
		LogUtil.MSG.debug("setWeeklyLimitTimes: " + weeklyLimitTimes);
		this.weeklyLimitTimes = weeklyLimitTimes;
	}

}
