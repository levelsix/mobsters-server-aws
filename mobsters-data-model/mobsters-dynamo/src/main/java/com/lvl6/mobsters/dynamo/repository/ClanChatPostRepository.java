package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanChatPost;
@Component public abstract class ClanChatPostRepository extends BaseDynamoItemRepositoryImpl<ClanChatPost>{
	public ClanChatPostRepository(){
		super(ClanChatPost.class);
	}

}