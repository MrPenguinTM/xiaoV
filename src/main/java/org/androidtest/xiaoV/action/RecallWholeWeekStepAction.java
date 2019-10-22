package org.androidtest.xiaoV.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidtest.xiaoV.action.ClockIn.WholeWeekStepClockIn;
import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.DateUtil;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;
import org.apache.log4j.Logger;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 撤回周步数打卡组件
 * 
 * @author caipeipei
 *
 */
public class RecallWholeWeekStepAction extends Action {

	private static final String actionName = "撤回周步数打卡";

	public RecallWholeWeekStepAction() {
		super(actionName, RECALL_WHOLE_WEEK_STEP_VAILD_KEYWORD_LIST);
		// TODO Auto-generated constructor stub
	}

	/**
	 * RecallWholeWeekStepAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> RECALL_WHOLE_WEEK_STEP_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("撤回周步数打卡", MsgTypeEnum.TEXT);
		}
	};

	protected Logger LOG = Logger.getLogger(RecallWholeWeekStepAction.class);

	@Override
	public String action(Group g, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
				+ g.getGroupNickName());
		String result = null;
		if (DateUtil.isSunday()) {
			String currentGroupNickName = g.getGroupNickName();
			List<String> list = WechatTools
					.getMemberListByGroupNickName2(currentGroupNickName);
			String senderNickName = WechatTools
					.getMemberDisplayOrNickNameByGroupNickName(
							currentGroupNickName, msg.getStatusNotifyUserName());

			String fileUserName = StringUtil.filter(senderNickName);
			if (StringUtil.ifNullOrEmpty(fileUserName)) {
				result = "@" + senderNickName + " 你的名字有无法识别的字符，无法处理！请改昵称。";
				LogUtil.MSG.warn("action: " + result);
				return result;
			}

			Group group = null;
			for (Group gr : Constant.groupList) {
				if (gr.getGroupNickName().equals(currentGroupNickName)) {
					group = gr;
					break;
				}
			}
			if (group != null) {
				if (group.containsAction(WholeWeekStepClockIn.class)) {
					String wstepfilename = Constant.SIMPLE_DAY_FORMAT_FILE
							.format(new Date()) + "-" + fileUserName + ".wstep";
					File wstepfile = new File(Constant.getCurrentWeekSavePath()
							+ File.separator + wstepfilename);
					if (wstepfile.exists()) {
						wstepfile.delete();
						result = "@" + senderNickName
								+ " 操作成功，你本周的周步数打卡数据已经删除。";
					} else {
						result = "@" + senderNickName
								+ " 操作失败，你今天还没有进行周步数打卡，没有数据，故无法删除。";

					}
				} else {
					result = "@"
							+ senderNickName
							+ " 本群不支持周步数打卡功能，故无法撤回周打卡数据。请联系管理员添加周步数打卡的组件，或者移除撤回周步数打卡的组件。";
				}
			}
		}
		return result;
	}

	@Override
	public boolean notify(Group group) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String report(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

}