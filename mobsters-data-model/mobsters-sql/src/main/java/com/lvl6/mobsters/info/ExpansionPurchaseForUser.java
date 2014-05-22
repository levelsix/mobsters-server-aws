package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ExpansionPurchaseForUser extends BasePersistentObject{	

	
	private static final long serialVersionUID = -5045515317226423462L;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "x_position")
	private int xPosition;
	@Column(name = "y_position")
	private int yPosition;
	@Column(name = "is_expanding")
	private boolean isExpanding;
	@Column(name = "expand_start_time")
	private Date expandStartTime; // refers to last time the user clicks the  // upgrade button, not when the last upgrade
  // was complete
  
	public ExpansionPurchaseForUser(){}
	public ExpansionPurchaseForUser(int userId, int xPosition, int yPosition,
			boolean isExpanding, Date expandStartTime) {
		super();
		this.userId = userId;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.isExpanding = isExpanding;
		this.expandStartTime = expandStartTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public boolean isExpanding() {
		return isExpanding;
	}

	public void setExpanding(boolean isExpanding) {
		this.isExpanding = isExpanding;
	}

	public Date getExpandStartTime() {
		return expandStartTime;
	}

	public void setExpandStartTime(Date expandStartTime) {
		this.expandStartTime = expandStartTime;
	}

	@Override
	public String toString() {
		return "ExpansionPurchaseForUser [userId=" + userId + ", xPosition="
				+ xPosition + ", yPosition=" + yPosition + ", isExpanding="
				+ isExpanding + ", expandStartTime=" + expandStartTime + "]";
	}

}
