package com.lvl6.mobsters.noneventproto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.info.Task;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.FullTaskProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.MinimumUserTaskProto;

public class NoneventTaskProtoSerializerImpl implements NoneventTaskProtoSerializer 
{

	private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());

	@Override
	public FullTaskProto createFullTaskProtoFromTask( Task task )
	{
		FullTaskProto.Builder builder = FullTaskProto.newBuilder();
		String str = task.getGoodName();

		builder.setTaskId(task.getId());
		if (null != str) {
			builder.setName(str);
		}

		str = task.getDescription();
		if (null != str) {
			builder.setDescription(str);
		}

		ITask prerequisiteTask = task.getPrerequisiteTask();
		if (null != prerequisiteTask) {
			builder.setPrerequisiteTaskId(prerequisiteTask.getId());
		}

		Quest prerequisiteQuest = task.getPrerequisiteQuest();
		if (null != prerequisiteQuest) {
			builder.setPrerequisiteQuestId(prerequisiteQuest.getId());
		}

		return builder.build();
	}

	//BEGIN USER DATA SERIALIZATION
	
	public MinimumUserTaskProto createMinimumUserTaskProto( String userId,
		TaskForUserOngoing aTaskForUser )
	{
		MinimumUserTaskProto.Builder mutpb = MinimumUserTaskProto.newBuilder();
	  	mutpb.setUserUuid(userId);
	  	
	  	int taskId = aTaskForUser.getTaskId();
	  	mutpb.setTaskId(taskId);
	  	int taskStageId = aTaskForUser.getTaskStageId();
	  	mutpb.setCurTaskStageId(taskStageId);
	  	String userTaskId = aTaskForUser.getTaskForUserId();
	  	mutpb.setUserTaskUuid(userTaskId);
	  	
	  	return mutpb.build();
	}

}
