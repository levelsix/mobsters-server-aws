package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.info.Task;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.FullTaskProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.MinimumUserTaskProto;

public interface NoneventTaskProtoSerializer
{

	public FullTaskProto createFullTaskProtoFromTask(Task task);
	
	//BEGIN USER DATA SERIALIZATION
	
	public MinimumUserTaskProto createMinimumUserTaskProto( String userId,
		TaskForUserOngoing aTaskForUser);
	
}
