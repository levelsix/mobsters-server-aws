package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AnimatedSpriteOffset extends BasePersistentObject{

	
	private static final long serialVersionUID = 8547110329111943501L;
	@Column(name = "img_name")
	private String imgName;
	@Column(name = "off_set")
	private CoordinatePair offSet;
	public AnimatedSpriteOffset(){}
	public AnimatedSpriteOffset(String imgName, CoordinatePair offSet) {
		this.imgName = imgName;
		this.offSet = offSet;
	}

	public String getImgName() {
		return imgName;
	}

	public CoordinatePair getOffSet() {
		return offSet;
	}
}
