package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Dialogue;
public class DialogueRepository extends BaseDynamoRepository<Dialogue>{
	public DialogueRepository(){
		super(Dialogue.class);
	}

}