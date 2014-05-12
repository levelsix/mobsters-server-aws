package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.QuestJobForUser;
public class QuestJobForUserRepository extends BaseDynamoRepository<QuestJobForUser>{
	public QuestJobForUserRepository(){
		super(QuestJobForUser.class);
	}

}