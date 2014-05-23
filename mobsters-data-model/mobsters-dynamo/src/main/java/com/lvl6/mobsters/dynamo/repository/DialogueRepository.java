package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Dialogue;
@Component public class DialogueRepository extends BaseDynamoRepository<Dialogue>{
	public DialogueRepository(){
		super(Dialogue.class);
	}

}