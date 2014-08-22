package com.lvl6.mobsters.dynamo.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.lvl6.mobsters.conditions.ConditionStrategyFactory;
import com.lvl6.mobsters.conditions.Director;
import com.lvl6.mobsters.conditions.IBooleanConditionBuilder;
import com.lvl6.mobsters.conditions.IConditionsBuilder;
import com.lvl6.mobsters.conditions.IIntConditionBuilder;
import com.lvl6.mobsters.conditions.IStringConditionBuilder;
import com.lvl6.mobsters.dynamo.MonsterForUser;

@Component
public class MonsterForUserRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<MonsterForUser, String>
	implements
		MonsterForUserRepository
{

	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterForUserRepositoryImpl.class);

	protected MonsterForUserRepositoryImpl()
	{
		super(MonsterForUser.class, "monsterForUserUuid", String.class);
	}

	@Override
	public List<MonsterForUser> findByUserIdAndMonsterForUserIdIn(
		final String userId,
		final Iterable<String> monsterForUserIds )
	{
		final List<AttributeValue> monsterForUserIdz = new ArrayList<>();
		final MonsterForUser hashKey = new MonsterForUser();
		hashKey.setUserId(userId);
		for (final String monsterForUserId : monsterForUserIds) {
			monsterForUserIdz.add(new AttributeValue().withS(monsterForUserId.toString()));
		}

		final DynamoDBQueryExpression<MonsterForUser> query =
			new DynamoDBQueryExpression<MonsterForUser>()
			.withHashKeyValues(hashKey)
				.withQueryFilterEntry("monsterForUserId",
					new Condition().withComparisonOperator(ComparisonOperator.IN)
						.withAttributeValueList(monsterForUserIdz))
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<MonsterForUser> monsterForUsersForUser = query(query);
		monsterForUsersForUser.loadAllResults();
		return monsterForUsersForUser;
	}

	@Override
	public List<MonsterForUser> findByUserIdAndMonsterForUserIdInOrTeamSlotNumAndUserId(
		final String userId,
		final Collection<String> monsterForUserIds,
		final Integer teamSlotNum )
	{
		final List<AttributeValue> monsterForUserIdz = new ArrayList<>();
		for (final String monsterForUserId : monsterForUserIds) {
			monsterForUserIdz.add(new AttributeValue().withS(monsterForUserId.toString()));
		}

		final MonsterForUser hashKey = new MonsterForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<MonsterForUser> query =
			new DynamoDBQueryExpression<MonsterForUser>()
			// .withIndexName("userIdGlobalIndex")
			.withHashKeyValues(hashKey)
				.withConditionalOperator(ConditionalOperator.OR)
				.withQueryFilterEntry("monsterForUserId",
					new Condition().withComparisonOperator(ComparisonOperator.IN)
						.withAttributeValueList(monsterForUserIdz))
				.withQueryFilterEntry(
					"teamSlotNum",
					new Condition().withComparisonOperator(ComparisonOperator.EQ)
						.withAttributeValueList(
							new AttributeValue().withN(teamSlotNum.toString())))
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<MonsterForUser> monstersForUser = query(query);
		monstersForUser.loadAllResults();
		return monstersForUser;
	}

	@Override
	public List<MonsterForUser> findByUserId( final String userId )
	{
		final MonsterForUser hashKey = new MonsterForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<MonsterForUser> query =
			new DynamoDBQueryExpression<MonsterForUser>()
			// .withIndexName("userIdGlobalIndex")
				.withHashKeyValues(hashKey)
				.withConsistentRead(true);

		LOG.info("Query: {}", query);
		final PaginatedQueryList<MonsterForUser> monsterForUsersForUser = query(query);
		monsterForUsersForUser.loadAllResults();
		return monsterForUsersForUser;
	}
	
	@Override
	public List<MonsterForUser> findByUserIdAndAll( 
		String userId, Director<MonsterForUserConditionBuilder> director )
	{
		return findByUserIdWithFilter(userId, director, ConditionalOperator.AND);
	}
	
	@Override
	public List<MonsterForUser> findByUserIdAndAny( 
		String userId, Director<MonsterForUserConditionBuilder> director )
	{
		return findByUserIdWithFilter(userId, director, ConditionalOperator.OR);
	}
	
	private List<MonsterForUser> findByUserIdWithFilter(
		final String userId,
		final Director<MonsterForUserConditionBuilder> director,
		final ConditionalOperator conjunction
	) {
		final MonsterForUser hashKey = new MonsterForUser();
		hashKey.setUserId(userId);

		final DynamoDBQueryExpression<MonsterForUser> query =
			new DynamoDBQueryExpression<MonsterForUser>()
				.withHashKeyValues(hashKey)
				.withConditionalOperator(conjunction);
		
		// This is the Bridge design pattern.  Each repository type has a ConditionBuilder specific to its
		// Entity's field names and types.  That implementation knows how to delegate to an IConditionStategy
		// There will be at least two such strategies, one that attaches Conditions to a Query's filter clause,
		// and another that attaches them to a Scan's filtering clause.  This is a query use case, so we inject
		// a QueryFilterConditionStrategy that is in turn wired to the DynamoDBQueryExpression being built.
		director.apply(
			new MonsterForUserConditionBuilderImpl(
				ConditionStrategyFactory.getQueryFilterBuilder(query)));
		
		return query(query);
	}
	
	// Use composition, not inheritance, so the IConditionStategy is the polymorphic root
	// of hierarchy with one small subtype for Queries Filters and another for Scan Filters.
	// To do the same with inheritance, the class below would have to be redundantly declared
	// and maintained twice.
	static class MonsterForUserConditionBuilderImpl implements MonsterForUserConditionBuilder {
		private final IConditionsBuilder builderContext;

		MonsterForUserConditionBuilderImpl(final IConditionsBuilder builderContext) {
			this.builderContext = builderContext;
		}

		@Override
		public MonsterForUserConditionBuilder monsterForUserUuid(
			Director<IStringConditionBuilder> director) 
		{
			builderContext.filterStringField("monsterForUserUuid", director);
			return this;
		}

		@Override
		public MonsterForUserConditionBuilder monsterId(
			Director<IIntConditionBuilder> director) 
		{
			builderContext.filterIntField("monsterId", director);
			return this;
		}

		@Override
		public MonsterForUserConditionBuilder currentExp(
			Director<IIntConditionBuilder> director) 
		{
			builderContext.filterIntField("currentExp", director);
			return this;
		}

		@Override
		public MonsterForUserConditionBuilder currentLvl(
			Director<IIntConditionBuilder> director) 
		{
			builderContext.filterIntField("currentLvl", director);
			return this;
		}

		@Override
		public MonsterForUserConditionBuilder currentHealth(
			Director<IIntConditionBuilder> director) 
		{
			builderContext.filterIntField("currentHealth", director);
			return this;
		}

		@Override
		public MonsterForUserConditionBuilder numPieces(
			Director<IIntConditionBuilder> director) 
		{
			builderContext.filterIntField("numPieces", director);
			return this;
		}

		@Override
		public MonsterForUserConditionBuilder complete(
			Director<IBooleanConditionBuilder> director) 
		{
			builderContext.filterBooleanField("complete", director);
			return this;
		}

		@Override
		public MonsterForUserConditionBuilder teamSlotNum(
			Director<IIntConditionBuilder> director) 
		{
			builderContext.filterIntField("teamSlotNum", director);
			return this;
		}
	}
}
