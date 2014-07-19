package com.lvl6.mobsters.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName = "QuestForUser")
public class QuestForUser {
	// @DynamoDBIndexHashKey(globalSecondaryIndexName = "userIdGlobalIndex")
	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	// @DynamoDBIndexHashKey(globalSecondaryIndexName = "questIdGlobalIndex")
	@DynamoDBRangeKey(attributeName = "questId")
	private int questId;

	@DynamoDBVersionAttribute
	private Long version;

	private boolean redeemed = false;

	private boolean complete = false;

	public QuestForUser() {
	}

	public QuestForUser(String userId, int questId, boolean redeemed,
			boolean complete) {
		super();
		this.userId = userId;
		this.questId = questId;
		this.redeemed = redeemed;
		this.complete = complete;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@DynamoDBAttribute(attributeName="redeemed")
	public boolean isRedeemed() {
		return redeemed;
	}

	public void setRedeemed(boolean redeemed) {
		this.redeemed = redeemed;
	}

	@DynamoDBAttribute(attributeName="complete")
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + questId;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		QuestForUser other = (QuestForUser) obj;
		if (questId != other.questId) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "QuestForUser [version=" + version + ", userId=" + userId
				+ ", questId=" + questId + ", isRedeemed=" + redeemed
				+ ", isComplete=" + complete + "]";
	}

}