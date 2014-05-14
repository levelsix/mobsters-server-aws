package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.BlacksmithAttempt;
@Component public class BlacksmithAttemptRepository extends BaseDynamoRepository<BlacksmithAttempt>{
	public BlacksmithAttemptRepository(){
		super(BlacksmithAttempt.class);
	}

}