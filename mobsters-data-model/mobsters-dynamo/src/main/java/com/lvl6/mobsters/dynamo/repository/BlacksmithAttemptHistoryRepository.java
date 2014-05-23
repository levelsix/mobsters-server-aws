package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.BlacksmithAttemptHistory;
@Component public class BlacksmithAttemptHistoryRepository extends BaseDynamoRepository<BlacksmithAttemptHistory>{
	public BlacksmithAttemptHistoryRepository(){
		super(BlacksmithAttemptHistory.class);
	}

}