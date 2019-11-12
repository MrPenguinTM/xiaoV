package org.androidtest.robotp.publicutils;

/**
 * thread处理工具类
 *
 * @author Vince蔡培培
 */
public class ThreadUtil {

	/**
	 * showThreadInfo
	 *
	 * @return
	 */
	public static final String showThreadInfo() {

		return "^^^^^^^^^^^^^^^^^^^^^^^ currentThread: name="
				+ Thread.currentThread().getName() + "  , id="
				+ Thread.currentThread().getId() +

				"   ,state=" + Thread.currentThread().getState()
				+ "   , Priority=" + Thread.currentThread().getPriority();
	}

	/**
	 * sleep not catch Exception
	 *
	 * @param ms
	 * @return
	 */
	public static boolean sleep(int ms) {
		try {
			LogUtil.MSG.debug("thread sleep " + ms + " ms...");
			Thread.sleep(ms);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}
}