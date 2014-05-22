package com.lvl6.mobsters.info.repository;
import org.springframework.data.repository.CrudRepository;

import com.lvl6.mobsters.info.Task;
public interface TaskRepository extends CrudRepository<Task, String>{

}