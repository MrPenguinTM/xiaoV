package cn.zhouyafeng.itchat4j;

import cn.zhouyafeng.itchat4j.controller.LoginController;
import cn.zhouyafeng.itchat4j.core.MsgCenter;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import org.androidtest.robotp.Config;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.helper.GroupListHelper;
import org.androidtest.robotp.publicutils.LogUtil;
import org.androidtest.robotp.publicutils.StringUtil;
import org.androidtest.robotp.publicutils.ThreadUtil;

import java.util.concurrent.TimeUnit;

public class Wechat {
	private IMsgHandlerFace msgHandler;

	public Wechat(IMsgHandlerFace msgHandler) {
		System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
		this.msgHandler = msgHandler;
		LogUtil.MSG.info("0. 初始化" + Config.dir_groups.getAbsolutePath()
				+ "下的所有群配置文件");
		GroupListHelper.updateGroupList();
		// 登陆
		LoginController login = new LoginController();
		login.login(Config.dir_home.getAbsolutePath());
	}

	private void setBoardcastListener() {
		while (true) {
			boolean isReported = false;
			for (Group currentGroup : GroupListHelper.getGroupList()) {
				if (StringUtil.ifNotNullOrEmpty(currentGroup.getGroupPlugin())) {
					if (StringUtil.ifNotNullOrEmpty(currentGroup
							.getGroupPlugin().getReminderPluginArray())) {
						for (int i = 0; i < currentGroup.getGroupPlugin()
								.getReminderPluginArray().size(); i++) {
							boolean result = currentGroup.getGroupPlugin()
									.getReminderPluginArray().get(i)
									.reply(currentGroup, null);
							if (result && isReported == false) {
								isReported = true;
							}
						}
					}
				}
			}
			if (isReported) {
				LogUtil.MSG.debug("setBoardcastListener: " + "isReported "
						+ isReported);
				isReported = false;
				ThreadUtil.sleep(70 * 1000);// 执行完一轮后需要停止超过1分钟，避免重复报
			} else {
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void start() {
		LogUtil.MSG.info("+++++++++++++++++++开始消息处理+++++++++++++++++++++");
		new Thread(new Runnable() {
			@Override
			public void run() {
				MsgCenter.handleMsg(msgHandler);
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				setBoardcastListener();
			}
		}).start();
	}
}
