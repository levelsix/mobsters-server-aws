package com.lvl6.mobsters.info.repository;
import org.springframework.data.repository.CrudRepository;

import com.lvl6.mobsters.info.User;
public interface UserRepository extends CrudRepository<User, String>{

}