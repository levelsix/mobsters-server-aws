package com.lvl6.mobsters.services.monster;

import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.Function;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.services.monster.MonsterServiceImpl.ModifyMonstersSpecBuilderImpl;

public interface MonsterService
{
	/**
     * Apply an arbitary number of property changes to an arbitrary number of monsters all owned by a
     * single user. In the details table, a row corresponds to the identifier for a specific user
     * monster, a column corresponds to a specific type of property change operation, and a value is the
     * argument required to perform the column-specified operation.
	 * 
	 * @param details
	 * @see MonsterForUserOp
	 */
    public abstract void modifyMonstersForUser( String userId, ModifyMonstersSpec details );

    public interface ModifyMonstersSpecBuilder
    {
		ModifyMonstersSpecBuilder setHealthAbsolute(String monsterId, int value);

		ModifyMonstersSpecBuilder setHealthRelative(String monsterId, int delta);

        ModifyMonstersSpecBuilder setExperienceAbsolute( String monsterId, int value );

        ModifyMonstersSpecBuilder setExperienceRelative( String monsterId, int delta );

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
