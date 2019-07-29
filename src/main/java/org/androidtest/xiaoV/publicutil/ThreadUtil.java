package org.androidtest.xiaoV.publicutil;

/**
 * @author caipeipei
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