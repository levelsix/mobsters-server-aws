package com.lvl6.mobsters.application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

import com.lvl6.mobsters.common.utils.ICallableAction;
import com.lvl6.mobsters.domain.game.api.IUserResourceFactory;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.services.task.TaskService;
import com.lvl6.mobsters.services.task.TaskService.AddStageGenerateUserTaskListener;
import com.lvl6.mobsters.services.task.TaskService.GenerateUserTaskListener;
import com.lvl6.mobsters.utility.common.TimeUtils;
import com.lvl6.mobsters.utility.lambda.Director;

@Configuration
@EnableAsync
@EnableAutoConfiguration
@ImportResource(
	value={"spring-configuration.xml", "spring-db.xml", "spring-commons.xml", "spring-redis.xml", "spring-dynamo.xml", "spring-domain.xml", "spring-services.xml"}
)
@ComponentScan
public class MobRunTest002 implements CommandLineRunner {

    @Autowired
    IUserResourceFactory gameServer;
    
    @Autowired
    TaskService taskService;

    @Autowired
    UserRepository userRepo;
    
    @Override
    public void run(String... args) throws Exception {
    	int numRepeats = Integer.parseInt(args[0]);
    	int numTimes = Integer.parseInt(args[1]);
    	
    	ArrayList<Integer> questIdList = new ArrayList<Integer>(5);
    	questIdList.add(1);
    	questIdList.add(2);
    	
    	BufferedReader bufRdr = 
    		new BufferedReader(
    			new InputStreamReader(
    				new FileInputStream( args[2] )));
    	String nextLine = bufRdr.readLine();

    	String[][] userUuids = new String[numRepeats][];
    	for( int jj=0; jj<numRepeats; jj++ ) {
    		userUuids[jj] = new String[numTimes];
        	for( int ii=0; ii<numTimes; ii++ ) {
        		userUuids[jj][ii] = nextLine;
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
                		userUuids[jj][ii], TimeUtils.createNow(), 42, false, -1, 0, questIdList, null, false, true);
                action.verifySyntax();
                action.execute(
                	new GenerateUserTaskListener() {
        				
        				@Override
        				public GenerateUserTaskListener endUserTask(String userUuid, int taskId,
        						String userTaskUuid) {
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
            System.out.println(
            	String.format(
            		"Elapsed time: %d milliseconds for %d tasks begun.  Average of %d milliseconds each.",
            		totalTime, numTimes, avgTime));
        }
        // Print results, including elapsed time
    }

    public static void main(String[] args) {
        SpringApplication.run(MobRunTest002.class, args);
    }

    private String makeUser(String username) throws Exception {
    	User userObj = new User("jheinnic", false, 2, 50, 1539, 492, 3851, null, null);
    	userRepo.save(userObj);
    	return userObj.getId();
    }
}