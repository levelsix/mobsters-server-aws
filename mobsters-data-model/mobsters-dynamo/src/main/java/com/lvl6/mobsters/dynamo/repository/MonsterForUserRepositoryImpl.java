package com.lvl6.mobsters.dynamo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;

@Component
public class MonsterForUserRepositoryImpl extends BaseDynamoRepository<MonsterForUser>
    implements
        MonsterForUserRepository
{
    public MonsterForUserRepositoryImpl()
    {
        super(
            MonsterForUser.class);
    }

    @Override
    public List<MonsterForUser> findByUserIdAndId(
        final String userId,
        final Iterable<String> monsterIds )
    {
        final ArrayList<MonsterForUser> retVal = new ArrayList<MonsterForUser>();
        // TODO
        // final Builder retVal = ImmutableList.builder();
        for (final String monsterForUserId : monsterIds) {
            retVal.add(
	        load(
                    userId,
                    monsterForUserId));
        }
        return retVal;
    }

}
