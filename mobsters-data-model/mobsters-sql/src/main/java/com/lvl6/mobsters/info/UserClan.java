package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserClan extends BasePersistentObject{	

	
	private static final long serialVersionUID = 4400616881122130880L;	

	@Column(name = "user_id")
	private int userId;
	@Column(name = "clan_id")
	private int clanId;
	@Column(name = "status")
	private String status;
	@Column(name = "request_time")
	private Date requestTime;
	public UserClan(){}
	public UserClan(int userId, int clanId, String status, Date requestTime) {
		super();
		this.userId = userId;
		this.clanId = clanId;
		this.status = status;
		this.requestTime = requestTime;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getClanId() {
		return clanId;
	}
	public void setClanId(int clanId) {
		this.clanId = clanId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	
	@Override
	public String toString() {
		return "UserClan [userId=" + userId + ", clanId=" + clanId
				+ ", status=" + status + ", requestTime=" + requestTime + "]";
	}

}
