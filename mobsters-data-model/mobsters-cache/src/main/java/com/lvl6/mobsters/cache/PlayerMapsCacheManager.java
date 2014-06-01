package com.lvl6.mobsters.cache;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.info.ConnectedPlayer;


@Component
public class PlayerMapsCacheManager extends AbstractCacheManager<ConnectedPlayer> {
	
	public PlayerMapsCacheManager() {
		super(ConnectedPlayer.class);
	}

	protected static String connectedPlayersPreDB = "connectedPlayersPreDB";
	protected static String connectedPlayers = "connectedPlayers";
	
	protected static Long connectedPlayerTimeout = 12*60*60l;
	protected static Long connectedPlayerPreDBTimeout = 60*60l;
	
	
	
	public ConnectedPlayer getPlayer(String userId) {
		return getEntity(userId);
	}
	
	
	public ConnectedPlayer getPlayerPreDB(String uuid) {
		return getEntity(uuid);
	}
	
	
	public void savePlayer(ConnectedPlayer player) {
		saveEntity(player, player.getPlayerId(), connectedPlayerTimeout);
	}
	
	public void savePlayerPreDB(ConnectedPlayer player) {
		saveEntity(player, player.getUdid(), connectedPlayerPreDBTimeout);
	}
	
	
	
	protected String connectedPlayerKey(String userId) {
		return new StringBuffer().append(userId).append(":").append(connectedPlayers).toString();
	}
	
	protected String connectedPlayerPreDBKey(String uuid) {
		return new StringBuffer().append(uuid).append(":").append(connectedPlayersPreDB).toString();
	}
	
	
}
