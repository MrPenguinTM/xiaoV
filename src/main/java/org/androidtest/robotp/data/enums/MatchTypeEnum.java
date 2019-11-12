package org.androidtest.robotp.data.enums;

/**
 * 消息类型枚举类
 *
 * @author Vince蔡培培
 */
public enum MatchTypeEnum {
	TEXT(0, "文本消息"), PIC(1, "图片消息"), VOICE(2, "语音消息"), VIEDO(3, "小视频消息"), NAMECARD(
			4, "名片消息"), SYS(5, "系统消息"), VERIFYMSG(6, "添加好友"), MEDIA(7, "文件消息"), PLUGINCODE(
			8, "代码嵌入");

	private int code;
	private String type;

	MatchTypeEnum(int code, String type) {
		this.type = type;
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return getType();
	}
}
