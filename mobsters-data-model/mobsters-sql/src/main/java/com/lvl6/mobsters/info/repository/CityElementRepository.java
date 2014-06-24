package com.lvl6.mobsters.info.repository;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.CityElement;
public interface CityElementRepository extends JpaRepository<CityElement, Integer>{
	
	List<CityElement> findByCityIdIn( Collection<Integer> idList );

}
