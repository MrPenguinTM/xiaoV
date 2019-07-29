package org.androidtest.xiaoV.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.androidtest.xiaoV.action.Action;
import org.androidtest.xiaoV.chat.TulingRobot;
import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;
import org.apache.log4j.Logger;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;

public class MessageHandler {
	private static Logger LOG = Logger.getLogger(MessageHandler.class);

	private static String robotCall = "@" + Core.getInstance().getNickName();

	/**
	 * 处理群消息
	 * 
	 * @param msg
	 * @return
	 */
	public static String groupMsgHandle(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("groupMsgHandle: " + group + ", " + msg);

		String result = null;
		String currentGroupNickName = WechatTools
				.getGroupNickNameByGroupUserName(msg.getFromUserName());
		if (isCurrentMsgFromVaildGroup(group, currentGroupNickName)) {
			final String content = msg.getText();
			String robotDisplayName = WechatTools
					.getMemberDisplayNameByGroupNickName(currentGroupNickName,
							msg.getToUserName());
			if (StringUtil.ifNullOrEmpty(robotDisplayName)) {
				robotDisplayName = robotCall;
			}
			String keyword = returnSpecificKeywordFromTextMsg(group, msg);
			Action action = group.getActionFromVaildKeywords(keyword,
					MsgTypeEnum.TEXT);

			if (keyword != null) {
				if (action != null) {
					result = action.action(group, msg);
				}
			} else {
				LOG.info("groupMsgHandle: " + "非法参数: " + content
						+ "，消息过滤不处理.result=" + result);
			}
			if ((result == null && content.contains(robotCall))
					|| (result == null && content.contains(robotDisplayName))) {
				LogUtil.MSG.debug("groupMsgHandle: result: " + result
						+ ",content:" + content + ",robotCall: " + robotCall
						+ ",robotDisplayName: " + robotDisplayName);
				String nickName = WechatTools
						.getMemberDisplayOrNickNameByGroupNickName(
								group.getGroupNickName(),
								msg.getStatusNotifyUserName());
				result = TulingRobot.chat(nickName, content);
				if (StringUtil.ifNullOrEmpty(result)) {
					result = "我听不懂，需要\"菜单\"请回复菜单";
				}

			}
		}
		return result;
	}

	private static boolean isCurrentMsgFromVaildGroup(Group group,
			String currentGroupNickName) {
		return group.getGroupNickName().equals(currentGroupNickName);
	}

