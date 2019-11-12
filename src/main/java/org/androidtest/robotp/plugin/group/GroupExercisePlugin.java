package org.androidtest.robotp.plugin.group;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import org.androidtest.robotp.beans.group.ExercisePluginConfig;
import org.androidtest.robotp.beans.group.Group;
import org.androidtest.robotp.helper.GroupListHelper;
import org.androidtest.robotp.helper.GroupMsgHelper;
import org.androidtest.robotp.plugin.IPlugin;
import org.androidtest.robotp.publicutils.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.androidtest.robotp.data.Constant.SIMPLE_DAY_FORMAT_FILE;

/**
 * GroupExercisePlugin，锻炼打卡形式插件
 * <p>
 * TODO 该类继承旧代码快速开发，需要重构
 *
 * @Author: Vince蔡培培
 */
public class GroupExercisePlugin implements Serializable, IPlugin {

    /**
     * 周报相关
     */
    private static final String MATCH_WORD_OF_WEEKLY_REPORT = "周报";
    private static final String MATCH_WORD_OF_LAST_WEEK_REPORT = "上周周报";
    /**
     * 步数👣打卡相关
     */
    private static final String MATCH_WORD_OF_STEP = "步数打卡";
    private static final String MATCH_WORD_OF_RECALL_STEP = "撤回步数打卡";
    private static final String FILE_SUFFIX_OF_STEP = ".step";
    /**
     * 周步数👣打卡相关
     */
    private static final String MATCH_WORD_OF_WSTEP = "周步数打卡";
    private static final String MATCH_WORD_OF_RECALL_WSTEP = "撤回周步数打卡";
    private static final String FILE_SUFFIX_OF_WSTEP = ".wstep";
    /**
     * 运动🏃打卡相关
     */
    private static final String MATCH_WORD_OF_SPORT = "运动打卡";
    private static final String MATCH_WORD_OF_RECALL_SPORT = "撤回运动打卡";
    private static final String FILE_SUFFIX_OF_SPORT = ".sport";

    private static final String LAST_WEEK_TITLE = "=== 上周数据重新报告 ===\n";

    private int stepClockin;
    private int sportClockin;
    private int wstepClockin;
    private ExercisePluginConfig exercisePluginConfig;

    /**
     * 输出路径相关
     */
    public static String getOutPutPath() {
        return FileOperatorUtil.mkdirs(System.getProperty("user.dir"))
                .getAbsolutePath();

    }

    public static String getDataSavePath() {
        return FileOperatorUtil.mkdirs(
                getOutPutPath() + File.separator + "data").getAbsolutePath();

    }

    public static String getLastWeekSavePath() {
        return FileOperatorUtil.mkdirs(
                getDataSavePath() + File.separator + WeekHelper.getLastWeek())
                .getAbsolutePath();

    }

    public static String getCurrentWeekSavePath() {
        return FileOperatorUtil.mkdirs(
                getDataSavePath() + File.separator
                        + WeekHelper.getCurrentWeek()).getAbsolutePath();

    }

    public int getStepClockin() {
        return stepClockin;
    }

    public void setStepClockin(int stepClockin) {
        this.stepClockin = stepClockin;
    }

    public int getSportClockin() {
        return sportClockin;
    }

    public void setSportClockin(int sportClockin) {
        this.sportClockin = sportClockin;
    }

    public int getWstepClockin() {
        return wstepClockin;
    }

    public void setWstepClockin(int wstepClockin) {
        this.wstepClockin = wstepClockin;
    }

    public ExercisePluginConfig getExercisePluginConfig() {
        return exercisePluginConfig;
    }

    public void setExercisePluginConfig(
            ExercisePluginConfig exercisePluginConfig) {
        this.exercisePluginConfig = exercisePluginConfig;
    }

    @Override
    public String toString() {
        return "ExercisePlugin{" + "stepClockin=" + stepClockin
                + ", sportClockin=" + sportClockin + ", wstepClockin="
                + wstepClockin + ", exercisePluginConfig="
                + exercisePluginConfig + '}';
    }

    @Override
    public List<String> getMatchWordsToList() {
        List<String> list = new ArrayList<>();
        if (getSportClockin() > 0) {
            list.add(MATCH_WORD_OF_SPORT);
            if (getExercisePluginConfig().getRecallSelfClockin()) {
                list.add(MATCH_WORD_OF_RECALL_SPORT);
            }
        }
        if (getWstepClockin() > 0) {
            list.add(MATCH_WORD_OF_WSTEP);
            if (getExercisePluginConfig().getRecallSelfClockin()) {
                list.add(MATCH_WORD_OF_RECALL_WSTEP);
            }
        }
        if (getStepClockin() > 0) {
            list.add(MATCH_WORD_OF_STEP);
            if (getExercisePluginConfig().getRecallSelfClockin()) {
                list.add(MATCH_WORD_OF_RECALL_STEP);
            }
        }
        list.add(MATCH_WORD_OF_WEEKLY_REPORT);
        list.add(MATCH_WORD_OF_LAST_WEEK_REPORT);
        return list;
    }

    private String returnSpecificKeywordFromTextMsg(Group group, BaseMsg msg) {
        LogUtil.MSG.debug("returnSpecificKeywordFromTextMsg: " + group + ", "
                + msg);
        List<String> currentGroupVaildKeyword = getMatchWordsToList();
        String content = msg.getText();
        String robotDisplayName = GroupMsgHelper
                .getRobotDisplayName(group, msg);
        if (StringUtil.ifNotNullOrEmpty(currentGroupVaildKeyword)) {
            for (int i = 0; i < currentGroupVaildKeyword.size(); i++) {
                if (GroupMsgHelper.isMatchGlobalStartExpression(content,
                        currentGroupVaildKeyword.get(i), robotDisplayName)) {
                    return currentGroupVaildKeyword.get(i);
                }
            }
        }
        LogUtil.MSG.debug("returnSpecificKeywordFromTextMsg: return " + null);
        return null;
    }

