package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.BlacksmithAttemptHistory;
@Component public class BlacksmithAttemptHistoryRepository extends BaseDynamoRepository<BlacksmithAttemptHistory>{
	public BlacksmithAttemptHistoryRepository(){
		super(BlacksmithAttemptHistory.class);
	}

}