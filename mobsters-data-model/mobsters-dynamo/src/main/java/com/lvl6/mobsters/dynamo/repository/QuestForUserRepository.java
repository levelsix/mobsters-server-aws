package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestForUser;
@Component public class QuestForUserRepository extends BaseDynamoRepository<QuestForUser>{
	public QuestForUserRepository(){
		super(QuestForUser.class);
	}

}