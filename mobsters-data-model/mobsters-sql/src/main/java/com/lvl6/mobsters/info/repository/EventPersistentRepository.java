package com.lvl6.mobsters.info.repository;
import org.springframework.data.repository.CrudRepository;

import com.lvl6.mobsters.info.EventPersistent;
public interface EventPersistentRepository extends CrudRepository<EventPersistent, String>{

}