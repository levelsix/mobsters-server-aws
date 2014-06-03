package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.MonsterForUser;

public interface MonsterForUserRepository {
	List<MonsterForUser> findByUserIdAndId(String userId, Iterable<String> iterable);
}