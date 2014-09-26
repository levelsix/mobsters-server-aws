package com.lvl6.mobsters.control.messages

import java.util.Date
import java.util.UUID

@Data
class GameStateReady
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
	 * For the purpose of generating 
	 */
	long nextSequenceId;
	// UUID nextRequestUuid;
	String userDocClaimCheck
}