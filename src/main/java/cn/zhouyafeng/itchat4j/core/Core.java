package cn.zhouyafeng.itchat4j.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.MyHttpClient;
import cn.zhouyafeng.itchat4j.utils.enums.parameters.BaseParaEnum;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 核心存储类，全局只保存一份，单例模式
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年4月23日 下午2:33:56
 */
public class Core {

	private static Core instance;

	public static Core getInstance() {
		if (instance == null) {
			synchronized (Core.class) {
				instance = new Core();
			}
		}
		return instance;
	}

	boolean alive = false;
	Map<String, Object> loginInfo = new HashMap<String, Object>();
	// CloseableHttpClient httpClient = HttpClients.createDefault();
	MyHttpClient myHttpClient = MyHttpClient.getInstance();
	String uuid = null;
	boolean useHotReload = false;
	String hotReloadDir = "itchat.pkl";
	int receivingRetryCount = 5;
	private int memberCount = 0;
	private String indexUrl;
	private String userName;
	private String nickName;; // 群
	private List<BaseMsg> msgList = new ArrayList<BaseMsg>();
	private JSONObject userSelf; // 登陆账号自身信息
	;// 公众号／服务号
	private List<JSONObject> memberList = new ArrayList<JSONObject>(); // 好友+群聊+公众号+特殊账号
	;// 特殊账号
	private List<JSONObject> contactList = new ArrayList<JSONObject>();// 好友
	private List<JSONObject> groupList = new ArrayList<JSONObject>();
	private Map<String, JSONArray> groupMemeberMap = new HashMap<String, JSONArray>(); // 群聊成员字典
	private List<JSONObject> publicUsersList = new ArrayList<JSONObject>();
	private List<JSONObject> specialUsersList = new ArrayList<JSONObject>();
	private List<String> groupIdList = new ArrayList<String>(); // 群ID列表
	private List<String> groupNickNameList = new ArrayList<String>(); // 群NickName列表
	private Map<String, JSONObject> userInfoMap = new HashMap<String, JSONObject>();

	private long lastNormalRetcodeTime; // 最后一次收到正常retcode的时间，秒为单位

	private Core() {

	}

	public List<JSONObject> getContactList() {
		return contactList;
	}

	public List<String> getGroupIdList() {
		return groupIdList;
	}

	public List<JSONObject> getGroupList() {
		return groupList;
	}

	public Map<String, JSONArray> getGroupMemeberMap() {
		return groupMemeberMap;
	}

	public List<String> getGroupNickNameList() {
		return groupNickNameList;
	}

	public String getHotReloadDir() {
		return hotReloadDir;
	}

	public String getIndexUrl() {
		return indexUrl;
	}

	public synchronized long getLastNormalRetcodeTime() {
		return lastNormalRetcodeTime;
	}

	public Map<String, Object> getLoginInfo() {
		return loginInfo;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public List<JSONObject> getMemberList() {
		return memberList;
	}

	public List<BaseMsg> getMsgList() {
		return msgList;
	}

	public MyHttpClient getMyHttpClient() {
		return myHttpClient;
	}

	public String getNickName() {
		return nickName;
	}

	/**
	 * 请求参数
	 */
	public Map<String, Object> getParamMap() {
		return new HashMap<String, Object>(1) {
			/**
             *
             */
			private static final long serialVersionUID = 1L;

			{
				Map<String, String> map = new HashMap<String, String>();
				for (BaseParaEnum baseRequest : BaseParaEnum.values()) {
					map.put(baseRequest.para(),
							getLoginInfo().get(baseRequest.value()).toString());
				}
				put("BaseRequest", map);
			}
		};
	}

	public List<JSONObject> getPublicUsersList() {
		return publicUsersList;
	}

	public int getReceivingRetryCount() {
		return receivingRetryCount;
	}

	public List<JSONObject> getSpecialUsersList() {
		return specialUsersList;
	}

	public Map<String, JSONObject> getUserInfoMap() {
		return userInfoMap;
	}

	public String getUserName() {
		return userName;
	}

	public JSONObject getUserSelf() {
		return userSelf;
	}

	public String getUuid() {
		return uuid;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isUseHotReload() {
		return useHotReload;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setContactList(List<JSONObject> contactList) {
		this.contactList = contactList;
	}

	public void setGroupIdList(List<String> groupIdList) {
		this.groupIdList = groupIdList;
	}

	public void setGroupList(List<JSONObject> groupList) {
		this.groupList = groupList;
	}

	public void setGroupMemeberMap(Map<String, JSONArray> groupMemeberMap) {
		this.groupMemeberMap = groupMemeberMap;
	}

	public void setGroupNickNameList(List<String> groupNickNameList) {
		this.groupNickNameList = groupNickNameList;
	}

	public void setHotReloadDir(String hotReloadDir) {
		this.hotReloadDir = hotReloadDir;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public synchronized void setLastNormalRetcodeTime(long lastNormalRetcodeTime) {
		this.lastNormalRetcodeTime = lastNormalRetcodeTime;
	}

	public void setLoginInfo(Map<String, Object> loginInfo) {
		this.loginInfo = loginInfo;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public void setMemberList(List<JSONObject> memberList) {
		this.memberList = memberList;
	}

	public void setMsgList(List<BaseMsg> msgList) {
		this.msgList = msgList;
	}

	public void setMyHttpClient(MyHttpClient myHttpClient) {
		this.myHttpClient = myHttpClient;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setPublicUsersList(List<JSONObject> publicUsersList) {
		this.publicUsersList = publicUsersList;
	}

	public void setReceivingRetryCount(int receivingRetryCount) {
		this.receivingRetryCount = receivingRetryCount;
	}

	public void setSpecialUsersList(List<JSONObject> specialUsersList) {
		this.specialUsersList = specialUsersList;
	}

	public void setUseHotReload(boolean useHotReload) {
		this.useHotReload = useHotReload;
	}

	public void setUserInfoMap(Map<String, JSONObject> userInfoMap) {
		this.userInfoMap = userInfoMap;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserSelf(JSONObject userSelf) {
		this.userSelf = userSelf;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
