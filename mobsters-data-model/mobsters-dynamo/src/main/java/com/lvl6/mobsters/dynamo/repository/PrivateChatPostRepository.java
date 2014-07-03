package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.PrivateChatPost;
@Component public abstract class PrivateChatPostRepository extends BaseDynamoItemRepositoryImpl<PrivateChatPost>{
	public PrivateChatPostRepository(){
		super(PrivateChatPost.class);
	}

}