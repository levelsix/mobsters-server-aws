package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Quest;
@Component public class QuestRepository extends BaseDynamoRepository<Quest>{
	public QuestRepository(){
		super(Quest.class);
	}

}