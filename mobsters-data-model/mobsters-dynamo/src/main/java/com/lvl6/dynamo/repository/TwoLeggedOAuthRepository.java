package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TwoLeggedOAuth;
@Component public class TwoLeggedOAuthRepository extends BaseDynamoRepository<TwoLeggedOAuth>{
	public TwoLeggedOAuthRepository(){
		super(TwoLeggedOAuth.class);
	}

}