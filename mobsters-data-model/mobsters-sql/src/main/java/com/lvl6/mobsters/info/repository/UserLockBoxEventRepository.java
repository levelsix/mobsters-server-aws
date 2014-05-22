package com.lvl6.mobsters.info.repository;
import org.springframework.data.repository.CrudRepository;

import com.lvl6.mobsters.info.UserLockBoxEvent;
public interface UserLockBoxEventRepository extends CrudRepository<UserLockBoxEvent, String>{

}