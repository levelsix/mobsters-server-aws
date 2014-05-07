package com.lvl6.info.repository;
import org.springframework.data.repository.CrudRepository;
import com.lvl6.info.Task;
public interface TaskRepository extends CrudRepository<Task, String>{

}