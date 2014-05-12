package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.QuestJob;
public class QuestJobRepository extends BaseDynamoRepository<QuestJob>{
	public QuestJobRepository(){
		super(QuestJob.class);
	}

}