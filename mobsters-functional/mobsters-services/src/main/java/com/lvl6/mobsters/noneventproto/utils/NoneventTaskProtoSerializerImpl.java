package com.lvl6.mobsters.noneventproto.utils;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.TaskStageForUser;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.ITaskStageMonster;
import com.lvl6.mobsters.info.Item;
import com.lvl6.mobsters.info.TaskStage;
import com.lvl6.mobsters.info.repository.ItemRepository;
import com.lvl6.mobsters.info.repository.TaskStageMonsterRepository;
import com.lvl6.mobsters.info.repository.TaskStageRepository;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.FullTaskProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.MinimumUserTaskProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.TaskStageMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.TaskStageMonsterProto.MonsterType;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.TaskStageProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.UserPersistentEventProto;

public class NoneventTaskProtoSerializerImpl implements NoneventTaskProtoSerializer 
{

	private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());

	@Autowired
	protected TaskStageRepository taskStageRepository;
	
	@Autowired
	protected TaskStageMonsterRepository taskStageMonsterRepository;
	
	@Autowired
	protected ItemRepository itemRepository;
	
	@Override
	public FullTaskProto createFullTaskProtoFromTask( ITask task )
	{
		FullTaskProto.Builder builder = FullTaskProto.newBuilder();
		String str = task.getName();

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

		return builder.build();
	}

	//BEGIN USER DATA SERIALIZATION
	@Override
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

	@Override
	public TaskStageProto createTaskStageProto (int taskId, int stageNum,
		  List<TaskStageForUser> monsters) {
	  TaskStageProto.Builder tspb = TaskStageProto.newBuilder();
	  
	  TaskStage ts = taskStageRepository.findByTaskIdAndStageNum(taskId,
			  stageNum);
	  int taskStageId = ts.getId();
	  tspb.setStageId(taskStageId);
	  
	  for (TaskStageForUser tsfu : monsters) {
		  TaskStageMonsterProto tsmp = createTaskStageMonsterProto(tsfu); 
		  tspb.addStageMonsters(tsmp);
	  }
	  
	  return tspb.build();
	}
	
	@Override
	public TaskStageMonsterProto createTaskStageMonsterProto(TaskStageForUser tsfu) {
		int tsmId = tsfu.getTaskStageMonsterId();
		  ITaskStageMonster tsm = taskStageMonsterRepository
				  .findOne(tsmId);

		  int tsmMonsterId = tsm.getMonster().getId();
		  boolean didPieceDrop = tsfu.isMonsterPieceDropped();
		  //check if monster id exists
		  if (didPieceDrop && null == tsm.getMonster()) {
			  throw new RuntimeException("Non existent monsterId for userTask=" +
				  tsfu);
		  }

		  TaskStageMonsterProto.Builder bldr = TaskStageMonsterProto.newBuilder();
		  bldr.setMonsterId(tsmMonsterId);
		  String tsmMonsterType = tsfu.getMonsterType(); 
		  try {
			  MonsterType mt = MonsterType.valueOf(tsmMonsterType);
			  bldr.setMonsterType(mt);
		  } catch (Exception e) {
			  log.error("monster type incorrect, tsm=" + tsm);
		  }
		  bldr.setCashReward(tsfu.getCashGained());
		  bldr.setOilReward(tsfu.getOilGained());
		  bldr.setPuzzlePieceDropped(didPieceDrop);
		  bldr.setExpReward(tsfu.getExpGained());
		  
		  bldr.setLevel(tsm.getLevel());
		  bldr.setDmgMultiplier(tsm.getDmgMultiplier());

		  int itemId = tsfu.getItemIdDropped();
		  if (itemId > 0) {
			  //check if item exists
			  Item item = itemRepository.findOne(itemId);
			  if (null == item) {
				  throw new RuntimeException("nonexistent itemId for userTask=" +
						  tsfu);
			  }
			  bldr.setItemId(itemId);
		  }

		  return bldr.build();
	}

	@Override
	public UserPersistentEventProto createUserPersistentEventProto(
		EventPersistentForUser epfu) {
		UserPersistentEventProto.Builder upepb = UserPersistentEventProto.newBuilder();

		Date timeOfEntry = epfu.getTimeOfEntry();

		upepb.setUserUuid(epfu.getUserId());
		upepb.setEventId(epfu.getEventPersistentId());

		if (null != timeOfEntry) {
			upepb.setCoolDownStartTime(timeOfEntry.getTime());
		}

		return upepb.build();
	}
	
	public TaskStageRepository getTaskStageRepository()
	{
		return taskStageRepository;
	}

	public void setTaskStageRepository( TaskStageRepository taskStageRepository )
	{
		this.taskStageRepository = taskStageRepository;
	}

}
