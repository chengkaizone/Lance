package org.lance.util;

import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.SimpleEmail;
/**
 * �ʼ�������
 * 
 * @author lance
 */
public class MailService {

	public static final String MAIN_PROPERTIES = "main";

	/**
	 * 
	 * @param map
	 *            ����MAIL�ıر����� serverAddress 1�ʼ���������ַ mailTo 2���������� mailFrom
	 *            3�ռ������� user 4�û��� pwd ���� subject 5�ʼ������� msg 6����
	 * @throws EmailException
	 */
	// public static void sendMail(Map map) throws EmailException {
	// String mailBerail = propertyBundle ();
	//
	// //���������ļ��е��ʼ����Ϳ��ؾ����ʼ��ķ��ͷ����Ƿ���
	// if(mailBerail.equals("true")){
	// SimpleEmail email = new SimpleEmail();
	// // �ʼ���������ַ
	// String serverAddress = (String) map.get("serverAddress");
	// email.setHostName(serverAddress);
	// // �ռ�������
	// String mailTo = (String) map.get("mailTo");
	// email.addTo(mailTo);
	// // ����������
	// String mailFrom = (String) map.get("mailFrom");
	// email.setFrom(mailFrom);
	//
	// // ���Ҫ�������֤�������û��������룬�ֱ�Ϊ���������ʼ���������ע����û���������
	// String user =(String)map.get("user");
	// String pwd =(String)map.get("pwd");
	// email.setAuthentication(user,pwd);
	// email.setCharset("UTF-8");
	// //�����ʼ�������
	// String subject =(String)map.get("subject");
	// email.setSubject(subject);
	// //����
	// String msg =(String)map.get("msg");
	// email.setMsg(msg);
	// email.send();
	// }else{
	// System.out.println("�ʼ����ͷ���ر�!");
	// }
	// }

	// ���������ļ�
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
