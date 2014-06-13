package com.lvl6.mobsters.dynamo;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

@DynamoDBTable(tableName = "MonsterForUserHistory")
public class MonsterForUserHistory {

    private String uId;

    private Date date;

    private Long version;

    private String mfuId;

    //for type: deleted by quest, d. by enhancing;
    //modified by quest, m. by task, m. by boosterpack
    private String t; 
    
    private String info;
    
    public MonsterForUserHistory() {}

    public MonsterForUserHistory( String uId, Date date, String mfuId, String t, String info )
	{
		super();
		this.uId = uId;
		this.date = date;
		this.mfuId = mfuId;
		this.t = t;
		this.info = info;
	}


	@DynamoDBHashKey(attributeName = "uId")
    public String getuId() {
        return uId;
    }

    public void setuId( String uId ) {
        this.uId = uId;
    }

    @DynamoDBRangeKey(attributeName = "date")
    public void setDate( Date date )
    {
    	this.date = date;
    }
    
    public Date getDate()
    {
    	return date;
    }
    
    @DynamoDBVersionAttribute
    public Long getVersion() {
        return version;
    }

    public void setVersion( Long version ) {
    	this.version = version;
    }

	public String getMfuId()
	{
		return mfuId;
	}

	public void setMfuId( String mfuId )
	{
		this.mfuId = mfuId;
	}

	public String getT()
	{
		return t;
	}

	public void setT( String t )
	{
		this.t = t;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo( String info )
	{
		this.info = info;
	}

}
