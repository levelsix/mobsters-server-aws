package com.lvl6.mobsters.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="AnimatedSpriteOffset")
public class AnimatedSpriteOffset {



	private String id;
	private Long version;

	
	private String imgName;
	private CoordinatePair offSet;
	public AnimatedSpriteOffset(){}
	public AnimatedSpriteOffset(String imgName, CoordinatePair offSet) {
		this.imgName = imgName;
		this.offSet = offSet;
	}


	@DynamoDBHashKey(attributeName = "id")
	@DynamoDBAutoGeneratedKey
	public String getId(){return id;}
	public void setId(String id){this.id = id;}


	@DynamoDBVersionAttribute
	public Long getVersion(){return version;}
	public void setVersion(Long version){this.version = version;}


	public String getImgName() {		return imgName;
	}

	public CoordinatePair getOffSet() {
		return offSet;
	}
 	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnimatedSpriteOffset other = (AnimatedSpriteOffset) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}