package cn.zhouyafeng.itchat4j.api;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * 辅助工具类，该类暂时未用，请忽略
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年5月22日 下午10:34:46
 */
public class AssistTools {
	private static final MediaType MEDIA_TYPE_PNG = MediaType
			.parse("image/png");
	private static OkHttpClient client = new OkHttpClient();

	public static boolean sendQrPicToServer(String username, String password,
			String uploadUrl, String localPath) throws IOException {
		File file = new File(localPath);
		RequestBody requestBody = new MultipartBody.Builder()
				.addFormDataPart("username", username)
				.addFormDataPart("password", password)
				.addFormDataPart("file", file.getName(),
						RequestBody.create(MEDIA_TYPE_PNG, file)).build();
		Request request = new Request.Builder().url(uploadUrl)
				.post(requestBody).build();
		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
