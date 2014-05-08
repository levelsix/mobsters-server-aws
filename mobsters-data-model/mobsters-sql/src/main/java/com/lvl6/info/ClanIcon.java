package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ClanIcon extends BasePersistentObject{

	
	private static final long serialVersionUID = -3648830914482901288L;	

	@Column(name = "img_name")
	private String imgName;
	@Column(name = "is_available")
	private boolean isAvailable;	
	
	public ClanIcon() {
		super();
	}

	public ClanIcon(int id, String imgName, boolean isAvailable) {
		super();
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
