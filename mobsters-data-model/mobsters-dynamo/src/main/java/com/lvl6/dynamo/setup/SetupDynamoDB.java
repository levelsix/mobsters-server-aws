package com.lvl6.dynamo.setup;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.lvl6.dynamo.repository.BaseDynamoRepository;



public class SetupDynamoDB implements ApplicationContextAware{
	
	
	protected ApplicationContext context;
	
	protected boolean checkTables = false;
	
	
	@PostConstruct
	public void checkDynamoTables() {
		Map<String, BaseDynamoRepository> dynamoRepos = context.getBeansOfType(BaseDynamoRepository.class);
		for(BaseDynamoRepository<?> repo : dynamoRepos.values()) {
			repo.checkTable();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;
	}
	
}
