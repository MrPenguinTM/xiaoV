package org.androidtest.robotp.publicutils;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.androidtest.robotp.data.Constant.CRON_DATE_FORMAT;

/**
 * cron时间工具类
 *
 * @author Vince蔡培培
 */
public class CronDateUtil {

	public static List<String> parser(String cronExpression) {
		List<String> result = new ArrayList<String>();
		if (cronExpression == null || cronExpression.length() < 1) {
			return result;
		} else {
			CronExpression exp = null;
			Calendar calendar = Calendar.getInstance();
			String cronDate = calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE);
			String sStart = cronDate + " 00:00:00";
			SimpleDateFormat sdf = CRON_DATE_FORMAT;
			Date dStart = null;
			Date dEnd = null;
			try {
				exp = new CronExpression(cronExpression);
				dStart = sdf.parse(sStart);
				calendar.setTime(dStart);
				calendar.add(Calendar.DATE, 1);
				dEnd = calendar.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
				return result;
			}
			Date dd = new Date();
			dd = exp.getNextValidTimeAfter(dd);
			while (dd.getTime() <= dEnd.getTime()) {
				result.add(sdf.format(dd));
				dd = exp.getNextValidTimeAfter(dd);
			}
			exp = null;
		}
		return result;
	}

	public static void main(String[] args) {
		String CRON_EXPRESSION = "* 59 23 ? * *";
		System.out.println("CRON_EXPRESSION:" + CRON_EXPRESSION);
		List<String> lTime = new ArrayList<String>();
		lTime = parser(CRON_EXPRESSION);
		for (int i = 0; i < lTime.size(); i++) {
			System.out.println("lTime" + i + ":" + lTime.get(i));
			// System.out.println("currentTime:" + CRON_DATE_FORMAT.format(new
			// Date()));
		}
	}

}