    @Override
    public boolean reply(Group group, BaseMsg msg) {
        boolean isReply = false;
        String result = null;
        String keyword = returnSpecificKeywordFromTextMsg(group, msg);
        switch (keyword) {
            case MATCH_WORD_OF_STEP:
                result = clockInStep(group, msg);
                break;
            case MATCH_WORD_OF_SPORT:
                result = clockInSport(group, msg);
                break;
            case MATCH_WORD_OF_WSTEP:
                result = clockInWStep(group, msg);
                break;
            case MATCH_WORD_OF_LAST_WEEK_REPORT:
                result = reportLastWeek(group, msg);
                break;
            case MATCH_WORD_OF_WEEKLY_REPORT:
                result = report(group);
                break;
            case MATCH_WORD_OF_RECALL_SPORT:
                result = recallSport(group, msg);
                break;
            case MATCH_WORD_OF_RECALL_STEP:
                result = recallStep(group, msg);
                break;
            case MATCH_WORD_OF_RECALL_WSTEP:
                break;
            default:
                break;
        }
        if (StringUtil.ifNotNullOrEmpty(result)) {
            MessageTools.sendGroupMsgByNickName(result, group.getGroupName());
            isReply = true;
        }
        if (keyword.equals(MATCH_WORD_OF_WEEKLY_REPORT)) {
            ThreadUtil.sleep(2000);
            result = processPK(group);
            MessageTools.sendGroupMsgByNickName(result, group.getGroupName());
        }
        return isReply;
    }

    private String reportLastWeek(Group g, BaseMsg msg) {
        LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
                + g.getGroupName());
        String result = null;
        String currentGroupNickName = g.getGroupName();
        File dir = new File(getLastWeekSavePath());
        List<String> list = WechatTools
                .getMemberListByGroupNickName2(currentGroupNickName);
        LogUtil.MSG.debug("action: " + currentGroupNickName + "群成员:"
                + list.toString());
        String error404Name = "";
        Group group = null;
        for (Group gr : GroupListHelper.getGroupList()) {
            if (gr.getGroupName().equals(currentGroupNickName)) {
                group = gr;
                break;
            }
        }
        if (group != null) {
            boolean supportSport = false;
            boolean supportStep = false;
            boolean supportWStep = false;
            if (group.getGroupPlugin().getExercisePlugin().getSportClockin() > 0) {
                supportSport = true;
            }
            if (group.getGroupPlugin().getExercisePlugin().getStepClockin() > 0) {
                supportStep = true;
            }
            if (group.getGroupPlugin().getExercisePlugin().getWstepClockin() > 0) {
                supportWStep = true;
            }
            if (supportSport == true || supportStep == true
                    || supportWStep == true) {
                if (dir.isDirectory()) {
                    File[] array = dir.listFiles();
                    for (int i = 0; i < list.size(); i++) {
                        int countstep = 0;
                        int countsport = 0;
                        int countwstep = 0;
                        String name = StringUtil.filter(list.get(i));
                        if (StringUtil.ifNullOrEmpty(name)) {
                            error404Name = error404Name + "@" + list.get(i)
                                    + " ";
                            continue;
                        }
                        for (int j = 0; j < array.length; j++) {

                            if ((array[j].isFile()
                                    && array[j].getName().endsWith(
                                    FILE_SUFFIX_OF_STEP) && array[j]
                                    .getName()
                                    .contains("-" + list.get(i) + "."))
                                    || (array[j].isFile()
                                    && array[j].getName().endsWith(
                                    FILE_SUFFIX_OF_STEP) && array[j]
                                    .getName().contains(
                                            "-" + name + "."))) {
                                countstep++;
                            }
                            if ((array[j].isFile()
                                    && array[j].getName().endsWith(
                                    FILE_SUFFIX_OF_SPORT) && array[j]
                                    .getName()
                                    .contains("-" + list.get(i) + "."))
                                    || (array[j].isFile()
                                    && array[j].getName().endsWith(
                                    FILE_SUFFIX_OF_SPORT) && array[j]
                                    .getName().contains(
                                            "-" + name + "."))) {
                                countsport++;
                            }

                            if ((array[j].isFile()
                                    && array[j].getName().endsWith(
                                    FILE_SUFFIX_OF_WSTEP) && array[j]
                                    .getName()
                                    .contains("-" + list.get(i) + "."))
                                    || (array[j].isFile()
                                    && array[j].getName().endsWith(
                                    FILE_SUFFIX_OF_WSTEP) && array[j]
                                    .getName().contains(
                                            "-" + name + "."))) {
                                countwstep++;
                            }
                        }

                        if (result == null) {
                            result = LAST_WEEK_TITLE;
                            result = result + WeekHelper.getLastWeek() + "\n";
                            result = result + list.get(i) + ":";

                            if (supportSport) {
                                result = result + "	🏃" + countsport + "/"
                                        + getSportClockin();
                            }
                            if (supportStep) {
                                result = result + "	\uD83D\uDC63" + countstep
                                        + "/" + getStepClockin();
                            }
                            if (supportWStep) {
                                result = result + "	周\uD83D\uDC63" + countwstep
                                        + "/" + getWstepClockin();
                            }
                            result = result + "；\n";
                        } else {
                            result = result + list.get(i) + ":";

                            if (supportSport) {
                                result = result + "	🏃" + countsport + "/"
                                        + getSportClockin();
                            }
                            if (supportStep) {
                                result = result + "	\uD83D\uDC63" + countstep
                                        + "/" + getStepClockin();
                            }
                            if (supportWStep) {
                                result = result + "	周\uD83D\uDC63" + countwstep
                                        + "/" + getWstepClockin();
                            }
                            result = result + "；\n";
                        }
                    }
                    if (!StringUtil.ifNullOrEmpty(error404Name)) {
                        result = result + "\n如下（微信名含非法字符无法统计: " + error404Name
                                + "）";
                    }
                } else {
                    LogUtil.MSG.error("action: " + "error:"
                            + dir.getAbsolutePath() + "非文件夹路径！");
                }
            }
        }

