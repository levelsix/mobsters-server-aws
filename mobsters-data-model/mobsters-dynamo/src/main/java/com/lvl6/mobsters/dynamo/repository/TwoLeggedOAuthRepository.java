package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.TwoLeggedOAuth;
@Component public class TwoLeggedOAuthRepository extends BaseDynamoRepository<TwoLeggedOAuth>{
	public TwoLeggedOAuthRepository()
	{
		super(
			TwoLeggedOAuth.class);
	}

}