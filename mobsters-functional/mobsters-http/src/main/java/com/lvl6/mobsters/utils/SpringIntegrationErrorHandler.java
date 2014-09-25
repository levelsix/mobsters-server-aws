package com.lvl6.mobsters.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

public class SpringIntegrationErrorHandler {
	static final Logger LOG = 
		LoggerFactory.getLogger(SpringIntegrationErrorHandler.class);
	
	public void handleError(Message<?> errorMessage) {
		MessagingException error = 
			((MessagingException) errorMessage.getPayload());
		LOG.error("Error processing message", error);
	}
}
