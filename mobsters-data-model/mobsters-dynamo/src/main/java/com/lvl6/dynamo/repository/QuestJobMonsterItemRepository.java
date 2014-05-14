package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.QuestJobMonsterItem;
@Component public class QuestJobMonsterItemRepository extends BaseDynamoRepository<QuestJobMonsterItem>{
	public QuestJobMonsterItemRepository(){
		super(QuestJobMonsterItem.class);
	}

}