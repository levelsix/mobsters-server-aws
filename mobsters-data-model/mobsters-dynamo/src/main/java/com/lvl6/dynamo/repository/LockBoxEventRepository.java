package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.LockBoxEvent;
@Component public class LockBoxEventRepository extends BaseDynamoRepository<LockBoxEvent>{
	public LockBoxEventRepository(){
		super(LockBoxEvent.class);
	}

}