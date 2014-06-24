package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.MiniJob;
public interface MiniJobRepository extends JpaRepository<MiniJob, Integer>{
	
	List<MiniJob> findByStructId( int structId );
	
	// TODO: would like float getMiniJobProbabilitySumForStructId(int structId)
}
