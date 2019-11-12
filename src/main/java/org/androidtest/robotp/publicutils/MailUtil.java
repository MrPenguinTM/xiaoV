package org.androidtest.robotp.publicutils;

import org.androidtest.robotp.beans.MailSenderInfo;
import org.androidtest.robotp.data.SysConfig;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import java.util.Properties;

/**
 * 邮件工具类
 *
 * @author Vince蔡培培
 */
public class MailUtil {
	private static final String MAIL_SUBJECT = "RobotP-REPORT";

	private static MailSenderInfo getMailSenderInfo(String content) {
		Properties properties = SysConfig.getConfiguration();
		MailSenderInfo mailInfo = new MailSenderInfo(); // 这个类主要是设置邮件
		mailInfo.setMailServerHost(properties.getProperty("mail.smtp.ip"));
		mailInfo.setMailServerPort(properties.getProperty("mail.smtp.port"));
		mailInfo.setSslenable(properties.getProperty("mail.smtp.ssl.enable")
				.equals("true"));
		mailInfo.setValidate(true);
		mailInfo.setUserName(properties.getProperty("mail.smtp.username"));
		// 您的邮箱密码
		mailInfo.setPassword(properties.getProperty("mail.smtp.password"));
		mailInfo.setFromAddress(properties.getProperty("mail.smtp.username"));
		// 标题
		mailInfo.setSubject(MAIL_SUBJECT);
		// 内容
		mailInfo.setContent(content);
		mailInfo.setToAddress(properties.getProperty("mail.smtp.address"));
		return mailInfo;
	}

	@SuppressWarnings("deprecation")
	public static void sendMail(String content) {
		MailSenderInfo mailInfo = getMailSenderInfo(content);
		MultiPartEmail email = new MultiPartEmail();
		try {
			email.setTLS(mailInfo.isSslenable());
			email.setHostName(mailInfo.getMailServerHost());
			email.setAuthentication(mailInfo.getUserName(),
					mailInfo.getPassword());// 用户名和密码
			email.setFrom(mailInfo.getFromAddress()); // 发送方
			email.addTo(mailInfo.getToAddress());// 接收方
			email.setSubject(mailInfo.getSubject()); // 标题
			email.setCharset("UTF-8");
			email.setMsg(mailInfo.getContent()); // 内容
			email.send();

		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
}