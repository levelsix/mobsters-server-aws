package com.lvl6.mobsters.info.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Structure;
public interface StructureRepository extends JpaRepository<Structure, Integer>{
	
	// TODO: would like Structure getUpgradedStructForStructId(int structId)
	
	// TODO: would like Structure getPredecessorStructForStructId(int structId)
	
	// TODO: would like Structure getPredecessorStructForStructIdAndLvl(int structId, int lvl)

	Structure findByIdIn( Iterable<Integer> idList );
	
	Structure findByIdIn( int[] idList );
	
	Structure findById(int id);
}
