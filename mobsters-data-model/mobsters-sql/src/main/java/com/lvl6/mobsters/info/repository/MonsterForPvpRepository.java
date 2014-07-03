package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.MonsterForPvp;
public interface MonsterForPvpRepository extends JpaRepository<MonsterForPvp, Integer>{

	List<MonsterForPvp> findByEloBetween( int minElo, int maxElo );
	
}
