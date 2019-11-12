package org.androidtest.robotp.helper;

import com.alibaba.fastjson.JSONObject;
import org.androidtest.robotp.Config;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.publicutils.FileOperatorUtil;
import org.androidtest.robotp.publicutils.LogUtil;
import org.androidtest.robotp.publicutils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 群列表的工具类
 *
 * @author Vince蔡培培
 */
public class GroupListHelper {
	private static List<Group> groupList = new ArrayList<>();

	public static List<Group> updateGroupList() {
		groupList.clear();
		if (Config.dir_groups.isDirectory()) {
			File[] array = Config.dir_groups.listFiles();
			for (int i = 0; i < array.length; i++) {
				try {
					String content = FileOperatorUtil.readFromFile(array[i]);
					Group group = JSONObject.parseObject(content, Group.class);
					groupList.add(group);
				}catch (Throwable e){
					LogUtil.MSG.error(array[i].getAbsolutePath());
					e.printStackTrace();
				}
			}
		}
		return groupList;
	}

	public static List<Group> getGroupList() {
		if (StringUtil.ifNullOrEmpty(groupList)) {
			groupList = updateGroupList();
		}
		return groupList;
	}
}
