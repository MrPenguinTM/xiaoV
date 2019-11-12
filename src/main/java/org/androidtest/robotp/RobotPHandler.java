package org.androidtest.robotp;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.helper.GroupMsgHelper;
import org.androidtest.robotp.helper.UserMsgHelper;
import org.androidtest.robotp.publicutils.LogUtil;
import org.androidtest.robotp.publicutils.StringUtil;

/**
 * RobotP对消息处理的中控类
 *
 * @author Vince蔡培培
 */
public class RobotPHandler implements IMsgHandlerFace {

	@Override
	public String mediaMsgHandle(BaseMsg msg) {
		if (UserMsgHelper.isVaildAdmin(msg)) {
			MessageTools.sendMsgById(msg.toString(), msg.getFromUserName());
		}
		return null;
	}

	@Override
	public String nameCardMsgHandle(BaseMsg msg) {
		if (UserMsgHelper.isVaildAdmin(msg)) {
			MessageTools.sendMsgById(msg.toString(), msg.getFromUserName());
		}
		return null;
	}

	@Override
	public String picMsgHandle(BaseMsg msg) {
		if (UserMsgHelper.isVaildAdmin(msg)) {
			MessageTools.sendMsgById(msg.toString(), msg.getFromUserName());
		}
		return null;
	}

	@Override
	public void sysMsgHandle(BaseMsg msg) {
		return;
	}

	@Override
	public String textMsgHandle(BaseMsg msg) {
		LogUtil.MSG.debug("textMsgHandle: ");
		String result = null;
		Group group = GroupMsgHelper.isVaildWhiteGroup(msg);
		if (StringUtil.ifNotNullOrEmpty(group)) {// 只处理指定群
			result = GroupMsgHelper.groupTextMsgHandle(group, msg);
		} else if (UserMsgHelper.isVaildAdmin(msg)) {
			result = UserMsgHelper.userTextMsgHandle(msg);
		} else {
			LogUtil.MSG.info("textMsgHandle: " + "非法消息: " + msg + "，消息过滤不处理");
		}
		return result;
	}

	@Override
	public String verifyAddFriendMsgHandle(BaseMsg msg) {
		if (UserMsgHelper.isVaildAdmin(msg)) {
			MessageTools.sendMsgById(msg.toString(), msg.getFromUserName());
		}
		return null;
	}

	@Override
	public String viedoMsgHandle(BaseMsg msg) {
		if (UserMsgHelper.isVaildAdmin(msg)) {
			MessageTools.sendMsgById(msg.toString(), msg.getFromUserName());
		}
		return null;
	}

	@Override
	public String voiceMsgHandle(BaseMsg msg) {
		if (UserMsgHelper.isVaildAdmin(msg)) {
			MessageTools.sendMsgById(msg.toString(), msg.getFromUserName());
		}
		return null;
	}
}
