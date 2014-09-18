package com.lvl6.mobsters.websockets;

import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.UserSessionRegistry;

// import com.google.common.base.Preconditions;

public class MobstersUserDestinationResolver 
	extends DefaultUserDestinationResolver 
{
	public MobstersUserDestinationResolver(UserSessionRegistry userSessionRegistry) 
	{
		super(userSessionRegistry);
	}

	/**
	 * Return the target destination to use. Provided as input are the original source
	 * destination, as well as the same destination with the target prefix removed.
	 *
	 * @param sourceDestination the source destination from the input message
	 * @param sourceDestinationWithoutPrefix the source destination with the target prefix removed
	 * @param sessionId an active user session id
	 * @param user the user
	 * @return the target destination
	 */
	protected String getTargetDestination(
		String sourceDestination, 
		String sourceDestinationWithoutPrefix, 
		String sessionId, 
		String user ) 
	{
		// NOTE: The lack of session presence is something that makes it easy to route messages between
		//       users connected to different websocket servers, but it also complicates that task of
		//       severing the message flow to an "old" session.  Consider stashing information in Redis
		//       that can be combined with the UserID to yield a server-specific queue during handshake,
		//       and retrieve that information here.
		// 
		//       Ideally, make any Game State Server depend upon that same cache of information as a 
		//       precondition for acting on any ActionMessage it receives processing and committing it.
		return sourceDestinationWithoutPrefix + '_' + user;
	}
}
