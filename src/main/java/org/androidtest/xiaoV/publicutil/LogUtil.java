package org.androidtest.xiaoV.publicutil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 系统日志记录 TODO 待迭代改进，新需求：
 * 1，想加个LogUtil.MAIL.info或者LogUtil.APP.mailInfo(不仅发mail还有原来info功能)
 * 2，还有加个方法，会固定加前缀，我好知道这个LOG来自哪个taskid
 */
public class LogUtil {

	/**
	 * 记录业务日志，监控业务执行情况
	 */
	public static final Log MSG = LogFactory.getLog("msg");

	public static final Log SYS = LogFactory.getLog("sys");
}
