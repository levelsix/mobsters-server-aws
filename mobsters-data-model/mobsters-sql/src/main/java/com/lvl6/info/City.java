package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class City extends BasePersistentObject{

	@Column(name = "final")
	private static final long serialVersionUID = 8092805006560290613L;
	@Column(name = "name")
	private String name;
	@Column(name = "map_img_name")
	private String mapImgName;
	@Column(name = "center")
	private CoordinatePair center;
	@Column(name = "road_img_name")
	private String roadImgName;
	@Column(name = "map_tmx_name")
	private String mapTmxName;
	@Column(name = "road_img_coords")
	private CoordinatePair roadImgCoords;
	@Column(name = "attack_map_label_img_name")
	private String attackMapLabelImgName;	
	public City(){}
	public City(int id, String name, String mapImgName, CoordinatePair center,
			String roadImgName, String mapTmxName, CoordinatePair roadImgCoords,
			String attackMapLabelImgName) {
		super();
		this.name = name;
		this.mapImgName = mapImgName;
		this.center = center;
		this.roadImgName = roadImgName;
		this.mapTmxName = mapTmxName;
		this.roadImgCoords = roadImgCoords;
		this.attackMapLabelImgName = attackMapLabelImgName;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapImgName() {
		return mapImgName;
	}

	public void setMapImgName(String mapImgName) {
		this.mapImgName = mapImgName;
	}

	public CoordinatePair getCenter() {
		return center;
	}

	public void setCenter(CoordinatePair center) {
		this.center = center;
	}

	public String getRoadImgName() {
		return roadImgName;
	}

	public void setRoadImgName(String roadImgName) {
		this.roadImgName = roadImgName;
	}

	public String getMapTmxName() {
		return mapTmxName;
	}

	public void setMapTmxName(String mapTmxName) {
		this.mapTmxName = mapTmxName;
	}

	public CoordinatePair getRoadImgCoords() {
		return roadImgCoords;
	}

	public void setRoadImgCoords(CoordinatePair roadImgCoords) {
		this.roadImgCoords = roadImgCoords;
	}

	public String getAttackMapLabelImgName() {
		return attackMapLabelImgName;
	}

	public void setAttackMapLabelImgName(String attackMapLabelImgName) {
		this.attackMapLabelImgName = attackMapLabelImgName;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", mapImgName=" + mapImgName
				+ ", center=" + center + ", roadImgName=" + roadImgName
				+ ", mapTmxName=" + mapTmxName + ", roadImgCoords=" + roadImgCoords
				+ ", attackMapLabelImgName=" + attackMapLabelImgName + "]";
	}
	
}
