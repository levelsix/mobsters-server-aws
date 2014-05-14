package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.PrivateChatPost;
@Component public class PrivateChatPostRepository extends BaseDynamoRepository<PrivateChatPost>{
	public PrivateChatPostRepository(){
		super(PrivateChatPost.class);
	}

}