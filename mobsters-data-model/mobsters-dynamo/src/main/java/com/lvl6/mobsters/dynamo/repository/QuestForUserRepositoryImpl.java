package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.lvl6.mobsters.conditions.ConditionStrategyFactory;
import com.lvl6.mobsters.conditions.Director;
import com.lvl6.mobsters.conditions.IBooleanConditionBuilder;
import com.lvl6.mobsters.conditions.IConditionsBuilder;
import com.lvl6.mobsters.conditions.IIntConditionBuilder;
import com.lvl6.mobsters.dynamo.QuestForUser;

@Component
public class QuestForUserRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<QuestForUser, Integer>
	implements QuestForUserRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(QuestForUserRepositoryImpl.class);
	
	public QuestForUserRepositoryImpl()
	{
		super(QuestForUser.class, "questId", Integer.TYPE);
	}

	/*
	@Override
	public List<QuestForUser> findByUserIdAndIsCompleteAndQuestIdIn(
		final String userId,
		final boolean isComplete,
		final Collection<Integer> questIds )
	{
		final QuestForUser hashKey = new QuestForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<QuestForUser> query =
			new DynamoDBQueryExpression<QuestForUser>()
				.withHashKeyValues(hashKey)
				.withConditionalOperator(conjunction);

		for (final Integer quest : questIds) {
			questIdz.add(new AttributeValue().withN(quest.toString()));
		}

		final DynamoDBQueryExpression<QuestForUser> query =
			new DynamoDBQueryExpression<QuestForUser>()
				//.withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withQueryFilterEntry(
						"isComplete",
						new Condition().withComparisonOperator(
								ComparisonOperator.NE).withAttributeValueList(
								new AttributeValue()
										.withN(getBoolean(isComplete))))
				.withQueryFilterEntry(
						"questId",
						new Condition().withComparisonOperator(
								ComparisonOperator.IN).withAttributeValueList(
								questIdz)).withConsistentRead(true);
		QuestForUserRepositoryImpl.log.info(
			"Query: {}",
			query);
		final PaginatedQueryList<QuestForUser> questsForUser = query(query);
		questsForUser.loadAllResults();
		return questsForUser;
	}
	*/

	@Override
	public List<QuestForUser> findByUserId( String userId )
	{
		return loadAll(userId);
	}

	@Override
	public List<QuestForUser> findByUserIdAndAll( 
		final String userId, final Director<QuestForUserConditionBuilder> director )
	{
		return findByUserIdWithFilter(userId, director, ConditionalOperator.AND);
	}

	@Override
	public List<QuestForUser> findByUserIdAndAny( 
		final String userId, final Director<QuestForUserConditionBuilder> director )
	{
		return findByUserIdWithFilter(userId, director, ConditionalOperator.OR);
	}
	
	private List<QuestForUser> findByUserIdWithFilter( 
		final String userId, 
		final Director<QuestForUserConditionBuilder> director, 
		final ConditionalOperator conjunction ) 
	{
		final QuestForUser hashKey = new QuestForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<QuestForUser> query =
			new DynamoDBQueryExpression<QuestForUser>()
				.withHashKeyValues(hashKey)
				.withConditionalOperator(conjunction);
		
		// This is the Bridge design pattern.  Each repository type has a ConditionBuilder specific to its
		// Entity's field names and types.  That implementation knows how to delegate to an IConditionStategy
		// There will be at least two such strategies, one that attaches Conditions to a Query's filter clause,
		// and another that attaches them to a Scan's filtering clause.  This is a query use case, so we inject
		// a QueryFilterConditionStrategy that is in turn wired to the DynamoDBQueryExpression being built.
		director.apply(
			new QuestForUserConditionBuilderImpl(
				ConditionStrategyFactory.getQueryFilterBuilder(query)));
		
		return query(query);
	}
	
	// Use composition, not inheritance, so the IConditionStategy is the polymorphic root
	// of hierarchy with one small subtype for Queries Filters and another for Scan Filters.
	// To do the same with inheritance, the class below would have to be redundantly declared
	// and maintained twice.
	static class QuestForUserConditionBuilderImpl implements QuestForUserConditionBuilder {
		private final IConditionsBuilder builderContext;

		QuestForUserConditionBuilderImpl(final IConditionsBuilder builderContext) {
			this.builderContext = builderContext;
		}

		@Override
		public QuestForUserConditionBuilder complete(Director<IBooleanConditionBuilder> director) {
			builderContext.filterBooleanField("complete", director);
			return this;
		}

		@Override
		public QuestForUserConditionBuilder redeemed(Director<IBooleanConditionBuilder> director) {
			builderContext.filterBooleanField("redeemed", director);
			return this;
		}

		@Override
		public QuestForUserConditionBuilder questId(Director<IIntConditionBuilder> director) {
			builderContext.filterIntField("questId", director);
			return this;
		}
	}

	// NOTE: One of the indices implied below is redundant with the base table
	//       definition, and the other two are probably invalid unless 
	//       userId+redeemed and userId+complete are unique tuples, which seems
	//       unlikely to be true.  Filters are adequate for separating true/false
	//       sets on this note.
	@Override
	public List<LocalSecondaryIndex> getLocalIndexes()
	{
		/*
		//isCompleteIndex
		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		indexKeySchema.add(new KeySchemaElement("questId", KeyType.RANGE));
		indexes.add(new LocalSecondaryIndex()
			.withIndexName("questIdIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));
		//isRedeemedIndex
		indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		indexKeySchema.add(new KeySchemaElement("isRedeemed", KeyType.RANGE));
		indexes.add(new LocalSecondaryIndex()
			.withIndexName("isRedeemedIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));
		//isCompleteIndex
				indexKeySchema = new ArrayList<KeySchemaElement>();
				indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
				indexKeySchema.add(new KeySchemaElement("isComplete", KeyType.RANGE));
				indexes.add(new LocalSecondaryIndex()
					.withIndexName("isRedeemedIndex")
					.withKeySchema(indexKeySchema)
					.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));
		 */
		return null;
	}

	// TODO: Is there a use case for getting data from this table across all users, but grouped
	//       by questId?  There may be a global secondary index that has value if so, but be aware
	//       that Dynamo might impose a size limit on secondary collection and there are more users
	//       per quest Id than there are quests per user.
	
	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes(){
/*		List<GlobalSecondaryIndex> indexes = new ArrayList<>();
		//userId
		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("userId", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex()
			.withIndexName("userIdGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL))
			.withProvisionedThroughput(provisionedThroughput));*/

		//questId
/*		indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement("questId", KeyType.HASH));
		indexes.add(new GlobalSecondaryIndex()
			.withIndexName("questIdGlobalIndex")
			.withKeySchema(indexKeySchema)
			.withProjection(new Projection().withProjectionType(ProjectionType.ALL))
			.withProvisionedThroughput(provisionedThroughput));
		*/
		return null;
	}
}