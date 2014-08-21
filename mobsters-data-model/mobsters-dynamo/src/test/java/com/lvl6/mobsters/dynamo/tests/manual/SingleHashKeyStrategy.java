package com.lvl6.mobsters.dynamo.tests.manual;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.lvl6.mobsters.dynamo.tests.manual.SingleHashKeyStrategy.ChildOne;
import com.lvl6.mobsters.dynamo.tests.manual.SingleHashKeyStrategy.ParentOne;

/**
 * Strategy class that exercises
 * 
 * @author John
 */
@Component
@Qualifier("VariantOne")
public class SingleHashKeyStrategy implements VariantStrategy<ParentOne, ChildOne>
{
	@Autowired
	SingleHashKeyRepository pcRepo;

	private String currentParentHashKey = null;
	private ParentOne currentParent = null;
	private final ArrayList<ChildOne> currentChildren = new ArrayList<ChildOne>(0);

	@Override
	public String getNextParent( final int expectedChildCount )
	{
		currentChildren.clear();
		currentChildren.ensureCapacity(expectedChildCount);

		currentParent = new ParentOne();
		pcRepo.saveParent(currentParent);

		currentParentHashKey = currentParent.getId();
		return currentParentHashKey;
	}

	@Override
	public BaseParentChildRepository<ParentOne, ChildOne> getRepository()
	{
		return pcRepo;
	}

	@Override
	public ChildDataAttrs addNextChild()
	{
		final ChildOne retVal = new ChildOne();
		// retVal.setUserId(currentParentHashKey);
		currentChildren.add(retVal);
		return retVal;
	}

	@Override
	public void saveChildren()
	{
		pcRepo.saveChildren(currentParentHashKey, currentChildren);
		currentParentHashKey = null;
		currentChildren.clear();
	}

	@DynamoDBTable(tableName = "ParentOne")
	public static class ParentOne
	{
		@DynamoDBAutoGeneratedKey
		@DynamoDBHashKey(attributeName = "id")
		private String id;

		@DynamoDBVersionAttribute
		private Long version;

		@DynamoDBAttribute(attributeName = "name")
		private String name;

		public ParentOne()
		{
			super();
		}

		public ParentOne( final String id /* , String name */)
		{
			super();

			this.id = id;
			// this.name = name;
		}

		public String getId()
		{
			return id;
		}

		/**
		 * Replaces this object's id attribute with a new caller-provided
		 * value..
		 * 
		 * The semantics of this operation, especially in terms of the large
		 * universe of "other" objects, is not terribly well defined. There may
		 * be entity types that are highly intolerant of their Id changing
		 * during the course of their existence, as Id storage is the means of
		 * creating links between a visible object and any other object that
		 * depends on it by reference.
		 * 
		 * @param id
		 */
		public void setId( final String id )
		{
			this.id = id;
		}

		public Long getVersion()
		{
			return version;
		}

		public void setVersion( final Long version )
		{
			this.version = version;
		}

		public String getName()
		{
			return name;
		}

		public void setName( final String name )
		{
			this.name = name;
		}
	}

	@DynamoDBTable(tableName = "ChildOne")
	public static class ChildOne extends ChildDataAttrs
	{
		private String userId;

		private String id;
		private Long version;

		public ChildOne()
		{
			super();
		}

		public ChildOne( final String userId, final String id, final String name,
		    final int monsterId, final int currentExp, final int currentLvl,
		    final int currentHealth, final int numPieces, final boolean isComplete,
		    final Date combineStartTime, final int teamSlotNum, final String sourceOfPieces,
		    final double tradeValue )
		{
			super(name, monsterId, currentExp, currentLvl, currentHealth, numPieces,
			    isComplete, combineStartTime, teamSlotNum, sourceOfPieces, tradeValue);
			this.userId = userId;
			this.id = id;
		}

		// @DynamoDBAutoGeneratedKey
		@DynamoDBRangeKey(attributeName = "id")
		public String getId()
		{
			return id;
		}

		public void setId( final String id )
		{
			this.id = id;
		}

		@DynamoDBVersionAttribute
		public Long getVersion()
		{
			return version;
		}

		public void setVersion( final Long version )
		{
			this.version = version;
		}

		@DynamoDBHashKey(attributeName = "userId")
		public String getUserId()
		{
			return userId;
		}

		public void setUserId( final String userId )
		{
			this.userId = userId;
		}

		@Override
		@DynamoDBAttribute
		public String getName()
		{
			return super.getName();
		}

		@Override
		public void setName( final String name )
		{
			super.setName(name);
		}

		@Override
		@DynamoDBAttribute
		public int getMonsterId()
		{
			return super.getMonsterId();
		}

		@Override
		public void setMonsterId( final int monsterId )
		{
			super.setMonsterId(monsterId);
		}

		@Override
		@DynamoDBAttribute
		public int getCurrentExp()
		{
			return super.getCurrentExp();
		}

		@Override
		public void setCurrentExp( final int currentExp )
		{
			super.setCurrentExp(currentExp);
		}

		@Override
		@DynamoDBAttribute
		public int getCurrentLvl()
		{
			return super.getCurrentLvl();
		}

		@Override
		public void setCurrentLvl( final int currentLvl )
		{
			super.setCurrentLvl(currentLvl);
		}

		@Override
		@DynamoDBAttribute
		public int getCurrentHealth()
		{
			return super.getCurrentHealth();
		}

		@Override
		public void setCurrentHealth( final int currentHealth )
		{
			super.setCurrentHealth(currentHealth);
		}

		@Override
		@DynamoDBAttribute
		public int getNumPieces()
		{
			return super.getNumPieces();
		}

		@Override
		public void setNumPieces( final int numPieces )
		{
			super.setNumPieces(numPieces);
		}

		@Override
		@DynamoDBAttribute
		public boolean isComplete()
		{
			return super.isComplete();
		}

		@Override
		public void setComplete( final boolean isComplete )
		{
			super.setComplete(isComplete);
		}

		@Override
		@DynamoDBAttribute
		public Date getCombineStartTime()
		{
			return super.getCombineStartTime();
		}

		@Override
		public void setCombineStartTime( final Date combineStartTime )
		{
			super.setCombineStartTime(combineStartTime);
		}

		@Override
		@DynamoDBAttribute
		public int getTeamSlotNum()
		{
			return super.getTeamSlotNum();
		}

		@Override
		public void setTeamSlotNum( final int teamSlotNum )
		{
			super.setTeamSlotNum(teamSlotNum);
		}

		@Override
		@DynamoDBAttribute
		public String getSourceOfPieces()
		{
			return super.getSourceOfPieces();
		}

		@Override
		public void setSourceOfPieces( final String sourceOfPieces )
		{
			super.setSourceOfPieces(sourceOfPieces);
		}

		@Override
		@DynamoDBAttribute
		public double getTradeInValue()
		{
			return super.getTradeInValue();
		}

		@Override
		public void setTradeInValue( final double tradeValue )
		{
			super.setTradeInValue(tradeValue);
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = (prime * result)
			    + ((id == null) ? 0 : id.hashCode());
			result = (prime * result)
			    + ((userId == null) ? 0 : userId.hashCode());
			return result;
		}

		@Override
		public boolean equals( final Object obj )
		{
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ChildOne other = (ChildOne) obj;
			if (id == null) {
				if (other.id != null) {
					return false;
				}
			} else if (!id.equals(other.id)) {
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
	}
}
