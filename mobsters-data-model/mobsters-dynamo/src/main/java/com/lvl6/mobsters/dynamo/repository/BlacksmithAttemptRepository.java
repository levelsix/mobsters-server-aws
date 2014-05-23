package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.BlacksmithAttempt;
@Component public class BlacksmithAttemptRepository extends BaseDynamoRepository<BlacksmithAttempt>{
	public BlacksmithAttemptRepository(){
		super(BlacksmithAttempt.class);
	}

}