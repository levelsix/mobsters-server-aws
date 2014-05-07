package com.lvl6.info.repository;
import org.springframework.data.repository.CrudRepository;
import com.lvl6.info.UserLockBoxEvent;
public interface UserLockBoxEventRepository extends CrudRepository<UserLockBoxEvent, String>{

}