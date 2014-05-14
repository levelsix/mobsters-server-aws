package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.QuestForUser;
@Component public class QuestForUserRepository extends BaseDynamoRepository<QuestForUser>{
	public QuestForUserRepository(){
		super(QuestForUser.class);
	}

}