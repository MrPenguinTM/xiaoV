package org.androidtest.xiaoV.action;

import static org.androidtest.xiaoV.data.Constant.SIMPLE_DAY_FORMAT_FILE;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidtest.xiaoV.action.ClockIn.WeeklySportClockIn;
import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;
import org.apache.log4j.Logger;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 撤回步数打卡组件
 * 
 * @author caipeipei
 *
 */
public class RecallSportAction extends Action {

	private static final String actionName = "撤回运动打卡";

	public RecallSportAction() {
		super(actionName, RECALL_SPORT_VAILD_KEYWORD_LIST);
		// TODO Auto-generated constructor stub
	}

	/**
	 * RecallSportAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> RECALL_SPORT_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("撤回运动打卡", MsgTypeEnum.TEXT);
		}
	};

	protected Logger LOG = Logger.getLogger(RecallSportAction.class);

	@Override
	public String action(Group g, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
				+ g.getGroupNickName());
		String result = null;
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
			if (group.containsAction(WeeklySportClockIn.class)) {
				String sportfilename = SIMPLE_DAY_FORMAT_FILE
						.format(new Date()) + "-" + fileUserName + ".sport";
				File sportfile = new File(Constant.getCurrentWeekSavePath()
						+ File.separator + sportfilename);
				if (sportfile.exists()) {
					sportfile.delete();
					result = "@" + senderNickName + " 操作成功，你当天的运动打卡数据已经删除。";
				} else {
					File dir = new File(Constant.getCurrentWeekSavePath());
					if (dir.isDirectory()) {
						File[] array = dir.listFiles();
						Arrays.sort(array, new Comparator<File>() {
							@Override
							public int compare(File f1, File f2) {
								long diff = f1.lastModified()
										- f2.lastModified();
								if (diff > 0)
									return 1;
								else if (diff == 0)
									return 0;
								else
									return -1;// 如果 if 中修改为 返回-1 同时此处修改为返回 1
												// 排序就会是递减
							}

							@Override
							public boolean equals(Object obj) {
								return true;
							}

						});
						boolean isdelete = false;
						for (int i = array.length - 1; i >= 0; i--) {
							if (array[i].isFile()
									&& array[i].getName().endsWith(".sport")
									&& array[i].getName().contains(
											"-" + fileUserName + ".")) {
								array[i].delete();
								isdelete = true;
								result = "@" + senderNickName
										+ " 操作成功，你本周最近一次的运动打卡数据已经删除。";
								break;
							}
						}
						if (!isdelete) {
							result = "@" + senderNickName
									+ " 操作失败，你本周没有任何运动打卡数据，故无法删除。";
						}
					}
				}
			} else {
				result = "@"
						+ senderNickName
						+ " 本群不支持步数运动功能，故无法撤回打卡数据。请联系管理员添加运动打卡的组件，或者移除撤回运动打卡的组件。";
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