package com.lvl6.mobsters.info.repository;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Achievement;
public interface AchievementRepository extends JpaRepository<Achievement, String>{
	
	Collection<Achievement> findByLvlBetween(Integer low, Integer high);
	Collection<Achievement> findByQuantityGreaterThan(Integer low);
	
	Collection<Achievement> findByAchievementNameStartingWith(String startsWith);
	
}