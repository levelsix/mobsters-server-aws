package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Referral;
public class ReferralRepository extends BaseDynamoRepository<Referral>{
	public ReferralRepository(){
		super(Referral.class);
	}

}