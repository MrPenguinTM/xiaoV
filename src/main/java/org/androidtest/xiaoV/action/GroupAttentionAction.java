package org.androidtest.xiaoV.action;

import java.util.HashMap;
import java.util.Map;

import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 群注意事项说明组件
 * 
 * @author caipeipei
 *
 */
public class GroupAttentionAction extends Action {

	/**
	 * GroupAttentionAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> ATTENTION_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("注意事项", MsgTypeEnum.TEXT);
		}
	};

	private static final String actionName = "注意事项说明";

	private String attentionWord;

	public GroupAttentionAction(String text) {
		super(actionName, ATTENTION_VAILD_KEYWORD_LIST);
		setAttentionWord(text);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());
		String currentGroupNickName = group.getGroupNickName();
		MessageTools.sendGroupMsgByNickName(getAttentionWord(),
				currentGroupNickName);
		return null;
	}

	public String getAttentionWord() {
		return attentionWord;
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

	public void setAttentionWord(String attentionWord) {
		this.attentionWord = attentionWord;
	}

}