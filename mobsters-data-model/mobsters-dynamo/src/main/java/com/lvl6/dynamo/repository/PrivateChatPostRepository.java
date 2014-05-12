package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.PrivateChatPost;
public class PrivateChatPostRepository extends BaseDynamoRepository<PrivateChatPost>{
	public PrivateChatPostRepository(){
		super(PrivateChatPost.class);
	}

}