	/**
	 * 处理多媒体消息
	 * 
	 * @param msg
	 * @return
	 */
	public static String mediaMsgHandle(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("mediaMsgHandle: " + group + ", " + msg);
		String result = null;
		String currentGroupNickName = WechatTools
				.getGroupNickNameByGroupUserName(msg.getFromUserName());
		if (isCurrentMsgFromVaildGroup(group, currentGroupNickName)) {
			String keyword = returnSpecificKeywordFromMediaMsg(group, msg);
			Action customized = group.getActionFromVaildKeywords(keyword,
					MsgTypeEnum.MEDIA);
			if (keyword != null) {
				if (customized != null) {
					result = customized.action(group, msg);
				}
			} else {
				String fileName = Constant.SIMPLE_DATE_FORMAT_FILE
						.format(new Date()) + "-" + msg.getFileName();
				String filePath = Constant.getCurrentWeekSavePath()
						+ File.separator + fileName; // 这里是需要保存收到的文件路径，文件可以是任何格式如PDF，WORD，EXCEL等。
				DownloadTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getType(),
						filePath);
			}
		}
		return result;
	}

	private static String returnSpecificKeywordFromMediaMsg(Group group,
			BaseMsg msg) {
		LogUtil.MSG.debug("returnSpecificKeywordFromMediaMsg: " + group + ", "
				+ msg);
		List<String> currentGroupVaildKeyword = new ArrayList<String>();
		currentGroupVaildKeyword.addAll(group
				.getAllVaildKeyword(MsgTypeEnum.MEDIA));
		String content = msg.getContent();

		if (StringUtil.ifNotNullOrEmpty(currentGroupVaildKeyword)) {
			for (int i = 0; i < currentGroupVaildKeyword.size(); i++) {

				String expect = currentGroupVaildKeyword.get(i);
				if (expect.contains(Constant.COLON_SPLIT)) {
					if (content.contains(expect.split(Constant.COLON_SPLIT)[0])
							&& content.contains(expect
									.split(Constant.COLON_SPLIT)[1])) {
						LogUtil.MSG
								.debug("returnSpecificKeywordFromMediaMsg: return "
										+ currentGroupVaildKeyword.get(i));
						return currentGroupVaildKeyword.get(i);
					}
				}
				if (content.contains(expect)) {
					LogUtil.MSG
							.debug("returnSpecificKeywordFromMediaMsg: return "
									+ currentGroupVaildKeyword.get(i));
					return currentGroupVaildKeyword.get(i);
				}
			}
		}
		LogUtil.MSG.debug("returnSpecificKeywordFromMediaMsg: return " + null);
		return null;
	}

	/**
	 * 是否是指定的系统消息文本内容
	 * 
	 * @param content
	 * @return
	 */
	private static String returnSpecificKeywordFromSysMsg(Group group,
			BaseMsg msg) {
		LogUtil.MSG.debug("returnSpecificKeywordFromSysMsg: " + group + ", "
				+ msg);
		List<String> currentGroupVaildKeyword = new ArrayList<String>();
		currentGroupVaildKeyword.addAll(group
				.getAllVaildKeyword(MsgTypeEnum.SYS));
		String content = msg.getContent();
		if (StringUtil.ifNotNullOrEmpty(currentGroupVaildKeyword)) {
			for (int i = 0; i < currentGroupVaildKeyword.size(); i++) {
				String expect = currentGroupVaildKeyword.get(i);
				if (expect.contains(Constant.COLON_SPLIT)) {
					if (content.contains(expect.split(Constant.COLON_SPLIT)[0])
							&& content.contains(expect
									.split(Constant.COLON_SPLIT)[1])) {
						LogUtil.MSG
								.debug("returnSpecificKeywordFromSysMsg: return "
										+ currentGroupVaildKeyword.get(i));
						return currentGroupVaildKeyword.get(i);
					}
				}
				if (content.contains(expect)) {
					LogUtil.MSG
							.debug("returnSpecificKeywordFromSysMsg: return "
									+ currentGroupVaildKeyword.get(i));
					return currentGroupVaildKeyword.get(i);
				}
			}
		}
		LogUtil.MSG.debug("returnSpecificKeywordFromSysMsg: return " + null);
		return null;
	}

	/**
	 * 是否是指定的群消息文本内容
	 * 
	 * @param content
	 * @return
	 */
	private static String returnSpecificKeywordFromTextMsg(Group group,
			BaseMsg msg) {
		LogUtil.MSG.debug("returnSpecificKeywordFromTextMsg: " + group + ", "
				+ msg);
		List<String> currentGroupVaildKeyword = new ArrayList<String>();
		currentGroupVaildKeyword.addAll(group
				.getAllVaildKeyword(MsgTypeEnum.TEXT));
		String content = msg.getText();
		String robotDisplayName = WechatTools
				.getMemberDisplayOrNickNameByGroupNickName(
						group.getGroupNickName(), msg.getToUserName());
		if (StringUtil.ifNotNullOrEmpty(currentGroupVaildKeyword)) {
			for (int i = 0; i < currentGroupVaildKeyword.size(); i++) {
				if (content.startsWith(currentGroupVaildKeyword.get(i))
						|| (content.contains(currentGroupVaildKeyword.get(i)) && content
								.contains(robotCall))
						|| (content.contains(currentGroupVaildKeyword.get(i)) && content
								.contains(robotDisplayName))) {
					LogUtil.MSG
							.debug("returnSpecificKeywordFromTextMsg: return "
									+ currentGroupVaildKeyword.get(i));
					return currentGroupVaildKeyword.get(i);
				}
			}
		}
		LogUtil.MSG.debug("returnSpecificKeywordFromTextMsg: return " + null);
		return null;
	}

	/**
	 * 处理系统消息
	 * 
	 * @param msg
	 * @return
	 */
	public static String sysMsgHandle(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("sysMsgHandle: " + group + ", " + msg);
		String result = null;
		String currentGroupNickName = WechatTools
				.getGroupNickNameByGroupUserName(msg.getFromUserName());
		if (isCurrentMsgFromVaildGroup(group, currentGroupNickName)) {
			String keyword = returnSpecificKeywordFromSysMsg(group, msg);
			Action action = group.getActionFromVaildKeywords(keyword,
					MsgTypeEnum.SYS);
			if (action != null && keyword != null) {
				if (action.getVaildKeywords(MsgTypeEnum.SYS).contains(keyword)) {
					action.action(group, msg);
				}
			}
		}
		return result;
	}
}
