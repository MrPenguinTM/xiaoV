package cn.zhouyafeng.itchat4j;

import static org.androidtest.xiaoV.data.Constant.groupList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.androidtest.xiaoV.Config;
import org.androidtest.xiaoV.action.Action;
import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;
import org.androidtest.xiaoV.publicutil.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.itchat4j.controller.LoginController;
import cn.zhouyafeng.itchat4j.core.MsgCenter;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;

public class Wechat {
	private static final Logger LOG = LoggerFactory.getLogger(Wechat.class);
	private IMsgHandlerFace msgHandler;

	public Wechat(IMsgHandlerFace msgHandler) {
		System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
		this.msgHandler = msgHandler;
		LOG.info("0. 初始化群白名单和对应管理员信息");
		Config.initGroupAndAdmin();
		// 登陆
		LoginController login = new LoginController();
		login.login(Constant.getOutPutPath());
	}

	private void setBoardcastListener() {
		while (true) {
			boolean isReported = false;
			for (Group currentGroup : groupList) {
				List<Action> customizedList = currentGroup.getActionList();
				if (StringUtil.ifNotNullOrEmpty(customizedList)) {
					for (Action customized : customizedList) {
						boolean result = customized.notify(currentGroup);
						if (result && isReported == false) {
							isReported = true;
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
		LOG.info("+++++++++++++++++++开始消息处理+++++++++++++++++++++");
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
