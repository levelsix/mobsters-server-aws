package com.lvl6.mobsters.info.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.Task;
public interface TaskRepository extends JpaRepository<Task, String>{

}
