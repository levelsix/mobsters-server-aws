package com.lvl6.mobsters.cache;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.info.ConnectedPlayer;


@Component
public class PlayerMapsCacheManager extends AbstractCacheManager<ConnectedPlayer> {
	
	public PlayerMapsCacheManager() {
		super(ConnectedPlayer.class);
	}

	public static String connectedPlayersPreDB = "connectedPlayersPreDB";
	public static String connectedPlayers = "connectedPlayers";
	
}
