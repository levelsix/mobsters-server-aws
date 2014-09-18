package com.lvl6.mobsters.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lvl6.mobsters.domain.game.api.IUserResourceFactory;

public class MobRunTest004 {
    final static Logger LOG = LoggerFactory.getLogger(MobRunTest004.class);
    
    /**
     * Main method.
     */
    public static void main(String[] args) {
        LOG.info("Initializing Spring context.");
        
        ApplicationContext applicationContext = 
        	new ClassPathXmlApplicationContext(
        		"spring-configuration.xml", "spring-commons.xml", "spring-redis.xml", 
        		"spring-dynamo.xml", "spring-db.xml", "spring-domain-two.xml",  
        		"spring-domain.xml", "spring-services.xml");
        
        LOG.info("Spring context initialized.");

        IUserResourceFactory gameSrvr = 
        	(IUserResourceFactory) applicationContext.getBean(
        		IUserResourceFactory.class);

        LOG.warn("game server component={}", gameSrvr);
    }
}
