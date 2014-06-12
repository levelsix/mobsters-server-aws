package com.lvl6.mobsters.dynamo.repository;

import com.lvl6.mobsters.dynamo.TwoLeggedOAuth;

public class TwoLeggedOAuthRepository extends BaseDynamoRepositoryImpl<TwoLeggedOAuth>
{
	public TwoLeggedOAuthRepository()
	{
		super(
			TwoLeggedOAuth.class);
	}

}