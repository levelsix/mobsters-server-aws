package com.lvl6.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName="QuestJob")
public class QuestJob {



	private String id;
	private Long version;

	

	private int questId;
	private String questJobType;
	private String description;
	private int staticDataId;
	private int quantity;	
	//how this quest job is ordered among other quest jobs
	//with the same quest id

	private int priority;	
	//could be 0

	private int cityId;	
	//could be 0

	private int cityAssetNum;	
	public QuestJob(){}
	public QuestJob(int id, int questId, String questJobType,
			String description, int staticDataId, int quantity, int priority,
			int cityId, int cityAssetNum) {
		super();
		this.questId = questId;
		this.questJobType = questJobType;
		this.description = description;
		this.staticDataId = staticDataId;
		this.quantity = quantity;
		this.priority = priority;
		this.cityId = cityId;
		this.cityAssetNum = cityAssetNum;
	}




	@DynamoDBHashKey(attributeName = "id")
	@DynamoDBAutoGeneratedKey
	public String getId(){return id;}
	public void setId(String id){this.id = id;}


	@DynamoDBVersionAttribute
	public Long getVersion(){return version;}
	public void setVersion(Long version){this.version = version;}


	public int getQuestId() {		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public String getQuestJobType() {
		return questJobType;
	}

	public void setQuestJobType(String questJobType) {
		this.questJobType = questJobType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStaticDataId() {
		return staticDataId;
	}

	public void setStaticDataId(int staticDataId) {
		this.staticDataId = staticDataId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getCityAssetNum() {
		return cityAssetNum;
	}

	public void setCityAssetNum(int cityAssetNum) {
		this.cityAssetNum = cityAssetNum;
	}

	@Override
	public String toString() {
		return "QuestJob [id=" + id + ", questId=" + questId
				+ ", questJobType=" + questJobType + ", description="
				+ description + ", staticDataId=" + staticDataId
				+ ", quantity=" + quantity + ", priority=" + priority
				+ ", cityId=" + cityId + ", cityAssetNum=" + cityAssetNum + "]";
	}
	
}
