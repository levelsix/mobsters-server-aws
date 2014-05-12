package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.ClanChatPost;
public class ClanChatPostRepository extends BaseDynamoRepository<ClanChatPost>{
	public ClanChatPostRepository(){
		super(ClanChatPost.class);
	}

}