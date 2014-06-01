package com.lvl6.mobsters.info.repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lvl6.mobsters.info.User;
public interface UserRepository extends JpaRepository<User, String>{
	@EntityGraph(value="User.withClan", type=EntityGraphType.LOAD)
	@Query("select u from User u where u.id = ?1")
    User findByIdWithClan(String id);
}