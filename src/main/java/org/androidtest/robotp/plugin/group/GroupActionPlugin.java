package org.androidtest.robotp.plugin.group;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import org.androidtest.robotp.Config;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.data.Constant;
import org.androidtest.robotp.data.enums.MatchRuleEnum;
import org.androidtest.robotp.data.enums.MatchTypeEnum;
import org.androidtest.robotp.plugin.IPlugin;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GroupActionPlugin，问答触发形式插件
 *
 * @Author: Vince蔡培培
 */
public class GroupActionPlugin implements Serializable, IPlugin {

	private String matchWords;
	private MatchTypeEnum matchType;
	private MatchRuleEnum matchRule;
	private MatchTypeEnum replyType;
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

	public MatchTypeEnum getMatchType() {
		return matchType;
	}

	public void setMatchType(MatchTypeEnum matchType) {
		this.matchType = matchType;
	}

	public MatchRuleEnum getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(MatchRuleEnum matchRule) {
		this.matchRule = matchRule;
	}

	public MatchTypeEnum getReplyType() {
		return replyType;
	}

	public void setReplyType(MatchTypeEnum replyType) {
		this.replyType = replyType;
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
		switch (getReplyType()) {
		case PIC:
			result = MessageTools.sendPicMsgByGroupNickName(
					group.getGroupName(), Config.dir_home + File.separator
							+ getReplyContent());
			break;
		case TEXT:
			result = MessageTools.sendGroupMsgByNickName(getReplyContent(),
					group.getGroupName());
			break;
		case MEDIA:
			result = MessageTools.sendGroupFileMsgByNickName(
					group.getGroupName(), Config.dir_home + File.separator
							+ getReplyContent());
			break;
		default:
			result = MessageTools.sendGroupMsgByNickName(
					"暂不支持该类型消息。请联系蔡培培开发处理。", group.getGroupName());
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		return "ActionPlugin{" + "matchWords='" + matchWords + '\''
				+ ", matchType=" + matchType + ", matchRule=" + matchRule
				+ ", replyType=" + replyType + ", replyContent='"
				+ replyContent + '\'' + '}';
	}
}