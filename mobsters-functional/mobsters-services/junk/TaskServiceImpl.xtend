package com.lvl6.mobsters.services.task

import com.google.common.base.Preconditions
import com.lvl6.mobsters.common.utils.AbstractAction
import com.lvl6.mobsters.common.utils.AbstractService
import com.lvl6.mobsters.common.utils.Director
import com.lvl6.mobsters.common.utils.ICallableAction
import com.lvl6.mobsters.dynamo.TaskForUserCompleted
import com.lvl6.mobsters.dynamo.TaskForUserOngoing
import com.lvl6.mobsters.dynamo.repository.EventPersistentForUserRepository
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepository
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepositoryImpl
import com.lvl6.mobsters.dynamo.repository.TaskForUserOngoingRepository
import com.lvl6.mobsters.dynamo.repository.TaskStageForUserRepository
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.validation.constraints.ConfigID
import com.lvl6.mobsters.validation.constraints.DynamoID
import java.util.ArrayList
import java.util.Date
import java.util.List
import javax.validation.constraints.Min
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.ScriptAssert
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TaskServiceImpl extends AbstractService implements TaskService {
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
	
	override generateUserTaskStages(String userId, Date curTime, int taskId, boolean isEvent, int eventId, int gemsSpent, List<Integer> questIds, String elementName, boolean forceEnemyElem) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	@ScriptAssert.List(
		@ScriptAssert(
			lang="javascript",
			script="if (_this.forceEnemyElem) { return(_this.elementName !== '' && _this.elementName !== null);}; return true;",
			message="elementName must not be blank or null when forceEnemyElem is set true"
		), 
		@ScriptAssert(
			lang="javascript",
			script="if (_this.isEvent) { return(_this.eventId > 0); }; return true;",
			message="If this is an event, then eventId must be a positive value."
		)
	)
	static class GenerateUserTaskStagesActionImpl 
		extends AbstractAction 
		implements ICallableAction<TaskService.GenerateUserTaskStagesResponseBuilder>
	{
	@DynamoID
	val String userId
	
	// Cannot mark @Past because client clock may run fast.  Create an annotation for
	// tolerated margin for error?
	val Date curTime
	
	@ConfigID
	val int taskId
	
	val boolean isEvent
	val int eventId
	
	@Min(0)
	val int gemsSpent
	
	@Size(min=1)
	val List<Integer> questIds
	
	val String elementName
	val boolean forceEnemyElem
	
	new(TaskServiceImpl parentService, String userId, Date curTime, int taskId,
			boolean isEvent, int eventId, int gemsSpent, List<Integer> questIds, String elementName,
			boolean forceEnemyElem)
		{
			super(parentService)
			this.userId = userId;
			this.curTime = curTime;
			this.taskId = taskId;
			this.isEvent = isEvent;
			this.eventId = eventId;
			this.gemsSpent = gemsSpent;
			this.questIds = questIds;
			this.elementName = elementName;
			this.forceEnemyElem = forceEnemyElem;
		}
		
		override execute(TaskService.GenerateUserTaskStagesResponseBuilder resultBuilder) 
		{
			throw new UnsupportedOperationException("TODO: auto-generated method stub")
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