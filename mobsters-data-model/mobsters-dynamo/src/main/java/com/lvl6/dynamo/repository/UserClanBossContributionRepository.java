package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.UserClanBossContribution;
@Component public class UserClanBossContributionRepository extends BaseDynamoRepository<UserClanBossContribution>{
	public UserClanBossContributionRepository(){
		super(UserClanBossContribution.class);
	}

}