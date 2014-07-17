package com.lvl6.mobsters.services.structure;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository;
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository;
import com.lvl6.mobsters.info.CoordinatePair;
import com.lvl6.mobsters.info.Structure;
import com.lvl6.mobsters.info.repository.StructureRepository;
import com.lvl6.mobsters.services.common.Lvl6MobstersException;
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode;

@Component
public class StructureServiceImpl implements StructureService {
    
    private static Logger LOG = LoggerFactory.getLogger(StructureServiceImpl.class);

    @Autowired
    private ObstacleForUserRepository obstacleForUserRepository;
    
    @Autowired
    private StructureForUserRepository structureForUserRepository;

    @Autowired
    private StructureRepository structureRepository;

	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC
//    public StructureForUser getStructureForUserIdAndId(String userId, String structureForUserId) {
//    	return structureForUserRepository.findByUserIdAndStructureForUserId(userId, structureForUserId);
//    }

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
    @Override
    public void createObstaclesForUser( String userId, CreateUserObstaclesSpec createSpec ) {
        // txManager.startTransaction();
        
        // get whatever we need from the database, which is nothing
        final Map<String, ObstacleForUser> userObstacleIdToOfu = createSpec.getUserObstacleIdToOfu();
        
        for ( ObstacleForUser ofu : userObstacleIdToOfu.values()) {
            ofu.setUserId(userId);
        }
        
        obstacleForUserRepository.saveEach(userObstacleIdToOfu.values());
    }
    
    // motivation for two separate Builders is because service will only be modifying
    // existing objects or creating new ones
    static class CreateUserObstaclesSpecBuilderImpl implements CreateUserObstaclesSpecBuilder
    {
        // the end state: objects to be saved to db
        final Map<String, ObstacleForUser> userObstacleIdToOfu;
        
        CreateUserObstaclesSpecBuilderImpl() {
            this.userObstacleIdToOfu = new HashMap<String, ObstacleForUser>();
        }

        private ObstacleForUser getTarget( String userObstacleId ) {
            ObstacleForUser afu = userObstacleIdToOfu.get(userObstacleId);
            if (null == afu) {
                afu = new ObstacleForUser();
                userObstacleIdToOfu.put(userObstacleId, afu);
            }
            return afu;
        }

        @Override
        public CreateUserObstaclesSpecBuilder setObstacleId(
            String userObstacleId,
            int miniJobId)
        {
            ObstacleForUser afu = getTarget(userObstacleId);
            afu.setObstacleId(miniJobId);
            
            return this;
        }

        @Override
        public CreateUserObstaclesSpecBuilder setXCoord(
            String userObstacleId,
            int xCoord)
        {
            ObstacleForUser afu = getTarget(userObstacleId);
            afu.setXcoord(xCoord);
            
            return this;
        }

        @Override
        public CreateUserObstaclesSpecBuilder setYCoord(
            String userObstacleId,
            int yCoord)
        {
            ObstacleForUser afu = getTarget(userObstacleId);
            afu.setYcoord(yCoord);
            
            return this;
        }
        
        @Override
        public CreateUserObstaclesSpecBuilder setOrientation(
            String userObstacleId,
            String orientation )
        {
            ObstacleForUser afu = getTarget(userObstacleId);
            afu.setOrientation(orientation);
            
            return this;
        }

        @Override
        public CreateUserObstaclesSpec build() {

            return new CreateUserObstaclesSpec(userObstacleIdToOfu);
        }
    }

    /**************************************************************************/

    @Override
    public void createStructuresForUser( String userId, CreateUserStructuresSpec createSpec ) {
        // txManager.startTransaction();
        
        // get whatever we need from the database, which is nothing
        final Map<String, StructureForUser> userStructureIdToOfu = createSpec.getUserStructureIdToOfu();
        
        for ( StructureForUser ofu : userStructureIdToOfu.values()) {
            ofu.setUserId(userId);
        }
        
        structureForUserRepository.saveEach(userStructureIdToOfu.values());
    }
    
    static class CreateUserStructuresSpecBuilderImpl implements CreateUserStructuresSpecBuilder
    {
        // the end state: objects to be saved to db
        final Map<String, StructureForUser> userStructureIdToOfu;
        
        CreateUserStructuresSpecBuilderImpl() {
            this.userStructureIdToOfu = new HashMap<String, StructureForUser>();
        }

        private StructureForUser getTarget( String userStructureId ) {
            StructureForUser afu = userStructureIdToOfu.get(userStructureId);
            if (null == afu) {
                afu = new StructureForUser();
                userStructureIdToOfu.put(userStructureId, afu);
            }
            return afu;
        }

        @Override
        public CreateUserStructuresSpecBuilder setStructureId(
            String userStructureId,
            int structureId)
        {
            StructureForUser sfu = getTarget(userStructureId);
            sfu.setStructId(structureId);
            
            return this;
        }

        @Override
        public CreateUserStructuresSpecBuilder setXCoord(
            String userStructureId,
            float xCoord)
        {
            StructureForUser afu = getTarget(userStructureId);
            afu.setxCoord(xCoord);
            
            return this;
        }

        @Override
        public CreateUserStructuresSpecBuilder setYCoord(
            String userStructureId,
            float yCoord)
        {
            StructureForUser afu = getTarget(userStructureId);
            afu.setyCoord(yCoord);
            
            return this;
        }
        
        @Override
        public CreateUserStructuresSpecBuilder setLastRetrievedTime(
            String userStructureId,
            Date lastRetrieved )
        {
            StructureForUser afu = getTarget(userStructureId);
            afu.setLastRetrieved(lastRetrieved);
            
            return this;
        }
        
