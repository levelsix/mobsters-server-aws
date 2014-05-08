package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PrivateChatPost extends BasePersistentObject{	
	protected static final long serialVersionUID = 8450554970377509383L;

	@Column(name="poster_id")
	protected int posterId;
	
	@Column(name="recipient_id")
	protected int recipientId;
	
	@Column(name="time_of_post")
	protected Date timeOfPost;
	
	protected String content;

	public PrivateChatPost(){}
	public PrivateChatPost(int posterId, int recipientId,
			Date timeOfPost, String content) {
		super();
		this.posterId = posterId;
		this.recipientId = recipientId;
		this.timeOfPost = timeOfPost;
		this.content = content;
	}


	public int getPosterId() {
		return posterId;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public Date getTimeOfPost() {
		return timeOfPost;
	}

	public String getContent() {
		return content;
	}

	public void setTimeOfPost(Date timeOfPost) {
		this.timeOfPost = timeOfPost;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	@Override
	public String toString() {
		return "PlayerWallPost [id=" + id + ", posterId=" + posterId
				+ ", recipientId=" + recipientId + ", timeOfPost=" + timeOfPost
				+ ", content=" + content + "]";
	}
}
