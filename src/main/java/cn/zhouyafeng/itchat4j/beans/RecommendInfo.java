package cn.zhouyafeng.itchat4j.beans;

import java.io.Serializable;

/**
 * RecommendInfo
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年7月3日 下午10:35:14
 */
public class RecommendInfo implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private String ticket;
	private String userName;
	private int sex;
	private int attrStatus;
	private String city;
	private String nickName;
	private int scene;
	private String province;
	private String content;
	private String alias;
	private String signature;
	private int opCode;
	private int qQNum;
	private int verifyFlag;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getAttrStatus() {
		return attrStatus;
	}

	public void setAttrStatus(int attrStatus) {
		this.attrStatus = attrStatus;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getOpCode() {
		return opCode;
	}

	public void setOpCode(int opCode) {
		this.opCode = opCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getqQNum() {
		return qQNum;
	}

	public void setqQNum(int qQNum) {
		this.qQNum = qQNum;
	}

	public int getScene() {
		return scene;
	}

	public void setScene(int scene) {
		this.scene = scene;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getVerifyFlag() {
		return verifyFlag;
	}

	public void setVerifyFlag(int verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	@Override
	public String toString() {
		return "RecommendInfo [ticket=" + ticket + ", userName=" + userName
				+ ", sex=" + sex + ", attrStatus=" + attrStatus + ", city="
				+ city + ", nickName=" + nickName + ", scene=" + scene
				+ ", province=" + province + ", content=" + content
				+ ", alias=" + alias + ", signature=" + signature + ", opCode="
				+ opCode + ", qQNum=" + qQNum + ", verifyFlag=" + verifyFlag
				+ "]";
	}

}
