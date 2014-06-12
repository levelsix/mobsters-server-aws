package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;

@Component
public class PvpLeagueForUserRepository extends BaseDynamoRepositoryImpl<PvpLeagueForUser>
{
	public PvpLeagueForUserRepository()
	{
		super(
			PvpLeagueForUser.class);
		isActive = true;
	}

	private static final Logger log = LoggerFactory.getLogger(PvpLeagueForUserRepository.class);

	// select * from pvp_league_for_user where elo betweent (min, max) and shield_end_time < "timeOne"
	// and battle_end_time < "timeOne"
	public List<PvpLeagueForUser> getLeaguesByEloAndShieldEndTimeLessThanAndBattleEndTimeLessThan(
		final Integer minElo,
		final Integer maxElo,
		final Long maxShieldEndTime,
		final Long maxBattleEndTime,
		final Integer limit )
	{
		final Object[] args = { minElo, maxElo, maxShieldEndTime, maxBattleEndTime, limit };
		PvpLeagueForUserRepository.log.info(
			"Searching for pvpLeaguForUser minElo: {} maxElo: {} maxShieldEndTime: {} maxBattleEndTime: {} limit: {}",
			args);
		final DynamoDBScanExpression scan = new DynamoDBScanExpression();
		scan.addFilterCondition(
			"elo",
			new Condition().withComparisonOperator(
				ComparisonOperator.BETWEEN).withAttributeValueList(
				new AttributeValue().withN(minElo.toString()),
				new AttributeValue().withN(maxElo.toString())));
		scan.addFilterCondition(
			"shieldEndTime",
			new Condition().withComparisonOperator(
				ComparisonOperator.LT).withAttributeValueList(
				new AttributeValue().withN(maxShieldEndTime.toString())));
		scan.addFilterCondition(
			"inBattleShieldEndTime",
			new Condition().withComparisonOperator(
				ComparisonOperator.LT).withAttributeValueList(
				new AttributeValue().withN(maxBattleEndTime.toString())));
		scan.setLimit(limit);
		final List<PvpLeagueForUser> leagues = scan(scan);
		return leagues;
	}

}