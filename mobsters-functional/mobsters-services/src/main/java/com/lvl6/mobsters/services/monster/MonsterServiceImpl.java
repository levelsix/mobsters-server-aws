package com.lvl6.mobsters.services.monster;


import java.util.Collection;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.info.MonsterForUser;
import com.lvl6.mobsters.info.repository.MonsterForUserRepository;

@Component
public class MonsterServiceImpl implements MonsterService {
	@Autowired
	private MonsterForUserRepository monsterForUserRepository;

	@Override
	@Transactional(dontRollbackOn = {}, value=TxType.REQUIRED)
	public void updateUserMonsterHealth(String userId, Map<String,Integer> monsterIdToHealthMap) {
		//get whatever we need from the database
		Collection<MonsterForUser> existingUserMonsters =
			monsterForUserRepository.findByUserIdAndId(userId, monsterIdToHealthMap.keySet());
		if (CollectionUtils.lacksSubstance(existingUserMonsters) || (monsterIdToHealthMap.size() != existingUserMonsters.size())) {
			throw new IllegalArgumentException();
		}

		// Mutate the objects
		for(final MonsterForUser nextMonster : existingUserMonsters) {
			nextMonster.setCurrentHealth(
				monsterIdToHealthMap.get(
					nextMonster.getId()
				)
			);
		}
		
		// Write back to the database, then close the transaction by returning
		monsterForUserRepository.save(existingUserMonsters);
	}

}
