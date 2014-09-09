package com.lvl6.mobsters.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lvl6.mobsters.domain.game.api.IUserResourceFactory;

public class MobRunTest001 {
    final static Logger logger = LoggerFactory.getLogger(MobRunTest001.class);
    
    /**
     * Main method.
     */
    public static void main(String[] args) {
        logger.info("Initializing Spring context.");
        
        ApplicationContext applicationContext = 
        	new ClassPathXmlApplicationContext(
        		"spring-commons.xml", "spring-db.xml", "spring-redis.xml", "spring-dynamo.xml", 
        		"spring-dynamo.xml", "spring-services.xml");
        
        logger.info("Spring context initialized.");

        IUserResourceFactory gameSrvr = (IUserResourceFactory) applicationContext.getBean(IUserResourceFactory.class);

        logger.debug("game server component=%s", gameSrvr);
    }
}
