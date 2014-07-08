package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.MiniJob;
public interface MiniJobRepository extends JpaRepository<MiniJob, Integer>{
	
	List<MiniJob> findByRequiredStructId( int structId );
	
	// TODO: would like float getMiniJobProbabilitySumForStructId(int structId)
	// -- Hint: Use XTend to implement getMiniJobProbability( List<MiniJob> ) in
	//          an extension library class.  Then call it as follows:
	//
	//          miniRepo.findByRequiredStructId(structId).getMiniJobProbability()
	//
	//          Remember, let every class do one thing and _only_ one thing.
	//          Repository Access and Calculated Derivation are two different
	//          responsibilities, so do not mix them.
}
