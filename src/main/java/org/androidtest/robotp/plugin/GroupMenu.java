package org.androidtest.robotp.plugin;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.data.Constant;
import org.androidtest.robotp.data.enums.MatchRuleEnum;
import org.androidtest.robotp.publicutils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GroupMenu，菜单功能
 *
 * @Author: Vince蔡培培
 */
public class GroupMenu implements Serializable, IPlugin {

	private String matchWords;
	private MatchRuleEnum matchRule = MatchRuleEnum.GLOBAL_START;
	private String replyContent;

	public String getMatchWords() {
		return matchWords;
	}

	public void setMatchWords(String matchWords) {
		this.matchWords = matchWords;
	}

	@Override
	public List<String> getMatchWordsToList() {
		List<String> list = new ArrayList<>();
		String[] keyword = matchWords.split(Constant.SEMICOLON_SPILT);
		for (int i = 0; i < keyword.length; i++) {
			list.add(keyword[i]);
		}
		return list;
	}

	public MatchRuleEnum getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(MatchRuleEnum matchRule) {
		this.matchRule = matchRule;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	@Override
	public boolean reply(Group group, BaseMsg msg) {
		boolean result;

		if (StringUtil.ifNotNullOrEmpty(group.getGroupPlugin())) {
			for (int i = 0; i < group.getGroupPlugin().getReminderPluginArray()
					.size(); i++) {

			}
		}
		result = MessageTools.sendGroupMsgByNickName(getReplyContent(),
				group.getGroupName());

		return result;
	}
}