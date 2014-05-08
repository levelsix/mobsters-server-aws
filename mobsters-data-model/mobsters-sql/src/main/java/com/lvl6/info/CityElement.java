package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CityElement extends BasePersistentObject{	

	
	private static final long serialVersionUID = 4626120437231136146L;	

	@Column(name = "city_id")
	private int cityId;
	@Column(name = "asset_id")
	private int assetId;
	//@Column(name = "good_name")
//	private String goodName;
	@Column(name = "type")
	private String type;
	@Column(name = "coords")
	private CoordinatePair coords;
	@Column(name = "x_length")
	private float xLength;
	@Column(name = "y_length")
	private float yLength;
	@Column(name = "img_good")
	private String imgGood;
	@Column(name = "orientation")
	private String orientation;
	@Column(name = "sprite_coords")
	private CoordinatePair spriteCoords;	
	public CityElement(){}
	public CityElement(int cityId, int assetId, String type,
			CoordinatePair coords, float xLength, float yLength,
			String imgGood, String orientation, CoordinatePair spriteCoords) {
		super();
		this.cityId = cityId;
		this.xLength = xLength;
		this.yLength = yLength;
		this.imgGood = imgGood;
		this.orientation = orientation;
		this.spriteCoords = spriteCoords;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CoordinatePair getCoords() {
		return coords;
	}

	public void setCoords(CoordinatePair coords) {
		this.coords = coords;
	}

	public float getxLength() {
		return xLength;
	}

	public void setxLength(float xLength) {
		this.xLength = xLength;
	}

	public float getyLength() {
		return yLength;
	}

	public void setyLength(float yLength) {
		this.yLength = yLength;
	}

	public String getImgGood() {
		return imgGood;
	}

	public void setImgGood(String imgGood) {
		this.imgGood = imgGood;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public CoordinatePair getSpriteCoords() {
		return spriteCoords;
	}

	public void setSpriteCoords(CoordinatePair spriteCoords) {
		this.spriteCoords = spriteCoords;
	}
	@Override
	public String toString() {
		return "CityElement [cityId=" + cityId + ", assetId=" + assetId + ", type=" + type + ", coords="
				+ coords + ", xLength=" + xLength + ", yLength=" + yLength + ", imgGood=" + imgGood
				+ ", orientation=" + orientation + ", spriteCoords=" + spriteCoords + "]";
	}


	
}
