package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestJobForUser;
@Component public class QuestJobForUserRepository extends BaseDynamoRepository<QuestJobForUser>{
	public QuestJobForUserRepository(){
		super(QuestJobForUser.class);
	}

}