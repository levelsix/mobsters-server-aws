package com.lvl6.mobsters.info;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="clan")
@Cacheable(value=true)
public class Clan extends BasePersistentObject{	

	private static final long serialVersionUID = 500266166254551053L;

	// @Column(name = "owner_id")
	// private int ownerId;

	@Column(name = "name")
	private String name;

	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@Column(name = "description")
	private String description;

	@Column(name = "tag")
	private String tag;

	@Column(name = "request_to_join_required")
	private boolean requestToJoinRequired;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "clan_icon_id", foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ClanIcon clanIcon;	

	public Clan() {
		super();
	}

	public Clan(final String id, final String name, final Date createTime, final String description,
			final String tag, final boolean requestToJoinRequired, final ClanIcon clanIcon) {
		super(id);
		this.name = name;
		this.createTime = createTime;
		this.description = description;
		this.tag = tag;
		this.requestToJoinRequired = requestToJoinRequired;
		this.clanIcon = clanIcon;
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

//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}

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

	public ClanIcon getClanIcon() {
		return clanIcon;
	}

	public void setClanIcon(ClanIcon clanIcon) {
		this.clanIcon = clanIcon;
	}

	@Override
	public String toString() {
		return "Clan [id=" + id + ", name=" + name + ", createTime=" + createTime
				+ ", description=" + description + ", tag=" + tag
				+ ", requestToJoinRequired=" + requestToJoinRequired + ", clanIcon="
				+ clanIcon + "]";
	}
	
}
