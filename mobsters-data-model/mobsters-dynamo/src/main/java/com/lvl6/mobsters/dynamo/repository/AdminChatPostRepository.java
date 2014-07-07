package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.AdminChatPost;
@Component public abstract class AdminChatPostRepository extends BaseDynamoItemRepositoryImpl<AdminChatPost>{
	public AdminChatPostRepository(){
		super(AdminChatPost.class);
	}

}