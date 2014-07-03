package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.MonsterLevelInfo;
public interface MonsterLevelInfoRepository extends JpaRepository<MonsterLevelInfo, Integer>{

	List<MonsterLevelInfo> findByMonsterId( int id );
}
