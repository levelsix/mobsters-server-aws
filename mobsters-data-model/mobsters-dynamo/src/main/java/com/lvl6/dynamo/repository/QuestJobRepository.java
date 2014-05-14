package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.QuestJob;
@Component public class QuestJobRepository extends BaseDynamoRepository<QuestJob>{
	public QuestJobRepository(){
		super(QuestJob.class);
	}

}