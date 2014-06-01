package com.lvl6.mobsters.info.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.mobsters.info.MonsterForUser;
public interface MonsterForUserRepository extends JpaRepository<MonsterForUser, String>{
	List<MonsterForUser> findByUserIdAndId(String userId, Iterable<String> iterable);
}