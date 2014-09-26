package com.lvl6.mobsters.control.messages

import java.util.Date

@Data
class WebsocketConnected
{
	/**
	 * The local time at web socket server when player connected
	 */
	Date localTime;
	
	/**
	 * The server ID of the websocket server claiming authority for a connected user
	 */
	String socketServerId;
	
	/**
	 * The identifier for the connected player
	 */
	String userUuid;
	
	/**
	 * Orthogonal, but important.  This belongs in a header since a version mismatch
	 * might prevent it from being read here!
	 * 
	ProtocolVersion clientVersion;
	 */
}