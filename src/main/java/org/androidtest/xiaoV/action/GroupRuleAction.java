package org.androidtest.xiaoV.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.androidtest.xiaoV.data.Constant;
import org.androidtest.xiaoV.data.Group;
import org.androidtest.xiaoV.publicutil.LogUtil;
import org.androidtest.xiaoV.publicutil.StringUtil;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 群规则提醒组件
 * 
 * @author caipeipei
 *
 */
public class GroupRuleAction extends Action {

	/**
	 * RuleAction类的合法参数及参数类型
	 */
	@SuppressWarnings("serial")
	private static final Map<String, MsgTypeEnum> RULE_VAILD_KEYWORD_LIST = new HashMap<String, MsgTypeEnum>() {
		{
			put("规则", MsgTypeEnum.TEXT);
			// put("群规", MsgTypeEnum.TEXT);
		}
	};

	private static final String actionName = "群规则提醒";

	private File ruleFile;

	public GroupRuleAction(String groupNickName, File file) {
		super(actionName, RULE_VAILD_KEYWORD_LIST);
		setRuleFile(groupNickName, file);
	}

	@Override
	public String action(Group group, BaseMsg msg) {
		LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());
		String currentGroupNickName = group.getGroupNickName();
		MessageTools.sendPicMsgByGroupNickName(currentGroupNickName,
				getRuleFile().getAbsolutePath());
		return null;
	}

	private String createGroupRuleFilename(String groupNickName) {
		String fileGroupNickName = StringUtil.filter(groupNickName);
		if (StringUtil.ifNullOrEmpty(fileGroupNickName)) {
			throw new RuntimeException("名为" + groupNickName
					+ "的群有无法识别的字符，无法处理！请改群名。");
		}
		String result = Constant.GROUP_RULE_DEFAULT_FILENAME_TEMPLATES
				+ fileGroupNickName;
		LogUtil.MSG.debug("createGroupRuleFilename: " + result);
		return result;
	}

	private String getFileExtension(File filePath) {
		String result = filePath.getName().substring(
				filePath.getName().lastIndexOf("."));
		LogUtil.MSG.debug("getFileExtension: " + result);
		if (result.equalsIgnoreCase(".jpg") || result.equalsIgnoreCase(".jpeg")
				|| result.equalsIgnoreCase(".bmp")
				|| result.equalsIgnoreCase(".png")
				|| result.equalsIgnoreCase(".tif")
				|| result.equalsIgnoreCase(".gif")
				|| result.equalsIgnoreCase(".ico")) {
			return result;
		} else {
			throw new RuntimeException("getFileExtension: 规则文件要求为图片！实际规则文件"
					+ filePath.getName() + "文件格式为:" + result);
		}
	}

	public File getRuleFile() {
		return ruleFile;
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

	public void setRuleFile(String groupNickName, File ruleFile) {
		File newfile = new File(Constant.getDataSavePath() + File.separator
				+ createGroupRuleFilename(groupNickName)
				+ getFileExtension(ruleFile));
		LogUtil.MSG.info("setRuleFile: " + newfile.getAbsolutePath());
		if (ruleFile.exists() && ruleFile.isFile()) {// A路径存在
			LogUtil.MSG.debug("setRuleFile: " + groupNickName + ", 预设的路径存在文件"
					+ ruleFile.getAbsolutePath());
			if (newfile.exists() && newfile.isFile()) {// A和B路径同时存在，就删B
				newfile.delete();
			}
			if (ruleFile.renameTo(newfile)) {
				LogUtil.MSG.debug("setRuleFile: " + groupNickName
						+ ", 变更文件成功，新路径: " + newfile.getAbsolutePath());
			} else {
				throw new RuntimeException("setRuleFile:" + ruleFile.getName()
						+ "改名为" + newfile.getName() + "失败。对应群名为:"
						+ groupNickName + "。请检查原因。");
			}
		} else if (newfile.exists() && newfile.isFile()) {// A路径不在，新路径存在
			LogUtil.MSG.debug("setRuleFile: " + groupNickName
					+ ", 预设的文件路径不存在，但在另一路径找到，更新文件成功: "
					+ newfile.getAbsolutePath());
		} else {
			throw new RuntimeException("setRuleFile:"
					+ ruleFile.getAbsolutePath() + "路径不存在或者非文件，对应群名为:"
					+ groupNickName + "。请检查文件路径。");
		}
		if (!newfile.exists()) {
			throw new RuntimeException("setRuleFile:"
					+ newfile.getAbsolutePath() + "路径不存在或者非文件，对应群名为:"
					+ groupNickName + "。请检查文件路径。");
		}
		this.ruleFile = newfile;
	}

}