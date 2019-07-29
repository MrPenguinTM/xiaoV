package cn.zhouyafeng.itchat4j.beans;

import java.io.Serializable;

/**
 * 收到的微信消息
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年7月3日 下午10:28:06
 */
public class BaseMsg implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private int subMsgType;
	private int voiceLength;
	private String fileName;
	private int imgHeight;
	private String toUserName;
	private int hasProductId;
	private int imgStatus;
	private String url;
	private int imgWidth;
	private int forwardFlag;
	private int status;
	private String Ticket;
	/**
	 * 推荐消息报文
	 **/
	private RecommendInfo recommendInfo;
	private long createTime;
	private String newMsgId;
	/**
	 * 文本消息内容
	 **/
	private String text;
	/**
	 * 消息类型
	 **/
	private int msgType;
	/**
	 * 是否为群消息
	 **/
	private boolean groupMsg;
	private String msgId;
	private int statusNotifyCode;
	private AppInfo appInfo;
	private int appMsgType;
	private String Type;
	private int playLength;
	private String mediaId;
	private String content;
	private String statusNotifyUserName;
	/**
	 * 消息发送者ID
	 **/
	private String fromUserName;
	private String oriContent;
	private String fileSize;

	public AppInfo getAppInfo() {
		return appInfo;
	}

	public int getAppMsgType() {
		return appMsgType;
	}

	public String getContent() {
		return content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public int getForwardFlag() {
		return forwardFlag;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public int getHasProductId() {
		return hasProductId;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public int getImgStatus() {
		return imgStatus;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public String getMediaId() {
		return mediaId;
	}

	public String getMsgId() {
		return msgId;
	}

	public int getMsgType() {
		return msgType;
	}

	public String getNewMsgId() {
		return newMsgId;
	}

	public String getOriContent() {
		return oriContent;
	}

	public int getPlayLength() {
		return playLength;
	}

	public RecommendInfo getRecommendInfo() {
		return recommendInfo;
	}

	public int getStatus() {
		return status;
	}

	public int getStatusNotifyCode() {
		return statusNotifyCode;
	}

	public String getStatusNotifyUserName() {
		return statusNotifyUserName;
	}

	public int getSubMsgType() {
		return subMsgType;
	}

	public String getText() {
		return text;
	}

	public String getTicket() {
		return Ticket;
	}

	public String getToUserName() {
		return toUserName;
	}

	public String getType() {
		return Type;
	}

	public String getUrl() {
		return url;
	}

	public int getVoiceLength() {
		return voiceLength;
	}

	public boolean isGroupMsg() {
		return groupMsg;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public void setAppMsgType(int appMsgType) {
		this.appMsgType = appMsgType;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public void setForwardFlag(int forwardFlag) {
		this.forwardFlag = forwardFlag;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public void setGroupMsg(boolean groupMsg) {
		this.groupMsg = groupMsg;
	}

	public void setHasProductId(int hasProductId) {
		this.hasProductId = hasProductId;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public void setImgStatus(int imgStatus) {
		this.imgStatus = imgStatus;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public void setNewMsgId(String newMsgId) {
		this.newMsgId = newMsgId;
	}

	public void setOriContent(String oriContent) {
		this.oriContent = oriContent;
	}

	public void setPlayLength(int playLength) {
		this.playLength = playLength;
	}

	public void setRecommendInfo(RecommendInfo recommendInfo) {
		this.recommendInfo = recommendInfo;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setStatusNotifyCode(int statusNotifyCode) {
		this.statusNotifyCode = statusNotifyCode;
	}

	public void setStatusNotifyUserName(String statusNotifyUserName) {
		this.statusNotifyUserName = statusNotifyUserName;
	}

	public void setSubMsgType(int subMsgType) {
		this.subMsgType = subMsgType;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public void setType(String type) {
		Type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setVoiceLength(int voiceLength) {
		this.voiceLength = voiceLength;
	}

	@Override
	public String toString() {
		return "BaseMsg [subMsgType=" + subMsgType + ", voiceLength="
				+ voiceLength + ", fileName=" + fileName + ", imgHeight="
				+ imgHeight + ", toUserName=" + toUserName + ", hasProductId="
				+ hasProductId + ", imgStatus=" + imgStatus + ", url=" + url
				+ ", imgWidth=" + imgWidth + ", forwardFlag=" + forwardFlag
				+ ", status=" + status + ", Ticket=" + Ticket
				+ ", recommendInfo=" + recommendInfo + ", createTime="
				+ createTime + ", newMsgId=" + newMsgId + ", text=" + text
				+ ", msgType=" + msgType + ", groupMsg=" + groupMsg
				+ ", msgId=" + msgId + ", statusNotifyCode=" + statusNotifyCode
				+ ", appInfo=" + appInfo + ", appMsgType=" + appMsgType
				+ ", Type=" + Type + ", playLength=" + playLength
				+ ", mediaId=" + mediaId + ", content=" + content
				+ ", statusNotifyUserName=" + statusNotifyUserName
				+ ", fromUserName=" + fromUserName + ", oriContent="
				+ oriContent + ", fileSize=" + fileSize + "]";
	}
}
