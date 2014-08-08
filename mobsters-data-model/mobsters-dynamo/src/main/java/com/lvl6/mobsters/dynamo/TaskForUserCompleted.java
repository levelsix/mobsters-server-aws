package com.lvl6.mobsters.dynamo;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.lvl6.mobsters.utility.attachment.AtomicExtensibleObject;
import com.lvl6.mobsters.utility.attachment.ExtensibleObject;

@DynamoDBTable(
	tableName = "TaskForUserCompleted")
public class TaskForUserCompleted implements ExtensibleObject
{
	// Non-injectable default implementation unless/until we need to support runtime
	// concrete interchangability.
	private final ExtensibleObject attachments;
	
	private Long version;

	private String userId;

	private int taskId;

	private Date timeOfEntry;

	public TaskForUserCompleted()
	{
		// Select default implementation if not is provided
		attachments = new AtomicExtensibleObject();
	}
			
	public TaskForUserCompleted(ExtensibleObject attachments) {
		if (attachments == null) {
			this.attachments = new AtomicExtensibleObject();
		} else {
			this.attachments = attachments;
		}
	}


	public TaskForUserCompleted(
		final String userId, final int taskId, final Date timeOfEntry, final ExtensibleObject attachments )
	{
		super();
		this.userId = userId;
		this.taskId = taskId;
		this.timeOfEntry = timeOfEntry;
		if (attachments == null) {
			this.attachments = new AtomicExtensibleObject();
		} else {
			this.attachments = attachments;
		}
	}
	
	@DynamoDBVersionAttribute
	public Long getVersion()
	{
		return version;
	}

	public void setVersion( Long version )
	{
		this.version = version;
	}

	@DynamoDBHashKey(
		attributeName = "userId")
	public String getUserId()
	{
		return userId;
	}

	public void setUserId( String userId )
	{
		this.userId = userId;
	}

	@DynamoDBRangeKey(
		attributeName = "taskId")
	public int getTaskId()
	{
		return taskId;
	}

	public void setTaskId( int taskId )
	{
		this.taskId = taskId;
	}
	
	// Temporary redundancy to facilitate Templated rangeId in BaseDynamoCollectionRepository signature.
	public void setTaskId( Integer taskId ) {
		this.taskId = taskId.intValue();
	}

	public Date getTimeOfEntry()
	{
		return timeOfEntry;
	}

	public void setTimeOfEntry( Date timeOfEntry )
	{
		this.timeOfEntry = timeOfEntry;
	}

	@Override
	@DynamoDBIgnore
	public <T> T getAttachment(Class<T> attachmentClass) {
		return this.attachments.getAttachment(attachmentClass);
	}

	@Override
	public <T> void putAttachment(Class<T> attachmentClass, T attachmentObject) {
		this.attachments.putAttachment(attachmentClass, attachmentObject);
	}

	@Override
	public <T> T clearAttachment(Class<T> attachmentClass) {
		return this.attachments.clearAttachment(attachmentClass);
	}

	@Override
	public String toString()
	{
		return "TaskForUserCompleted [userId="
			+ userId
			+ ", taskId="
			+ taskId
			+ ", timeOfEntry="
			+ timeOfEntry
			+ "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = prime
			+ taskId;
		result = prime
			* result
			+ ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskForUserCompleted other = (TaskForUserCompleted) obj;
		if (taskId != other.taskId)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
