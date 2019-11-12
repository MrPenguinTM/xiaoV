package org.androidtest.robotp.publicutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期相关工具类
 *
 * @author Vince蔡培培
 */
public class DateUtil {
	public static final long SEC_MILL_SECONDS = 1000;
	public static final long MINUTE_MILL_SECONDS = 60 * SEC_MILL_SECONDS;
	public static final long HOUR_MILL_SECONDS = 60 * MINUTE_MILL_SECONDS;
	public static final long DAY_MILL_SECONDS = 24 * HOUR_MILL_SECONDS;

	public static int getCurrentTime() {
		int date = Integer.parseInt(new SimpleDateFormat("HHmm")
				.format(new Date()));
		return date;
	}

	public static int getCurrentYear() {
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.YEAR);

	}

	// 获取当前时间所在周的开始日期
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	public static boolean isSunday() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);// 从星期天开始，依次为1、2、3……到星期六为7。
		return day == 1;
	}

	/**
	 * 获取一年开始的第一周的周一 第一周如果有四天或以上就算是该年的第一周, 否则就是上一年的最后一周(用于跨年判断第一周开始时间)
	 *
	 * @param year
	 * @return
	 */
	public static Calendar getFirstDayOfWeek(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.JANUARY, 1, 0, 0, 0);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		// 如果1月1日是周五，周六或者周日，那么这一周是上一年最后一周，重新计算今年的第一周开始的周一
		if (dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY
				|| dayOfWeek == Calendar.SUNDAY) {
			while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				c.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return c;
	}

	// 获取某年的第几周的开始日期
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = getFirstDayOfWeek(year);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	// 获取当前时间所在周的结束日期
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	// 获取某年的第几周的结束日期
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = getFirstDayOfWeek(year);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	// 获取一年的最后一天
	public static Date getLastDayOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime
	 *            当前时间
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime,
			Date endTime) {
		if (Math.abs(nowTime.getTime() - startTime.getTime()) < 1000
				|| Math.abs(nowTime.getTime() - endTime.getTime()) < 1000) {
			return true;
		}

		Calendar date = new GregorianCalendar();
		date.setTime(nowTime);

		Calendar begin = new GregorianCalendar();
		begin.setTime(startTime);

		Calendar end = new GregorianCalendar();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断该年的最后一天(xxxx年12月31日)所在的周是不是当年的最后一周, 否则就是下一年的第一周
	 */
	public static boolean isLastDayInYearLastWeek(Date lastDate) {
		boolean result = false;
		Calendar c = new GregorianCalendar();
		c.setTime(lastDate);
		int lastDay = c.get(Calendar.DAY_OF_WEEK);
		// 当lastDay是周四、五、六、日， 它所在周就是是该年的最后一周
		if (lastDay == Calendar.THURSDAY || lastDay == Calendar.FRIDAY
				|| lastDay == Calendar.SATURDAY || lastDay == Calendar.SUNDAY) {
			result = true;
		}
		return result;
	}
}