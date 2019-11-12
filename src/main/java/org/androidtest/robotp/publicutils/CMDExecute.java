package org.androidtest.robotp.publicutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * CMD工具类
 *
 * @author Vince蔡培培
 */
public final class CMDExecute {
	/**
	 * 私有构造函数，防止被new
	 */
	private CMDExecute() {

	}

	/**
	 * 执行ADB命令行;返回命令执行结果，每行数据以“\r\n”
	 *
	 * @param deviceId
	 * @param adbStr
	 * @return
	 */
	public static String runADB(String deviceId, String adbStr) {
		return runCMD("adb" + " -s " + deviceId + " " + adbStr);
	}

	/**
	 * 执行ADB命令行;返回命令执行结果，每行数据以“\r\n”
	 *
	 * @param deviceId
	 * @param adbStr
	 * @return
	 */
	public static String runADBSynchronised(String deviceId, String adbStr) {
		return runCMDSynchronised("adb" + " -s " + deviceId + " " + adbStr);
	}

	/**
	 * 执行CMD命令行;返回命令执行结果，每行数据以“\r\n”
	 *
	 * @param cmdStr
	 * @return
	 */
	public static String runCMD(String cmdStr) {
		System.out.println("runCMD: " + cmdStr);
		StringBuilder sb = new StringBuilder();
		String line = "";

		try {
			Process process = Runtime.getRuntime().exec(cmdStr);
			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			while (((line = br.readLine()) != null)) {
				line = line.trim();
				if (line.equals("")) {
					continue;
				}
				sb.append(line);
				sb.append("\r\n");
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// LeiFengConsoleFactory.getInstance().consolePrintErr(
				// "执行 wait操作失败");
				e.printStackTrace();
				return null;
			}
		} catch (IOException e) {
			// LeiFengConsoleFactory.getInstance().consolePrintErr("执行操作失败");
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	/**
	 * 执行CMD命令行并同步输出打印，最终返回命令执行结果，每行数据以“\r\n”
	 *
	 * @param cmdStr
	 * @return
	 */
	public static String runCMDSynchronised(String cmdStr) {
		StringBuilder sb = new StringBuilder();
		String line = "";
		System.out.println("runCMDSynchronised: " + cmdStr);
		try {
			Process process = Runtime.getRuntime().exec(cmdStr);

			InputStream is = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));

			while ((line = br.readLine()) != null) {

				line = line.trim();
				if (line.equals("")) {
					continue;
				}
				sb.append(line);
				sb.append("\r\n");
				System.out.print(line + "\r\n");
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				// LeiFengConsoleFactory.getInstance().consolePrintErr(
				// "执行 wait操作失败");
				e.printStackTrace();
				return null;
			}
		} catch (IOException e) {
			// LeiFengConsoleFactory.getInstance().consolePrintErr("执行操作失败");
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	/**
	 * 执行SHELL命令行;返回命令执行结果，每行数据以“\r\n”
	 *
	 * @param deviceId
	 * @param shellStr
	 * @return
	 */
	public static String runSHELL(String deviceId, String shellStr) {
		// if (PCInfo.OSName == PCOSName.WINDOW) {
		return runADB(deviceId, "shell \"" + shellStr + "\"");
		// } else {
		// return runADB(deviceId, "shell " + shellStr);
		// }
	}

}
