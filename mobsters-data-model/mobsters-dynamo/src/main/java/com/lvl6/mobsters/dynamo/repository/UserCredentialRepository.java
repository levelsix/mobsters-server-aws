package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.UserCredential;
@Component public class UserCredentialRepository extends BaseDynamoRepository<UserCredential>{
	public UserCredentialRepository(){
		super(UserCredential.class);
		isActive = true;//for unit test
	}

}