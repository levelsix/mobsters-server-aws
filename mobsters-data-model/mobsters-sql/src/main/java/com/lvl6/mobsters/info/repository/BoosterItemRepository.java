package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.BoosterItem;
public interface BoosterItemRepository extends JpaRepository<BoosterItem, Integer>{

	
	List<BoosterItem> findByBoosterPackId( int boosterPackId );
	
}
