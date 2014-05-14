package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.UserDailyBonusRewardHistory;
@Component public class UserDailyBonusRewardHistoryRepository extends BaseDynamoRepository<UserDailyBonusRewardHistory>{
	public UserDailyBonusRewardHistoryRepository(){
		super(UserDailyBonusRewardHistory.class);
	}

}