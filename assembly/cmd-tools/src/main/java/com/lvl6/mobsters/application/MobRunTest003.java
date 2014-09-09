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
// @EnableAsync
@EnableAutoConfiguration
@ImportResource(
	value={"spring-configuration.xml", "spring-db.xml", "spring-commons.xml", "spring-redis.xml", "spring-dynamo.xml", "spring-domain.xml", "spring-services.xml", "spring-stomp.xml"}
)
@ComponentScan
public class MobRunTest003 implements CommandLineRunner {

    @Autowired
    IUserResourceFactory gameServer;
    
    @Autowired
    TaskService taskService;

    @Autowired
    UserRepository userRepo;
    
    @Override
    public void run(String... args) throws Exception {
    }

    public static void main(String[] args) {
        SpringApplication.run(MobRunTest003.class, args);
    }
}