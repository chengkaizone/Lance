package org.lance.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 邮件发送器
 * 
 * @author lance
 * 
 */
public class MailSender {
	private static Properties props = new Properties();
	private static boolean validate = true;
	static {
		props.put("mail.smtp.host", "smtp.qq.com");
		props.put("mail.smtp.port", "25");
		props.put("mail.smtp.auth", validate ? validate + "" : !validate + "");
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
	}

	public static void main(String[] args) {
		test();
	}

	private static void test() {
		MailInfo mailInfo = new MailInfo();
		mailInfo.setUserName("1094226429@qq.com");
		mailInfo.setPassword("***");
		mailInfo.setFromAddress("1094226429@qq.com");
		mailInfo.setToAddress("chengkaizone@163.com");
		mailInfo.setSubject("定单信息");
		mailInfo.setContent("呵呵呵呵");
		mailInfo.setAttachFilePaths(new String[] { "c:\\temp\\HelloWorld.h" });
		// 这个类主要来发送邮件

		MailSender sms = new MailSender();
		boolean bool = sms.sendAttachMail(mailInfo);
		if (bool) {
			System.out.println("发送成功");
		} else {
			System.out.println("发送失败");
		}
	}

	/**
	 * 设置邮件服务器
	 * 
	 * @param smtp
	 */
	public void setSmtpHost(String smtp) {
		props.put("mail.smtp.host", smtp);
	}

	/**
	 * 设置邮件端口
	 * 
	 * @param port
	 */
	public void setSmtpPort(String port) {
		props.put("mail.smtp.port", port);
	}

	/**
	 * 设置是否需要验证
	 * 
	 * @param validate
	 */
	public void setValidate(boolean validate) {
		props.put("mail.smtp.auth", validate ? validate + "" : !validate + "");
	}

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public boolean sendTextMail(MailInfo mailInfo) {
		// 判断是否需要身份认证
		CustomAuthenticator authenticator = null;
		if (validate) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new CustomAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/** */
	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public static boolean sendHtmlMail(MailInfo mailInfo) {
		// 判断是否需要身份认证
		CustomAuthenticator authenticator = null;
		// 如果需要身份认证，则创建一个密码验证器
		if (validate) {
			authenticator = new CustomAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 带附件的发送邮件
	 * 
	 * @param info
	 */
	public boolean sendAttachMail(MailInfo mailInfo) {
		// 判断是否需要身份认证
		CustomAuthenticator authenticator = null;
		// 如果需要身份认证，则创建一个密码验证器
		if (validate) {
			authenticator = new CustomAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart body = new MimeBodyPart();
			// 设置HTML内容
			body.setContent(mailInfo.getContent(), "text/*; charset=utf-8");
			mainPart.addBodyPart(body);
			String[] paths = mailInfo.getAttachFilePaths();
			for (String path : paths) {
				// 添加附件
				body = new MimeBodyPart();
				FileDataSource source = new FileDataSource(path);
				body.setDataHandler(new DataHandler(source));
				body.setFileName(javax.mail.internet.MimeUtility
						.encodeWord(path)); // 中文附件名先编码
				mainPart.addBodyPart(body);
			}
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
