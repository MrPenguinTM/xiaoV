package org.androidtest.xiaoV.data;

import java.util.ArrayList;
import java.util.List;

import org.androidtest.xiaoV.action.Action;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

public class Group {

	private String groupId;
	private String groupNickName;
	private String admin;
	private List<Action> actionList = new ArrayList<>();

	public Group(String groupNickName, String admin) {
		LogUtil.MSG.debug("Group: init: nickName: " + groupNickName
				+ ",admin: " + admin);
		this.groupNickName = groupNickName;
		this.admin = admin;
	}

	public void addAction(Action action) {
		// TODO 需要确保action唯一性，免得重复添加一样的action
		if (action != null) {
			this.actionList.add(action);
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean containsAction(Class actionClass) {
		List<Action> actions = getActionList();
		for (Action action : actions) {
			if (action.getClass() == actionClass) {
				return true;
			}
		}
		return false;
	}

	public Action getActionFromVaildKeywords(String keyword, MsgTypeEnum type) {
		LogUtil.MSG.debug("getActionFromVaildKeywords: groupNickName: "
				+ groupNickName + ", keyword: " + keyword);
		if (StringUtil.ifNotNullOrEmpty(keyword)) {
			for (Action action : actionList) {
				List<String> cList = action.getVaildKeywords(type);
				if (StringUtil.ifNotNullOrEmpty(cList)) {
					for (String word : cList) {
						if (keyword.equals(word)) {
							LogUtil.MSG
									.debug("getActionFromVaildKeywords: return "
											+ action.getClass().getSimpleName());
							return action;
						}
					}
				}
			}
		}
		LogUtil.MSG.debug("getActionFromVaildKeywords: return " + null);
		return null;
	}

	public List<Action> getActionList() {
		return actionList;
	}

	public String getAdmin() {
		return admin;
	}

	public List<String> getAllVaildKeyword(MsgTypeEnum type) {
		List<String> list = new ArrayList<String>();
		for (Action action : actionList) {
			List<String> cList = action.getVaildKeywords(type);
			if (StringUtil.ifNotNullOrEmpty(cList)) {
				for (String word : cList) {
					list.add(word);
				}
			}
		}
		return list;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getGroupNickName() {
		return groupNickName;
	}

	public List<String> getMemberList() {
		List<String> list = null;
		if (groupId != null) {
			list = WechatTools.getMemberListByGroupId2(groupId);
		} else {
			list = WechatTools.getMemberListByGroupNickName2(groupNickName);
		}
		return list;
	}

	public void setActionList(List<Action> actionsList) {
		this.actionList.clear();
		this.actionList = actionsList;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setGroupNickName(String groupNickName) {
		this.groupNickName = groupNickName;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupNickName=" + groupNickName
				+ ", admin=" + admin + ", actionList=" + actionList + "]";
	}

	// public Action getActionFromVaildKeywords(String keyword) {
	// LogUtil.MSG.debug("getActionFromVaildKeywords: groupNickName: "
	// + groupNickName + ", keyword: " + keyword);
	// if (StringUtil.ifNotNullOrEmpty(keyword)) {
	// for (Action customized : actionList) {
	// for (int i = 0; i < MsgTypeEnum.values().length; i++) {
	// List<String> cList = customized
	// .getVaildKeywords(MsgTypeEnum.values()[i]);
	// if (StringUtil.ifNotNullOrEmpty(cList)) {
	// for (String word : cList) {
	// if (keyword.equals(word)) {
	// LogUtil.MSG
	// .debug("getCustomizedFromVaildKeywords: return "
	// + customized.getClass()
	// .getSimpleName());
	// return customized;
	// }
	// }
	// }
	// }
	// }
	// }
	// LogUtil.MSG.debug("getActionFromVaildKeywords: return " + null);
	// return null;
	// }
}
