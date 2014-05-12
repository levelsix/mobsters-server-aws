package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.TwoLeggedOAuth;
public class TwoLeggedOAuthRepository extends BaseDynamoRepository<TwoLeggedOAuth>{
	public TwoLeggedOAuthRepository(){
		super(TwoLeggedOAuth.class);
	}

}