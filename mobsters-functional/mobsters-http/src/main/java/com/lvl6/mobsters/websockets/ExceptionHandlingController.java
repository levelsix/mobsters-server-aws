package com.lvl6.mobsters.websockets;

import java.util.Arrays;

import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.lvl6.mobsters.utility.exception.Lvl6MobstersException;


@Controller("exceptionHandlingController")
public class ExceptionHandlingController {
	private static Logger LOG =
		LoggerFactory.getLogger(ExceptionHandlingController.class);
	
	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	void handleException(final Throwable e) {
		LOG.error(
			"Unexpected exception thrown while handling request: %s\n%s",
			e.getMessage(),
			IterableExtensions.join(
				Arrays.asList(e.getStackTrace()), "\n")
		);
		
		if (e instanceof Lvl6MobstersException) {
			// TODO: Send expected ResponseProto as an event with status code taken from Exception.
		} else {
			// TODO: Send expected ResponseProto as an event with status code set to FAIL_OTHER.
		}
	}
}
