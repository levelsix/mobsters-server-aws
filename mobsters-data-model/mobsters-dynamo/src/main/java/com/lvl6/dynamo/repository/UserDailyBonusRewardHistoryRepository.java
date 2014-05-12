package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.UserDailyBonusRewardHistory;
public class UserDailyBonusRewardHistoryRepository extends BaseDynamoRepository<UserDailyBonusRewardHistory>{
	public UserDailyBonusRewardHistoryRepository(){
		super(UserDailyBonusRewardHistory.class);
	}

}