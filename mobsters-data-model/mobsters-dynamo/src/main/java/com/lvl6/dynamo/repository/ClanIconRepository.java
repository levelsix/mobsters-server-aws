package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanIcon;
@Component public class ClanIconRepository extends BaseDynamoRepository<ClanIcon>{
	public ClanIconRepository(){
		super(ClanIcon.class);
	}

}