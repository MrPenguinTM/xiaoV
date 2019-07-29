package org.androidtest.xiaoV;

import org.androidtest.xiaoV.handler.ApiManager;

import cn.zhouyafeng.itchat4j.Wechat;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;

/**
 * 
 * @author caipeipei
 *
 */
public class Main {

	public static void main(String[] args) {
		IMsgHandlerFace msgHandler = new ApiManager();// 实现IMsgHandlerFace接口的类

		Wechat wechat = new Wechat(msgHandler); // 【注入】
		wechat.start(); // 启动服务，会在qrPath下生成一张二维码图片，扫描即可登陆，注意，二维码图片如果超过一定时间未扫描会过期，过期时会自动更新，所以你可能需要重新打开图片
	}
}
