package com.lvl6.mobsters.info.repository;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.TaskStageMonster;
public interface TaskStageMonsterRepository extends JpaRepository<TaskStageMonster, Integer>{

	// TODO: would like
	// Map<Integer, List<TaskStageMonster>> getTaskStageIdsToTaskStageMonsters()
	
	List<TaskStageMonster> findByStageId( int stageId );
	
	List<TaskStageMonster> findByIdIn(Collection<Integer> idList);
}
