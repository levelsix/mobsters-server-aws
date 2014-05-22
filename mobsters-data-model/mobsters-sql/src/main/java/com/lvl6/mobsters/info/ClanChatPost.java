package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanChatPost extends BasePersistentObject{
	
	private static final long serialVersionUID = -7441177529235760896L;
	@Column(name = "poster_id")
	private int posterId;
	@Column(name = "clan_id")
	private int clanId;
	@Column(name = "time_of_post")
	private Date timeOfPost;
	@Column(name = "content")
	private String content;
	public ClanChatPost(){}
  public ClanChatPost(int id, int posterId, int clanId,
      Date timeOfPost, String content) {
    super();
    this.posterId = posterId;
    this.clanId = clanId;
    this.timeOfPost = timeOfPost;
    this.content = content;
  }


  public int getPosterId() {
    return posterId;
  }

  public int getClanId() {
    return clanId;
  }

  public Date getTimeOfPost() {
    return timeOfPost;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "ClanChatPost [id=" + id + ", posterId=" + posterId
        + ", clanID=" + clanId + ", timeOfPost=" + timeOfPost
        + ", content=" + content + "]";
  }
}
