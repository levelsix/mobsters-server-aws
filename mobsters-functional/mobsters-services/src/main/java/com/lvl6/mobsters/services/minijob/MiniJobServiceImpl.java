package com.lvl6.mobsters.services.minijob;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository;
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepositoryImpl;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.info.MiniJob;

@Component
public class MiniJobServiceImpl implements MiniJobService {
    
    private static Logger LOG = LoggerFactory.getLogger(MiniJobServiceImpl.class);

    @Autowired
    private MiniJobForUserRepository miniJobForUserRepository;

    @Autowired
    private DataServiceTxManager txManager;

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
            
            //regardless of whether or not we find one, prevent it from infinite looping
            numToSpawnCopy--;
        }
        if (spawnedMiniJobs.size() != numToSpawn) {
            LOG.error("Could not spawn " + numToSpawn +
                " mini jobs. spawned: " + spawnedMiniJobs);
        }
        return spawnedMiniJobs;
    }
    
    //READ-ONLY CRUD LOGIC******************************************************

    public List<MiniJobForUser> getMiniJobForUserId( String userId ) {
    	return miniJobForUserRepository.findByUserId(userId);
    }
	
 	//TRANSACTIONAL CRUD LOGIC**************************************************
     
    @Override
    public void modifyMiniJobsForUser( String userId, Director<ModifyUserMiniJobsSpecBuilder> director ) {
    	// Collect a work definition from the caller
    	ModifyUserMiniJobsSpecBuilderImpl specBuilder = new ModifyUserMiniJobsSpecBuilderImpl();
    	director.apply(specBuilder);

        // get whatever we need from the database
        final Multimap<String, UserMiniJobFunc> modSpecMultimap = specBuilder.getModSpecMultimap();
        final Set<String> miniJobIds = modSpecMultimap.keySet();
        
        boolean success = false;
        txManager.beginTransaction();
        try {
        List<MiniJobForUser> existingUserMiniJobs = miniJobForUserRepository.findByUserIdAndId(userId, miniJobIds);
	        if (CollectionUtils.isEmptyOrNull(existingUserMiniJobs)) {
	            final String message = "User has none of the required mini jobs. user=" + userId + ", miniJobIds=" + miniJobIds.toString();
				Log.error(message);
				
				// TODO: Select the Lvl6 Exception that expresses "none found"
	            throw new IllegalStateException(message);
	        } else if(existingUserMiniJobs.size() != miniJobIds.size()) {
	            final String message = "User lacks some of the required mini jobs. user=" + userId + ", miniJobIds=" + miniJobIds.toString();
				Log.error(message);
				
				// TODO: Select the Lvl6 Exception that expresses "some missing"
	            throw new IllegalStateException(message);
	        }
        
        // Mutate the objects
        for (final MiniJobForUser nextMiniJob : existingUserMiniJobs) {
	        	// No null test by design.  The contract of repository classes is to return a smaller list when objects do not exist, not to
	        	// return null placeholders.  Use unit tests to assert invariants hold true rather than redundant runtime null checks.
            
	            for (final UserMiniJobFunc nextMiniJobOp : 
	            	modSpecMultimap.get(
	            		nextMiniJob.getMiniJobForUserId()
	            	)
	            ) {
                nextMiniJobOp.apply(nextMiniJob);
            }
        }

	        // Write back to the database, then commit the transaction by toggling success to true.
	        miniJobForUserRepository.saveEach(existingUserMiniJobs);	        
	        success = true;
        } finally {
        	if (success) {
        		txManager.commit();
        } else {
        		txManager.rollback();
        	}
        }
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
        
        /**
         * This is this concrete builder's build step.  It therefore does NOT belong in the builder interface!
         */
        Multimap<String, UserMiniJobFunc> getModSpecMultimap() {
        	// TODO: There are better ways to prevent the value returned by a call to this method from potentially
        	//       being changed further, but extracting an immutable copy is the easiest to implement.
            return ImmutableMultimap.copyOf(modSpecMap);
        }

        @Override
        public ModifyUserMiniJobsSpecBuilder startJob( String userMiniJobId, Set<String> userMonsterIds, Date timeStarted ) {
            modSpecMap.put(
                userMiniJobId,
                new StartMiniJob(userMonsterIds, timeStarted)
            );
            return this;
        }
        
        @Override
        public ModifyUserMiniJobsSpecBuilder completeJob( String userMiniJobId, Date timeCompleted ) {
            modSpecMap.put(
                userMiniJobId,
                new SetTimeCompleted(timeCompleted)
            );
            return this;
        }
    }
    
    static class StartMiniJob implements UserMiniJobFunc {
        final private Date timeStarted;
		final private HashSet<String> userMonsterIds;
        
        StartMiniJob(Set<String> userMonsterIds, Date timeStarted) {
        	this.userMonsterIds = new HashSet<String>(userMonsterIds);
            this.timeStarted = timeStarted;
        }
        
        @Override
        public void apply( MiniJobForUser t )
        {
            if (CollectionUtils.lacksSubstance(t.getUserMonsterIds()) && (null == t.getTimeStarted())) {
                t.setUserMonsterIds(userMonsterIds);
                t.setTimeStarted(timeStarted);
            } else {
                Log.error("UserMiniJob already started (not reseting). MiniJob=" + t +
                	", userMonsterIds=" + userMonsterIds + 
                    ", timeStarted=" + timeStarted);
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
    public void createMiniJobsForUser( String userId, Director<CreateUserMiniJobsSpecBuilder> director ) {
        // txManager.startTransaction();
    	// Collect a work definition from the caller
    	CreateUserMiniJobsSpecBuilderImpl specBuilder = new CreateUserMiniJobsSpecBuilderImpl(userId);
    	director.apply(specBuilder);
        
        // get whatever we have been asked to save to the database
        final Map<String, MiniJobForUser> userMiniJobIdToMjfu = specBuilder.getUserMiniJobIdToMjfu();
        
        miniJobForUserRepository.saveEach(userMiniJobIdToMjfu.values());
    }
    
    // motivation for two separate Builders is because service will only be modifying
    // existing objects or creating new ones
    static class CreateUserMiniJobsSpecBuilderImpl implements CreateUserMiniJobsSpecBuilder
    {
        // the end state: objects to be saved to db
		private final String userId;
		private final Map<String, MiniJobForUser> userMiniJobIdToMjfu;
        
        CreateUserMiniJobsSpecBuilderImpl(final String userId) {
            this.userId = userId;
            this.userMiniJobIdToMjfu = new HashMap<String, MiniJobForUser>();
        }

        private MiniJobForUser getTarget( String userMiniJobId ) {
            MiniJobForUser afu = userMiniJobIdToMjfu.get(userMiniJobId);
            if (null == afu) {
                afu = new MiniJobForUser();
                afu.setUserId(userId);
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

       /**
        * This is this concrete builder's build step.  It therefore does NOT belong in the builder interface!
        */
       Map<String, MiniJobForUser> getUserMiniJobIdToMjfu() {
        	// TODO: There are better ways to prevent the value returned by a call to this method from potentially
        	//       being changed further, but extracting an immutable copy is the easiest to implement.
            return ImmutableMap.copyOf(userMiniJobIdToMjfu);
        }
    }

    //for the dependency injection
    public void setMiniJobForUserRepository( MiniJobForUserRepositoryImpl miniJobForUserRepository )
    {
        this.miniJobForUserRepository = miniJobForUserRepository;
    }
}
