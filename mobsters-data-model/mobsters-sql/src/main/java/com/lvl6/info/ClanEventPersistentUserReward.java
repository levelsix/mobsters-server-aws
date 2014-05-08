package com.lvl6.info;

import java.util.Date;

//user can have multiple of these (different clanIds)
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanEventPersistentUserReward extends BasePersistentObject{	

	
	private static final long serialVersionUID = -7227824016627786538L;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "crs_start_time")
	private Date crsStartTime;
	@Column(name = "crs_id")
	private int crsId;
	@Column(name = "crs_end_time")
	private Date crsEndTime;
	@Column(name = "resource_type")
	private String resourceType;
	@Column(name = "static_data_id")
	private int staticDataId;
	@Column(name = "quantity")
	private int quantity;
	@Column(name = "clan_event_persistent_id")
	private int clanEventPersistentId;
	@Column(name = "time_redeemed")
	private Date timeRedeemed;	
	public ClanEventPersistentUserReward(){}
	public ClanEventPersistentUserReward(int id, int userId, Date crsStartTime,
			int crsId, Date crsEndTime, String resourceType, int staticDataId,
			int quantity, int clanEventPersistentId, Date timeRedeemed) {
		super();
		this.userId = userId;
		this.crsStartTime = crsStartTime;
		this.crsId = crsId;
		this.crsEndTime = crsEndTime;
		this.resourceType = resourceType;
		this.staticDataId = staticDataId;
		this.quantity = quantity;
		this.clanEventPersistentId = clanEventPersistentId;
		this.timeRedeemed = timeRedeemed;
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCrsStartTime() {
		return crsStartTime;
	}

	public void setCrsStartTime(Date crsStartTime) {
		this.crsStartTime = crsStartTime;
	}

	public int getCrsId() {
		return crsId;
	}

	public void setCrsId(int crsId) {
		this.crsId = crsId;
	}

	public Date getCrsEndTime() {
		return crsEndTime;
	}

	public void setCrsEndTime(Date crsEndTime) {
		this.crsEndTime = crsEndTime;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public int getStaticDataId() {
		return staticDataId;
	}

	public void setStaticDataId(int staticDataId) {
		this.staticDataId = staticDataId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getClanEventPersistentId() {
		return clanEventPersistentId;
	}

	public void setClanEventPersistentId(int clanEventPersistentId) {
		this.clanEventPersistentId = clanEventPersistentId;
	}

	public Date getTimeRedeemed() {
		return timeRedeemed;
	}

	public void setTimeRedeemed(Date timeRedeemed) {
		this.timeRedeemed = timeRedeemed;
	}

	@Override
	public String toString() {
		return "ClanEventPersistentUserReward [id=" + id + ", userId=" + userId
				+ ", crsStartTime=" + crsStartTime + ", crsId=" + crsId
				+ ", crsEndTime=" + crsEndTime + ", resourceType=" + resourceType
				+ ", staticDataId=" + staticDataId + ", quantity=" + quantity
				+ ", clanEventPersistentId=" + clanEventPersistentId
				+ ", timeRedeemed=" + timeRedeemed + "]";
	}
	
}
