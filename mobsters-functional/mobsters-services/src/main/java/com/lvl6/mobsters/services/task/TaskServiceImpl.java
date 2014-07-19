package com.lvl6.mobsters.services.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;
import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.TaskStageForUser;
import com.lvl6.mobsters.dynamo.repository.EventPersistentForUserRepository;
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepository;
import com.lvl6.mobsters.dynamo.repository.TaskForUserCompletedRepositoryImpl;
import com.lvl6.mobsters.dynamo.repository.TaskForUserOngoingRepository;
import com.lvl6.mobsters.dynamo.repository.TaskStageForUserRepository;

@Component
public class TaskServiceImpl implements TaskService {
    
    private static Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskForUserCompletedRepositoryImpl taskForUserCompletedRepository;

    @Autowired
    protected TaskForUserOngoingRepository taskForUserOngoingRepository;
    
    @Autowired
    protected TaskStageForUserRepository taskStageForUserRepository;
    
    @Autowired
    protected EventPersistentForUserRepository eventPersistentForUserRepository;
    
    //NON CRUD LOGIC
    
    /**************************************************************************/
    //CRUD LOGIC
    
    // BEGIN READ ONLY LOGIC
    @Override
	public TaskForUserOngoing getUserTaskForUserId( String userId ) {
    	return taskForUserOngoingRepository.findByUserId( userId );
    }

    @Override
    public List<TaskForUserCompleted> getTaskCompletedForUser( String userId ) {
    	return taskForUserCompletedRepository.findByUserId(userId);
    }
    
    @Override
    public List<TaskStageForUser> getTaskStagesForUserWithTaskForUserId( String userTaskId ) {
    	return taskStageForUserRepository.findByTaskForUserId(userTaskId);
    }
    
    @Override
    public List<EventPersistentForUser> getUserPersistentEventForUserId( String userId ) {
    	return eventPersistentForUserRepository.findByUserId(userId);
    }
    
	// END READ ONLY LOGIC
    
    /**************************************************************************/

    @Override
    public void createTasksForUserCompleted( String userId, CreateUserTasksCompletedSpec createSpec ) {
        // txManager.startTransaction();
        
        // get whatever we need from the database, which is nothing
        final Map<Integer, TaskForUserCompleted> userTaskIdToOfu = createSpec.getTaskIdToTfuc();
        
        for ( TaskForUserCompleted ofu : userTaskIdToOfu.values()) {
            ofu.setUserId(userId);
        }
        
        taskForUserCompletedRepository.saveAll(userTaskIdToOfu.values());
    }
    
    // motivation for two separate Builders is because service will only be modifying
    // existing objects or creating new ones
    static class CreateUserTasksCompletedSpecBuilderImpl implements CreateUserTasksCompletedSpecBuilder
    {
        // the end state: objects to be saved to db
        final Map<Integer, TaskForUserCompleted> userTaskIdToOfu;
        
        CreateUserTasksCompletedSpecBuilderImpl() {
            this.userTaskIdToOfu = new HashMap<Integer, TaskForUserCompleted>();
        }

        private TaskForUserCompleted getTarget( int taskId ) {
            TaskForUserCompleted tfuc = userTaskIdToOfu.get(taskId);
            if (null == tfuc) {
                tfuc = new TaskForUserCompleted();
                userTaskIdToOfu.put(taskId, tfuc);
            }
            return tfuc;
        }

        @Override
        public CreateUserTasksCompletedSpecBuilder setTimeOfEntry(
            int taskId,
            Date timeOfEntry)
        {
            TaskForUserCompleted tfuc = getTarget(taskId);
            tfuc.setTimeOfEntry(timeOfEntry);
            
            return this;
        }

        @Override
        public CreateUserTasksCompletedSpec build() {

            return new CreateUserTasksCompletedSpec(userTaskIdToOfu);
        }
    }

    /**************************************************************************/
    
    //for the dependency injection
    public TaskForUserCompletedRepository getTaskForUserCompletedRepository()
    {
        return taskForUserCompletedRepository;
    }

    public void setTaskForUserCompletedRepository( TaskForUserCompletedRepositoryImpl taskForUserCompletedRepository )
    {
        this.taskForUserCompletedRepository = taskForUserCompletedRepository;
    }

	public TaskForUserOngoingRepository getTaskForUserOngoingRepository()
	{
		return taskForUserOngoingRepository;
	}

	public void setTaskForUserOngoingRepository(
		TaskForUserOngoingRepository taskForUserOngoingRepository )
	{
		this.taskForUserOngoingRepository = taskForUserOngoingRepository;
	}

	public TaskStageForUserRepository getTaskStageForUserRepository()
	{
		return taskStageForUserRepository;
	}

	public void setTaskStageForUserRepository( TaskStageForUserRepository taskStageForUserRepository )
	{
		this.taskStageForUserRepository = taskStageForUserRepository;
	}
	
}
