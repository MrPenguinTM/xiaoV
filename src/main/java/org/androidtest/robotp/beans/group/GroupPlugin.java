package org.androidtest.robotp.beans.group;

import org.androidtest.robotp.data.enums.MatchTypeEnum;
import org.androidtest.robotp.plugin.group.GroupActionPlugin;
import org.androidtest.robotp.plugin.group.GroupExercisePlugin;
import org.androidtest.robotp.plugin.group.GroupReminderPlugin;
import org.androidtest.robotp.publicutils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GroupPlugin java bean
 *
 * @Author: Vince蔡培培
 */
public class GroupPlugin implements Serializable {

	private List<GroupActionPlugin> actionPluginArray;
	private List<GroupReminderPlugin> reminderPluginArray;
	private GroupExercisePlugin exercisePlugin;

	public List<GroupActionPlugin> getActionPluginArray(
			MatchTypeEnum matchTypeEnum) {
		List<GroupActionPlugin> matchGroupActionPluginList = null;
		if (StringUtil.ifNotNullOrEmpty(actionPluginArray)) {
			matchGroupActionPluginList = new ArrayList<>();
			for (GroupActionPlugin groupActionPlugin : actionPluginArray) {
				if (groupActionPlugin.getMatchType() == matchTypeEnum) {
					matchGroupActionPluginList.add(groupActionPlugin);
				}
			}
		}
		return matchGroupActionPluginList;
	}

	public List<GroupActionPlugin> getActionPluginArray() {
		return actionPluginArray;
	}

	public void setActionPluginArray(List<GroupActionPlugin> actionPluginArray) {
		this.actionPluginArray = actionPluginArray;
	}

	public List<GroupReminderPlugin> getReminderPluginArray() {
		return reminderPluginArray;
	}

	public void setReminderPluginArray(
			List<GroupReminderPlugin> reminderPluginArray) {
		this.reminderPluginArray = reminderPluginArray;
	}

	public GroupExercisePlugin getExercisePlugin() {
		return exercisePlugin;
	}

	public void setExercisePlugin(GroupExercisePlugin exercisePlugin) {
		this.exercisePlugin = exercisePlugin;
	}

	@Override
	public String toString() {
		return "GroupPlugin{" + "actionPluginArray=" + actionPluginArray
				+ ", reminderPluginArray=" + reminderPluginArray
				+ ", exercisePlugin=" + exercisePlugin + '}';
	}
}