package org.lance.entity;

import java.io.Serializable;

/**
 *  µÃÂ¿‡
 * 
 * @author lance
 * 
 */
public class CommentResult implements Serializable {
	private static final long serialVersionUID = -1329588532552008645L;
	private String user;
	private String content;
	private String time;
	private int count;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
