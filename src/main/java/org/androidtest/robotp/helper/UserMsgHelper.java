package org.androidtest.robotp.helper;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import org.androidtest.robotp.Config;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.publicutils.StringUtil;

import java.util.List;

/**
 * 私聊消息的工具类
 *
 * @author Vince蔡培培
 */
public class UserMsgHelper {

	private static final String SYNC = "sync";
	private static final String REPORT = "report:";

	/**
	 * 是否是白名单管理者
	 *
	 * @param msg
	 * @return
	 */
	public static boolean isVaildAdmin(BaseMsg msg) {
		boolean result = false;
		if (StringUtil.ifNotNullOrEmpty(msg.getFromUserName())) {
			String nickname = WechatTools.getNickNameByUserName(msg
					.getFromUserName());
			result = Config.SUPERADMIN.equals(nickname);
		}
		return result;
	}

	/**
	 * 处理私发消息
	 *
	 * @param msg
	 * @return
	 */
	public static String userTextMsgHandle(BaseMsg msg) {
		String result = null;
		if (isVaildAdmin(msg)) {
			String content = msg.getContent().trim();
			if (content.startsWith(SYNC)) {
				result = doSyncWork();
			} else if (content.startsWith(REPORT)) {
				content = content.replaceFirst(REPORT, "");
				result = doReportWork(content);
			} else {
				result = content;
			}
		}
		return result;
	}

	private static String doReportWork(String content) {
		final String text = "\uD83D\uDCE2群发消息：\n" + "———————\n" + content;
		List<Group> groupList = GroupListHelper.getGroupList();
		int countSendGroup = 0;
		int countSendSuccess = 0;
		if (StringUtil.ifNotNullOrEmpty(groupList)) {
			for (Group group : groupList) {
				if (StringUtil.ifNotNullOrEmpty(group.getGroupRobotConfig())) {
					if (group.getGroupRobotConfig().isReceiverMassMsg()) {
						countSendGroup++;
						boolean isSendSuccess = MessageTools
								.sendGroupMsgByNickName(text,
										group.getGroupName());
						if (isSendSuccess) {
							countSendSuccess++;
						}
					}
				}
			}
		}
		return "群发结果：" + countSendSuccess + "个成功，"
				+ (countSendGroup - countSendSuccess) + "个失败。";
	}

	private static String doSyncWork() {
		GroupListHelper.updateGroupList();
		return "服务器" + Config.dir_groups + "下所有群组配置已更新。";
	}

	public static void main(String[] args) {
		System.out.println(doReportWork("\uD83D\uDCE2hello!"));
	}
}
