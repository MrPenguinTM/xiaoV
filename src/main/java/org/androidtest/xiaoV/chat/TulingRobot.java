package org.androidtest.xiaoV.chat;

import java.util.HashMap;
import java.util.Map;

import org.androidtest.xiaoV.publicutil.LogUtil;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import cn.zhouyafeng.itchat4j.utils.MyHttpClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 聊天机器人功能
 * 
 * @author caipeipei
 *
 */
public class TulingRobot {

	private static final String APIKEY = "2d7dcaee7db94f7e980b820e84c1d3a0";// 这里是我申请的图灵机器人API接口，每天只能5000次调用，建议自己去申请一个，免费的:)
	private static final String URL = "http://www.tuling123.com/openapi/api";

	public static String chat(String nickName, String text) {
		String result = null;
		MyHttpClient myHttpClient = MyHttpClient.getInstance();

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("key", APIKEY);
		paramMap.put("info", text);
		paramMap.put("userid", "123456");
		String paramStr = JSON.toJSONString(paramMap);
		try {
			HttpEntity entity = myHttpClient.doPost(URL, paramStr);
			result = EntityUtils.toString(entity, "UTF-8");
			JSONObject obj = JSON.parseObject(result);
			if (obj.getString("code").equals("100000")) {
				result = "@" + nickName + " " + obj.getString("text");
			} else if (obj.getString("code").equals("40004")) {
				result = "今天聊天太嗨了，条数超过5000条，明天再聊吧。@" + nickName
						+ " 今天你打卡了吗？？\n--------------------\n需要\"菜单\"请回复菜单";
			} else {
				LogUtil.MSG.info("chat: " + obj.toString());
				result = null;
			}
		} catch (Exception e) {
			result = null;
			LogUtil.MSG.info("chat: " + e.getMessage());
		}
		return result;
	}
}