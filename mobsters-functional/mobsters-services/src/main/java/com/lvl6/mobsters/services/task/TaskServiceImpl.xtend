package com.lvl6.mobsters.services.task

import com.google.common.base.Preconditions
import com.lvl6.mobsters.common.utils.Director
import com.lvl6.mobsters.dynamo.TaskForUserCompleted
import com.lvl6.mobsters.dynamo.TaskForUserOngoing
import com.lvl6.mobsters.dynamo.repository.EventPersistentForUserRepository
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepository
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepositoryImpl
import com.lvl6.mobsters.dynamo.repository.TaskForUserOngoingRepository
import com.lvl6.mobsters.dynamo.repository.TaskStageForUserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import java.util.ArrayList
import java.util.Date
import java.util.List
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TaskServiceImpl implements TaskService {
    private static val Logger LOG = LoggerFactory.getLogger(TaskServiceImpl);

    @Autowired
    private var TaskForUserCompletedRepository taskForUserCompletedRepository

    @Autowired
    private var TaskForUserOngoingRepository taskForUserOngoingRepository
    
    @Autowired
    protected var TaskStageForUserRepository taskStageForUserRepository
    
    @Autowired
    protected var EventPersistentForUserRepository eventPersistentForUserRepository
    
    @Autowired
    private var DataServiceTxManager txManager
    
    //NON CRUD LOGIC
    
    /**************************************************************************/
    //CRUD LOGIC
    
    // BEGIN READ ONLY LOGIC
    
    /* BEGIN READ ONLY LOGIC *********************************************/
	
	override getUserTaskForUserId(String userId) {
    	val List<TaskForUserOngoing> tfuoList = taskForUserOngoingRepository.findByUserId( userId ) 
    	Preconditions.checkArgument(
    		tfuoList.nullOrEmpty,
    		"No ongoing tasks found for userId=%s; taskList=%s", userId, tfuoList
    	)
    	
    	var TaskForUserOngoing retVal
    	if (tfuoList.size() > 1) {
    		LOG.warn(
    			"User with userId=%s has multiple ongoing tasks.  Selecting the most recent from taskList=%s",
    			userId, tfuoList)
    		retVal = tfuoList.reduce[ min, next |
    			var TaskForUserOngoing nextMin = next
    			if (min.getStartDate() < next.getStartDate()) {
    				nextMin = min
    			}
    			
    			return nextMin
    		]
    	} else {
    		retVal = tfuoList.get(0)
    	}
    	
    	return retVal	
    }
	
	override getTaskCompletedForUser(String userId) {
    	return taskForUserCompletedRepository.findByUserId(userId);
	}
	
	override getTaskStagesForUserWithTaskForUserId(String userTaskId) {
    	return taskStageForUserRepository.findByTaskForUserId(userTaskId);
	}
	
	override getUserPersistentEventForUserId(String userId) {
		return eventPersistentForUserRepository.findByUserId(userId);
	}

    /**************************************************************************/

    override completeTasks( 
    	String userUuid, Director<TaskService.CompleteTasksBuilder> director
    ) {
        val ArrayList<TaskForUserCompleted> saveList =
        	(new TaskServiceImpl.CompleteTasksBuilderImpl(userUuid) => [ listBuilder |
        		director.apply(listBuilder)
        	]).build()
        
        var boolean success = false
        val boolean isTxRoot = txManager.requireTransaction()
        try {
	        // Save whatever we were asked to create to Dynamo.
	        taskForUserCompletedRepository.saveEach(saveList);
	        
	        // TODO: Isn't there a concept of changing something previous OnGoing to Completed?
	        success = true
        } finally {
        	if (success) {
        		if (isTxRoot) {
        			txManager.commit();
        		}
        	} else {
        		txManager.rollback();
        	}
        }
    }
    
    // motivation for two separate Builders is because service will only be modifying
    // existing objects or creating new ones
    static class CompleteTasksBuilderImpl implements TaskService.CompleteTasksBuilder
    {
        // the end state: objects to be saved to db
        private val String userUuid
        private val ArrayList<TaskForUserCompleted> completedTaskList;

        new(String userUuid) {
        	this.userUuid = userUuid
            completedTaskList = new ArrayList<TaskForUserCompleted>(2);
        }
        
        override taskId(int taskId)
        {
        	throw new UnsupportedOperationException(
        		"The workflow for normal task completion is not yet clear enough to determine if this belongs here"
        	)
        }
        
        override taskId(int taskId, Date timeOfEntry)
        {
            completedTaskList.add(
            	new TaskForUserCompleted() => [
            		it.userId = userUuid
            		it.taskId = taskId
            		it.timeOfEntry = timeOfEntry
            	]
            )
            
            return this;
        }

        def ArrayList<TaskForUserCompleted> build() {
            return completedTaskList;
        }
    }

    /**************************************************************************/
    
    //for the dependency injection
    def void setTaskForUserCompletedRepository( TaskForUserCompletedRepositoryImpl taskForUserCompletedRepository )
    {
        this.taskForUserCompletedRepository = taskForUserCompletedRepository;
    }

	def void setTaskForUserOngoingRepository(
		TaskForUserOngoingRepository taskForUserOngoingRepository )
	{
		this.taskForUserOngoingRepository = taskForUserOngoingRepository;
	}

	def void setTaskStageForUserRepository( 
		TaskStageForUserRepository taskStageForUserRepository
	)
	{
		this.taskStageForUserRepository = taskStageForUserRepository;
	}
	
	def void setEventPersistentForUserRepository( 
		EventPersistentForUserRepository eventPersistentForUserRepository
	)
	{
		this.eventPersistentForUserRepository = eventPersistentForUserRepository
	}
	
	def void setDataServiceTxManager( DataServiceTxManager txManager )
	{
		this.txManager = txManager
	}
}