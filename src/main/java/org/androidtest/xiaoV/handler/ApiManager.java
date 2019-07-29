package org.androidtest.xiaoV.handler;

import static org.androidtest.xiaoV.data.Constant.groupList;

import java.io.File;
import java.util.Date;

import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.apache.log4j.Logger;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.enums.MsgCodeEnum;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;

public class ApiManager implements IMsgHandlerFace {

	Logger LOG = Logger.getLogger(ApiManager.class);

	/**
	 * 是否是白名单里的群
	 * 
	 * @param msg
	 * @return
	 */
	private Group isVaildWhiteGroup(BaseMsg msg) {
		LogUtil.MSG.debug("isVaildWhiteGroup: " + msg);
		if (msg.isGroupMsg()
				|| msg.getMsgType() == MsgCodeEnum.MSGTYPE_SYS.getCode()) {
			String currentGroupNickName = WechatTools
					.getGroupNickNameByGroupUserName(msg.getFromUserName());
			for (Group group : groupList) {
				if (currentGroupNickName.equals(group.getGroupNickName())) {
					LogUtil.MSG.debug("isVaildWhiteGroup: true: " + group);
					return group;
				}
			}
		}
		LogUtil.MSG.debug("isVaildWhiteGroup: false, return null");
		return null;
	}

	@Override
	public String mediaMsgHandle(BaseMsg msg) {
		LogUtil.MSG.debug("mediaMsgHandle: ");
		String result = null;
		final String text = msg.getContent();
		LOG.info("多媒体消息:" + text);
		Group group = isVaildWhiteGroup(msg);
		if (group != null) {// 只处理指定群
			result = MessageHandler.mediaMsgHandle(group, msg);
		}
		return result;
	}

	@Override
	public String nameCardMsgHandle(BaseMsg msg) {
		return null;
	}

	@Override
	public String picMsgHandle(BaseMsg msg) {
		LogUtil.MSG.debug("picMsgHandle: ");
		String nickName, currentGroupNickName = null;
		currentGroupNickName = WechatTools.getGroupNickNameByGroupUserName(msg
				.getFromUserName());
		Group group = isVaildWhiteGroup(msg);
		if (group != null) {// 只处理指定群
			nickName = WechatTools.getMemberDisplayOrNickNameByGroupNickName(
					currentGroupNickName, msg.getStatusNotifyUserName());

			String filename = Constant.SIMPLE_DATE_FORMAT_FILE
					.format(new Date()) + "-" + nickName + ".jpg";// 这里使用收到图片的时间作为文件名
			String picPath = Constant.getCurrentWeekSavePath() + File.separator
					+ filename;
			DownloadTools
					.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath); // 保存图片的路径
		}
		return null;
	}

	@Override
	public void sysMsgHandle(BaseMsg msg) {
		LogUtil.MSG.debug("sysMsgHandle: ");
		final String text = msg.getContent();
		Group group = isVaildWhiteGroup(msg);
		if (group != null) {// 只处理指定群
			LOG.info("系统消息:" + text);
			MessageHandler.sysMsgHandle(group, msg);
		}
	}

	@Override
	public String textMsgHandle(BaseMsg msg) {
		LogUtil.MSG.debug("textMsgHandle: ");
		String result = null;
		Group group = isVaildWhiteGroup(msg);
		if (group != null) {// 只处理指定群
			result = MessageHandler.groupMsgHandle(group, msg);
			// } else if (isVaildWhiteAdmin(msg)) {
			// result = MessageHandler.userMsgHandle(msg);
		} else {
			LOG.info("textMsgHandle: " + "非法消息: " + msg + "，消息过滤不处理");
		}
		return result;
	}

	@Override
	public String verifyAddFriendMsgHandle(BaseMsg msg) {
		return null;
	}

	@Override
	public String viedoMsgHandle(BaseMsg msg) {
		LogUtil.MSG.debug("viedoMsgHandle: ");
		String nickName, currentGroupNickName = null;
		currentGroupNickName = WechatTools.getGroupNickNameByGroupUserName(msg
				.getFromUserName());
		Group group = isVaildWhiteGroup(msg);
		if (group != null) {// 只处理指定群
			nickName = WechatTools.getMemberDisplayOrNickNameByGroupNickName(
					currentGroupNickName, msg.getStatusNotifyUserName());

			String filename = Constant.SIMPLE_DATE_FORMAT_FILE
					.format(new Date()) + "-" + nickName + ".mp4";
			String viedoPath = Constant.getCurrentWeekSavePath()
					+ File.separator + filename;
			DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(),
					viedoPath);
		}
		return null;
	}

	@Override
	public String voiceMsgHandle(BaseMsg msg) {
		return null;
	}

}
