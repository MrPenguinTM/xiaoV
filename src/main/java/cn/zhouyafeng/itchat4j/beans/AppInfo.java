package cn.zhouyafeng.itchat4j.beans;

import java.io.Serializable;

/**
 * AppInfo
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年7月3日 下午10:38:14
 */
public class AppInfo implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private int type;
	private String appId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AppInfo [type=" + type + ", appId=" + appId + "]";
	}

}
