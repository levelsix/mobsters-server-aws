package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
@Component public class UserRepositoryImpl extends BaseDynamoItemRepositoryImpl<User>
	implements
		UserRepository
{
	public UserRepositoryImpl(){
		super(User.class);
	}

	@Override
	protected User getHashKeyObject(final String hashKey) {
		User retVal = new User();
		retVal.setId(hashKey);
		return retVal;
	}

}
