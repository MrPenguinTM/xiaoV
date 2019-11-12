package org.androidtest.robotp.plugin;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import org.androidtest.robotp.beans.group.Group;

import java.util.List;

/**
 * 插件类
 *
 * @Author: Vince蔡培培
 */
public interface IPlugin {
	/**
	 * 获取该插件的所有关键字
	 * <p>
	 * 返回值为该插件的所有关键字
	 *
	 * @return
	 */
	List<String> getMatchWordsToList();

	/**
	 * 该接口用来对将回复的消息进行加工并回复
	 * <p>
	 * 返回值用来标识本次执行是否有回复消息
	 *
	 * @param group
	 * @param msg
	 * @return
	 */
	boolean reply(Group group, BaseMsg msg);
}
