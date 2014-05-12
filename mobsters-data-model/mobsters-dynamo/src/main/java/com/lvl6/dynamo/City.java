package com.lvl6.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="City")
public class City {



	private String id;
	private Long version;

	
	private String name;
	private String mapImgName;
	private CoordinatePair center;
	private String roadImgName;
	private String mapTmxName;
	private CoordinatePair roadImgCoords;
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




	@DynamoDBHashKey(attributeName = "id")
	@DynamoDBAutoGeneratedKey
	public String getId(){return id;}
	public void setId(String id){this.id = id;}


	@DynamoDBVersionAttribute
	public Long getVersion(){return version;}
	public void setVersion(Long version){this.version = version;}


	public String getName() {		return name;
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
