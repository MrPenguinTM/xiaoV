package org.androidtest.robotp.plugin.group;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import org.androidtest.robotp.Config;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.data.Constant;
import org.androidtest.robotp.data.enums.MatchTypeEnum;
import org.androidtest.robotp.plugin.IPlugin;
import org.androidtest.robotp.publicutils.CronDateUtil;
import org.androidtest.robotp.publicutils.ReflectMethodUtil;
import org.androidtest.robotp.publicutils.StringUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static org.androidtest.robotp.data.Constant.CRON_DATE_FORMAT;

/**
 * GroupReminderPlugin，定时提醒插件
 *
 * @Author: Vince蔡培培
 */
public class GroupReminderPlugin implements Serializable, IPlugin {

	private static final String UNSUPPORT_MSG_INFO = "暂不支持该消息类型的定时提醒。";
	private String cronTime;
	private String remindContent;
	private MatchTypeEnum replyType;

	public MatchTypeEnum getReplyType() {
		return replyType;
	}

	public void setReplyType(MatchTypeEnum replyType) {
		this.replyType = replyType;
	}

	public String getCronTime() {
		return cronTime;
	}

	public void setCronTime(String cronTime) {
		this.cronTime = cronTime;
	}

	public String getRemindContent() {
		return remindContent;
	}

	public void setRemindContent(String remindContent) {
		this.remindContent = remindContent;
	}

	@Override
	public String toString() {
		return "GroupReminderPlugin{" + "cronTime='" + cronTime + '\''
				+ ", remindContent='" + remindContent + '\'' + ", replyType="
				+ replyType + '}';
	}

	@Override
	public List<String> getMatchWordsToList() {
		return null;
	}

	@Override
	public boolean reply(Group group, BaseMsg msg) {
		boolean isRely = false;
		String CRON_EXPRESSION = getCronTime();
		List<String> lTime = CronDateUtil.parser(CRON_EXPRESSION);
		if (StringUtil.ifNotNullOrEmpty(lTime)) {
			for (int i = 0; i < lTime.size(); i++) {
				String currentTime = CRON_DATE_FORMAT.format(new Date());
				if (lTime.get(i).equals(currentTime)) {
					switch (getReplyType()) {
					case TEXT:
						isRely = MessageTools.sendGroupMsgByNickName(
								getRemindContent(), group.getGroupName());
						break;
					case PIC:
						isRely = MessageTools.sendPicMsgByGroupNickName(
								group.getGroupName(), Config.dir_home
										+ File.separator + getRemindContent());
						break;
					case MEDIA:
						isRely = MessageTools.sendGroupFileMsgByNickName(
								group.getGroupName(), Config.dir_home
										+ File.separator + getRemindContent());
						break;
					case PLUGINCODE:
						try {
							String result[] = getRemindContent().split(
									Constant.AT_SPILT);
							isRely = (Boolean) ReflectMethodUtil
									.invokeMethodWithParam(
											Class.forName(result[0]),
											result[1],
											true,
											true,
											new Class[] { String.class },
											new Object[] { group.getGroupName() });
						} catch (Throwable e) {
							e.printStackTrace();
						}
						break;
					default:
						String errorTip = UNSUPPORT_MSG_INFO;
						isRely = MessageTools.sendGroupMsgByNickName(errorTip,
								group.getGroupName());
						break;
					}
					break;
				}
			}
		}
		return isRely;
	}

}