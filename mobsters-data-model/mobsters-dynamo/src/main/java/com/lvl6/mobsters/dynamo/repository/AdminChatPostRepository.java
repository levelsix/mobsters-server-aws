package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.AdminChatPost;
@Component public class AdminChatPostRepository extends BaseDynamoRepository<AdminChatPost>{
	public AdminChatPostRepository(){
		super(AdminChatPost.class);
	}

}