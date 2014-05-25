package com.lvl6.mobsters.dynamo.repository;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
@Component public class PvpLeagueForUserRepository extends BaseDynamoRepository<PvpLeagueForUser>{
	public PvpLeagueForUserRepository(){
		super(PvpLeagueForUser.class);
		isActive = true;
	}
	
	
	
	private static final Logger log = LoggerFactory.getLogger(PvpLeagueForUserRepository.class);
	
	//select * from pvp_league_for_user where elo betweent (min, max) and shield_end_time < "timeOne" and battle_end_time < "timeOne"
	public List<PvpLeagueForUser> getLeaguesByEloAndShieldEndTimeLessThanAndBattleEndTimeLessThan(
			Integer minElo, 
			Integer maxElo, 
			Long maxShieldEndTime, 
			Long maxBattleEndTime,
			Integer limit){
		Object[] args = {minElo, maxElo, maxShieldEndTime, maxBattleEndTime, limit};
		log.info("Searching for pvpLeaguForUser minElo: {} maxElo: {} maxShieldEndTime: {} maxBattleEndTime: {} limit: {}", args);
		DynamoDBScanExpression scan = new DynamoDBScanExpression();
		scan.addFilterCondition("elo", new Condition()
			.withComparisonOperator(ComparisonOperator.BETWEEN)
			.withAttributeValueList(
					new AttributeValue().withN(minElo.toString()),
					new AttributeValue().withN(maxElo.toString())));
		scan.addFilterCondition("shieldEndTime", new Condition()
			.withComparisonOperator(ComparisonOperator.LT)
			.withAttributeValueList(
					new AttributeValue().withN(maxShieldEndTime.toString())));
		scan.addFilterCondition("inBattleShieldEndTime", new Condition()
			.withComparisonOperator(ComparisonOperator.LT)
			.withAttributeValueList(
					new AttributeValue().withN(maxBattleEndTime.toString())));
		scan.setLimit(limit);
		List<PvpLeagueForUser> leagues  = getMapper().scan(PvpLeagueForUser.class, scan);
		return leagues;
	}
	
	
	
	
	
	@Override
	protected void createTable() {
		try {
		log.info("Creating Dynamo table {}", getTableName());
		ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("userId").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("pvpLeagueId").withAttributeType("S"));
		
		ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
		ks.add(new KeySchemaElement().withAttributeName("userId").withKeyType(KeyType.HASH));
		ks.add(new KeySchemaElement().withAttributeName("pvpLeagueId").withKeyType(KeyType.RANGE));
		
		  
		CreateTableRequest request = new CreateTableRequest()
		    .withTableName(getTableName())
		    .withAttributeDefinitions(attributeDefinitions)
		    .withKeySchema(ks)
		    .withProvisionedThroughput(provisionedThroughput);
			List<GlobalSecondaryIndex> globalIndexes = getGlobalIndexes();
			if(globalIndexes != null && !globalIndexes.isEmpty()) {
				request.withGlobalSecondaryIndexes(globalIndexes);
			}
			List<LocalSecondaryIndex> localIndexes = getLocalIndexes();
			if(localIndexes != null && !localIndexes.isEmpty()) {
				request.withLocalSecondaryIndexes(localIndexes);
			}
		    log.info("Creating table: {}", request);
		CreateTableResult result = client.createTable(request);
		log.info("Create table result: {}", result);
		}catch(Throwable e) {
			log.error("Error creating Dynamo table {}", getTableName(), e);
			throw e;
		}
	}
	
}