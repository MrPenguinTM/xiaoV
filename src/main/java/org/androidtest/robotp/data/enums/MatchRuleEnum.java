package org.androidtest.robotp.data.enums;

import java.io.Serializable;

/**
 * 关键字匹配规则大全 枚举类
 *
 * @author Vince蔡培培
 */
public enum MatchRuleEnum implements Serializable {
	GLOBAL_EQUALS(0, "消息内容跟关键字完全相等，包含@方式"), GLOBAL_START(1, "消息内容以关键字开头，包含@方式"), GLOBAL_CONTAINS(
			2, "消息内容里包括关键字，包含@方式"), MENTION_EQUALS(3, "@方式的消息内容跟关键字完全相等"), MENTION_START(
			4, "@方式的消息内容以关键字开头"), MENTION_CONTAINS(5, "@方式的消息内容里包括关键字"), OTHER(
			6, "预留字段，做特殊处理");

	private int code;
	private String type;

	MatchRuleEnum(int code, String type) {
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
