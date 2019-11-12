package org.androidtest.robotp.publicutils;

import org.androidtest.robotp.beans.Week;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 周日期处理工具类
 *
 * @author Vince蔡培培
 */
public class WeekHelper {

	private static SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

	public static String getCurrentDateStartWeek() {
		return new SimpleDateFormat("yyyyMMdd").format(DateUtil
				.getFirstDayOfWeek(new Date()));
	}

	public static String getCurrentWeek() {
		String current = getCurrentDateStartWeek();
		List<Week> weeks = WeekHelper.getWeeksByCurrentYear();
		String result = null;
		for (Week week : weeks) {
			if (week.toString().startsWith(current)) {
				result = week.toString();
				break;
			}
		}
		return result;
	}

	public static int getCurrentDayOfWeek() {
		final int dayNames[] = { Calendar.SUNDAY, Calendar.MONDAY,
				Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
				Calendar.FRIDAY, Calendar.SATURDAY };
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	public static String getLastWeek() {
		String current = getCurrentDateStartWeek();
		List<Week> weeks = WeekHelper.getWeeksByCurrentYear();
		String result = null;
		for (int i = 0; i < weeks.size(); i++) {
			if (weeks.get(i).toString().startsWith(current)) {
				if (i == 0) {
					result = weeks.get(i).toString();
					break;
				} else {
					result = weeks.get(i - 1).toString();
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 获取当前时间所在的年份
	 *
	 * @param nowCal
	 * @return
	 */
	public static int getSelectYear(Calendar nowCal) {
		int currentYear = nowCal.get(Calendar.YEAR);
		int currentYear1 = currentYear + 1;
		int currentYear2 = currentYear - 1;
		Date[] resultDate = getYearMinDayAndMasDay(currentYear);
		if (DateUtil.isEffectiveDate(nowCal.getTime(), resultDate[0],
				resultDate[1])) {
			return currentYear;
		} else {
			resultDate = getYearMinDayAndMasDay(currentYear1);
			if (DateUtil.isEffectiveDate(nowCal.getTime(), resultDate[0],
					resultDate[1])) {
				return currentYear1;
			} else {
				resultDate = getYearMinDayAndMasDay(currentYear2);
				if (DateUtil.isEffectiveDate(nowCal.getTime(), resultDate[0],
						resultDate[1])) {
					return currentYear2;
				}
			}
		}
		return 0;
	}

	public static List<Week> getWeeksByCurrentYear() {
		return getWeeksByYear(DateUtil.getCurrentYear());
	}

	/**
	 * 获取某一年所有周
	 *
	 * @param currentYear
	 * @return
	 */
	public static List<Week> getWeeksByYear(int currentYear) {
		ArrayList<Week> weekList = new ArrayList<>();
		Date lastDay = DateUtil.getLastDayOfYear(currentYear);
		boolean result = DateUtil.isLastDayInYearLastWeek(lastDay);
		// 一年最多有53个星期
		for (int i = 0; i < 53; i++) {
			Date firstDayOfWeek = DateUtil.getFirstDayOfWeek(currentYear, i);
			Date lastDayOfWeek = DateUtil.getLastDayOfWeek(currentYear, i);

			Calendar startCal = Calendar.getInstance();
			startCal.setTime(firstDayOfWeek);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(lastDayOfWeek);
			String WeekNumStr = String.valueOf(i + 1);
			if (i < 9) {
				WeekNumStr = "0" + (i + 1);
			}
			Week everyWeek = new Week();
			everyWeek.setBeginYear(startCal.get(Calendar.YEAR));
			everyWeek.setEndYear(endCal.get(Calendar.YEAR));
			everyWeek.setWeekBegin(sdf.format(startCal.getTime()));
			everyWeek.setWeekEnd(sdf.format(endCal.getTime()));
			everyWeek.setWeekNum(WeekNumStr);
			if (i <= 50) {
				weekList.add(everyWeek);
			}
			if (i > 50) {
				// 判断最后一天是否在这区间内
				if (DateUtil.isEffectiveDate(lastDay, firstDayOfWeek,
						lastDayOfWeek)) {
					// 判断该年的最后一天(xxxx年12月31日)所在的周是不是当年的最后一周
					if (result) {
						weekList.add(everyWeek);
					}
					break;
				} else {
					weekList.add(everyWeek);
				}
			}

		}
		return weekList;
	}

	/**
	 * 根据当前日期算出所在周的开始时间和结束时间
	 *
	 * @param currentDate
	 * @return
	 */
	public static String[] getWeekStartAndEndDay(Date currentDate) {
		String[] weekDay = new String[2];
		Date firstDayOfWeek = DateUtil.getFirstDayOfWeek(currentDate);
		Date lastDayOfWeek = DateUtil.getLastDayOfWeek(currentDate);
		weekDay[0] = sdf2.format(firstDayOfWeek.getTime());
		weekDay[1] = sdf2.format(lastDayOfWeek.getTime());
		return weekDay;
	}

	/**
	 * 获取一年第一周的第一天 和 最后一周的最后一天
	 *
	 * @param currentYear
	 * @return
	 */
	public static Date[] getYearMinDayAndMasDay(int currentYear) {
		Date[] resultDate = new Date[2];
		List<Week> weeks = getWeeksByYear(currentYear);
		Week week0 = weeks.get(0);
		Week week1 = weeks.get(weeks.size() - 1);
		Date minDate = week0.getWeekBeginDate();
		Date maxDate = week1.getWeekEndDate();
		resultDate[0] = minDate;
		resultDate[1] = maxDate;
		return resultDate;
	}
}