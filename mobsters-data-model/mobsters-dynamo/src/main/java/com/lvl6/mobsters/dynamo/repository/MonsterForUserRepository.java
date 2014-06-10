package com.lvl6.mobsters.dynamo.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.lvl6.mobsters.dynamo.MonsterForUser;

@Component
public class MonsterForUserRepository extends
BaseDynamoRepository<MonsterForUser> {
    
    private static final Logger log = LoggerFactory
        .getLogger(MonsterForUserRepository.class);

    protected MonsterForUserRepository()
    {
        super(MonsterForUser.class);
        isActive = true;// for unit test
    }

    public List<MonsterForUser> findByUserIdAndId(
        final String userId,
        final Collection<String> monsterForUserIds )
    {
        final List<AttributeValue> monsterForUserIdz = new ArrayList<>();
        final MonsterForUser hashKey = new MonsterForUser();
        hashKey.setUserId(userId);
        for (final String monsterForUserId : monsterForUserIds) {
            monsterForUserIdz.add(new AttributeValue().withS(monsterForUserId.toString()));
        }

        final DynamoDBQueryExpression<MonsterForUser> query = new DynamoDBQueryExpression<MonsterForUser>()
            // .withIndexName("userIdGlobalIndex")
            .withHashKeyValues(hashKey)
            .withQueryFilterEntry(
                "monsterForUserId",
                new Condition().withComparisonOperator(
                    ComparisonOperator.IN).withAttributeValueList(
                        monsterForUserIdz)).withConsistentRead(true);

        MonsterForUserRepository.log.info("Query: {}", query);
        final PaginatedQueryList<MonsterForUser> monsterForUsersForUser = query(query);
        monsterForUsersForUser.loadAllResults();
        return monsterForUsersForUser;
    }
    
    public Map<String, MonsterForUser> findByUserIdAndIdOrTeamSlotNum (
        final String userId,
        Collection<String> monsterForUserIds,
        Integer teamSlotNum)
   {
        final List<AttributeValue> monsterForUserIdz = new ArrayList<>();
        for (final String monsterForUserId : monsterForUserIds) {
            monsterForUserIdz.add(new AttributeValue().withS(monsterForUserId.toString()));
        }
        
        final MonsterForUser hashKey = new MonsterForUser();
        hashKey.setUserId(userId);

        final DynamoDBQueryExpression<MonsterForUser> query = new DynamoDBQueryExpression<MonsterForUser>()
            // .withIndexName("userIdGlobalIndex")
            .withHashKeyValues(hashKey)
            .withConditionalOperator(ConditionalOperator.OR)
            .withQueryFilterEntry(
                "monsterForUserId",
                new Condition().withComparisonOperator(
                    ComparisonOperator.IN).withAttributeValueList(
                        monsterForUserIdz))
            .withQueryFilterEntry(
                "teamSlotNum",
                new Condition().withComparisonOperator(
                    ComparisonOperator.EQ).withAttributeValueList(
                        new AttributeValue().withN(
                            teamSlotNum.toString()))).withConsistentRead(true);

        MonsterForUserRepository.log.info("Query: {}", query);
        final PaginatedQueryList<MonsterForUser> monstersForUser = query(query);
        monstersForUser.loadAllResults();
        
        
        Map<String, MonsterForUser> userMonsterIdToMfu = new HashMap<String, MonsterForUser>();
        
        for (MonsterForUser mfu : monstersForUser) {
            userMonsterIdToMfu.put(mfu.getMonsterForUserId(), mfu);
        }
        return userMonsterIdToMfu;
    }
    
}
