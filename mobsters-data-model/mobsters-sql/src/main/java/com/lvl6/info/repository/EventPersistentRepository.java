package com.lvl6.info.repository;
import org.springframework.data.repository.CrudRepository;
import com.lvl6.info.EventPersistent;
public interface EventPersistentRepository extends CrudRepository<EventPersistent, String>{

}