package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class AdminChatPost extends PrivateChatPost {	
	public AdminChatPost(int id, int posterId, int recipientId, Date timeOfPost, String content) {
		super(id, posterId, recipientId, timeOfPost, content);
		//setUsername(username);
	}
	
	public AdminChatPost(){}
	public AdminChatPost(int id, int posterId, int recipientId, Date timeOfPost, String content, String username) {
		super(id, posterId, recipientId, timeOfPost, content);
		setUsername(username);
	}
	
	protected String username = "";
	

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	@Column(name = "final")
	private static final long serialVersionUID = -4608572851669225658L;	
}
