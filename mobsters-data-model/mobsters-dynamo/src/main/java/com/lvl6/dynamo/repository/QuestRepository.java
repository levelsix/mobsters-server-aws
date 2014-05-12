package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Quest;
public class QuestRepository extends BaseDynamoRepository<Quest>{
	public QuestRepository(){
		super(Quest.class);
	}

}