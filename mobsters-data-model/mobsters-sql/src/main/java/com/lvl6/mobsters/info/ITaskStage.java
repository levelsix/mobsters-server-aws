package com.lvl6.mobsters.info;

import java.util.List;

public interface ITaskStage extends IBaseIntPersistentObject
{
	public ITask getTask();

	public void setTask( ITask task );

	public int getStageNum();

	public void setStageNum( int stageNum );
	
	public List<ITaskStageMonster> getStageMonsters();
}