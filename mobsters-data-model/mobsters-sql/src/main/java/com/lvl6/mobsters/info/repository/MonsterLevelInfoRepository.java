package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lvl6.mobsters.info.MonsterLevelInfo;
public interface MonsterLevelInfoRepository extends JpaRepository<MonsterLevelInfo, Integer> {

	MonsterLevelInfo findByMonsterIdAndLevel(int id, int level);

	List<MonsterLevelInfo> findByMonsterId(int id);

	List<MonsterLevelInfo> findByMonsterIdOrderByLevel(int id);
	
	List<MonsterLevelInfo> findByMonsterIdOrderByLevel(int id, Pageable p);
	
	
	@Query( "select m from MonsterLevelInfo m where m.monster.id = :id order by m.id.level asc")
	Page<MonsterLevelInfo> findFirstLevelByMonsterId(@Param("id") int id, Pageable p);
}
