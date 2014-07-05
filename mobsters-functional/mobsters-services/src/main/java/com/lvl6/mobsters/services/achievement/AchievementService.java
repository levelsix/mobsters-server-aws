package com.lvl6.mobsters.services.achievement;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.Function;
import com.lvl6.mobsters.dynamo.AchievementForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.services.achievement.AchievementServiceImpl.ModifyUserAchievementsSpecBuilderImpl;

public interface AchievementService {


	//NON CRUD LOGIC
	
	/**************************************************************************/
	//CRUD LOGIC
    
    // BEGIN READ ONLY LOGIC
	public List<AchievementForUser> getAchievementsForUserId( String userId );

	// END READ ONLY LOGIC
	
	/**************************************************************************/
    
    
	
    /**
     * Apply an arbitary number of property changes to an arbitrary number of monsters all owned by a
     * single user. In the details table, a row corresponds to the identifier for a specific user
     * monster, a column corresponds to a specific type of property change operation, and a value is the
     * argument required to perform the column-specified operation.
     * 
     * @param details
     * @see AchievementForUserOp
     */
    public abstract void modifyAchievementsForUser( String userId, ModifyUserAchievementsSpec details );

    public interface ModifyUserAchievementsSpecBuilder {
        ModifyUserAchievementsSpec build();

        ModifyUserAchievementsSpecBuilder setProgressAbsolute( int achievementId, int progress );

        ModifyUserAchievementsSpecBuilder setIsComplete( int achievementId );

        ModifyUserAchievementsSpecBuilder setTimeComplete( int achievementId, Date timeCompleted );
    }
    
    interface UserAchievementFunc extends Function<AchievementForUser> {};

    public class ModifyUserAchievementsSpec {
        // the end state: objects to be saved to db
        final private Map<Integer, AchievementForUser> achievementIdToAfu;

        // modification specification map
        final private Multimap<Integer, UserAchievementFunc> modSpecMap;
        
        ModifyUserAchievementsSpec( Map<Integer, AchievementForUser> achievementIdToAfu,
            Multimap<Integer, UserAchievementFunc> modSpecMap) {
            this.achievementIdToAfu = achievementIdToAfu;
            this.modSpecMap = modSpecMap;
        }

        Map<Integer, AchievementForUser> getAchievementsForUser() {
            return achievementIdToAfu;
        }
        
        Multimap<Integer, UserAchievementFunc> getModSpecMultimap() {
            return modSpecMap;
        }

        public static ModifyUserAchievementsSpecBuilder builder() {
            return new ModifyUserAchievementsSpecBuilderImpl();
        }
    }

    
    public User redeemAchievement( String userId, int achievementId, Date now );
}
