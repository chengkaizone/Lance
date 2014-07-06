package org.lance.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * ������֤
 * 
 * @author lance
 * 
 */
public class CustomAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public CustomAuthenticator() {
	}

	public CustomAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

}
