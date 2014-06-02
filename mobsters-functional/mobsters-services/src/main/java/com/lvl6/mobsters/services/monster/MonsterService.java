package com.lvl6.mobsters.services.monster;

import java.util.Map;

public interface MonsterService {
	public void updateUserMonsterHealth(String userId, Map<String,Integer> monsterIdToHealthMap);
}
