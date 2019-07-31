package org.androidtest.xiaoV.data;

import java.util.Properties;


/**
 * 系统配置参数
 */
public class SysConfig {
	private static final Properties SYS_CONFIG = new Properties();
	private static final String SYS_CONFIG_FILE = "/sys_config.properties";

	static {
		try {
			SYS_CONFIG.load(SysConfig.class
					.getResourceAsStream(SYS_CONFIG_FILE));
		} catch (Exception e) {
				System.out.println("在当前根目录下找不到" + SYS_CONFIG_FILE+",请将"+SYS_CONFIG_FILE+"放置到运行程序根目录下。");
				e.printStackTrace();
		}
	}

	private SysConfig() {
	}

	public static Properties getConfiguration() {
		return SYS_CONFIG;
	}
}
