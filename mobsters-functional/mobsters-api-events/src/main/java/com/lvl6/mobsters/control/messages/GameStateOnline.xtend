package com.lvl6.mobsters.control.messages

import java.util.Date
import java.util.UUID

@Data
class GameStateOnline
{
	/**
	 * The time local to the game state server when this was generated
	 */
	Date localTime;
	
	/**
	 * The server ID of the responding game server
	 */
	String gameServerId;

	/**
	 * The server ID of the websocket server whose request has been accepted
	 */	
	String socketServerId;
	
	/**
	 * The user identifier presented for the account to bring online
	 */
	String userUuid;
	
	/**
	 * For convenience and clarity, the routing key created for sending messages to
	 * the responding game server.
	 */
	String routingKey;
	
	/**
	 * For the purpose of generating message IDs.  Messages are identified by a sequence ID
	 * and a counter.  
	 * 
	 * Every time the server needs to allocate a new thread of events that are understood 
	 * to all happen after the most recent thread, it increments and returns a user's latest 
	 * sequence counter.
	 * 
	 * Any source of requests that is given a sequence counter uses the same value in every
	 * request.  The counter value is monotonically incremented with each message and is not
	 * given in this message because it implicitly always begins at 1.
	 * 
	 * The pair of integers can be concatenated to achieve a chronological ordering by 
	 * letting the sequence ID take position of the most significant bits and the counter
	 * the least significant, yielding a long value.
	 */
	long nextSequenceId;
	
	/**
	 * A string identifier that was created as a lookup key when a copy of user's game state
	 * document as of the last event before <code>nextSequenceId</code>, was placed in the
	 * bulk-transfer LRU cache in order to keep the broker message light.  The claim check
	 * can be used for a short while to retrieve the user document for return message 
	 * needs. 
	 * until its LRU cache 
	 */
	UUID userDocClaimCheck
}