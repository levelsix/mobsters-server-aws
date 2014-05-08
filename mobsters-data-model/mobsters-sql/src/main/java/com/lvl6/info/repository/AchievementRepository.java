package com.lvl6.info.repository;
import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.lvl6.info.Achievement;
public interface AchievementRepository extends CrudRepository<Achievement, String>{
	
	Collection<Achievement> findByLvlBetween(Integer low, Integer high);
	Collection<Achievement> findByQuantityGreaterThan(Integer low);
	
	Collection<Achievement> findByAchievementNameStartingWith(String startsWith);
}