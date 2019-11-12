package org.androidtest.robotp.helper;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.utils.enums.MsgCodeEnum;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.data.enums.MatchRuleEnum;
import org.androidtest.robotp.data.enums.MatchTypeEnum;
import org.androidtest.robotp.plugin.IPlugin;
import org.androidtest.robotp.plugin.TulingRobot;
import org.androidtest.robotp.plugin.group.GroupActionPlugin;
import org.androidtest.robotp.plugin.group.GroupExercisePlugin;
import org.androidtest.robotp.publicutils.LogUtil;
import org.androidtest.robotp.publicutils.StringUtil;

import java.util.List;

/**
 * 群消息的工具类
 *
 * @author Vince蔡培培
 */
public class GroupMsgHelper {
    private static String robotCall = "@" + Core.getInstance().getNickName();

    // //////////////////////////////////////////////////////////////////////

    /**
     * 是否是白名单里的群
     *
     * @param msg
     * @return
     */
    public static Group isVaildWhiteGroup(BaseMsg msg) {
        LogUtil.MSG.debug("isVaildWhiteGroup: " + msg);
        if (msg.isGroupMsg()
                || msg.getMsgType() == MsgCodeEnum.MSGTYPE_SYS.getCode()) {
            String currentGroupNickName = WechatTools
                    .getGroupNickNameByGroupUserName(msg.getFromUserName());
            for (Group group : GroupListHelper.getGroupList()) {
                if (group.getGroupName().equals(currentGroupNickName)) {
                    LogUtil.MSG.debug("isVaildWhiteGroup: true: " + group);
                    return group;
                }
            }
        }
        LogUtil.MSG.debug("isVaildWhiteGroup: false, return null");
        return null;
    }

    // //////////////////////////////////////////////////////////////////////

    /**
     * 处理群消息
     *
     * @param msg
     * @return
     */
    public static String groupTextMsgHandle(Group group, BaseMsg msg) {
        LogUtil.MSG.debug("groupMsgHandle: " + group + ", " + msg);

        String result = null;
        boolean isRepled = false;
        String currentGroupNickName = WechatTools
                .getGroupNickNameByGroupUserName(msg.getFromUserName());
        if (isCurrentMsgFromVaildGroup(group, currentGroupNickName)) {
            final String content = msg.getText().trim();
            IPlugin iPlugin = returnPluginFrowmGroupMsg(group, msg,
                    MatchTypeEnum.TEXT);
            if (StringUtil.ifNotNullOrEmpty(iPlugin)) {
                isRepled = iPlugin.reply(group, msg);
            } else {
                LogUtil.MSG.info("groupMsgHandle: " + "非法参数: " + content
                        + "，消息过滤不处理.result=" + result);
            }
            if (!isRepled && isContentContainsRobotName(group, msg, content)) {
                result = unknownTextMsgReply(group, msg, content);
            }
        }
        return result;
    }

    private static boolean isCurrentMsgFromVaildGroup(Group group,
                                                      String currentGroupNickName) {
        return group.getGroupName().equals(currentGroupNickName);
    }

    private static IPlugin findMatchPlugin(Group group, BaseMsg msg,
                                           IPlugin plugin, MatchRuleEnum matchRuleEnum) {
        List<String> matchWords = plugin.getMatchWordsToList();
        String robotDisplayName = getRobotDisplayName(group, msg);
        final String content = msg.getContent();
        IPlugin iPlugin = null;
        for (String matchWord : matchWords) {
            switch (matchRuleEnum) {
                case GLOBAL_EQUALS:
                    // TODO
                    break;
                case GLOBAL_START:
                    if (isMatchGlobalStartExpression(content, matchWord,
                            robotDisplayName)) {
                        iPlugin = plugin;
                    }
                    break;
                case GLOBAL_CONTAINS:
                    if (isMatchGlobalContainsExpression(content, matchWord)) {
                        iPlugin = plugin;
                    }
                    break;
                case MENTION_EQUALS:
                    // TODO
                    break;
                case MENTION_START:
                    // TODO
                    break;
                case MENTION_CONTAINS:
                    // TODO
                    break;
                case OTHER:
                    // TODO
                    break;
                default:
                    // TODO
                    break;
            }
            if (StringUtil.ifNotNullOrEmpty(iPlugin)) {
                break;
            }
        }
        return iPlugin;
    }

