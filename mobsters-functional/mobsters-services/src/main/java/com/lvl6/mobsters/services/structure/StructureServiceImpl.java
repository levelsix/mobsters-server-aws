package com.lvl6.mobsters.services.structure;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository;

@Component
public class StructureServiceImpl implements StructureService {
    
    private static Logger LOG = LoggerFactory.getLogger(StructureServiceImpl.class);

    @Autowired
    private ObstacleForUserRepository obstacleForUserRepository;

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

    //for the dependency injection
    public ObstacleForUserRepository getObstacleForUserRepository()
    {
        return obstacleForUserRepository;
    }

    public void setObstacleForUserRepository( ObstacleForUserRepository miniJobForUserRepository )
    {
        this.obstacleForUserRepository = miniJobForUserRepository;
    }

}
