package org.androidtest.xiaoV.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 菜单组件
 * 
 * @author caipeipei
 *
 */
public class MenuAction extends Action {

	/**
	 * MenuAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> MENU_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("菜单", MsgTypeEnum.TEXT);
		}
	};

	private static final String actionName = "菜单";

	public MenuAction() {
		super(actionName, MENU_VAILD_KEYWORD_LIST);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());

		String result = "===支持的关键字大全===";
		List<String> keywordList = group.getAllVaildKeyword(MsgTypeEnum.TEXT);
		for (String keyword : keywordList) {
			result = result + "\n" + keyword;
		}
		result = result + "\n\n===已启用的机器人功能===";
		List<Action> actionList = group.getActionList();
		for (Action action : actionList) {
			String currentActionName = action.getActionName();
			if (StringUtil.ifNotNullOrEmpty(currentActionName)) {
				result = result + "\n✔️ " + currentActionName;
			}
		}
		return result;
	}

	@Override
	public boolean notify(Group group) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String report(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

}