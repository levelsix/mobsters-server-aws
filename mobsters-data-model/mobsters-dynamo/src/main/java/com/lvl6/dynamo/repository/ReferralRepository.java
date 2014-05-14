package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Referral;
@Component public class ReferralRepository extends BaseDynamoRepository<Referral>{
	public ReferralRepository(){
		super(Referral.class);
	}

}