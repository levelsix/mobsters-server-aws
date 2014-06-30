package com.lvl6.mobsters.info.repository;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Obstacle;
public interface ObstacleRepository extends JpaRepository<Obstacle, Integer>{

		List<Obstacle> findByIdIn( Collection<Integer> idList );
}
