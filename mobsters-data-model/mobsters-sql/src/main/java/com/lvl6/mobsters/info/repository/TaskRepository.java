package com.lvl6.mobsters.info.repository;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Task;
public interface TaskRepository extends JpaRepository<Task, Integer>{	
	List<Task> findByIdIn( Collection<Integer> idList );
}