    private static IPlugin returnPluginFrowmGroupMsg(Group group, BaseMsg msg,
                                                     MatchTypeEnum matchTypeEnum) {
        IPlugin iPlugin = null;
        if (StringUtil.ifNotNullOrEmpty(group.getGroupPlugin())) {// group是否有插件
            List<GroupActionPlugin> actionPluginsList = group.getGroupPlugin()
                    .getActionPluginArray(matchTypeEnum);
            if (StringUtil.ifNullOrEmpty(iPlugin)
                    && StringUtil.ifNotNullOrEmpty(actionPluginsList)) {// 插件里是否包含指定类型的actionplugin
                for (GroupActionPlugin actionPlugin : actionPluginsList) {// 遍历actionplugin
                    iPlugin = findMatchPlugin(group, msg, actionPlugin,
                            actionPlugin.getMatchRule());
                    if (StringUtil.ifNotNullOrEmpty(iPlugin)) {
                        break;
                    }
                }
            }
            GroupExercisePlugin exercisePlugin = group.getGroupPlugin()
                    .getExercisePlugin();
            if (StringUtil.ifNullOrEmpty(iPlugin)
                    && StringUtil.ifNotNullOrEmpty(exercisePlugin)) {// 插件里是否包含运动插件
                if (exercisePlugin.getWstepClockin() > 0 || exercisePlugin.getSportClockin() > 0 || exercisePlugin.getStepClockin() > 0) {
                    iPlugin = findMatchPlugin(group, msg, group.getGroupPlugin()
                            .getExercisePlugin(), MatchRuleEnum.GLOBAL_START);
                }
            }
        }
        return iPlugin;
    }

    public static boolean isMatchGlobalStartExpression(String actualContent,
                                                       String expectMatchWord, String robotDisplayName) {
        return actualContent.startsWith(expectMatchWord)
                || (actualContent.contains(expectMatchWord) && actualContent
                .contains(robotCall))
                || (actualContent.contains(expectMatchWord) && actualContent
                .contains(robotDisplayName));
    }

    public static boolean isMatchGlobalContainsExpression(String actualContent,
                                                          String expectMatchWord) {
        return actualContent.contains(expectMatchWord);
    }

    private static boolean isContentContainsRobotName(Group group, BaseMsg msg,
                                                      String content) {
        String robotDisplayName = getRobotDisplayName(group, msg);
        return content.contains(robotCall)
                || content.contains(robotDisplayName);
    }

    public static String getRobotDisplayName(Group group, BaseMsg msg) {
        String robotDisplayName = WechatTools
                .getMemberDisplayNameByGroupNickName(group.getGroupName(),
                        msg.getToUserName());
        if (StringUtil.ifNullOrEmpty(robotDisplayName)) {
            robotDisplayName = robotCall;
        }
        return robotDisplayName;
    }

    private static String unknownTextMsgReply(Group group, BaseMsg msg,
                                              String content) {
        String nickName = WechatTools
                .getMemberDisplayOrNickNameByGroupNickName(
                        group.getGroupName(), msg.getStatusNotifyUserName());
        String result = null;
        if (group.getGroupRobotConfig().getRobotChat()) {
            result = TulingRobot.chat(nickName, content);
        }
        if (group.getGroupRobotConfig().getUnknownReply()) {
            if (StringUtil.ifNullOrEmpty(result)) {
                result = "我听不懂，需要\"菜单\"请回复菜单";
            }
        }
        return result;
    }

    // //////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////
}
