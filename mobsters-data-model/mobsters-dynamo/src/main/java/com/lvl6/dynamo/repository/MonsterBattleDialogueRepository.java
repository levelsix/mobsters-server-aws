package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.MonsterBattleDialogue;
@Component public class MonsterBattleDialogueRepository extends BaseDynamoRepository<MonsterBattleDialogue>{
	public MonsterBattleDialogueRepository(){
		super(MonsterBattleDialogue.class);
	}

}