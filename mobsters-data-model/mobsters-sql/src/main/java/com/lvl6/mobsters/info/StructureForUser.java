package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StructureForUser extends BasePersistentObject{	//any change in this class should also change the UpdateUtils.java method updateUserStructsLastretrieved()
	

	
	private static final long serialVersionUID = -3430826000446530128L;	

	@Column(name = "user_id")
	private int userId;
	@Column(name = "struct_id")
	private int structId;
	@Column(name = "last_retrieved")
	private Date lastRetrieved;
	@Column(name = "coordinates")
	private CoordinatePair coordinates;
	//@Column(name = "level")
//	private int level;
	@Column(name = "purchase_time")
	private Date purchaseTime;
	@Column(name = "is_complete")
	private boolean isComplete;
	@Column(name = "orientation")
	private String orientation;
	@Column(name = "fb_invite_struct_lvl")
	private int fbInviteStructLvl;	
	public StructureForUser(){}
	public StructureForUser(int id, int userId, int structId,
			Date lastRetrieved, CoordinatePair coordinates, Date purchaseTime,
			boolean isComplete, String orientation, int fbInviteStructLvl) {
		super();
		this.userId = userId;
		this.structId = structId;
		this.lastRetrieved = lastRetrieved;
		this.coordinates = coordinates;
		this.purchaseTime = purchaseTime;
		this.isComplete = isComplete;
		this.orientation = orientation;
		this.fbInviteStructLvl = fbInviteStructLvl;
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStructId() {
		return structId;
	}

	public void setStructId(int structId) {
		this.structId = structId;
	}

	public Date getLastRetrieved() {
		return lastRetrieved;
	}

	public void setLastRetrieved(Date lastRetrieved) {
		this.lastRetrieved = lastRetrieved;
	}

	public CoordinatePair getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(CoordinatePair coordinates) {
		this.coordinates = coordinates;
	}

	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public int getFbInviteStructLvl() {
		return fbInviteStructLvl;
	}

	public void setFbInviteStructLvl(int fbInviteStructLvl) {
		this.fbInviteStructLvl = fbInviteStructLvl;
	}

	@Override
	public String toString() {
		return "StructureForUser [id=" + id + ", userId=" + userId
				+ ", structId=" + structId + ", lastRetrieved=" + lastRetrieved
				+ ", coordinates=" + coordinates + ", purchaseTime="
				+ purchaseTime + ", isComplete=" + isComplete
				+ ", orientation=" + orientation + ", fbInviteStructLvl="
				+ fbInviteStructLvl + "]";
	}

}
