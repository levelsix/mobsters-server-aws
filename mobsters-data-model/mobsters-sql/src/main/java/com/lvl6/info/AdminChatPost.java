package com.lvl6.info;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class AdminChatPost extends PrivateChatPost {	
	
	
	public AdminChatPost(int posterId, int recipientId, Date timeOfPost, String content) {
		super(posterId, recipientId, timeOfPost, content);
		//setUsername(username);
	}
	
	public AdminChatPost(){}
	public AdminChatPost(int posterId, int recipientId, Date timeOfPost, String content, String username) {
		super(posterId, recipientId, timeOfPost, content);
		setUsername(username);
	}
	
	protected String username = "";
	

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	
	private static final long serialVersionUID = -4608572851669225658L;	
}
