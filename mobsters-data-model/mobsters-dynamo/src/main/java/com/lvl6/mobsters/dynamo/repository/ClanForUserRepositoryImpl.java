package com.lvl6.mobsters.dynamo.repository;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.ClanForUser;
@Component public class ClanForUserRepositoryImpl extends BaseDynamoRepositoryImpl<ClanForUser> implements ClanForUserRepository{
	
	private static final Logger log = LoggerFactory
		.getLogger(ClanForUserRepositoryImpl.class);
	
	public ClanForUserRepositoryImpl(){
		super(ClanForUser.class);
	}

	@Override
	public List<ClanForUser> findByUserId( String userId )
	{
		final ClanForUser hashKey = new ClanForUser();
		hashKey.setUserId(userId);
		
		final DynamoDBQueryExpression<ClanForUser> query =
			new DynamoDBQueryExpression<ClanForUser>()
			//.withIndexName("userIdGlobalIndex")
			.withHashKeyValues(hashKey)
			.withConsistentRead(true);
		log.info(
			"Query: {}",
			query);
		final PaginatedQueryList<ClanForUser> clansForUser = query(query);
		clansForUser.loadAllResults();
		return clansForUser;
	}
 
}