package org.androidtest.xiaoV.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;

/**
 * 每日反思组件
 * 
 * @author caipeipei
 *
 */
@SuppressWarnings("rawtypes")
public class DailySelfReflectionAction extends Action {

	/**
	 * DailySelfReflectionAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> DAILY_SELF_REFLECTION_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put(Constant.DAILY_SELF_REFLECTION_DEFAULT_FILENAME_TEMPLATES
					+ ":xlsx", MsgTypeEnum.MEDIA);
			put(Constant.DAILY_SELF_REFLECTION_DEFAULT_FILENAME_TEMPLATES
					+ ":xls", MsgTypeEnum.MEDIA);
		}
	};

	private static final String actionName = "每日反思打卡";

	private Map<String, File> whiteList = new HashMap<String, File>();
	private int noonRemindTime = 1300;
	private int nightRemindTime = 2230;

	private boolean noonRemind = false;

	public DailySelfReflectionAction(Map<String, File> whiteList,
			boolean noonRemind) {
		super(actionName, DAILY_SELF_REFLECTION_VAILD_KEYWORD_LIST);
		setWhiteList(whiteList);
		setNoonRemind(noonRemind);
	}

	public DailySelfReflectionAction(Map<String, File> whiteList,
			int nightRemindTime) {
		super(actionName, DAILY_SELF_REFLECTION_VAILD_KEYWORD_LIST);
		setWhiteList(whiteList);
		setNoonRemind(false);
		setNightRemindTime(nightRemindTime);
	}

	public DailySelfReflectionAction(Map<String, File> whiteList,
			int noonRemindTime, int nightRemindTime) {
		super(actionName, DAILY_SELF_REFLECTION_VAILD_KEYWORD_LIST);
		setWhiteList(whiteList);
		setNoonRemind(true);
		setNightRemindTime(nightRemindTime);
		setNoonRemindTime(noonRemindTime);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
				+ group.getGroupNickName());
		String result = null;

		String currentGroupNickName = group.getGroupNickName();
		String currentDisplayName = WechatTools
				.getMemberDisplayOrNickNameByGroupNickName(
						currentGroupNickName, msg.getStatusNotifyUserName());
		String currentNickName = WechatTools.getMemberNickNameByGroupNickName(
				currentGroupNickName, msg.getStatusNotifyUserName());
		String currentFileName = msg.getFileName();

		Iterator iter = whiteList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String nickName = (String) entry.getKey();
			File filePath = (File) entry.getValue();
			LogUtil.MSG.debug("action: nickName: " + nickName
					+ ",currentNickName: " + currentNickName
					+ ",currentDisplayName: " + currentDisplayName);
			LogUtil.MSG.debug("action: currentFileName: " + currentFileName
					+ ",createDailySelfReflectionFilename(nickName): "
					+ createDailySelfReflectionFilename(nickName));
			if ((nickName.contains(currentNickName) || nickName
					.contains(currentDisplayName))
					&& currentFileName
							.contains(createDailySelfReflectionFilename(nickName))) {
				DownloadTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getType(),
						filePath.getAbsolutePath());
				result = "@"
						+ currentNickName
						+ " 今天每日反思打卡成功！记录已更新，存档已覆盖。生活如果不经过归纳总结和深入思考，就永远无法转化为阅历和智慧。今天你反思了吗？";
				break;
			}
		}
		if (result == null) {
			result = "@" + currentNickName + " 你不在每日反思名单中，无法完成该功能。请联系管理员" + "@"
					+ group.getAdmin() + " 报名。";
		}
		return result;
	}

	private String createDailySelfReflectionFilename(String nickName) {
		String result = Constant.DAILY_SELF_REFLECTION_DEFAULT_FILENAME_TEMPLATES
				+ " @" + nickName;
		LogUtil.MSG.debug("createDailySelfReflectionFilename: " + result);
		return result;
	}

	private String getFileExtension(File filePath) {
		String result = filePath.getName().substring(
				filePath.getName().lastIndexOf("."));
		LogUtil.MSG.debug("getFileExtension: " + result);
		return result;
	}

	private int getNightRemindTime() {
		return nightRemindTime;
	}

	private int getNoonRemindTime() {
		return noonRemindTime;
	}

	@SuppressWarnings("unused")
	private Map<String, File> getWhiteList() {
		return whiteList;
	}

	private boolean isNoonRemind() {
		return noonRemind;
	}

	@Override
	public boolean notify(Group currentGroup) {
		Date currentDate = new Date();
		int currentTime = Integer.parseInt(new SimpleDateFormat("HHmm")
				.format(currentDate));
		boolean result = false;
		if (isNoonRemind()) {
			// LogUtil.MSG.debug("notify: currentTime: " + currentTime
			// + ", getNoonRemindTime(): " + getNoonRemindTime());
			if (currentTime == getNoonRemindTime()) {
				Iterator iter = whiteList.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String nickName = (String) entry.getKey();
					File filePath = (File) entry.getValue();
					MessageTools
							.sendGroupMsgByNickName(
									"【* 每日反思打卡】\n@"
											+ nickName
											+ " 您已报名每日反思打卡（午间反思+睡前反思），请填写下面的反思自检清单表格，并将文件回传留档。",
									currentGroup.getGroupNickName());
					if (filePath.exists()) {
						MessageTools.sendGroupFileMsgByNickName(
								currentGroup.getGroupNickName(),
								filePath.getAbsolutePath());
					} else {
						MessageTools.sendGroupMsgByNickName("文件不存在: "
								+ filePath.getAbsolutePath(),
								currentGroup.getGroupNickName());
					}
				}
				result = true;// 传false表示允许轮询，不阻塞消息线程
				LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
						+ ": report " + result);
			}
		}
		if (currentTime == getNightRemindTime()) {
			// LogUtil.MSG.debug("notify: currentTime: " + currentTime
			// + ", getNightRemindTime(): " + getNightRemindTime());
			Iterator iter = whiteList.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String nickName = (String) entry.getKey();
				File filePath = (File) entry.getValue();
				MessageTools.sendGroupMsgByNickName("【* 每日反思打卡】\n@" + nickName
						+ " 您已报名每日反思打卡，一天即将过去，请完成下面的反思自检清单表格，并在睡前将文件回传留档。",
						currentGroup.getGroupNickName());
				if (filePath.exists()) {
					MessageTools.sendGroupFileMsgByNickName(
							currentGroup.getGroupNickName(),
							filePath.getAbsolutePath());
				} else {
					MessageTools.sendGroupMsgByNickName(
							"文件不存在: " + filePath.getAbsolutePath(),
							currentGroup.getGroupNickName());
				}

			}
			result = true;// 传false表示允许轮询，不阻塞消息线程
			LogUtil.MSG.info("notify: " + currentGroup.getGroupNickName()
					+ ": report " + result);

		}
		return result;
	}

	@Override
	public String report(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setNightRemindTime(int nightRemindTime) {
		LogUtil.MSG.debug("setNightRemindTime: " + nightRemindTime);
		if (nightRemindTime >= 0 && nightRemindTime <= 2400) {
			this.nightRemindTime = nightRemindTime;
		} else {
			throw new RuntimeException("setNightRemindTime: out of range: "
					+ nightRemindTime);
		}

	}

	private void setNoonRemind(boolean noonRemind) {
		LogUtil.MSG.debug("setNoonRemind: " + noonRemind);
		this.noonRemind = noonRemind;

	}

	private void setNoonRemindTime(int noonRemindTime) {
		LogUtil.MSG.debug("setNoonRemindTime: " + noonRemindTime);
		if (noonRemindTime >= 0 && noonRemindTime <= 2400) {
			this.noonRemindTime = noonRemindTime;
		} else {
			throw new RuntimeException("setNoonRemindTime: out of range: "
					+ noonRemindTime);
		}

	}

	private void setWhiteList(Map<String, File> whiteList) {
		this.whiteList.clear();

		Map<String, File> newWhiteList = new HashMap<String, File>();
		Iterator iter = whiteList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String nickName = (String) entry.getKey();
			File filePath = (File) entry.getValue();

			File newfile = new File(Constant.getDataSavePath() + File.separator
					+ createDailySelfReflectionFilename(nickName)
					+ getFileExtension(filePath));
			LogUtil.MSG.info("setWhiteList: " + newfile.getAbsolutePath());
			if (filePath.exists() && filePath.isFile()) {// A路径存在
				LogUtil.MSG.debug("setWhiteList: " + nickName + ", 预设的路径存在文件"
						+ filePath.getAbsolutePath());
				if (newfile.exists() && newfile.isFile()) {// 如果A路径存在情况下，B路径也存在，就先删除B
					newfile.delete();
				}
				if (filePath.renameTo(newfile)) {
					newWhiteList.put(nickName, newfile);
					LogUtil.MSG.debug("setWhiteList: " + nickName
							+ ", 变更文件成功，新路径: " + newfile.getAbsolutePath());
				} else {
					throw new RuntimeException("setWhiteList:"
							+ filePath.getName() + "改名为" + newfile.getName()
							+ "失败。请检查原因。");
				}
			} else if (newfile.exists() && newfile.isFile()) {// A路径不在，新路径存在
				newWhiteList.put(nickName, newfile);
				LogUtil.MSG.debug("setWhiteList: " + nickName
						+ ", 预设的文件路径不存在，但在另一路径找到，更新文件成功: "
						+ newfile.getAbsolutePath());
			} else {
				throw new RuntimeException("setWhiteList:"
						+ filePath.getAbsolutePath() + "路径不存在或者非文件。请检查文件路径。");
			}
		}
		this.whiteList = newWhiteList;
	}
}