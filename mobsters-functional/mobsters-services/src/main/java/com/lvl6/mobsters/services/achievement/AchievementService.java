package com.lvl6.mobsters.services.achievement;

import java.util.Date;
import java.util.Map;

import com.lvl6.mobsters.dynamo.AchievementForUser;
import com.lvl6.mobsters.services.achievement.AchievementServiceImpl.ModifyUserAchievementsSpecBuilderImpl;

public interface AchievementService {

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

    public class ModifyUserAchievementsSpec {
        final Map<Integer, AchievementForUser> achievementIdToAfu;

        ModifyUserAchievementsSpec( Map<Integer, AchievementForUser> achievementIdToAfu ) {
            this.achievementIdToAfu = achievementIdToAfu;
        }

        Map<Integer, AchievementForUser> getAchievementsForUser() {
            return achievementIdToAfu;
        }

        public static ModifyUserAchievementsSpecBuilder builder() {
            return new ModifyUserAchievementsSpecBuilderImpl();
        }
    }

}
