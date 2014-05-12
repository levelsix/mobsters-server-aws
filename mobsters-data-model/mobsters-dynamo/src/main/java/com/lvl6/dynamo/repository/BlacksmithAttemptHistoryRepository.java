package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.BlacksmithAttemptHistory;
public class BlacksmithAttemptHistoryRepository extends BaseDynamoRepository<BlacksmithAttemptHistory>{
	public BlacksmithAttemptHistoryRepository(){
		super(BlacksmithAttemptHistory.class);
	}

}