package com.lvl6.mobsters.services.achievement;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.AchievementForUser;
import com.lvl6.mobsters.dynamo.repository.AchievementForUserRepository;

@Component
public class AchievementServiceImpl implements AchievementService {
    @Autowired
    private AchievementForUserRepository achievementForUserRepository;

    @Override
    public void modifyAchievementsForUser( String userId, ModifyUserAchievementsSpec modifySpec ) {
        // txManager.startTransaction();

        // get whatever we need from the database
        final Map<Integer, AchievementForUser> modMap = modifySpec.getAchievementsForUser();
        final Collection<AchievementForUser> modifiedUserAchievements = modMap.values();

        // Mutate the objects

        // txManager.startTransaction();
        for (final AchievementForUser nextAchievement : modifiedUserAchievements) {
            nextAchievement.setUserId(userId);
        }

        achievementForUserRepository.saveAll(modifiedUserAchievements);
        // Write back to the database, then close the transaction by returning
        // TBD: Need to restore a workable save interface.
        // monsterForUserRepository.save(existingUserAchievements);
        // txManager.endTransaction();
    }

    static class ModifyUserAchievementsSpecBuilderImpl implements ModifyUserAchievementsSpecBuilder
    {
        final Map<Integer, AchievementForUser> achievementIdToAfu;

        ModifyUserAchievementsSpecBuilderImpl() {
            this.achievementIdToAfu = new HashMap<Integer, AchievementForUser>();
        }

        private AchievementForUser getTarget( int achievementId ) {
            AchievementForUser afu = achievementIdToAfu.get(achievementId);
            if (null == afu) {
                afu = new AchievementForUser();
                achievementIdToAfu.put(achievementId, afu);
            }
            return afu;
        }

        @Override
        public ModifyUserAchievementsSpecBuilder setProgressAbsolute(
            int achievementId,
            int progress )
        {
            AchievementForUser afu = getTarget(achievementId);
            afu.setProgress(progress);
            return this;
        }

        @Override
        public ModifyUserAchievementsSpecBuilder setIsComplete( int achievementId ) {
            AchievementForUser afu = getTarget(achievementId);
            afu.setComplete(true);
            return this;
        }

        @Override
        public ModifyUserAchievementsSpecBuilder setTimeComplete(
            int achievementId,
            Date timeCompleted )
        {
            AchievementForUser afu = getTarget(achievementId);
            afu.setTimeCompleted(timeCompleted);
            return this;
        }

        @Override
        public ModifyUserAchievementsSpec build() {
            final ModifyUserAchievementsSpec retVal =
                new ModifyUserAchievementsSpec(achievementIdToAfu);

            return retVal;
        }
    }

}
