package com.lvl6.mobsters.services.monster;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.Function;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.services.monster.MonsterServiceImpl.ModifyMonstersSpecBuilderImpl;

public interface MonsterService
{
    
    public abstract void modifyMonstersForUserTeamSlot (
        String userId,
        Set<String> monsterForUserIds,
        int teamSlotNum);
    
	/**
     * Apply an arbitary number of property changes to an arbitrary number of monsters all owned by a
     * single user. In the details object which encapsulates a map, a key corresponds to the identifier for a specific user
     * monster, the values correspond to a specific type of property change operation. If
     * the existingUserMonsters object argument is set, then the db won't be accessed.
	 * 
	 * @param details
	 * @see MonsterForUserOp
	 */
    public abstract void modifyMonstersForUser(
        String userId,
        ModifyMonstersSpec details,
        Collection<MonsterForUser> existingUserMonsters);

    public interface ModifyMonstersSpecBuilder
    {
		ModifyMonstersSpecBuilder setHealthAbsolute(String userMonsterId, int value);

		ModifyMonstersSpecBuilder setHealthRelative(String userMonsterId, int delta);

        ModifyMonstersSpecBuilder setExperienceAbsolute( String userMonsterId, int value );

        ModifyMonstersSpecBuilder setExperienceRelative( String userMonsterId, int delta );

        ModifyMonstersSpecBuilder setTeamSlotNum( String userMonsterId, int newTeamSlotNum);
        
		ModifyMonstersSpec build();
	}

    interface MonsterFunc extends Function<MonsterForUser> {};

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
}
