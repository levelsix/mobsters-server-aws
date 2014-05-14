package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Dialogue;
@Component public class DialogueRepository extends BaseDynamoRepository<Dialogue>{
	public DialogueRepository(){
		super(Dialogue.class);
	}

}