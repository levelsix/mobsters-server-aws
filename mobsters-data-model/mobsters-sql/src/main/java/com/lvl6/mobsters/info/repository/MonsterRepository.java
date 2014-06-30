package com.lvl6.mobsters.info.repository;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Monster;
public interface MonsterRepository extends JpaRepository<Monster, Integer>{
	
	List<Monster> findByIdIn( Collection<Integer> idList );
	
}
