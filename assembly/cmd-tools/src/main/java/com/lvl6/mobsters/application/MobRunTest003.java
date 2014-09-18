package com.lvl6.mobsters.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.lvl6.mobsters.domain.game.api.IUserResourceFactory;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.services.task.TaskService;

@Configuration
// @EnableAsync
@EnableAutoConfiguration
@ImportResource(
	value={
		"spring-configuration.xml", "spring-commons.xml", "spring-redis.xml", 
		"spring-dynamo.xml", "spring-db.xml", "spring-domain-two.xml", 
		"spring-domain.xml", "spring-services.xml", "spring-stomp.xml"}
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