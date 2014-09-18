package com.lvl6.mobsters.application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lvl6.mobsters.common.utils.ICallableAction;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.services.task.TaskService;
import com.lvl6.mobsters.services.task.TaskService.AddStageGenerateUserTaskListener;
import com.lvl6.mobsters.services.task.TaskService.GenerateUserTaskListener;
import com.lvl6.mobsters.utility.common.TimeUtils;
import com.lvl6.mobsters.utility.lambda.Director;

public class MobRunTest002 
{
    private static final Logger LOG = 
    	LoggerFactory.getLogger(MobRunTest002.class);
    
    public static void main(String[] args) throws Exception
    {
    	MobRunTest002 inst = new MobRunTest002();
    	inst.run(args);
    }

    public void run(String... args) throws Exception {
    	int numRepeats = Integer.parseInt(args[0]);
    	int numTimes = Integer.parseInt(args[1]);
    	boolean doCreate = args[3] != null && "create".equals(args[3]);
    	ArrayList<Integer> questIdList = new ArrayList<Integer>(5);
    	questIdList.add(1);
    	questIdList.add(2);

    	@SuppressWarnings("resource")
    	final ApplicationContext applicationContext = 
        	new ClassPathXmlApplicationContext(
        		"spring-configuration.xml", "spring-commons.xml", "spring-db.xml", "spring-redis.xml",
        		"spring-domain-two.xml", "spring-domain.xml", "spring-dynamo.xml", "spring-services.xml");
    	TaskService taskService =
    		(TaskService) applicationContext.getBean("taskServiceImpl");
        UserRepository userRepo =
       		(UserRepository) applicationContext.getBean("userRepository");
	
    	BufferedReader bufRdr = null;
    	try {
	    	bufRdr =
	    		new BufferedReader(
	    			new InputStreamReader(
	    				new FileInputStream(args[2])));
	    	String nextLine = bufRdr.readLine();
	
	    	String[][] userUuids = new String[numRepeats][];
	    	for( int jj=0; jj<numRepeats; jj++ ) {
	    		userUuids[jj] = new String[numTimes];
	        	for( int ii=0; ii<numTimes; ii++ ) {
	        		userUuids[jj][ii] = nextLine;
	        		
	        		if (doCreate) {
	        			this.makeUser(nextLine, userRepo);
	        		}
	        		
	        		nextLine = bufRdr.readLine();
	        		if (nextLine == null || nextLine.isEmpty()) {
	        			throw new IllegalArgumentException(
	        				String.format(
	        					"Could only read until just before line <%d> out of <%d> on repeat <%d> out of <%d> from <%s>.",
	        					ii, numTimes, jj, numRepeats, args[2]));
	        		}
	        	}
	    	}
	    	
	    	for( int jj=0; jj<numRepeats; jj++ ) {
	            // Start the clock
	            long start = System.currentTimeMillis();
	            for( int ii=0; ii<numTimes; ii++ ) {
	                ICallableAction<GenerateUserTaskListener> action = 
	                	taskService.generateUserTaskStages(
	                		userUuids[jj][ii], 
	                		TimeUtils.createNow(), 
	                		42, 
	                		false, 
	                		-1, 
	                		0, 
	                		questIdList,
	                		null, 
	                		false, 
	                		true);
	                action.verifySyntax();
	                action.execute(
	                	new GenerateUserTaskListener() {
	        				
	        				@Override
	        				public GenerateUserTaskListener endUserTask(
	        					String userUuid, int taskId, String userTaskUuid ) 
	        				{
	        					// TODO Auto-generated method stub
	        					return this;
	        				}
	        				
	        				@Override
	        				public GenerateUserTaskListener beginUserTask(String userUuid, int taskId) {
	        					// TODO Auto-generated method stub
	        					return this;
	        				}
	        				
	        				@Override
	        				public GenerateUserTaskListener addUserTaskStage(int stageNum,
	        						Director<AddStageGenerateUserTaskListener> optionsDirector) {
	        					// TODO Auto-generated method stub
	        					return this;
	        				}
	        			}
	                );
	            }
	            long totalTime = (System.currentTimeMillis() - start);
	            long avgTime = totalTime / numTimes;
	            LOG.info(
	            	"Elapsed time: {} milliseconds for {} tasks.  Average response time: {} milliseconds.",
	            	new Object[] {totalTime, numTimes, avgTime});
	    	}
    	} finally {
    		if (bufRdr != null) {
    			bufRdr.close();
    		}
		}
    }
    // Print results, including elapsed time

    
    private String makeUser(
    	String username, UserRepository userRepo)
    throws Exception 
    {
    	User userObj = new User(username, false, 2, 50, 1539, 492, 3851, null, null);
    	userObj.setId(username);
    	userRepo.save(userObj);
    	return userObj.getId();
    }
}