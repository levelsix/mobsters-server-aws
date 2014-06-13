package com.lvl6.mobsters.services.structure;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository;
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository;

@Component
public class StructureServiceImpl implements StructureService {
    
    private static Logger LOG = LoggerFactory.getLogger(StructureServiceImpl.class);

    @Autowired
    private ObstacleForUserRepository obstacleForUserRepository;
    
    @Autowired
    private StructureForUserRepository structureForUserRepository;

    //NON CRUD LOGIC******************************************************************
    
    
    //CRUD LOGIC******************************************************************

    /**************************************************************************/

    @Override
    public void createObstaclesForUser( String userId, CreateUserObstaclesSpec createSpec ) {
        // txManager.startTransaction();
        
        // get whatever we need from the database, which is nothing
        final Map<String, ObstacleForUser> userObstacleIdToOfu = createSpec.getUserObstacleIdToOfu();
        
        for ( ObstacleForUser ofu : userObstacleIdToOfu.values()) {
            ofu.setUserId(userId);
        }
        
        obstacleForUserRepository.saveAll(userObstacleIdToOfu.values());
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
        
        structureForUserRepository.saveAll(userStructureIdToOfu.values());
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
    
    //for the dependency injection
    public ObstacleForUserRepository getObstacleForUserRepository()
    {
        return obstacleForUserRepository;
    }

	public void setObstacleForUserRepository( ObstacleForUserRepository miniJobForUserRepository )
	{
		this.obstacleForUserRepository = miniJobForUserRepository;
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

}
