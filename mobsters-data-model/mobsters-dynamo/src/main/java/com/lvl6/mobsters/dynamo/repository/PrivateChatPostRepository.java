package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.PrivateChatPost;
@Component public class PrivateChatPostRepository extends BaseDynamoRepositoryImpl<PrivateChatPost>{
	public PrivateChatPostRepository(){
		super(PrivateChatPost.class);
	}

}