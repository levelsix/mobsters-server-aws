package com.lvl6.mobsters.noneventproto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.info.Task;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.FullTaskProto;

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


}
