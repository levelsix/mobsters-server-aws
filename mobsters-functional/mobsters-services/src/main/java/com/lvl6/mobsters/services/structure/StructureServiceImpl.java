package com.lvl6.mobsters.services.structure;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository;
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;

@Component
public class StructureServiceImpl implements StructureService {
    
    private static Logger LOG = LoggerFactory.getLogger(StructureServiceImpl.class);

    @Autowired
    private ObstacleForUserRepository obstacleForUserRepository;
    
    @Autowired
    private StructureForUserRepository structureForUserRepository;
    
    @Autowired
    private DataServiceTxManager txManager;

    //NON CRUD LOGIC******************************************************************
    
    
    //CRUD LOGIC******************************************************************

    /**************************************************************************/

    @Override
    public void createObstaclesForUser( 
    	final String userId, 
    	final Director<CreateObstacleCollectionBuilder> director ) 
    {
    	final CreateObstacleCollectionBuilderImpl builder =
    		new CreateObstacleCollectionBuilderImpl(userId);
    	director.apply(builder);
        
    	boolean success = false;
    	txManager.requireTransaction();
    	try {
	        // save whatever the callback created through the builder
	        obstacleForUserRepository.saveEach(
	        	builder.build());
    	} finally {
    		if(success) {
    			txManager.commit();
    		} else {
    			txManager.rollback();
    		}
    	}
    }
    
    // motivation for two separate Builders is because service will only be modifying
    // existing objects or creating new ones
    static class CreateObstacleCollectionBuilderImpl implements CreateObstacleCollectionBuilder
    {
        // the end state: objects to be saved to db
        private final ArrayList<ObstacleForUser> retVal;
		private final String userId;
        
        CreateObstacleCollectionBuilderImpl(final String userId) {
        	this.userId = userId;
            this.retVal = new ArrayList<ObstacleForUser>();
        }

		@Override
		public CreateObstacleCollectionBuilder addStructure(
			int obstacleId, float xCoord, float yCoord, String orientation) {
			return this;
		}

        ArrayList<ObstacleForUser> build() {
            return retVal;
        }
    }

    /**************************************************************************/

    @Override
    public void createStructuresForUser( 
    	final String userId, 
    	Director<CreateStructureCollectionBuilder> director ) 
    {
    	final CreateStructureCollectionBuilderImpl builder =
    		new CreateStructureCollectionBuilderImpl(userId);
    	director.apply(builder);
        
    	boolean success = false;
    	txManager.requireTransaction();
    	try {
	        // save whatever the callback created through the builder
    		structureForUserRepository.saveEach(
	        	builder.build());
    	} finally {
    		if(success) {
    			txManager.commit();
    		} else {
    			txManager.rollback();
    		}
    	}
    }
      
    //for the dependency injection
	void setObstacleForUserRepository(
		ObstacleForUserRepository obstacleForUserRepository )
	{
		this.obstacleForUserRepository = obstacleForUserRepository;
	}

	void setStructureForUserRepository(
		StructureForUserRepository structureForUserRepository )
	{
		this.structureForUserRepository = structureForUserRepository;
	}

}
