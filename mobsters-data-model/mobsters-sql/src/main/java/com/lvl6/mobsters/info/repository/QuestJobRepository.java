package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.QuestJob;
public interface QuestJobRepository extends JpaRepository<QuestJob, Integer>{

	// TODO: would like List<Integer> getQuestJobIdsForQuestIds(List<Integer> questIdList)
	// return value can be a Set
	
	List<QuestJob> findByQuestId( int questId );
	
	
}
