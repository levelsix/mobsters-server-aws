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
	
	private static int PREDB_KEY_BASELEN = connectedPlayersPreDB.length() + 1;
	private static int POSTDB_KEY_BASELEN = connectedPlayers.length() + 1;
	
	protected static Long connectedPlayerTimeout = 12*60*60l;
	protected static Long connectedPlayerPreDBTimeout = 60*60l;
	
	
	
	public ConnectedPlayer getPlayer(String userId) {
		return getEntity(connectedPlayerKey(userId));
	}
	
	
	public ConnectedPlayer getPlayerPreDB(String udid) {
		return getEntity(connectedPlayerPreDBKey(udid));
	}
	
	
	public void savePlayer(ConnectedPlayer player) {
		saveEntity(player, connectedPlayerKey(player.getPlayerId()), connectedPlayerTimeout);
	}
	
	public void savePlayerPreDB(ConnectedPlayer player) {
		saveEntity(player, connectedPlayerPreDBKey(player.getUdid()), connectedPlayerPreDBTimeout);
	}
	
	private String connectedPlayerKey(String userId) {
		return 
			new StringBuffer(
				userId.length() + POSTDB_KEY_BASELEN)
			.append(userId)
			.append(":")
			.append(connectedPlayers)
			.toString();
	}
	
	private String connectedPlayerPreDBKey(String uuid) {
		return 
			new StringBuffer(
				uuid.length() + PREDB_KEY_BASELEN)
			.append(uuid)
			.append(":")
			.append(connectedPlayersPreDB)
			.toString();
	}
}
