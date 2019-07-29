package org.androidtest.xiaoV.publicutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author caipeipei
 */
public class DownloadUtil {

	/**
	 * 从网络Url中下载文件 （加了进程锁，避免同时下载多个文件消耗过多系统资源）
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static synchronized void downLoadFromUrl(String urlStr,
			String fileName, String savePath) {
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 得到输入流
			InputStream inputStream = conn.getInputStream();
			// 获取自己数组
			// byte[] getData = readInputStream(inputStream);
			// 文件保存位置
			File saveDir = new File(savePath);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}
			File file = new File(saveDir + File.separator + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			// fos.write(getData);
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf)) != -1) {
				fos.write(buf, 0, len);
				// os.flush();
			}
			inputStream.close();
			if (fos != null) {
				fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			// LogUtil.APP.info("download success: " + url);
		} catch (Exception e) {
			// LogUtil.APP.error("downLoadFromUrl fail", e);
			// TODO 返回EXCEPTION到测试用例执行结果里
		}
	}

	private DownloadUtil() {

	}
}