        @Override
        public CreateUserStructuresSpecBuilder setPurchaseTime(
            String userStructureId,
            Date purchaseTime )
        {
            StructureForUser afu = getTarget(userStructureId);
            afu.setPurchaseTime(purchaseTime);
            
            return this;
        }
        
        @Override
        public CreateUserStructuresSpecBuilder setComplete(
            String userStructureId,
            boolean isComplete )
        {
            StructureForUser afu = getTarget(userStructureId);
            afu.setComplete(isComplete);
            
            return this;
        }
        
        @Override
        public CreateUserStructuresSpecBuilder setFbInviteStructLvl(
            String userStructureId,
            int fbInviteStructLvl )
        {
            StructureForUser afu = getTarget(userStructureId);
            afu.setFbInviteStructLvl(fbInviteStructLvl);
            
            return this;
        }

        @Override
        public CreateUserStructuresSpec build() {

            return new CreateUserStructuresSpec(userStructureIdToOfu);
        }
    }
    
    /**************************************************************************/
    
	@Override
	public void beginUpgradingUserStruct(StructureForUser sfu, Date upgradeTime) {
		Structure currentStruct = structureRepository.findOne(sfu.getStructId());
		int nextLevelStructId = currentStruct.getSuccessorStruct().getId(); 
		
		sfu.setStructId(nextLevelStructId);
		sfu.setPurchaseTime(upgradeTime);
		sfu.setComplete(false);
		structureForUserRepository.save(sfu);
	}

	@Override
	public void moveUserStructure(String userId, String userStructId, CoordinatePair cp) {
		StructureForUser sfu = structureForUserRepository.load(userId, userStructId);
		
		if (null == sfu) {
			LOG.error("No StructureForUser for id:" + userStructId);
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
		}
		
		sfu.setxCoord(cp.getX());
		sfu.setyCoord(cp.getY());
		structureForUserRepository.save(sfu);
	}
	
	@Override
	public void finishConstructingUserStructures( String userId, List<String> userStructIdList, Date now ) {
		// TODO: Transactionify
		List<StructureForUser> sfuList = structureForUserRepository.loadEach(userId, userStructIdList);
		
		checkIfUserCanFinishConstruction(userStructIdList, now, sfuList);
		
		//for each structure, select the ones that can indeed be constructed
		List<StructureForUser> canBeConstructedStructureForUsers =
		selectUserStructsThatCanFinishConstruction(now, sfuList);
		
		structureForUserRepository.saveEach(canBeConstructedStructureForUsers);
	}

	private void checkIfUserCanFinishConstruction(
			List<String> userStructIdList, Date now,
			List<StructureForUser> sfuList)
	{
		if ( CollectionUtils.lacksSubstance(sfuList) ) {
			LOG.error("no StructureForUsers for ids: " + userStructIdList);
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
		}
		
		if ( userStructIdList.size() != sfuList.size() ) {
			LOG.error( "some structs missing. userStructId=" + userStructIdList +
					", StructureForUsers=" + sfuList
			);
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
		}
		
	}

	private List<StructureForUser> selectUserStructsThatCanFinishConstruction(Date now,
			List<StructureForUser> sfuList)
	{
		List<StructureForUser> canBeConstructedStructureForUsers =
				new ArrayList<StructureForUser>();
		Map<Integer, Structure> idToStructureMap = getStructIdsToStructures(sfuList);
		for ( StructureForUser sfu : sfuList )
		{
			int structId = sfu.getStructId();
			if (!idToStructureMap.containsKey(structId)) {
				LOG.warn("no struct in db exists with id " + structId +
						", structureForUser=" + sfu
				);
				continue;
			}
			Structure struct = idToStructureMap.get(structId);
			
			Date purchaseDate = sfu.getPurchaseTime();
			
			if (null == purchaseDate) {
				LOG.warn("user struct has never been bought or purchased according to db. " + sfu);
				continue;
			}
			
			long buildTimeMillis = 60000*struct.getMinutesToBuild();
			long timeBuildFinished = purchaseDate.getTime() + buildTimeMillis;
			if (timeBuildFinished > buildTimeMillis) {
				LOG.warn("the building is not done yet. userstruct=" + ", client time is " +
          		now + ", purchase time was " + purchaseDate + ", buildTimeMillis=" + buildTimeMillis);
				continue;
			}
			
			sfu.setLastRetrieved(new Date(timeBuildFinished));
			canBeConstructedStructureForUsers.add(sfu);
		}
		
		return canBeConstructedStructureForUsers;
	}
	
	protected Map<Integer, Structure> getStructIdsToStructures(List<StructureForUser> sfuList) {
		Set<Integer> structureIds = new HashSet<Integer>();
		
		for ( StructureForUser sfu : sfuList ) {
			structureIds.add(sfu.getStructId());
		}
		
		List<Structure> structureList = structureRepository.findAll(structureIds);
		
		Map<Integer, Structure> structureMap = new HashMap<Integer, Structure>();
		for (Structure s : structureList) {
			structureMap.put(s.getId(), s);
		}
		return structureMap;
	}
    
    //for the dependency injection
    public ObstacleForUserRepository getObstacleForUserRepository()
    {
        return obstacleForUserRepository;
    }

	public void setObstacleForUserRepository( ObstacleForUserRepository obstacleForUserRepository )
	{
		this.obstacleForUserRepository = obstacleForUserRepository;
	}

	public StructureForUserRepository getStructureForUserRepository()
	{
		return structureForUserRepository;
	}

	public void setStructureForUserRepository(
		StructureForUserRepository structureForUserRepository )
	{
		this.structureForUserRepository = structureForUserRepository;
	}

	public StructureRepository getStructureRepository() {
		return structureRepository;
	}

	public void setStructureRepository(StructureRepository structureRepository) {
		this.structureRepository = structureRepository;
	}
	
}
