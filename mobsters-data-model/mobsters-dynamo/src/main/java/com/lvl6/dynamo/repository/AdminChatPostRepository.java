package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.AdminChatPost;
public class AdminChatPostRepository extends BaseDynamoRepository<AdminChatPost>{
	public AdminChatPostRepository(){
		super(AdminChatPost.class);
	}

}