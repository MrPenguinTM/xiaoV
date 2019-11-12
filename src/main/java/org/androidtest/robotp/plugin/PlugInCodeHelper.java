package org.androidtest.robotp.plugin;

import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.helper.GroupListHelper;
import org.androidtest.robotp.publicutils.StringUtil;

/**
 * replyType为PluinInCode类型的工具类
 *
 * @Author: Vince蔡培培
 */
public class PlugInCodeHelper {

	/**
	 * 执行周报数据广播
	 *
	 * @param groupName
	 * @return
	 */
	public static boolean exerciseWeeklyReport(String groupName) {
		boolean isReply = false;
		Group group = null;
		for (Group g : GroupListHelper.getGroupList()) {
			if (g.getGroupName().equals(groupName)) {
				group = g;
				break;
			}
		}
		if (StringUtil.ifNotNullOrEmpty(group)) {
			if (StringUtil.ifNotNullOrEmpty(group.getGroupPlugin())) {
				if (StringUtil.ifNotNullOrEmpty(group.getGroupPlugin()
						.getExercisePlugin())) {
					isReply = group.getGroupPlugin().getExercisePlugin()
							.notifyReport(group);
				}
			}
		}
		return isReply;
	}
}
