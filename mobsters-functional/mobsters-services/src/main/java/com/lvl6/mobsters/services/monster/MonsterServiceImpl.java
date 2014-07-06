package com.lvl6.mobsters.services.monster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.dynamo.repository.MonsterEnhancingForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterEvolvingForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterForUserHistoryRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MonsterHealingForUserRepository;
import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.IMonsterLevelInfo;
import com.lvl6.mobsters.info.MonsterLevelInfo;

@Component
public class MonsterServiceImpl implements MonsterService
{
	@Autowired
	private MonsterForUserRepository monsterForUserRepository;
	
	@Autowired
	private MonsterForUserHistoryRepository monsterForUserHistoryRepository;
	
	@Autowired
	private MonsterHealingForUserRepository monsterHealingForUserRepository;

	@Autowired
	private MonsterEnhancingForUserRepository monsterEnhancingForUserRepository;

	@Autowired
	private MonsterEvolvingForUserRepository monsterEvolvingForUserRepository;
	
	// BEGIN READ ONLY LOGIC******************************************************************
	
	@Override
	public List<MonsterForUser> getMonstersForUser(String userId) {
		return monsterForUserRepository.findByUserId(userId);
	}

	@Override
	public List<MonsterHealingForUser> getMonstersInHealingForUser( String userId ) {
		return monsterHealingForUserRepository.findByUserId(userId);
	}
	
	@Override
	public List<MonsterEnhancingForUser> getMonstersInEnhancingForUser( String userId ) {
		return monsterEnhancingForUserRepository.findByUserId(userId);
	}
	
	@Override
	public List<MonsterEvolvingForUser> getMonstersInEvolution( String userId ) {
		return monsterEvolvingForUserRepository.findByUserId(userId);
	}
	
