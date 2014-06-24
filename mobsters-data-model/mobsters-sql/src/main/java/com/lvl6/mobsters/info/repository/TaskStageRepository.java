package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.TaskStage;
public interface TaskStageRepository extends JpaRepository<TaskStage, Integer>{
	
	// TODO: would like
	// Map<Integer, Map<Integer, TaskStage>> getTaskIdsToTaskStageIdsToTaskStages()
	
	TaskStage findByTaskIdAndStageNum( int taskId, int stageNum );
	
	List<TaskStage> findByTaskId( int taskId );
	
	// TODO: would like
	// getFirstTaskStageIdForTaskId(int taskId)
}
