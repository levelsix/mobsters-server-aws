package com.lvl6.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ObstacleForUser extends BasePersistentObject{	

	@Column(name = "final")
	private static final long serialVersionUID = -4951893395081957418L;	

	@Column(name = "user_id")
	private int userId;
	@Column(name = "obstacle_id")
	private int obstacleId;
	@Column(name = "xcoord")
	private int xcoord;
	@Column(name = "ycoord")
	private int ycoord;
	@Column(name = "removal_time")
	private Date removalTime;
	@Column(name = "orientation")
	private String orientation;	
	public ObstacleForUser() {
		super();
	}

	public ObstacleForUser(int id, int userId, int obstacleId, int xcoord,
			int ycoord, Date removalTime, String orientation) {
		super();
		this.userId = userId;
		this.obstacleId = obstacleId;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.removalTime = removalTime;
		this.orientation = orientation;
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getObstacleId() {
		return obstacleId;
	}

	public void setObstacleId(int obstacleId) {
		this.obstacleId = obstacleId;
	}

	public int getXcoord() {
		return xcoord;
	}

	public void setXcoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public int getYcoord() {
		return ycoord;
	}

	public void setYcoord(int ycoord) {
		this.ycoord = ycoord;
	}

	public Date getRemovalTime() {
		return removalTime;
	}

	public void setRemovalTime(Date removalTime) {
		this.removalTime = removalTime;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	@Override
	public String toString() {
		return "ObstacleForUser [id=" + id + ", userId=" + userId
				+ ", obstacleId=" + obstacleId + ", xcoord=" + xcoord
				+ ", ycoord=" + ycoord + ", removalTime=" + removalTime
				+ ", orientation=" + orientation + "]";
	}

}