	// END READ ONLY LOGIC******************************************************************
		
	
	@Override
	public void addMonsterForUserToTeamSlot(
		String userId,
		String monsterForUserId,
		int teamSlotNum )
	{
		Set<String> monsterForUserIds = Collections.singleton(monsterForUserId);

		List<MonsterForUser> monstersForUser =
			monsterForUserRepository.findByUserIdAndMonsterForUserIdInOrTeamSlotNumAndUserId(userId,
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
//		List<MonsterForUser> monsterList =
//			monsterForUserRepository.findAll(userId, monsterForUserIds);
//
//		for (MonsterForUser mfu : monsterList) {
//			mfu.setTeamSlotNum(0);
//		}
//
//		monsterForUserRepository.saveAll(monsterList);

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
			monsterForUserRepository.findByUserIdAndMonsterForUserIdIn(userId, userMonsterIds);
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
	
	/**************************************************************************/
	
	@Override
	public void createCompleteMonstersForUser( String userId, List<Integer> monsterIds, Date combineStartTime ) {
		List<MonsterForUser> mfuList = new ArrayList<MonsterForUser>();
		
		for (int i = 0; i < monsterIds.size(); i++) {
	  		int monsterId = monsterIds.get(i);
	  		int teamSlotNum = i + 1;
	  		
	  		IMonster monzter = null;//MonsterRetrieveUtils.getMonsterForMonsterId(monsterId);
	  		Map<Integer, MonsterLevelInfo> info = null;//MonsterLevelInfoRetrieveUtils.getMonsterLevelInfoForMonsterId(monsterId);
	  		
	  		List<Integer> lvls = new ArrayList<Integer>(info.keySet());
	  		Collections.sort(lvls);
	  		int firstOne = lvls.get(0);
	  		IMonsterLevelInfo mli = info.get(firstOne);
	  		
	  		MonsterForUser mfu = new MonsterForUser();
	  		mfu.setUserId(userId);
	  		mfu.setMonsterId(monsterId);
	  		mfu.setCurrentExp(0);
	  		mfu.setCurrentLvl(mli.getLevel());
	  		mfu.setCurrentHealth(mli.getHp());
	  		mfu.setNumPieces(monzter.getNumPuzzlePieces());
	  		mfu.setComplete(true);
	  		mfu.setCombineStartTime(combineStartTime);
	  		mfu.setTeamSlotNum(teamSlotNum);
	  		
	  	}
		
		monsterForUserRepository.saveAll(mfuList);
		// TODO: Record into monsterForUserHistory
		//String sourceOfPieces = ControllerConstants.MFUSOP__USER_CREATE;
	}

	/*
	static class CreateMonstersSpecBuilderImpl implements CreateMonstersSpecBuilder
	{
		final private Map<String, MonsterForUser> userMonsterIdToMfu;

		CreateMonstersSpecBuilderImpl()
		{
			userMonsterIdToMfu = new HashMap<String, MonsterForUser>();
		}

		private MonsterForUser getTarget( String monsterForUserId ) {
            MonsterForUser mfu = userMonsterIdToMfu.get(monsterForUserId);
            if (null == mfu) {
                mfu = new MonsterForUser();
                userMonsterIdToMfu.put(monsterForUserId, mfu);
            }
            return mfu;
        }


		@Override
		public CreateMonstersSpec build()
		{
			final CreateMonstersSpec retVal = new CreateMonstersSpec(userMonsterIdToMfu);

			return retVal;
		}

		@Override
		public CreateMonstersSpecBuilder setMonsterId( String userMonsterId, int monsterId )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setMonsterId(monsterId);
			return this;
		}

		@Override
		public CreateMonstersSpecBuilder setCurrentExp( String userMonsterId, int currentExp )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setCurrentExp(currentExp);
			return this;
		}

		@Override
		public CreateMonstersSpecBuilder setCurrentLvl( String userMonsterId, int currentLvl )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setCurrentLvl(currentLvl);
			return this;
		}

		@Override
		public CreateMonstersSpecBuilder setCurrentHealth(
			String userMonsterId,
			int currentHealth )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setCurrentHealth(currentHealth);
			return this;
		}

		@Override
		public CreateMonstersSpecBuilder setNumPieces( String userMonsterId, int numPieces )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setNumPieces(numPieces);
			return this;
		}

		@Override
		public CreateMonstersSpecBuilder setComplete( String userMonsterId )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setComplete(true);
			return this;
		}

		@Override
		public CreateMonstersSpecBuilder setCombineStateTime(
			String userMonsterId,
			Date combineStartTime )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setCombineStartTime(combineStartTime);
			return this;
		}

		@Override
		public CreateMonstersSpecBuilder setTeamSlotNum(
			String userMonsterId,
			int teamSlotNum )
		{
			MonsterForUser mfu = getTarget(userMonsterId);
			mfu.setTeamSlotNum(teamSlotNum);
			return this;
		}
	}
	*/
	

	public MonsterForUserRepository getMonsterForUserRepository()
	{
		return monsterForUserRepository;
	}

	public void setMonsterForUserRepository( MonsterForUserRepository monsterForUserRepository )
	{
		this.monsterForUserRepository = monsterForUserRepository;
	}

	public MonsterForUserHistoryRepository getMonsterForUserHistoryRepository()
	{
		return monsterForUserHistoryRepository;
	}

	public void setMonsterForUserHistoryRepository(
		MonsterForUserHistoryRepository monsterForUserHistoryRepository )
	{
		this.monsterForUserHistoryRepository = monsterForUserHistoryRepository;
	}

	public MonsterHealingForUserRepository getMonsterHealingForUserRepository()
	{
		return monsterHealingForUserRepository;
	}

	public void setMonsterHealingForUserRepository(
		MonsterHealingForUserRepository monsterHealingForUserRepository )
	{
		this.monsterHealingForUserRepository = monsterHealingForUserRepository;
	}

	public MonsterEnhancingForUserRepository getMonsterEnhancingForUserRepository()
	{
		return monsterEnhancingForUserRepository;
	}

	public void setMonsterEnhancingForUserRepository(
		MonsterEnhancingForUserRepository monsterEnhancingForUserRepository )
	{
		this.monsterEnhancingForUserRepository = monsterEnhancingForUserRepository;
	}
	
}
