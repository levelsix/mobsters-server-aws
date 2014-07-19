package com.lvl6.mobsters.services.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lvl6.mobsters.dynamo.EventPersistentForUser;
import com.lvl6.mobsters.dynamo.TaskForUserCompleted;
import com.lvl6.mobsters.dynamo.TaskForUserOngoing;
import com.lvl6.mobsters.dynamo.TaskStageForUser;
import com.lvl6.mobsters.services.task.TaskServiceImpl.CreateUserTasksCompletedSpecBuilderImpl;
public interface TaskService {
    
	
	
	//NON CRUD LOGIC
	
	/**************************************************************************/
	//CRUD LOGIC
    
    // BEGIN READ ONLY LOGIC
    
	public TaskForUserOngoing getUserTaskForUserId( String userId );

	public List<TaskForUserCompleted> getTaskCompletedForUser( String userId ); 
	
	public List<TaskStageForUser> getTaskStagesForUserWithTaskForUserId( String userTaskId );
	
	public List<EventPersistentForUser> getUserPersistentEventForUserId( String userId );
	
	// END READ ONLY LOGIC
	
	/**************************************************************************/
    
    public void createTasksForUserCompleted( String userId, CreateUserTasksCompletedSpec createSpec );
    

	public interface CreateUserTasksCompletedSpecBuilder {
        public CreateUserTasksCompletedSpec build();
        
        public CreateUserTasksCompletedSpecBuilder setTimeOfEntry( int taskId, Date timeOfEntry );
        
    }
    
    public class CreateUserTasksCompletedSpec {
        // the end state: objects to be saved to db
        final private Map<Integer, TaskForUserCompleted> userTaskIdToTfuc;

        CreateUserTasksCompletedSpec( Map<Integer, TaskForUserCompleted> userTaskIdToTfuc) {
            this.userTaskIdToTfuc = userTaskIdToTfuc;
        }
        
        public Map<Integer, TaskForUserCompleted> getTaskIdToTfuc() {
            return userTaskIdToTfuc;
        }

        public static CreateUserTasksCompletedSpecBuilderImpl builder() {
            return new CreateUserTasksCompletedSpecBuilderImpl();
        }
    }

    /**************************************************************************/
    
}
