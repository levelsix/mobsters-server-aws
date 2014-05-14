package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanChatPost;
@Component public class ClanChatPostRepository extends BaseDynamoRepository<ClanChatPost>{
	public ClanChatPostRepository(){
		super(ClanChatPost.class);
	}

}