        return result;
    }

    private String recallStep(Group g, BaseMsg msg) {
        LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
                + g.getGroupName());
        String result = null;
        String currentGroupNickName = g.getGroupName();
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

        if (g.getGroupPlugin().getExercisePlugin().getStepClockin() > 0) {
            if (g.getGroupPlugin().getExercisePlugin()
                    .getExercisePluginConfig().getRecallSelfClockin()) {
                String stepfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date())
                        + "-" + fileUserName + FILE_SUFFIX_OF_STEP;
                File stepfile = new File(getCurrentWeekSavePath()
                        + File.separator + stepfilename);
                if (stepfile.exists()) {
                    stepfile.delete();
                    result = "@" + senderNickName + " 操作成功，你当天的步数👣打卡数据已经删除。";
                } else {
                    File dir = new File(getCurrentWeekSavePath());
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
                                    && array[i].getName().endsWith(
                                    FILE_SUFFIX_OF_STEP)
                                    && array[i].getName().contains(
                                    "-" + fileUserName + ".")) {
                                array[i].delete();
                                isdelete = true;
                                result = "@" + senderNickName
                                        + " 操作成功，你本周最近一次的步数👣打卡数据已经删除。";
                                break;
                            }
                        }
                        if (!isdelete) {
                            result = "@" + senderNickName
                                    + " 操作失败，你本周没有任何步数👣打卡数据，故无法删除。";
                        }
                    }
                }
            }
        } else {
            result = "@"
                    + senderNickName
                    + " 本群不支持步数👣打卡功能，故无法撤回打卡数据。请联系管理员添加步数👣打卡的组件，或者移除撤回步数👣打卡的组件。";
        }
        return result;
    }

    private String recallSport(Group g, BaseMsg msg) {
        LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
                + g.getGroupName());
        String result = null;
        String currentGroupNickName = g.getGroupName();
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

        if (g.getGroupPlugin().getExercisePlugin().getSportClockin() > 0) {
            if (g.getGroupPlugin().getExercisePlugin()
                    .getExercisePluginConfig().getRecallSelfClockin()) {
                String sportfilename = SIMPLE_DAY_FORMAT_FILE
                        .format(new Date())
                        + "-"
                        + fileUserName
                        + FILE_SUFFIX_OF_SPORT;
                File sportfile = new File(getCurrentWeekSavePath()
                        + File.separator + sportfilename);
                if (sportfile.exists()) {
                    sportfile.delete();
                    result = "@" + senderNickName + " 操作成功，你当天的运动🏃打卡数据已经删除。";
                } else {
                    File dir = new File(getCurrentWeekSavePath());
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
                                    && array[i].getName().endsWith(
                                    FILE_SUFFIX_OF_SPORT)
                                    && array[i].getName().contains(
                                    "-" + fileUserName + ".")) {
                                array[i].delete();
                                isdelete = true;
                                result = "@" + senderNickName
                                        + " 操作成功，你本周最近一次的运动🏃打卡数据已经删除。";
                                break;
                            }
                        }
                        if (!isdelete) {
                            result = "@" + senderNickName
                                    + " 操作失败，你本周没有任何运动🏃打卡数据，故无法删除。";
                        }
                    }
                }
            }
        } else {
            result = "@"
                    + senderNickName
                    + " 本群不支持运动🏃打卡功能，故无法撤回打卡数据。请联系管理员添加运动🏃打卡的组件，或者移除撤回运动🏃打卡的组件。";
        }
        return result;
    }

    /**
     * TODO 旧代码，需要解耦
     *
     * @param g
     * @return
     */
    private String report(Group g) {
        LogUtil.MSG.debug("action: " + this.getClass().getSimpleName() + ", "
                + g.getGroupName());
        String result = null;
        String currentGroupNickName = g.getGroupName();
        File dir = new File(getCurrentWeekSavePath());
        List<String> list = WechatTools
                .getMemberListByGroupNickName2(currentGroupNickName);
        LogUtil.MSG.debug("action: " + currentGroupNickName + "群成员:"
                + list.toString());
        String error404Name = "";
        boolean supportSport = false;
        boolean supportStep = false;
        boolean supportWStep = false;
        double expectTotal = 0;// 预期一周下来总的运动数量
        double actualProcess = 0;// 实际当前运动进度
        if (g.getGroupPlugin().getExercisePlugin().getSportClockin() > 0) {
            supportSport = true;
            expectTotal = expectTotal + list.size()
                    * g.getGroupPlugin().getExercisePlugin().getSportClockin();
        }
        if (g.getGroupPlugin().getExercisePlugin().getStepClockin() > 0) {
            supportStep = true;
            expectTotal = expectTotal + list.size()
                    * g.getGroupPlugin().getExercisePlugin().getStepClockin();
        }
        if (g.getGroupPlugin().getExercisePlugin().getWstepClockin() > 0) {
            supportWStep = true;
            expectTotal = expectTotal + list.size()
                    * g.getGroupPlugin().getExercisePlugin().getWstepClockin();
        }
        if (supportSport == true || supportStep == true || supportWStep == true) {
            if (dir.isDirectory()) {
                File[] array = dir.listFiles();
                for (int i = 0; i < list.size(); i++) {
                    int countstep = 0;
                    int countsport = 0;
                    int countwstep = 0;
                    String name = StringUtil.filter(list.get(i));
                    if (StringUtil.ifNullOrEmpty(name)) {
                        error404Name = error404Name + "@" + list.get(i) + " ";
                        continue;
                    }
                    for (int j = 0; j < array.length; j++) {

                        if ((array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_STEP) && array[j]
                                .getName().contains("-" + list.get(i) + "."))
                                || (array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_STEP) && array[j]
                                .getName().contains("-" + name + "."))) {
                            countstep++;
                            actualProcess++;
                        }
                        if ((array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_SPORT) && array[j]
                                .getName().contains("-" + list.get(i) + "."))
                                || (array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_SPORT) && array[j]
                                .getName().contains("-" + name + "."))) {
                            countsport++;
                            actualProcess++;
                        }

                        if ((array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_WSTEP) && array[j]
                                .getName().contains("-" + list.get(i) + "."))
                                || (array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_WSTEP) && array[j]
                                .getName().contains("-" + name + "."))) {
                            countwstep++;
                            actualProcess++;
                        }
                    }
                    if (result == null) {
                        result = WeekHelper.getCurrentWeek() + "\n";
                        result = result + list.get(i) + ":";
                        if (supportSport) {
                            result = result + "	🏃" + countsport + "/"
                                    + getSportClockin();
                        }
                        if (supportStep) {
                            result = result + "	\uD83D\uDC63" + countstep + "/"
                                    + getStepClockin();
                        }
                        if (supportWStep) {
                            result = result + "	周\uD83D\uDC63" + countwstep
                                    + "/" + getWstepClockin();
                        }
                        result = result + "；\n";
                    } else {
                        result = result + list.get(i) + ":";

                        if (supportSport) {
                            result = result + "	🏃" + countsport + "/"
                                    + getSportClockin();
                        }
                        if (supportStep) {
                            result = result + "	\uD83D\uDC63" + countstep + "/"
                                    + getStepClockin();
                        }
                        if (supportWStep) {
                            result = result + "	周\uD83D\uDC63" + countwstep
                                    + "/" + getWstepClockin();
                        }
                        result = result + "；\n";
                    }
                }
                if (!StringUtil.ifNullOrEmpty(error404Name)) {
                    result = result + "\n如下（微信名含非法字符无法统计: " + error404Name
                            + "）";
                }

                String content = "";
                if (supportStep) {
                    content = content + "\n" + reportStep(g);
                }
                if (supportSport) {
                    content = content + "\n" + reportSport(g);
                }
                if (supportWStep) {
                    content = content + "\n" + reportWStep(g);
                }
                if (StringUtil.ifNotNullOrEmpty(content)) {
                    result = result + content;
                    String process = "------------------------------\n"
                            + "🔔本群本周锻炼进度："
                            + actualProcess
                            + "/"
                            + expectTotal
                            + "，"
                            + String.format("%.2f",
                            (actualProcess / expectTotal) * 100) + "%。";
                    result = result + "\n" + process;
                }
            } else {
                LogUtil.MSG.error("action: " + "error:" + dir.getAbsolutePath()
                        + "非文件夹路径！");
            }
        }
        return result;
    }

    /**
     * TODO 旧代码，需要解耦
     *
     * @param currentGroup
     * @return
     */
    public boolean notifyReport(Group currentGroup) {
        String currentGroupNickName = currentGroup.getGroupName();
        String currentTime = new SimpleDateFormat("yyyy/MM/dd HH:MM")
                .format(new Date());
        MessageTools.sendGroupMsgByNickName(currentTime + "，开始进行本周播报🔔。",
                currentGroupNickName);
        ThreadUtil.sleep(2000);
        String result = report(currentGroup);
        boolean isReply = MessageTools.sendGroupMsgByNickName(result,
                currentGroupNickName);
        result = processPK(currentGroup);
        MessageTools.sendGroupMsgByNickName(result, currentGroupNickName);
        LogUtil.MSG.info("notify: " + currentGroup.getGroupName()
                + ": isReply " + isReply);
        return isReply;
    }

    private String clockInWStep(Group group, BaseMsg msg) {
        LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());
        String result = null;

        String currentGroupNickName = WechatTools
                .getGroupNickNameByGroupUserName(msg.getFromUserName());
        String senderNickName = WechatTools
                .getMemberDisplayOrNickNameByGroupNickName(
                        currentGroupNickName, msg.getStatusNotifyUserName());

        String fileUserName = StringUtil.filter(senderNickName);
        if (StringUtil.ifNullOrEmpty(fileUserName)) {
            result = "@" + senderNickName + " 你的名字有无法识别的字符，无法处理！请改昵称。";
            LogUtil.MSG.warn("action: " + result);
            return result;
        }
        if (!DateUtil.isSunday()) {
            result = "@"
                    + senderNickName
                    + " 周步数👣打卡请在周天0:00-24:00之间打卡。符合群规达标才打卡，不符群规不达标不打卡。今天非周天，打卡失败。";
            LogUtil.MSG.warn("action: " + result);
            return result;
        }
        String wstepfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
                + fileUserName + FILE_SUFFIX_OF_WSTEP;
        File wstepfile = new File(getCurrentWeekSavePath() + File.separator
                + wstepfilename);
        try {
            boolean isExist = false;
            if (wstepfile.exists()) {
                isExist = true;
            } else {
                wstepfile.createNewFile();
            }
            if (isExist) {
                result = "@" + senderNickName + " 你本周已经周步数👣打卡过，无需重复打卡。"
                        + WeekHelper.getCurrentWeek() + "周步数👣打卡已完成，再接再励！";
            } else {
                result = "@" + senderNickName + " 本周步数👣打卡成功！"
                        + WeekHelper.getCurrentWeek() + "步数👣打卡已完成，继续保持。";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean notifyWStep(Group currentGroup) {
        if (DateUtil.isSunday()) {
            Date currentDate = new Date();
            int currentTime = Integer.parseInt(new SimpleDateFormat("HHmm")
                    .format(currentDate));
            String currentTimeString = new SimpleDateFormat("HH:mm")
                    .format(currentDate);
            List<Integer> autoReportTime = new ArrayList<>();
            autoReportTime.add(1000);
            autoReportTime.add(1500);
            autoReportTime.add(2100);
            for (Integer time : autoReportTime) {
                if (currentTime == time) {
                    String currentGroupNickName = currentGroup.getGroupName();
                    MessageTools
                            .sendGroupMsgByNickName(
                                    "今天需要进行本周步数👣打卡，符合群规达标才打卡，不符群规不达标不打卡。还没打卡的小伙伴记得及时打卡噢！",
                                    currentGroupNickName);
                    LogUtil.MSG.info("notify: " + currentGroup.getGroupName()
                            + ": report " + true);
                    return true;
                }
            }
            int startTime = 0;
            int endTime = 2330;
            if (currentTime == startTime) {
                String currentGroupNickName = currentGroup.getGroupName();
                MessageTools
                        .sendGroupMsgByNickName(
                                WeekHelper.getCurrentWeek()
                                        + "周步数👣打卡功能已经开启！今天需要进行本周步数👣打卡，还没打卡的小伙伴记得及时打卡噢！(符合群规达标才打卡，不符群规不达标不打卡)",
                                currentGroupNickName);
                LogUtil.MSG.info("notify: " + currentGroup.getGroupName()
                        + ": report " + true);
                return true;
            } else if (currentTime == endTime) {
                String currentGroupNickName = currentGroup.getGroupName();
                MessageTools
                        .sendGroupMsgByNickName(
                                WeekHelper.getCurrentWeek()
                                        + "周步数👣打卡功能将在0点关闭！还没打卡的小伙伴赶紧及时打卡噢！(符合群规达标才打卡，不符群规不达标不打卡)",
                                currentGroupNickName);
                LogUtil.MSG.info("notify: " + currentGroup.getGroupName()
                        + ": report " + true);
                return true;
            }
        }
        return false;
    }

    private String reportWStep(Group group) {
        String result = null;
        if (DateUtil.isSunday()) {
            LogUtil.MSG.debug("report: " + this.getClass().getSimpleName()
                    + ", " + group.getGroupName());
            String currentGroupNickName = group.getGroupName();

            File dir = new File(getCurrentWeekSavePath());
            List<String> list = WechatTools
                    .getMemberListByGroupNickName2(currentGroupNickName);
            LogUtil.MSG.debug("report: " + currentGroupNickName + "群成员:"
                    + list.toString());
            String errorSport = null;
            String error404Name = "";
            if (dir.isDirectory()) {
                File[] array = dir.listFiles();
                for (int i = 0; i < list.size(); i++) {
                    int countwstep = 0;
                    String name = StringUtil.filter(list.get(i));
                    if (StringUtil.ifNullOrEmpty(name)) {
                        error404Name = error404Name + "@" + list.get(i) + " ";
                        continue;
                    }
                    for (int j = 0; j < array.length; j++) {
                        if ((array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_WSTEP) && array[j]
                                .getName().contains("-" + list.get(i) + "."))
                                || (array[j].isFile()
                                && array[j].getName().endsWith(
                                FILE_SUFFIX_OF_WSTEP) && array[j]
                                .getName().contains("-" + name + "."))) {
                            countwstep++;
                        }
                    }
                    if (countwstep < getWstepClockin()) {
                        if (errorSport == null) {
                            errorSport = "@" + list.get(i) + " ";
                        } else {
                            errorSport = errorSport + "@" + list.get(i) + " ";
                        }
                    }
                }
                if (errorSport == null) {
                    errorSport = "无";
                }
                result = "------------------------------\n本周\uD83D\uDC63未达标：\n"
                        + errorSport;
                if (!StringUtil.ifNullOrEmpty(error404Name)) {
                    result = result + "\n如下（微信名含非法字符无法统计: " + error404Name
                            + "）";
                }
            } else {
                LogUtil.MSG.error("report: " + "error:" + dir.getAbsolutePath()
                        + "非文件夹路径！");
            }
        }
        return result;
    }

    /**
     * TODO 旧代码，需要解耦
     *
     * @param group
     * @param msg
     * @return
     */
    private String clockInStep(Group group, BaseMsg msg) {
        LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());

        String currentGroupNickName = WechatTools
                .getGroupNickNameByGroupUserName(msg.getFromUserName());
        String senderNickName = WechatTools
                .getMemberDisplayOrNickNameByGroupNickName(
                        currentGroupNickName, msg.getStatusNotifyUserName());
        String result = null;
        String fileUserName = StringUtil.filter(senderNickName);
        if (StringUtil.ifNullOrEmpty(fileUserName)) {
            result = "@" + senderNickName + " 你的名字有无法识别的字符，无法处理！请改昵称。";
            LogUtil.MSG.warn("action: " + result);
            return result;
        }

        String stepfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
                + fileUserName + FILE_SUFFIX_OF_STEP;
        File stepfile = new File(getCurrentWeekSavePath() + File.separator
                + stepfilename);
        String sportfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
                + fileUserName + FILE_SUFFIX_OF_SPORT;
        File sportfile = new File(getCurrentWeekSavePath() + File.separator
                + sportfilename);
        try {
            boolean isExist = false;
            boolean isDiffExist = false;
            if (stepfile.exists()) {
                isExist = true;
            } else if (getExercisePluginConfig().getClockinOnceADay()
                    && sportfile.exists()) {
                isDiffExist = true;
            } else {
                stepfile.createNewFile();
            }
            File dir = new File(getCurrentWeekSavePath());
            if (dir.isDirectory()) {
                File[] array = dir.listFiles();
                int count = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i].isFile()
                            && array[i].getName().endsWith(FILE_SUFFIX_OF_STEP)
                            && array[i].getName().contains(
                            "-" + fileUserName + ".")) {
                        count++;
                    }
                }
                if (isExist) {
                    result = "@" + senderNickName + " 你今天已经步数👣打卡过，无需重复打卡。"
                            + WeekHelper.getCurrentWeek() + "步数👣打卡已完成了"
                            + count + "次，再接再励！";
                } else if (getExercisePluginConfig().getClockinOnceADay()
                        && isDiffExist) {
                    result = "@" + senderNickName + " 你今天已经运动🏃打卡过，不能步数👣打卡。";
                } else {
                    result = "@" + senderNickName + " 今天步数👣打卡成功！"
                            + WeekHelper.getCurrentWeek() + "步数👣打卡已完成了"
                            + count + "次，继续保持。";
                }

            } else {
                LogUtil.MSG.error("action: " + "error:" + dir.getAbsolutePath()
                        + "非文件夹路径！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * TODO 旧代码，需要解耦
     *
     * @param group
     * @return
     */
    private String reportStep(Group group) {
        LogUtil.MSG.debug("report: " + this.getClass().getSimpleName() + ", "
                + group.getGroupName());
        String currentGroupNickName = group.getGroupName();
        String result = null;
        File dir = new File(getCurrentWeekSavePath());
        List<String> list = WechatTools
                .getMemberListByGroupNickName2(currentGroupNickName);
        LogUtil.MSG.debug("report: " + currentGroupNickName + "群成员:"
                + list.toString());
        String errorStep = null;
        String error404Name = "";
        String todaystepkeyword = SIMPLE_DAY_FORMAT_FILE.format(new Date());
        boolean isCurrentUserExistTodayStep = false;
        if (dir.isDirectory()) {
            File[] array = dir.listFiles();
            for (int i = 0; i < list.size(); i++) {
                isCurrentUserExistTodayStep = false;
                String name = StringUtil.filter(list.get(i));
                if (StringUtil.ifNullOrEmpty(name)) {
                    error404Name = error404Name + "@" + list.get(i) + " ";
                    continue;
                }
                int count = 0;
                for (int j = 0; j < array.length; j++) {

                    if ((array[j].isFile()
                            && array[j].getName().endsWith(FILE_SUFFIX_OF_STEP) && array[j]
                            .getName().contains("-" + list.get(i) + "."))
                            || (array[j].isFile()
                            && array[j].getName().endsWith(
                            FILE_SUFFIX_OF_STEP) && array[j]
                            .getName().contains("-" + name + "."))) {
                        if (getStepClockin() >= 7) {
                            if (array[j].getName().contains(todaystepkeyword)) {
                                isCurrentUserExistTodayStep = true;
                                break;
                            }
                        } else {
                            count++;
                        }
                    }

                }
                if (getStepClockin() >= 7) {
                    if (!isCurrentUserExistTodayStep) {
                        if (errorStep == null) {
                            errorStep = "@" + list.get(i) + " ";
                        } else {
                            errorStep = errorStep + "@" + list.get(i) + " ";
                        }
                    }
                } else {
                    if (count < getStepClockin()) {
                        if (errorStep == null) {
                            errorStep = "@" + list.get(i) + " ";
                        } else {
                            errorStep = errorStep + "@" + list.get(i) + " ";
                        }
                    }

                }
            }
            if (errorStep == null) {
                errorStep = "无";
            }
            if (getStepClockin() >= 7) {
                result = "------------------------------\n今日步数👣未完成：\n"
                        + errorStep;
            } else {
                result = "------------------------------\n本周步数👣未完成：\n"
                        + errorStep;
            }
            if (!StringUtil.ifNullOrEmpty(error404Name)) {
                result = result + "\n如下（微信名含非法字符无法统计: " + error404Name + "）";
            }
        } else {
            LogUtil.MSG.error("report: " + "reportProcess: " + "error:"
                    + dir.getAbsolutePath() + "非文件夹路径！");
        }
        return result;
    }

    /**
     * TODO 旧代码，需要解耦
     *
     * @param group
     * @param msg
     * @return
     */
    private String clockInSport(Group group, BaseMsg msg) {
        LogUtil.MSG.debug("action: " + this.getClass().getSimpleName());
        String result = null;
        String currentGroupNickName = WechatTools
                .getGroupNickNameByGroupUserName(msg.getFromUserName());
        String senderNickName = WechatTools
                .getMemberDisplayOrNickNameByGroupNickName(
                        currentGroupNickName, msg.getStatusNotifyUserName());
        LogUtil.MSG.info("action: senderNickName: " + senderNickName);
        String fileUserName = StringUtil.filter(senderNickName);
        LogUtil.MSG.info("action: fileUserName: " + fileUserName);
        int week_sport_limit_times = getSportClockin();
        if (StringUtil.ifNullOrEmpty(fileUserName)) {
            return "@" + senderNickName + " 你的名字有无法识别的字符，无法处理！请改昵称。";
        }
        String sportfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
                + fileUserName + FILE_SUFFIX_OF_SPORT;
        String stepfilename = SIMPLE_DAY_FORMAT_FILE.format(new Date()) + "-"
                + fileUserName + FILE_SUFFIX_OF_STEP;
        File sportfile = new File(getCurrentWeekSavePath() + File.separator
                + sportfilename);
        File stepfile = new File(getCurrentWeekSavePath() + File.separator
                + stepfilename);
        try {
            boolean isExist = false;
            boolean isDiffExist = false;
            if (sportfile.exists()) {
                isExist = true;
            } else if (getExercisePluginConfig().getClockinOnceADay()
                    && stepfile.exists()) {
                isDiffExist = true;
            } else {
                sportfile.createNewFile();
            }
            File dir = new File(getCurrentWeekSavePath());
            if (dir.isDirectory()) {
                File[] array = dir.listFiles();
                int count = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i].isFile()
                            && array[i].getName()
                            .endsWith(FILE_SUFFIX_OF_SPORT)
                            && array[i].getName().contains(
                            "-" + fileUserName + ".")) {
                        count++;
                    }
                }
                if (isExist) {
                    if (week_sport_limit_times > count) {
                        result = "@" + senderNickName + " 你今天已经运动🏃打卡过，无需重复打卡。"
                                + WeekHelper.getCurrentWeek() + "打卡运动🏃第"
                                + count + "次，本周还差"
                                + (week_sport_limit_times - count) + "次";
                    } else {
                        result = "@" + senderNickName + " 你今天已经运动🏃打卡过，无需重复打卡。"
                                + WeekHelper.getCurrentWeek() + "打卡运动🏃第"
                                + count + "次，本周已经达标。已经运动🏃了" + count + "次";
                    }
                } else if (getExercisePluginConfig().getClockinOnceADay()
                        && isDiffExist) {
                    result = "@" + senderNickName + " 你今天已经步数👣打卡过，不能运行打卡。";

                } else {
                    if (week_sport_limit_times > count) {
                        result = "@" + senderNickName + " 于"
                                + WeekHelper.getCurrentWeek() + "打卡运动🏃第"
                                + count + "次，本周还差"
                                + (week_sport_limit_times - count) + "次";
                    } else {
                        result = "@" + senderNickName + " 于"
                                + WeekHelper.getCurrentWeek() + "打卡运动🏃第"
                                + count + "次，本周已经达标。已经运动🏃了" + count + "次";
                    }
                }

            } else {
                LogUtil.MSG.error("action: " + "error:" + dir.getAbsolutePath()
                        + "非文件夹路径！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * TODO 旧代码，需要解耦
     *
     * @param group
     * @return
     */
    private String reportSport(Group group) {
        LogUtil.MSG.debug("report: " + this.getClass().getSimpleName() + ", "
                + group.getGroupName());
        String currentGroupNickName = group.getGroupName();
        String result = null;
        File dir = new File(getCurrentWeekSavePath());
        List<String> list = WechatTools
                .getMemberListByGroupNickName2(currentGroupNickName);
        LogUtil.MSG.debug("report: " + currentGroupNickName + "群成员:"
                + list.toString());
        String errorSport = null;
        String error404Name = "";
        if (dir.isDirectory()) {
            File[] array = dir.listFiles();
            for (int i = 0; i < list.size(); i++) {
                int countsport = 0;
                String name = StringUtil.filter(list.get(i));
                if (StringUtil.ifNullOrEmpty(name)) {
                    error404Name = error404Name + "@" + list.get(i) + " ";
                    continue;
                }
                for (int j = 0; j < array.length; j++) {
                    if ((array[j].isFile()
                            && array[j].getName()
                            .endsWith(FILE_SUFFIX_OF_SPORT) && array[j]
                            .getName().contains("-" + list.get(i) + "."))
                            || (array[j].isFile()
                            && array[j].getName().endsWith(
                            FILE_SUFFIX_OF_SPORT) && array[j]
                            .getName().contains("-" + name + "."))) {
                        countsport++;
                    }
                }
                if (countsport < getSportClockin()) {
                    if (errorSport == null) {
                        errorSport = "@" + list.get(i) + " ";
                    } else {
                        errorSport = errorSport + "@" + list.get(i) + " ";
                    }
                }
            }
            if (errorSport == null) {
                errorSport = "无";
            }
            result = "------------------------------\n本周运动🏃未达标：\n"
                    + errorSport;
            if (!StringUtil.ifNullOrEmpty(error404Name)) {
                result = result + "\n如下（微信名含非法字符无法统计: " + error404Name + "）";
            }
        } else {
            LogUtil.MSG.error("report: " + "error:" + dir.getAbsolutePath()
                    + "非文件夹路径！");
        }
        return result;
    }

    private static String processPK(Group group) {
        String result = null;
        List<Group> groupList = GroupListHelper.getGroupList();
        File dir = new File(getCurrentWeekSavePath());
        Map<String, Float> pklist = new TreeMap<>();
        if (dir.isDirectory()) {
            // int exerciseGroupTotalNum = 0;// 一共有多少个群组有锻炼插件功能
            if (StringUtil.ifNotNullOrEmpty(groupList)) {
                for (Group g : groupList) {
                    if (StringUtil.ifNotNullOrEmpty(g.getGroupPlugin())) {
                        if (StringUtil.ifNotNullOrEmpty(g.getGroupPlugin()
                                .getExercisePlugin())) {
                            // exerciseGroupTotalNum++;
                            List<String> members = WechatTools
                                    .getMemberListByGroupNickName2(g
                                            .getGroupName());
                            GroupExercisePlugin exercisePlugin = g
                                    .getGroupPlugin().getExercisePlugin();
                            double weekTotalExerciseTimes = 0;// 本周预期应有的锻炼次数
                            int membersSize = members.size();// 减去机器人自己
                            if (exercisePlugin.getStepClockin() > 0) {
                                weekTotalExerciseTimes = weekTotalExerciseTimes
                                        + exercisePlugin.getStepClockin()
                                        * membersSize;
                            }
                            if (exercisePlugin.getSportClockin() > 0) {
                                weekTotalExerciseTimes = weekTotalExerciseTimes
                                        + exercisePlugin.getSportClockin()
                                        * membersSize;
                            }
                            if (exercisePlugin.getWstepClockin() > 0) {
                                weekTotalExerciseTimes = weekTotalExerciseTimes
                                        + exercisePlugin.getWstepClockin()
                                        * membersSize;
                            }
                            if (weekTotalExerciseTimes < 1) {// 如果应有锻炼次数小于1，就跳过该群
                                continue;
                            }
                            double currentExerciseTimes = 0;
                            File[] array = dir.listFiles();
                            for (int i = 0; i < members.size(); i++) {
                                String name = StringUtil.filter(members.get(i));
                                if (StringUtil.ifNullOrEmpty(name)) {
                                    continue;
                                }
                                for (int j = 0; j < array.length; j++) {
                                    if ((array[j].isFile()
                                            && array[j].getName().endsWith(
                                            FILE_SUFFIX_OF_STEP) && array[j]
                                            .getName().contains(
                                                    "-" + members.get(i) + "."))
                                            || (array[j].isFile()
                                            && array[j]
                                            .getName()
                                            .endsWith(
                                                    FILE_SUFFIX_OF_STEP) && array[j]
                                            .getName().contains(
                                                    "-" + name + "."))) {
                                        currentExerciseTimes++;
                                    }
                                    if ((array[j].isFile()
                                            && array[j].getName().endsWith(
                                            FILE_SUFFIX_OF_SPORT) && array[j]
                                            .getName().contains(
                                                    "-" + members.get(i) + "."))
                                            || (array[j].isFile()
                                            && array[j]
                                            .getName()
                                            .endsWith(
                                                    FILE_SUFFIX_OF_SPORT) && array[j]
                                            .getName().contains(
                                                    "-" + name + "."))) {
                                        currentExerciseTimes++;
                                    }

                                    if ((array[j].isFile()
                                            && array[j].getName().endsWith(
                                            FILE_SUFFIX_OF_WSTEP) && array[j]
                                            .getName().contains(
                                                    "-" + members.get(i) + "."))
                                            || (array[j].isFile()
                                            && array[j]
                                            .getName()
                                            .endsWith(
                                                    FILE_SUFFIX_OF_WSTEP) && array[j]
                                            .getName().contains(
                                                    "-" + name + "."))) {
                                        currentExerciseTimes++;
                                    }
                                }
                            }
                            float process = Float
                                    .parseFloat(String
                                            .format("%.2f",
                                                    (currentExerciseTimes / weekTotalExerciseTimes) * 100));
                            pklist.put(g.getGroupName(), process);
                        }
                    }
                }

                String firstName = null;
                float firstprocess = 0;
                String lastName = null;
                float lastprocess = 0;
                pklist = ListUtil.sortByValueAscending(pklist);// 升序排序
                for (Map.Entry<String, Float> entry : pklist.entrySet()) {
                    lastName = entry.getKey();
                    lastprocess = entry.getValue();
                    break;
                }
                pklist = ListUtil.sortByValueDescending(pklist);// 降序排序
                for (Map.Entry<String, Float> entry : pklist.entrySet()) {
                    firstName = entry.getKey();
                    firstprocess = entry.getValue();
                    break;
                }
                int count = 0;
                for (Map.Entry<String, Float> entry : pklist.entrySet()) {
                    count++;
                    if (entry.getKey().equals(group.getGroupName())) {
                        break;
                    }
                }
                if (lastprocess != firstprocess
                        && StringUtil.ifNotNullOrEmpty(lastName)
                        && StringUtil.ifNotNullOrEmpty(firstName)) {
                    result = "🆚多群实时运动进度PK榜\n"
                            + WeekHelper.getCurrentWeek()
                            + "\n"
                            + "------------------------------\n"
                            + "🚀霸榜王者：\n"
                            + firstName
                            + "\n\n"
                            + "进度"
                            + firstprocess
                            + "%，人数"
                            + WechatTools.getMemberListByGroupNickName2(
                            firstName).size()
                            + "人\n"
                            + "------------------------------\n"
                            + "🚲倔强青铜：\n"
                            + lastName
                            + "\n\n"
                            + "进度"
                            + lastprocess
                            + "%，人数"
                            + WechatTools.getMemberListByGroupNickName2(
                            lastName).size() + "人" + "\n"
                            + "------------------------------\n"
                            + "🔔本群当前群进度排名: 第" + count + "名";
                }
            }
        } else {
            LogUtil.MSG.error("action: " + "error:" + dir.getAbsolutePath()
                    + "非文件夹路径！");
        }
        return result;
    }
}