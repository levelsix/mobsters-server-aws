package com.lvl6.mobsters.services.monster;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.repository.MonsterForUserRepository;

@Component
public class MonsterServiceImpl implements MonsterService
{
	@Autowired
	private MonsterForUserRepository monsterForUserRepository;

	@Override
	public void addMonsterForUserToTeamSlot(
		String userId,
		String monsterForUserId,
		int teamSlotNum )
	{
		Set<String> monsterForUserIds = Collections.singleton(monsterForUserId);

		List<MonsterForUser> monstersForUser =
			monsterForUserRepository.findByUserIdAndIdOrTeamSlotNumAndUserId(userId,
				monsterForUserIds, teamSlotNum);

		final Map<String, MonsterForUser> userMonsterIdToMfu =
			new HashMap<String, MonsterForUser>();

		for (final MonsterForUser mfu : monstersForUser) {
			userMonsterIdToMfu.put(mfu.getMonsterForUserId(), mfu);
		}

		for (MonsterForUser mfu : userMonsterIdToMfu.values()) {
			if (monsterForUserIds.contains(mfu.getMonsterForUserId())) {
				mfu.setTeamSlotNum(teamSlotNum);

			} else if (mfu.getTeamSlotNum() == teamSlotNum) {
				mfu.setTeamSlotNum(0);
			}
		}
	}

	@Override
	public void clearMonstersForUserTeamSlot( String userId, Set<String> monsterForUserIds )
	{
		List<MonsterForUser> monsterList =
			monsterForUserRepository.findAll(userId, monsterForUserIds);

		for (MonsterForUser mfu : monsterList) {
			mfu.setTeamSlotNum(0);
		}

		monsterForUserRepository.saveAll(monsterList);

	}

	/*
	 * @Override
	 * @Transactional(dontRollbackOn = {}, value=TxType.REQUIRED) public void
	 * updateUserMonsterHealth(String userId, Map<String,Integer> monsterIdToHealthMap) { //get whatever
	 * we need from the database Collection<MonsterForUser> existingUserMonsters =
	 * monsterForUserRepository.findByUserIdAndId(userId, monsterIdToHealthMap.keySet()); if
	 * (CollectionUtils.lacksSubstance(existingUserMonsters) || (monsterIdToHealthMap.size() !=
	 * existingUserMonsters.size())) { throw new IllegalArgumentException(); } // Mutate the objects
	 * for(final MonsterForUser nextMonster : existingUserMonsters) { nextMonster.setCurrentHealth(
	 * monsterIdToHealthMap.get( nextMonster.getId() ) ); } // Write back to the database, then close the
	 * transaction by returning monsterForUserRepository.save(existingUserMonsters); }
	 */

	@Override
	public void modifyMonstersForUser( String userId, ModifyMonstersSpec modifySpec )
	{
		// txManager.startTransaction();

		// get whatever we need from the database
		final Multimap<String, MonsterFunc> specMap = modifySpec.getSpecMultimap();
		final Set<String> userMonsterIds = specMap.keySet();

		List<MonsterForUser> existingUserMonsters =
			monsterForUserRepository.findByUserIdAndId(userId, userMonsterIds);
		if (CollectionUtils.lacksSubstance(existingUserMonsters)
			|| (userMonsterIds.size() != existingUserMonsters.size())) {
			// txManager.rollback();
			throw new IllegalArgumentException("No monsters for userId "
				+ userId
				+ " and userMonsterIds: "
				+ userMonsterIds);
		}

		// Mutate the objects

		// txManager.startTransaction();
		for (final MonsterForUser nextMonster : existingUserMonsters) {
			Collection<MonsterFunc> monsterOps = specMap.get(nextMonster.getMonsterForUserId());
			for (MonsterFunc nextMonsterOp : monsterOps) {
				nextMonsterOp.apply(nextMonster);
			}
		}

		// Write back to the database, then close the transaction by returning
		// TBD: Need to restore a workable save interface.
		// monsterForUserRepository.save(existingUserMonsters);
		// txManager.endTransaction();
	}

	static class ModifyMonstersSpecBuilderImpl implements ModifyMonstersSpecBuilder
	{
		Multimap<String, MonsterFunc> opMap;

		ModifyMonstersSpecBuilderImpl()
		{
			opMap = ArrayListMultimap.create();
		}

		@Override
		public ModifyMonstersSpecBuilder setHealthAbsolute( String userMonsterId, int value )
		{
			opMap.put(userMonsterId, new SetCurrentHealthAbsoluteFunction(value));
			return this;
		}

		@Override
		public ModifyMonstersSpecBuilder setHealthRelative( String userMonsterId, int delta )
		{
			opMap.put(userMonsterId, new SetCurrentHealthRelativeFunction(delta));
			return this;
		}

		@Override
		public ModifyMonstersSpecBuilder setExperienceAbsolute( String userMonsterId, int value )
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ModifyMonstersSpecBuilder setExperienceRelative( String userMonsterId, int delta )
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ModifyMonstersSpecBuilder setTeamSlotNum(
			String userMonsterId,
			int newTeamSlotNum )
		{
			opMap.put(userMonsterId, new SetTeamSlotNum(newTeamSlotNum));
			return this;
		}

		@Override
		public ModifyMonstersSpec build()
		{
			final ModifyMonstersSpec retVal = new ModifyMonstersSpec(opMap);

			return retVal;
		}
	}

	static class SetCurrentHealthRelativeFunction implements MonsterFunc
	{
		final int health;

		SetCurrentHealthRelativeFunction( int health )
		{
			this.health = health;
		}

		@Override
		public void apply( MonsterForUser m )
		{
			m.setCurrentHealth(m.getCurrentHealth()
				+ health);
		}
	};

	static class SetCurrentHealthAbsoluteFunction implements MonsterFunc
	{
		final int health;

		SetCurrentHealthAbsoluteFunction( int health )
		{
			this.health = health;
		}

		@Override
		public void apply( MonsterForUser m )
		{
			m.setCurrentHealth(health);
		}
	};

	static class SetTeamSlotNum implements MonsterFunc
	{
		final int teamSlotNum;

		SetTeamSlotNum( int teamSlotNum )
		{
			this.teamSlotNum = teamSlotNum;
		}

		@Override
		public void apply( MonsterForUser m )
		{
			m.setCurrentHealth(teamSlotNum);
		}
	};
}
