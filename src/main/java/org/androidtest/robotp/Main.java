package org.androidtest.robotp;

import java.io.File;

import cn.zhouyafeng.itchat4j.Wechat;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;

/**
 * 程序启动的主入口
 *
 * @Author: Vince蔡培培
 */
public class Main {

	public static void main(String[] args) {
		cleanLastTmpFile();
		IMsgHandlerFace msgHandler = new RobotPHandler();// 实现IMsgHandlerFace接口的类
		Wechat wechat = new Wechat(msgHandler); // 【注入】
		wechat.start(); // 启动服务，会在qrPath下生成一张二维码图片，扫描即可登陆，注意，二维码图片如果超过一定时间未扫描会过期，过期时会自动更新，所以你可能需要重新打开图片
	}

	/**
	 * 清掉上次运行留下来的临时文件（二维码图片和日志文件）
	 */
	private static void cleanLastTmpFile() {
		if (Config.dir_home.isDirectory()) {
			File[] array = Config.dir_home.listFiles();
			for (int i = 0; i < array.length; i++) {
				if (array[i].getName().endsWith(".jpg")
						|| array[i].getName().endsWith(".log")) {
					array[i].delete();
				}
			}
		}
	}
}
