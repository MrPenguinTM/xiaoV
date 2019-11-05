package org.androidtest.xiaoV;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.factory.ActionFactory;

/**
 * 配置类
 *
 * @author caipeipei
 */
public class Config {

	public static final boolean DEBUG = false;// 全局的代码调试开关，打开后会一键进行测试模块，会在短时间里把各个组件的主动触发功能执行一遍

	/**
	 * 需要配置的群白名单和该群所需的组件信息。 可选组件的工厂见org.androidtest.vince.factory
	 * 注意：配置的群需要保存进通讯录，不然会导致获取群成员时空指针
	 */
	public static void initGroupAndAdmin() {
		Group group = new Group("彭于晏已退出群聊", Constant.DEFAULT_ADMIN);// 新建群信息
		group.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		group.addAction(ActionFactory.createGroupRuleAction(
				group.getGroupNickName(),
				new File(System.getProperty("user.home") + File.separator
						+ "Desktop" + File.separator + "彭于晏已退出群聊.jpg")));// 配置该群有规则提醒功能
		group.addAction(ActionFactory.createWeeklySportClockIn(2));// 配置该群有运动打卡功能，要求一周2次打卡
		group.addAction(ActionFactory.createDailyStepClockIn(2));// 配置该群有每日步数功能
		// group.addAction(ActionFactory.createLifeRoutineClockInAction(true,
		// 731,
		// true, 2245));// 配置该群有作息打卡功能，设定每天0731晚起提醒和2230睡觉提醒
		group.addAction(ActionFactory.createWeeklyReportClockInAction(2, 2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group.addAction(ActionFactory.createLastWeeklyReportClockInAction(2, 2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		Map<String, File> whiteList = new HashMap<String, File>();
		whiteList.put("Vince蔡培培", new File(System.getProperty("user.home")
				+ File.separator + "Desktop" + File.separator
				+ "DAILY CHECK LIST @Vince蔡培培.xlsx"));
		whiteList.put("王快乐。", new File(System.getProperty("user.home")
				+ File.separator + "Desktop" + File.separator
				+ "DAILY CHECK LIST @王快乐.xlsx"));
		// whiteList.put("张荆钗", new File(System.getProperty("user.home")
		// + File.separator + "Desktop" + File.separator
		// + "2019个人检视清单@张荆钗.xlsx"));
		whiteList.put("shine", new File(System.getProperty("user.home")
				+ File.separator + "Desktop" + File.separator
				+ "DAILY CHECK LIST @shine.xlsx"));
		group.addAction(ActionFactory.createDailySelfReflectionAction(
				whiteList, true));// 配置该群有每日反思打卡功能，需要配置反思人及对应的反思excel文件地址
		group.addAction(ActionFactory.createRecallSportAction());
		group.addAction(ActionFactory.createRecallStepAction());
		// //////////////////////////////////////////////
		Group group2 = new Group("运动 or  2000", Constant.DEFAULT_ADMIN);// 新建群信息
		group2.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		group2.addAction(ActionFactory.createGroupRuleAction(
				group2.getGroupNickName(),
				new File(System.getProperty("user.home") + File.separator
						+ "Desktop" + File.separator + "运动or2000.jpg")));// 配置该群有规则提醒功能
		group2.addAction(ActionFactory.createWeeklySportClockIn(1, true));// 配置该群有运动打卡功能，要求一周2次打卡
		group2.addAction(ActionFactory.createDailyStepClockIn(2, true));// 配置该群有每日步数功能
		group2.addAction(ActionFactory.createWeeklyReportClockInAction(2, 1));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group2.addAction(ActionFactory
				.createLastWeeklyReportClockInAction(2, 1));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		String text = "注意事项如下：\n1️⃣把群昵称改成自己的名字。\n2️⃣及时打卡，当天运动当天打卡，当天走万步当天打卡。\n3️⃣打卡形式为回复关键字+运动截图。\n4️⃣取消女生生理期请假，生理期当周请完成3次万步。\n5️⃣打卡务必输入关键字，若不清楚关键字，可在群里输入“菜单”查询。";
		group2.addAction(ActionFactory.createGroupAttentionAction(text));
		group2.addAction(ActionFactory.createRecallStepAction());
		group2.addAction(ActionFactory.createRecallSportAction());
		// //////////////////////////////////////////////
		Group group3 = new Group("TEST123", Constant.DEFAULT_ADMIN);// 新建群信息
		group3.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		group3.addAction(ActionFactory.createGroupRuleAction(
				group3.getGroupNickName(),
				new File(System.getProperty("user.home") + File.separator
						+ "Desktop" + File.separator + "视睿供应链运动family.jpg")));// 配置该群有规则提醒功能
		group3.addAction(ActionFactory.createWeeklyReportClockInAction(2, 2, 1));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group3.addAction(ActionFactory.createLastWeeklyReportClockInAction(2,
				2, 1));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group3.addAction(ActionFactory.createWeeklySportClockIn(2));// 配置该群有运动打卡功能，要求一周1次打卡
		group3.addAction(ActionFactory.createWholeWeekStepClockIn());//
		group3.addAction(ActionFactory.createRecallStepAction());
		group3.addAction(ActionFactory.createRecallSportAction());
		group3.addAction(ActionFactory.createDailyStepClockIn(2));
		// //////////////////////////////////////////////
		Group group4 = new Group("运动基金群", Constant.DEFAULT_ADMIN);// 新建群信息
		group4.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		group4.addAction(ActionFactory.createGroupRuleAction(
				group4.getGroupNickName(),
				new File(System.getProperty("user.home") + File.separator
						+ "Desktop" + File.separator + "运动基金群.jpg")));// 配置该群有规则提醒功能
		group4.addAction(ActionFactory.createWeeklySportClockIn(1, true));// 配置该群有运动打卡功能，要求一周1次打卡
		group4.addAction(ActionFactory.createRecallSportAction());
		// group4.addAction(ActionFactory.createDailyStepClockIn(2, true));//
		// 配置该群有每日步数功能
		group4.addAction(ActionFactory.createLastWeeklyReportClockInAction(-1,
				1));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group4.addAction(ActionFactory.createWeeklyReportClockInAction(-1, 1));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		// //////////////////////////////////////////////
		Group group5 = new Group("视睿供应链运动family", Constant.DEFAULT_ADMIN);// 新建群信息
		group5.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		group5.addAction(ActionFactory.createRecallSportAction());
		group5.addAction(ActionFactory.createGroupRuleAction(
				group5.getGroupNickName(),
				new File(System.getProperty("user.home") + File.separator
						+ "Desktop" + File.separator + "视睿供应链运动family.jpg")));// 配置该群有规则提醒功能
		group5.addAction(ActionFactory.createWeeklySportClockIn(2));// 配置该群有运动打卡功能，要求一周1次打卡
		// 配置该群有每日步数功能
		group5.addAction(ActionFactory.createLastWeeklyReportClockInAction(-1,
				2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group5.addAction(ActionFactory.createWeeklyReportClockInAction(-1, 2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		// //////////////////////////////////////////////
		Group group6 = new Group("策采Family运动群", Constant.DEFAULT_ADMIN);// 新建群信息
		group6.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		group6.addAction(ActionFactory.createGroupRuleAction(
				group6.getGroupNickName(),
				new File(System.getProperty("user.home") + File.separator
						+ "Desktop" + File.separator + "策采Family运动群.jpg")));// 配置该群有规则提醒功能
		group6.addAction(ActionFactory.createRecallSportAction());
		group6.addAction(ActionFactory.createWeeklySportClockIn(2));// 配置该群有运动打卡功能，要求一周1次打卡
		group6.addAction(ActionFactory.createLastWeeklyReportClockInAction(-1,
				2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group6.addAction(ActionFactory.createWeeklyReportClockInAction(-1, 2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		// //////////////////////////////////////////////
		Group group7 = new Group("【内】供服family运动打卡群", Constant.DEFAULT_ADMIN);// 新建群信息
		group7.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		group7.addAction(ActionFactory.createGroupRuleAction(
				group7.getGroupNickName(),
				new File(System.getProperty("user.home") + File.separator
						+ "Desktop" + File.separator + "【内】供服family运动打卡群.jpg")));//
		// 配置该群有规则提醒功能
		group7.addAction(ActionFactory.createRecallSportAction());
		group7.addAction(ActionFactory.createWeeklySportClockIn(2));// 配置该群有运动打卡功能，要求一周1次打卡
		group7.addAction(ActionFactory.createLastWeeklyReportClockInAction(-1,
				2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		group7.addAction(ActionFactory.createWeeklyReportClockInAction(-1, 2));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		// //////////////////////////////////////////////
		// Group group8 = new Group("V1.0每周跑步10公里", Constant.DEFAULT_ADMIN);//
		// 新建群信息
		// group8.addAction(ActionFactory.createMenuAction());// 配置该群有菜单功能
		// group8.addAction(ActionFactory.createGroupRuleAction(
		// group8.getGroupNickName(),
		// new File(System.getProperty("user.home") + File.separator
		// + "Desktop" + File.separator + "V1.0每周跑步10公里.jpg")));// 配置该群有规则提醒功能
		// group8.addAction(ActionFactory.createRecallSportAction());
		// group8.addAction(ActionFactory.createWeeklySportClockIn(1, true));//
		// 配置该群有运动打卡功能，要求一周1次打卡
		// // group4.addAction(ActionFactory.createDailyStepClockIn(2, true));//
		// // 配置该群有每日步数功能
		// group8.addAction(ActionFactory.createWeeklyReportClockInAction(-1,
		// 1));// 配置该群有周报功能，周报功能会统计并播报运动打卡和每日步数数据
		// /////////////////统一ADD 进 GROUP////////////////////////////
		Constant.groupList.add(group);
		Constant.groupList.add(group2);
		Constant.groupList.add(group3);
		Constant.groupList.add(group4);
		Constant.groupList.add(group6);
		Constant.groupList.add(group5);
		Constant.groupList.add(group7);
		// Constant.groupList.add(group8);
	}
}
