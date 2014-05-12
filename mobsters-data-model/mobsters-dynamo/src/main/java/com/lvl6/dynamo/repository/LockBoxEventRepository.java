package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.LockBoxEvent;
public class LockBoxEventRepository extends BaseDynamoRepository<LockBoxEvent>{
	public LockBoxEventRepository(){
		super(LockBoxEvent.class);
	}

}