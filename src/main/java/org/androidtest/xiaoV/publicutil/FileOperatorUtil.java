package org.androidtest.xiaoV.publicutil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author caipeipei
 * @Description 操作PC上文件相关的工具类
 */
public class FileOperatorUtil {

	/**
	 * 删除文件，可以是文件或文件夹
	 *
	 * @param fileName
	 *            要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			// System.out.println("删除文件失败:" + fileName + "不存在！");
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}

	/**
	 * 删除目录及目录下的文件
	 *
	 * @param dir
	 *            要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			// LogUtil.APP.warn("删除目录失败：" + dir + "不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			// LogUtil.APP.warn("删除目录失败！");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			// LogUtil.APP.info("删除目录" + dir + "成功！");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @param fileName
	 *            要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				// LogUtil.APP.debug("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				// LogUtil.APP.warn("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			// LogUtil.APP.warn("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}

	public static File mkdirs(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 读取txt文件的内容
	 *
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static String readFromFile(File file) {
		if (!file.exists()) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static void releaceFile(String fromPCPath, String toPCPath)
			throws IOException {
		FileInputStream fis = new FileInputStream(fromPCPath);
		BufferedInputStream bufis = new BufferedInputStream(fis);

		FileOutputStream fos = new FileOutputStream(toPCPath);
		BufferedOutputStream bufos = new BufferedOutputStream(fos);

		int len = 0;
		while ((len = bufis.read()) != -1) {
			bufos.write(len);
		}
		bufis.close();
		bufos.close();

	}

	/**
	 * 将数据写入文件
	 *
	 * @param file
	 * @param isAppend
	 * @param text
	 */
	public static void writeToFile(File file, boolean isAppend, String text) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, isAppend);
			fw.write(text + System.getProperty("line.separator"));
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
