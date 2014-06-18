package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanForUser;
@Component public class ClanForUserRepositoryImpl extends BaseDynamoRepositoryImpl<ClanForUser> implements ClanForUserRepository{
	public ClanForUserRepositoryImpl(){
		super(ClanForUser.class);
	}
 
}