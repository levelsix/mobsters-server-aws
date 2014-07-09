package com.lvl6.mobsters.noneventproto.utils;

import java.util.List;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.TaskStageForUser;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.FullTaskProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.MinimumUserTaskProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.TaskStageMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.TaskStageProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.UserPersistentEventProto;

public interface NoneventTaskProtoSerializer
{

	public FullTaskProto createFullTaskProtoFromTask(ITask task);
	
	//BEGIN USER DATA SERIALIZATION
	
	public MinimumUserTaskProto createMinimumUserTaskProto( String userId,
		TaskForUserOngoing aTaskForUser);

	public TaskStageProto createTaskStageProto(
		int taskId,
		int stageNum,
		List<TaskStageForUser> monsters );

	public TaskStageMonsterProto createTaskStageMonsterProto( TaskStageForUser tsfu );

	public UserPersistentEventProto createUserPersistentEventProto( EventPersistentForUser epfu );
	
}
