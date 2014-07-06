package com.lvl6.mobsters.services.minijob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepositoryImpl;
import com.lvl6.mobsters.info.MiniJob;

@Component
public class MiniJobServiceImpl implements MiniJobService {
    
    private static Logger LOG = LoggerFactory.getLogger(MiniJobServiceImpl.class);

    @Autowired
    private MiniJobForUserRepository miniJobForUserRepository;

    //NON CRUD LOGIC******************************************************************
    @Override
    public List<MiniJob> spawnMiniJobs(int numToSpawn, int structId) {
        Map<Integer, MiniJob> miniJobIdToMiniJob = null;// someClass..getMiniJobForStructId(structId);

        List<MiniJob> spawnedMiniJobs = new ArrayList<MiniJob>();

        if (0 == numToSpawn) {
            LOG.info("client just reseting the last spawn time");
            return spawnedMiniJobs;
        }

        float probabilitySum = 0;// someClass.getMiniJobProbabilitySumForStructId(structId);
        Random rand = new Random();
        LOG.info("probabilitySum=" + probabilitySum);
        LOG.info("miniJobIdToMiniJob=" + miniJobIdToMiniJob);

        int numToSpawnCopy = numToSpawn;
        while (numToSpawnCopy > 0) {
            float randFloat = rand.nextFloat();
            LOG.info("randFloat=" + randFloat);

            float probabilitySoFar = 0;
            for (MiniJob mj : miniJobIdToMiniJob.values()) {
                float chanceToAppear = mj.getChanceToAppear();

                probabilitySoFar += chanceToAppear;
                float normalizedProb = probabilitySoFar / probabilitySum;

                LOG.info("probabilitySoFar=" + probabilitySoFar);
                LOG.info("normalizedProb=" + normalizedProb);
                if (randFloat > normalizedProb) {
                    continue;
                }

                LOG.info("selected MiniJob!: " + mj);
                //we have a winner
                spawnedMiniJobs.add(mj);
                break;
            }
            //regardless of whether or not we find one,
            //prevent it from infinite looping
            numToSpawnCopy--;
        }
        if (spawnedMiniJobs.size() != numToSpawn) {
            LOG.error("Could not spawn " + numToSpawn +
                " mini jobs. spawned: " + spawnedMiniJobs);
        }
        return spawnedMiniJobs;
    }
    
    /**************************************************************************/
	//CRUD LOGIC
    
    // BEGIN READ ONLY LOGIC
    @Override
    public List<MiniJobForUser> getMiniJobForUserId( String userId ) {
    	return miniJobForUserRepository.findByUserId(userId);
    }
    // END READ ONLY LOGIC
	
 	/**************************************************************************/
     
    @Override
    public void modifyMiniJobsForUser( String userId, ModifyUserMiniJobsSpec modifySpec ) {
        // txManager.startTransaction();

        // get whatever we need from the database
        final Multimap<String, UserMiniJobFunc> modSpecMultimap = modifySpec.getModSpecMultimap();
        final Set<String> miniJobIds = modSpecMultimap.keySet();
        
        List<MiniJobForUser> existingUserMiniJobs = miniJobForUserRepository.findByUserIdAndMiniJobForUserIdIn(userId, miniJobIds);
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

    /**************************************************************************/

    @Override
    public void createMiniJobsForUser( String userId, CreateUserMiniJobsSpec createSpec ) {
        // txManager.startTransaction();
        
        // get whatever we need from the database, which is nothing
        final Map<String, MiniJobForUser> userMiniJobIdToMjfu = createSpec.getUserMiniJobIdToMjfu();
        
        for ( MiniJobForUser mjfu : userMiniJobIdToMjfu.values()) {
            mjfu.setUserId(userId);
        }
        
        miniJobForUserRepository.saveAll(userMiniJobIdToMjfu.values());
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
        public CreateUserMiniJobsSpecBuilder setMiniJobId(
            String userMiniJobId,
            int miniJobId)
        {
            MiniJobForUser afu = getTarget(userMiniJobId);
            afu.setMiniJobId(miniJobId);
            
            return this;
        }

        @Override
        public CreateUserMiniJobsSpecBuilder setBaseDmgReceived(
            String userMiniJobId,
            int baseDmgReceived)
        {
            MiniJobForUser afu = getTarget(userMiniJobId);
            afu.setMiniJobId(baseDmgReceived);
            
            return this;
        }

        @Override
        public CreateUserMiniJobsSpecBuilder setDurationMinutes(
            String userMiniJobId,
            int durationMinutes)
        {
            MiniJobForUser afu = getTarget(userMiniJobId);
            afu.setMiniJobId(durationMinutes);
            
            return this;
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

    public void setMiniJobForUserRepository( MiniJobForUserRepositoryImpl miniJobForUserRepository )
    {
        this.miniJobForUserRepository = miniJobForUserRepository;
    }

}
