package com.lvl6.mobsters.dynamo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.lvl6.mobsters.dynamo.repository.AchievementForUserRepository;
import com.lvl6.mobsters.dynamo.setup.SetupDynamoDB;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-configuration.xml", "classpath:spring-dynamo.xml"})
public class TestTableCreation {

    @Autowired
    public SetupDynamoDB setup;

    @Autowired
    public AmazonDynamoDBClient dynamoClient;

    @Autowired
    public AchievementForUserRepository achRepo;

    @Test
    public void test() {
        // AchievementForUser ach = new AchievementForUser(1, 1, 1, false, false);
        // achRepo.save(ach);
        // AchievementForUser achRetrieved = achRepo.load(ach.getId());
        // assertTrue("Equal", ach.equals(achRetrieved));
    }

    public SetupDynamoDB getSetup() {
        return setup;
    }

    public void setSetup( SetupDynamoDB setup ) {
        this.setup = setup;
    }

    public AmazonDynamoDBClient getDynamoClient() {
        return dynamoClient;
    }

    public void setDynamoClient( AmazonDynamoDBClient dynamoClient ) {
        this.dynamoClient = dynamoClient;
    }

    public AchievementForUserRepository getAchRepo() {
        return achRepo;
    }

    public void setAchRepo( AchievementForUserRepository achRepo ) {
        this.achRepo = achRepo;
    }

}
