package org.androidtest.xiaoV.factory;

import java.io.File;
import java.util.Map;

import org.androidtest.xiaoV.Config;
import org.androidtest.xiaoV.action.Action;
import org.androidtest.xiaoV.action.DailySelfReflectionAction;
import org.androidtest.xiaoV.action.GroupAttentionAction;
import org.androidtest.xiaoV.action.GroupRuleAction;
import org.androidtest.xiaoV.action.LastWeeklyReportAction;
import org.androidtest.xiaoV.action.LifeRoutineAction;
import org.androidtest.xiaoV.action.MenuAction;
import org.androidtest.xiaoV.action.RecallSportAction;
import org.androidtest.xiaoV.action.RecallStepAction;
import org.androidtest.xiaoV.action.WeeklyReportAction;
import org.androidtest.xiaoV.action.ClockIn.WeeklySportClockIn;
import org.androidtest.xiaoV.action.ClockIn.WeeklyStepClockIn;
import org.androidtest.xiaoV.action.ClockIn.WholeWeekStepClockIn;
import org.androidtest.xiaoV.publicutil.DateUtil;

public class ActionFactory {

	public static DailySelfReflectionAction createDailySelfReflectionAction(
			Map<String, File> whiteList, boolean noonRemind) {
		DailySelfReflectionAction action = null;
		if (Config.DEBUG) {
			action = new DailySelfReflectionAction(whiteList,
					DateUtil.getCurrentTime() + 5,
					DateUtil.getCurrentTime() + 7);
		} else {
			action = new DailySelfReflectionAction(whiteList, noonRemind);
		}

		return action;
	}

	public static WeeklyStepClockIn createDailyStepClockIn() {
		WeeklyStepClockIn action = new WeeklyStepClockIn(7);
		return action;
	}

	public static WholeWeekStepClockIn createWholeWeekStepClockIn() {
		WholeWeekStepClockIn action = new WholeWeekStepClockIn();
		return action;
	}

	public static RecallStepAction createRecallStepAction() {
		RecallStepAction action = new RecallStepAction();
		return action;
	}

	public static RecallSportAction createRecallSportAction() {
		RecallSportAction action = new RecallSportAction();
		return action;
	}

	public static WeeklyStepClockIn createDailyStepClockIn(int times) {
		return createDailyStepClockIn(times, false);
	}

	public static WeeklyStepClockIn createDailyStepClockIn(int times,
			boolean isDiff) {
		WeeklyStepClockIn action = new WeeklyStepClockIn(times, isDiff);
		return action;
	}

	public static Action createGroupRuleAction(String groupNickName, File file) {
		GroupRuleAction action = new GroupRuleAction(groupNickName, file);
		return action;
	}

	public static LifeRoutineAction createLifeRoutineClockInAction(
			boolean life_routine_monrning_call, int morning_call_time,
			boolean life_routine_sleep_remind, int sleep_remind_time) {
		LifeRoutineAction action = null;
		if (Config.DEBUG) {
			action = new LifeRoutineAction(life_routine_monrning_call,
					DateUtil.getCurrentTime() + 1, life_routine_sleep_remind,
					DateUtil.getCurrentTime() + 3);
		} else {
			action = new LifeRoutineAction(life_routine_monrning_call,
					morning_call_time, life_routine_sleep_remind,
					sleep_remind_time);
		}
		return action;
	}

	public static MenuAction createMenuAction() {
		MenuAction action = new MenuAction();
		return action;
	}

	public static WeeklyReportAction createWeeklyReportClockInAction(
			int dailyStep_weeklyLimitTimes, int weeklySport_weeklyLimitTimes) {
		WeeklyReportAction action = new WeeklyReportAction(
				dailyStep_weeklyLimitTimes, weeklySport_weeklyLimitTimes, -1);
		return action;
	}

	public static LastWeeklyReportAction createLastWeeklyReportClockInAction(
			int dailyStep_weeklyLimitTimes, int weeklySport_weeklyLimitTimes) {
		LastWeeklyReportAction action = new LastWeeklyReportAction(
				dailyStep_weeklyLimitTimes, weeklySport_weeklyLimitTimes, -1);
		return action;
	}

	public static WeeklyReportAction createWeeklyReportClockInAction(
			int dailyStep_weeklyLimitTimes, int weeklySport_weeklyLimitTimes,
			int wholeweeklyStep_weeklyLimitTimes) {
		WeeklyReportAction action = new WeeklyReportAction(
				dailyStep_weeklyLimitTimes, weeklySport_weeklyLimitTimes,
				wholeweeklyStep_weeklyLimitTimes);
		return action;
	}

	public static LastWeeklyReportAction createLastWeeklyReportClockInAction(
			int dailyStep_weeklyLimitTimes, int weeklySport_weeklyLimitTimes,
			int wholeweeklyStep_weeklyLimitTimes) {
		LastWeeklyReportAction action = new LastWeeklyReportAction(
				dailyStep_weeklyLimitTimes, weeklySport_weeklyLimitTimes,
				wholeweeklyStep_weeklyLimitTimes);
		return action;
	}

	public static WeeklySportClockIn createWeeklySportClockIn(
			int week_sport_limit_times) {
		return createWeeklySportClockIn(week_sport_limit_times, false);
	}

	public static WeeklySportClockIn createWeeklySportClockIn(
			int week_sport_limit_times, boolean isDiff) {
		WeeklySportClockIn action = new WeeklySportClockIn(
				week_sport_limit_times, isDiff);
		return action;
	}

	public static GroupAttentionAction createGroupAttentionAction(String text) {
		GroupAttentionAction action = new GroupAttentionAction(text);
		return action;
	}

}
