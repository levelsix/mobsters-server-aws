package com.lvl6.mobsters.info.repository;
import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.lvl6.mobsters.info.Achievement;
public interface AchievementRepository extends CrudRepository<Achievement, String>{
	
	Collection<Achievement> findByLvlBetween(Integer low, Integer high);
	Collection<Achievement> findByQuantityGreaterThan(Integer low);
	
	Collection<Achievement> findByAchievementNameStartingWith(String startsWith);
}