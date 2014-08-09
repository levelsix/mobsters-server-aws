package com.lvl6.mobsters.services.monster;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.Function;
import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.services.monster.MonsterServiceImpl.ModifyMonstersSpecBuilderImpl;
public interface MonsterService
{
	
	// BEGIN READ ONLY LOGIC******************************************************************
	public List<MonsterForUser> getMonstersForUser( String userId );
	
	public List<MonsterHealingForUser> getMonstersInHealingForUser( String userId );

	public List<MonsterEnhancingForUser> getMonstersInEnhancingForUser( String userId );
	
	public List<MonsterEvolvingForUser> getMonstersInEvolution( String userId );
	
	// END READ ONLY LOGIC******************************************************************
		
	public User combineMonsterForUser(String userIdString, List<String> userMonsterIds,
		int gemCost, Date curDate);
	
	/**
	 * The monsterForUserId will have its teamSlotNum property set to the given teamSlotNum. All
	 * monstersForUser currently in the teamSlot (at most 1) will have their property set to 0.
	 * 
	 * @param userId
	 * @param monsterForUserId
	 * @param teamSlotNum
	 *        - should not be 0
	 */
	public void addMonsterForUserToTeamSlot(
		String userId,
		String monsterForUserId,
		int teamSlotNum );

	/**
	 * 
	 * @param userId
	 * @param monsterForUserIds
	 */
	public void clearMonstersForUserTeamSlot(
		String userId,
		Set<String> monsterForUserIds );

	/**
	 * Apply an arbitary number of property changes to an arbitrary number of monsters all owned by a
	 * single user. In the details object which encapsulates a map, a key corresponds to the identifier
	 * for a specific user monster, the values correspond to a specific type of property change
	 * operation.
	 * 
	 * @param details
	 * @see MonsterForUserOp
	 */
	public void modifyMonstersForUser( String userId, ModifyMonstersSpec details );

	public interface ModifyMonstersSpecBuilder
	{
		ModifyMonstersSpecBuilder setHealthAbsolute( String userMonsterId, int value );

		ModifyMonstersSpecBuilder setHealthRelative( String userMonsterId, int delta );

		ModifyMonstersSpecBuilder setExperienceAbsolute( String userMonsterId, int value );

		ModifyMonstersSpecBuilder setExperienceRelative( String userMonsterId, int delta );

		ModifyMonstersSpecBuilder setTeamSlotNum( String userMonsterId, int newTeamSlotNum );

		ModifyMonstersSpec build();
	}

	interface MonsterFunc extends Function<MonsterForUser>
	{};

	public class ModifyMonstersSpec
	{
		private final Multimap<String, MonsterFunc> specMap;

		ModifyMonstersSpec( final Multimap<String, MonsterFunc> specMap )
		{
			this.specMap = specMap;
		}

		Multimap<String, MonsterFunc> getSpecMultimap()
		{
			return specMap;
		}

		public static ModifyMonstersSpecBuilder builder()
		{
			return new ModifyMonstersSpecBuilderImpl();
		}
	}
	
	/**************************************************************************/
	
	/**
	 * At the moment, only used in UserCreateController. 
	 * @param userId 
	 * @param monsterIds - monster ids that the user owns
	 * @param combineStartTime
	 */
	
	public void createCompleteMonstersForUser( String userId, List<Integer> monsterIds, Date combineStartTime );

	/*
	public interface CreateMonstersSpecBuilder
	{
		CreateMonstersSpec build();
		
		CreateMonstersSpecBuilder setMonsterId( String userMonsterId, int monsterId);

		CreateMonstersSpecBuilder setCurrentExp( String userMonsterId, int currentExp );

		CreateMonstersSpecBuilder setCurrentLvl( String userMonsterId, int currentLvl );

		CreateMonstersSpecBuilder setCurrentHealth( String userMonsterId, int currentHealth );

		CreateMonstersSpecBuilder setNumPieces( String userMonsterId, int numPieces );
		
		CreateMonstersSpecBuilder setComplete( String userMonsterId );
		
		CreateMonstersSpecBuilder setCombineStateTime( String userMonsterId, Date combineStartTime );

		CreateMonstersSpecBuilder setTeamSlotNum( String userMonsterId, int teamSlotNum );
	}

	public class CreateMonstersSpec
	{
		final private Map<String, MonsterForUser> userMonsterIdToMfu;

		CreateMonstersSpec( final Map<String, MonsterForUser> userMonsterIdToMfu )
		{
			this.userMonsterIdToMfu = userMonsterIdToMfu;
		}

		public Map<String, MonsterForUser> getUserMonsterIdToMfu()
		{
			return userMonsterIdToMfu;
		}

		public static CreateMonstersSpecBuilder builder()
		{
			return new CreateMonstersSpecBuilderImpl();
		}
	}
	*/
	
	public void restrictUserMonsters(String userId, List<String> userMonsterIds );
}
