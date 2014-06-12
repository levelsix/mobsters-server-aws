package com.lvl6.mobsters.services.structure;

import java.util.Map;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.services.structure.StructureServiceImpl.CreateUserObstaclesSpecBuilderImpl;
public interface StructureService {
    
    //NON CRUD LOGIC******************************************************************
    
    //CRUD LOGIC******************************************************************


    /**************************************************************************/
    
    public abstract void createObstaclesForUser( String userId, CreateUserObstaclesSpec createSpec );
    
    public interface CreateUserObstaclesSpecBuilder {
        public CreateUserObstaclesSpec build();
        
        public CreateUserObstaclesSpecBuilder setObstacleId( String userObstacleId, int obstacleId);
        
        public CreateUserObstaclesSpecBuilder setXCoord( String userObstacleId, int xCoord);

        public CreateUserObstaclesSpecBuilder setYCoord( String userObstacleId, int yCoord);
        
        public CreateUserObstaclesSpecBuilder setOrientation( String userObstacleId, String orientation );
        
    }
    
    public class CreateUserObstaclesSpec {
        // the end state: objects to be saved to db
        final private Map<String, ObstacleForUser> userObstacleIdToOfu;

        CreateUserObstaclesSpec( Map<String, ObstacleForUser> userObstacleIdToOfu) {
            this.userObstacleIdToOfu = userObstacleIdToOfu;
        }
        
        Map<String, ObstacleForUser> getUserObstacleIdToOfu() {
            return userObstacleIdToOfu;
        }

        public static CreateUserObstaclesSpecBuilder builder() {
            return new CreateUserObstaclesSpecBuilderImpl();
        }
    }
}
