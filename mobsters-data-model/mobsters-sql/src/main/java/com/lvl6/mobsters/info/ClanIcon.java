package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name="ClanIcon")
@Table(name="clan_icon")
public class ClanIcon extends BaseIntPersistentObject{
	
	private static final long serialVersionUID = -5510816987235168564L;	

	@Column(name = "img_name")
	private String imgName;

	@Column(name = "is_available")
	private boolean isAvailable;	
	
	public ClanIcon() {
		super();
	}

	public ClanIcon(final int id, final String imgName, final boolean isAvailable) {
		super(id);
		this.imgName = imgName;
		this.isAvailable = isAvailable;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "ClanIcon [id=" + id + ", imgName=" + imgName + ", isAvailable="
				+ isAvailable + "]";
	}
}
