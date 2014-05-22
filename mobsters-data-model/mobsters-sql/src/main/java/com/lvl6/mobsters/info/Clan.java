package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Clan extends BasePersistentObject{	

//	@Column(name = "owner_id")
//	private int ownerId;
	@Column(name = "name")
	private String name;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "description")
	private String description;
	@Column(name = "tag")
	private String tag;
	@Column(name = "request_to_join_required")
	private boolean requestToJoinRequired;
	@Column(name = "clan_icon_id")
	private int clanIconId;	
	public Clan() {
		super();
	}

	public Clan(int id, String name, Date createTime, String description,
			String tag, boolean requestToJoinRequired, int clanIconId) {
		super();
		this.name = name;
		this.createTime = createTime;
		this.description = description;
		this.tag = tag;
		this.requestToJoinRequired = requestToJoinRequired;
		this.clanIconId = clanIconId;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isRequestToJoinRequired() {
		return requestToJoinRequired;
	}

	public void setRequestToJoinRequired(boolean requestToJoinRequired) {
		this.requestToJoinRequired = requestToJoinRequired;
	}

	public int getClanIconId() {
		return clanIconId;
	}

	public void setClanIconId(int clanIconId) {
		this.clanIconId = clanIconId;
	}

	@Override
	public String toString() {
		return "Clan [id=" + id + ", name=" + name + ", createTime=" + createTime
				+ ", description=" + description + ", tag=" + tag
				+ ", requestToJoinRequired=" + requestToJoinRequired + ", clanIconId="
				+ clanIconId + "]";
	}
	
}
