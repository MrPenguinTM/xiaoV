package org.androidtest.robotp.beans.group;

import java.io.Serializable;

/**
 * Group java bean
 *
 * @Author: Vince蔡培培
 */
public class Group implements Serializable {
	private String groupId;
	private String groupName;
	private String groupAdmin;
	private GroupRobotConfig groupRobotConfig;
	private GroupPlugin groupPlugin;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupAdmin() {
		return groupAdmin;
	}

	public void setGroupAdmin(String groupAdmin) {
		this.groupAdmin = groupAdmin;
	}

	public GroupRobotConfig getGroupRobotConfig() {
		return groupRobotConfig;
	}

	public void setGroupRobotConfig(GroupRobotConfig groupRobotConfig) {
		this.groupRobotConfig = groupRobotConfig;
	}

	public GroupPlugin getGroupPlugin() {
		return groupPlugin;
	}

	public void setGroupPlugin(GroupPlugin groupPlugin) {
		this.groupPlugin = groupPlugin;
	}

	@Override
	public String toString() {
		return "Group{" + "groupId='" + groupId + '\'' + ", groupName='"
				+ groupName + '\'' + ", groupAdmin='" + groupAdmin + '\''
				+ ", groupRobotConfig=" + groupRobotConfig + ", groupPlugin="
				+ groupPlugin + '}';
	}
}