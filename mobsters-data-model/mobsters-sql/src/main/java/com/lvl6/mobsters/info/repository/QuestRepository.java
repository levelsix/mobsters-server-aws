package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Quest;
public interface QuestRepository extends JpaRepository<Quest, Integer>{
	public List<Quest> findByIdIn(Iterable<Integer> idList);
	
	public List<Quest> findByIdIn(int[] idList);
	
	public Quest findById(int id);
}
