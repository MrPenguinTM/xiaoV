package org.androidtest.robotp.beans.group;

import java.io.Serializable;

/**
 * GroupRobotConfig java bean
 *
 * @Author: Vince蔡培培
 */
public class GroupRobotConfig implements Serializable {

	private boolean robotChat;
	private boolean unknownReply;
	private boolean receiverMassMsg;

	public boolean isReceiverMassMsg() {
		return receiverMassMsg;
	}

	public void setReceiverMassMsg(boolean receiverMassMsg) {
		this.receiverMassMsg = receiverMassMsg;
	}

	public boolean getRobotChat() {
		return robotChat;
	}

	public void setRobotChat(boolean robotChat) {
		this.robotChat = robotChat;
	}

	public boolean getUnknownReply() {
		return unknownReply;
	}

	public void setUnknownReply(boolean unknownReply) {
		this.unknownReply = unknownReply;
	}

	@Override
	public String toString() {
		return "GroupRobotConfig{" + "robotChat=" + robotChat
				+ ", unknownReply=" + unknownReply + ", receiverMassMsg="
				+ receiverMassMsg + '}';
	}
}