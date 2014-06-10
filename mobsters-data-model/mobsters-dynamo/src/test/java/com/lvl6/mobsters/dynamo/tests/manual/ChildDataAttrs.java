package com.lvl6.mobsters.dynamo.tests.manual;

import java.util.Date;

/**
 * Data attributes to give the child classes some "bulk". The properties defined
 * here provide 118 bytes worth of property names. Because of differences in
 * which properties are declared and/or how those properties are annotated,
 * declaration of all key and version attributes is deferred for subclasses to
 * address. The names "id", "userId", and "version" collectively consume an
 * additional 15 bytes, for a total property name allocation cost of 133 bytes.
 * Property values allocation costs are presumed to be fixed for all non-string
 * data attributes. The size requirement for the non-string subset of attribute
 * values prescribed here is estimated to require a total of 56 bytes. The two
 * remaining String attributes will have variable length, but are being treated
 * here as having averages lengths of 12 and 24, for a total of 36 bytes. It
 * bears stating that only one of these two strings, "sourceOfPieces" is used
 * (or even defined) in the model that was replicated as a representative
 * example for this test case. The "name" field was added as a precaution to
 * have at least one String unencumbered by any present-day semantics. With a
 * final 8 bytes coming from the version attribute, we have a total estimate of
 * child object size that only excludes the size of "userId" and "id" properties
 * and adds up to... 133 bytes + 56 bytes + 36 bytes + 8 bytes = 233 bytes
 * Finally, the base-class-identifier storage requirements will vary from
 * strategy-to-strategy. To calculate their contribution, treat UUID identifiers
 * as 32 bytes, and long identifiers as 8 bytes. The strategies in use call for
 * either a pair of UUID values (one for user and one for monster instance) or
 * one UUID value (for user) and one long value (for monster instance). The
 * final possible expected DynamoDB storage size for instances of this object is
 * either: 1) 233 bytes + 64 bytes = 297 bytes (for a pair of two UUIDs) 2) 233
 * bytes + 40 bytes = 273 bytes (for UUID,long pair)
 * 
 * @author John
 */
public abstract class ChildDataAttrs
{

	private String name;

	private int monsterId;

	private int currentExp;

	private int currentLvl;

	private int currentHealth;

	private int numPieces;

	private boolean isComplete;

	private Date combineStartTime;

	private int teamSlotNum;

	private String sourceOfPieces;

	private double tradeValue;

	public ChildDataAttrs()
	{
		super();
	}

	protected ChildDataAttrs( final String name, final int monsterId, final int currentExp,
	    final int currentLvl, final int currentHealth, final int numPieces,
	    final boolean isComplete, final Date combineStartTime, final int teamSlotNum,
	    final String sourceOfPieces, final double tradeValue )
	{
		super();
		this.name = name;
		this.monsterId = monsterId;
		this.currentExp = currentExp;
		this.currentLvl = currentLvl;
		this.currentHealth = currentHealth;
		this.numPieces = numPieces;
		this.isComplete = isComplete;
		this.combineStartTime = combineStartTime;
		this.teamSlotNum = teamSlotNum;
		this.sourceOfPieces = sourceOfPieces;
		this.tradeValue = tradeValue;
	}

	public String getName()
	{
		return name;
	}

	public int getMonsterId()
	{
		return monsterId;
	}

	public int getCurrentExp()
	{
		return currentExp;
	}

	public int getCurrentLvl()
	{
		return currentLvl;
	}

	public int getCurrentHealth()
	{
		return currentHealth;
	}

	public int getNumPieces()
	{
		return numPieces;
	}

	public boolean isComplete()
	{
		return isComplete;
	}

	public Date getCombineStartTime()
	{
		return combineStartTime;
	}

	public int getTeamSlotNum()
	{
		return teamSlotNum;
	}

	public String getSourceOfPieces()
	{
		return sourceOfPieces;
	}

	public double getTradeValue()
	{
		return tradeValue;
	}

	public void setName( final String name )
	{
		this.name = name;
	}

	public void setMonsterId( final int monsterId )
	{
		this.monsterId = monsterId;
	}

	public void setCurrentExp( final int currentExp )
	{
		this.currentExp = currentExp;
	}

	public void setCurrentLvl( final int currentLvl )
	{
		this.currentLvl = currentLvl;
	}

	public void setCurrentHealth( final int currentHealth )
	{
		this.currentHealth = currentHealth;
	}

	public void setNumPieces( final int numPieces )
	{
		this.numPieces = numPieces;
	}

	public void setComplete( final boolean isComplete )
	{
		this.isComplete = isComplete;
	}

	public void setCombineStartTime( final Date combineStartTime )
	{
		this.combineStartTime = combineStartTime;
	}

	public void setTeamSlotNum( final int teamSlotNum )
	{
		this.teamSlotNum = teamSlotNum;
	}

	public void setSourceOfPieces( final String sourceOfPieces )
	{
		this.sourceOfPieces = sourceOfPieces;
	}

	public void setTradeValue( final double tradeValue )
	{
		this.tradeValue = tradeValue;
	}

}
