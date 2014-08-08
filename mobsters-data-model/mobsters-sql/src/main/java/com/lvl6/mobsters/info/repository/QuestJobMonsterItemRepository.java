package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.QuestJobMonsterItem;
public interface QuestJobMonsterItemRepository 
	extends JpaRepository<QuestJobMonsterItem, Integer>
{
	List<QuestJobMonsterItem> findByMonsterAndQuestJobIn( IMonster m, Iterable<IQuestJob> qJobs );
}
