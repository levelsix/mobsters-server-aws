package com.lvl6.mobsters.info.repository;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Quest;
public interface QuestRepository extends JpaRepository<Quest, Integer>{

	Collection<Quest> findByIdIn(Collection<Integer> idList);
}
