package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lvl6.mobsters.info.MonsterLevelInfo;
public interface MonsterLevelInfoRepository extends JpaRepository<MonsterLevelInfo, Integer> {
    /*
     * Unnecessary to make Hibernate cache this query.  Instead, leverage the @OrderBy annotation on
     * Monster.lvlInfo:
     * 
     * monsterRepository.findById(monsterId).getLvlInfo().get(level)
     * 
     * As a rule of thumb, access configuration data by fetching root objects and navigate from there 
     * to minimize the amount of work Hibernate invests in cache unnecessary cache and query indices.
     */
	// MonsterLevelInfo findByMonsterIdAndLevel(int monsterId, int level);

	/*
	 * Unnecessary to make Hibernate cache this query.  Instead, leverage the @OrderBy annotation on
     * Monster.lvlInfo:
     * 
     * monsterRepository.findById(monsterId).getLvlInfo().get(level)
     * 
     * As a rule of thumb, access configuration data by fetching root objects and navigate from there 
     * to minimize the amount of work Hibernate invests in cache unnecessary cache and query indices.
	List<MonsterLevelInfo> findByMonsterId(int monsterId);
	 */

	
	// These should likely be removed as well, but leaving them purely for reference value:
	
	// List<MonsterLevelInfo> findByMonsterIdOrderByLevel(int monsterId);
	
	// List<MonsterLevelInfo> findByMonsterIdOrderByLevel(int monsterId, Pageable p);
	
	
	@Query( "select m from MonsterLevelInfo m where m.monster.id = :id order by m.id.level asc")
	Page<MonsterLevelInfo> findFirstLevelByMonsterId(@Param("id") int id, Pageable p);
}
