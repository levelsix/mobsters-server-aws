package com.lvl6.mobsters.services.minijob;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository;

@Component
public class MiniJobServiceImpl implements MiniJobService {
    @Autowired
    private MiniJobForUserRepository miniJobForUserRepository;

    @Override
    public void modifyMiniJobsForUser( String userId, ModifyUserMiniJobsSpec modifySpec ) {
        // txManager.startTransaction();

        // get whatever we need from the database
        final Multimap<String, UserMiniJobFunc> modSpecMultimap = modifySpec.getModSpecMultimap();
        final Set<String> miniJobIds = modSpecMultimap.keySet();
        
        List<MiniJobForUser> existingUserMiniJobs = miniJobForUserRepository.findByUserIdAndId(userId, miniJobIds);
        // Mutate the objects
        
        //update the existing ones
        // txManager.startTransaction();
        for (final MiniJobForUser nextMiniJob : existingUserMiniJobs) {
            String miniJobForUserId = nextMiniJob.getMiniJobForUserId();
            
            Collection<UserMiniJobFunc> miniJobOps = modSpecMultimap.get(miniJobForUserId);
            for (UserMiniJobFunc nextMiniJobOp : miniJobOps) {
                nextMiniJobOp.apply(nextMiniJob);
            }
        }

        if (!CollectionUtils.lacksSubstance(existingUserMiniJobs)) {
            miniJobForUserRepository.saveAll(existingUserMiniJobs);
        } else {
            Log.error("User has no mini jobs. user=" + userId + " modifySpec=" + modifySpec);
        }
        // Write back to the database, then close the transaction by returning
        // TBD: Need to restore a workable save interface.
        // monsterForUserRepository.save(existingUserMiniJobs);
        // txManager.endTransaction();
    }

    // motivation for two separate Builders is because service will only be modifying
    // existing objects or creating new ones
    static class ModifyUserMiniJobsSpecBuilderImpl implements ModifyUserMiniJobsSpecBuilder
    {
        // modification specification map
        final Multimap<String, UserMiniJobFunc> modSpecMap;

        ModifyUserMiniJobsSpecBuilderImpl() {
            this.modSpecMap = ArrayListMultimap.create();
        }
        
        @Override
        public ModifyUserMiniJobsSpec build() {
            final ModifyUserMiniJobsSpec retVal = new ModifyUserMiniJobsSpec(modSpecMap);
            
            return retVal;
        }

        @Override
        public ModifyUserMiniJobsSpecBuilder setUserMonsterIds( String userMiniJobId, Set<String> userMonsterIds) {
            modSpecMap.put(
                userMiniJobId,
                new SetUserMonsterIds(userMonsterIds)
            );
            return this;
        }
        
        @Override
        public ModifyUserMiniJobsSpecBuilder setTimeStarted( String userMiniJobId, Date timeStarted ) {
            modSpecMap.put(
                userMiniJobId,
                new SetTimeStarted(timeStarted)
            );
            return this;
        }
        
        @Override
        public ModifyUserMiniJobsSpecBuilder setTimeCompleted( String userMiniJobId, Date timeCompleted ) {
            modSpecMap.put(
                userMiniJobId,
                new SetTimeCompleted(timeCompleted)
            );
            return this;
        }
    }
    
    static class SetUserMonsterIds implements UserMiniJobFunc {

        final private Set<String> userMonsterIds;
        
        SetUserMonsterIds(Set<String> userMonsterIds) {
            this.userMonsterIds = userMonsterIds;
        }
        
        @Override
        public void apply( MiniJobForUser t )
        {
            if (CollectionUtils.lacksSubstance(t.getUserMonsterIds())) {
                t.setUserMonsterIds(userMonsterIds);
            } else {
                Log.error("UserMonsterIds already set (not reseting). MiniJob=" + t +
                    " newMonsterIds=" + userMonsterIds);
            }
        }
        
    }
    
    static class SetTimeStarted implements UserMiniJobFunc {

        final private Date timeStarted;
        
        SetTimeStarted(Date timeStarted) {
            this.timeStarted = timeStarted;
        }
        
        @Override
        public void apply( MiniJobForUser t )
        {
            if (null == t.getTimeStarted()) {
                t.setTimeStarted(timeStarted);
            } else {
                Log.error("UserMiniJob already started (not reseting). MiniJob=" + t +
                    " timeStarted=" + timeStarted);
            }
        }
        
    }
    
    static class SetTimeCompleted implements UserMiniJobFunc {

        final private Date timeStarted;
        
        SetTimeCompleted(Date timeStarted) {
            this.timeStarted = timeStarted;
        }
        
        @Override
        public void apply( MiniJobForUser t )
        {
            if (null == t.getTimeStarted()) {
                t.setTimeStarted(timeStarted);
            } else {
                Log.error("UserMiniJob already started (not reseting). MiniJob=" + t +
                    " timeStarted=" + timeStarted);
            }
        }
        
    }
    
    // motivation for two separate Builders is because service will only be modifying
    // existing objects or creating new ones
    static class CreateUserMiniJobsSpecBuilderImpl implements CreateUserMiniJobsSpecBuilder
    {
        // the end state: objects to be saved to db
        final Map<String, MiniJobForUser> userMiniJobIdToMjfu;
        
        CreateUserMiniJobsSpecBuilderImpl() {
            this.userMiniJobIdToMjfu = new HashMap<String, MiniJobForUser>();
        }

        private MiniJobForUser getTarget( String userMiniJobId ) {
            MiniJobForUser afu = userMiniJobIdToMjfu.get(userMiniJobId);
            if (null == afu) {
                afu = new MiniJobForUser();
                userMiniJobIdToMjfu.put(userMiniJobId, afu);
            }
            return afu;
        }

        @Override
        public CreateUserMiniJobsSpecBuilder setUserMonsterIds(
            String userMiniJobId,
            Set<String> userMonsterIds )
        {
            MiniJobForUser afu = getTarget(userMiniJobId);
            afu.setUserMonsterIds(userMonsterIds);
            
            return this;
        }

        @Override
        public CreateUserMiniJobsSpecBuilder setTimeStarted(
            String userMiniJobId, Date timeStarted )
        {
            MiniJobForUser afu = getTarget(userMiniJobId);
            afu.setTimeStarted(timeStarted);
            
            return this;
        }

        @Override
        public CreateUserMiniJobsSpecBuilder setTimeCompleted(
            String userMiniJobId, Date timeStarted )
        {
            MiniJobForUser afu = getTarget(userMiniJobId);
            afu.setTimeStarted(timeStarted);
            
            return this;
        }

        @Override
        public CreateUserMiniJobsSpec build() {

            return new CreateUserMiniJobsSpec(userMiniJobIdToMjfu);
        }
    }

    //for the dependency injection
    public MiniJobForUserRepository getMiniJobForUserRepository()
    {
        return miniJobForUserRepository;
    }

    public void setMiniJobForUserRepository( MiniJobForUserRepository miniJobForUserRepository )
    {
        this.miniJobForUserRepository = miniJobForUserRepository;
    }

}
