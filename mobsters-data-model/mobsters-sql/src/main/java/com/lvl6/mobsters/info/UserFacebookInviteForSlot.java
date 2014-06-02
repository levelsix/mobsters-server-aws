package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserFacebookInviteForSlot extends BasePersistentObject{

	
	private static final long serialVersionUID = -1731978369694889921L;
	@Column(name = "inviter_user_id")
	private int inviterUserId;
	@Column(name = "recipient_facebook_id")
	private String recipientFacebookId;
	@Column(name = "time_of_invite")
	private Date timeOfInvite;
	@Column(name = "time_accepted")
	private Date timeAccepted;
	@Column(name = "user_struct_id")
	private int userStructId;
	@Column(name = "user_struct_fb_lvl")
	private int userStructFbLvl;
	@Column(name = "time_redeemed")
	private Date timeRedeemed;	
	public UserFacebookInviteForSlot(){}
	public UserFacebookInviteForSlot(String id, int inviterUserId,
			String recipientFacebookId, Date timeOfInvite, Date timeAccepted,
			int userStructId, int userStructFbLvl, Date timeRedeemed) {
		super(id);
		this.inviterUserId = inviterUserId;
		this.recipientFacebookId = recipientFacebookId;
		this.timeOfInvite = timeOfInvite;
		this.timeAccepted = timeAccepted;
		this.userStructId = userStructId;
		this.userStructFbLvl = userStructFbLvl;
		this.timeRedeemed = timeRedeemed;
	}



	public int getInviterUserId() {
		return inviterUserId;
	}

	public void setInviterUserId(int inviterUserId) {
		this.inviterUserId = inviterUserId;
	}

	public String getRecipientFacebookId() {
		return recipientFacebookId;
	}

	public void setRecipientFacebookId(String recipientFacebookId) {
		this.recipientFacebookId = recipientFacebookId;
	}

	public Date getTimeOfInvite() {
		return timeOfInvite;
	}

	public void setTimeOfInvite(Date timeOfInvite) {
		this.timeOfInvite = timeOfInvite;
	}

	public Date getTimeAccepted() {
		return timeAccepted;
	}

	public void setTimeAccepted(Date timeAccepted) {
		this.timeAccepted = timeAccepted;
	}

	public int getUserStructId() {
		return userStructId;
	}

	public void setUserStructId(int userStructId) {
		this.userStructId = userStructId;
	}

	public int getUserStructFbLvl() {
		return userStructFbLvl;
	}

	public void setUserStructFbLvl(int userStructFbLvl) {
		this.userStructFbLvl = userStructFbLvl;
	}

	public Date getTimeRedeemed() {
		return timeRedeemed;
	}

	public void setTimeRedeemed(Date timeRedeemed) {
		this.timeRedeemed = timeRedeemed;
	}

	@Override
	public String toString() {
		return "UserFacebookInviteForSlot [id=" + id + ", inviterUserId="
				+ inviterUserId + ", recipientFacebookId=" + recipientFacebookId
				+ ", timeOfInvite=" + timeOfInvite + ", timeAccepted=" + timeAccepted
				+ ", userStructId=" + userStructId + ", userStructFbLvl="
				+ userStructFbLvl + ", timeRedeemed=" + timeRedeemed + "]";
	}
	
}
