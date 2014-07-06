package org.lance.util;

import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.SimpleEmail;
/**
 * 邮件工具类
 * 
 * @author lance
 */
public class MailService {

	public static final String MAIN_PROPERTIES = "main";

	/**
	 * 
	 * @param map
	 *            发送MAIL的必备参数 serverAddress 1邮件服务器地址 mailTo 2发件人邮箱 mailFrom
	 *            3收件人邮箱 user 4用户名 pwd 密码 subject 5邮件的主题 msg 6正文
	 * @throws EmailException
	 */
	// public static void sendMail(Map map) throws EmailException {
	// String mailBerail = propertyBundle ();
	//
	// //根据配置文件中的邮件发送开关决定邮件的发送服务是否开启
	// if(mailBerail.equals("true")){
	// SimpleEmail email = new SimpleEmail();
	// // 邮件服务器地址
	// String serverAddress = (String) map.get("serverAddress");
	// email.setHostName(serverAddress);
	// // 收件人邮箱
	// String mailTo = (String) map.get("mailTo");
	// email.addTo(mailTo);
	// // 发件人邮箱
	// String mailFrom = (String) map.get("mailFrom");
	// email.setFrom(mailFrom);
	//
	// // 如果要求身份验证，设置用户名、密码，分别为发件人在邮件服务器上注册的用户名和密码
	// String user =(String)map.get("user");
	// String pwd =(String)map.get("pwd");
	// email.setAuthentication(user,pwd);
	// email.setCharset("UTF-8");
	// //设置邮件的主题
	// String subject =(String)map.get("subject");
	// email.setSubject(subject);
	// //正文
	// String msg =(String)map.get("msg");
	// email.setMsg(msg);
	// email.send();
	// }else{
	// System.out.println("邮件发送服务关闭!");
	// }
	// }

	// 解析配置文件
	public static String propertyBundle() {
		return propertyBundle(MailService.MAIN_PROPERTIES);
	}

	public static String propertyBundle(String fileName) {
		PropertyResourceBundle bundle = (PropertyResourceBundle) ResourceBundle
				.getBundle(fileName);
		String mailBerail = String
				.valueOf(bundle.handleGetObject("mailBerail"));
		return mailBerail;
	}

}
