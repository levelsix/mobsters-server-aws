package com.lvl6.info.repository;
import org.springframework.data.repository.CrudRepository;
import com.lvl6.info.LockBoxEvent;
public interface LockBoxEventRepository extends CrudRepository<LockBoxEvent, String>{

}