package com.lvl6.mobsters.domain.game.event;

import com.google.common.collect.ImmutableList;


final class AddTask implements IAddTask {
  private final String taskUuid;
  
  private final String userUuid;
  
  private final int taskId;
  
  private final ImmutableList<AddUserTaskStage> stageList;
  
  AddTask(
	final String taskUuid, final int taskId, final String userUuid, ImmutableList<AddUserTaskStage> stageList)
  {
    this.taskUuid = taskUuid;
    this.userUuid = userUuid;
    this.taskId = taskId;
    this.stageList = stageList;
  }
  
  /* (non-Javadoc)
 * @see com.lvl6.mobsters.domain.game.event.IAddTask#getTaskUuid()
 */
@Override
public String getTaskUuid() {
    return this.taskUuid;
  }
  
  /* (non-Javadoc)
 * @see com.lvl6.mobsters.domain.game.event.IAddTask#getUserUuid()
 */
@Override
public String getUserUuid() {
    return this.userUuid;
  }
  
  /* (non-Javadoc)
 * @see com.lvl6.mobsters.domain.game.event.IAddTask#getTaskId()
 */
@Override
public int getTaskId() {
    return this.taskId;
  }

  /* (non-Javadoc)
 * @see com.lvl6.mobsters.domain.game.event.IAddTask#getStageList()
 */
@Override
public ImmutableList<AddUserTaskStage> getStageList() {
    return stageList;
  }
}
