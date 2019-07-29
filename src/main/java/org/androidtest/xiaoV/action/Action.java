package org.androidtest.xiaoV.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.androidtest.xiaoV.data.Group;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 对行为做出反应的组件基类（抽象类）
 * 
 * @author caipeipei
 *
 */
public abstract class Action {
	private Map<String, MsgTypeEnum> vailKeywordMap;
	private String actionName;

	protected Action(String actionName, Map<String, MsgTypeEnum> vailKeywordMap) {
		setActionName(actionName);
		setVailKeywordMap(vailKeywordMap);
	}

	/**
	 * 对行为执行反应
	 * 
	 * @param group
	 * @param msg
	 * @return
	 */
	public abstract String action(Group group, BaseMsg msg);

	protected String getActionName() {
		return actionName;
	}

	/**
	 * 返回该类的合法关键字列表
	 * 
	 * @param type
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getVaildKeywords(MsgTypeEnum type) {
		List<String> list = new ArrayList<String>();

		Iterator iter = getVailKeywordMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String vaildKeyword = (String) entry.getKey();
			MsgTypeEnum vaildType = (MsgTypeEnum) entry.getValue();
			if (vaildType == type) {
				list.add(vaildKeyword);
			}
		}
		return list;
	}

	private Map<String, MsgTypeEnum> getVailKeywordMap() {
		return vailKeywordMap;
	}

	/**
	 * 广播
	 * 
	 * @param group
	 * @return
	 */
	public abstract boolean notify(Group group);

	/**
	 * 播报
	 * 
	 * @param group
	 * @return
	 */
	public abstract String report(Group group);

	private void setActionName(String actionName) {
		this.actionName = actionName;
	}

	private void setVailKeywordMap(Map<String, MsgTypeEnum> vailKeywordMap) {
		this.vailKeywordMap = vailKeywordMap;
	}
}