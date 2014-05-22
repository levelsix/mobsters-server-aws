package com.lvl6.mobsters.info.repository;
import org.springframework.data.repository.CrudRepository;

import com.lvl6.mobsters.info.LockBoxEvent;
public interface LockBoxEventRepository extends CrudRepository<LockBoxEvent, String>{

}