package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanChatPost;
@Component public class ClanChatPostRepository extends BaseDynamoRepository<ClanChatPost>{
	public ClanChatPostRepository(){
		super(ClanChatPost.class);
	}

}