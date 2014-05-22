package com.lvl6.mobsters.cache;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.lvl6.mobsters.info.ConnectedPlayer;
import com.twitter.chill.KryoPool;


@Component
public class PlayerMapsCacheManager {
	
	@Autowired
	protected JedisPool jedisPool;
	
	@Resource(name="kryoPool")
	protected KryoPool kryoPool;

	protected static String connectedPlayersPreDB = "connectedPlayersPreDB";
	protected static String connectedPlayers = "connectedPlayers";
	
	
	public ConnectedPlayer getPlayerPreDatabaseByUDID(final String udid) {
		return new JedisGetTask<ConnectedPlayer>(jedisPool) {
			@Override
			public ConnectedPlayer task(Jedis jedis) {
				byte[] bytes = jedis.hget(connectedPlayersPreDB.getBytes(), udid.getBytes());
				if(bytes != null) {
					ConnectedPlayer player = kryoPool.fromBytes(bytes, ConnectedPlayer.class);
					return player;
				}else {
					return null;
				}
			}
		}.get();
	}
	
	public ConnectedPlayer getPlayer(final String id) {
		return new JedisGetTask<ConnectedPlayer>(jedisPool) {
			@Override
			public ConnectedPlayer task(Jedis jedis) {
				byte[] bytes = jedis.hget(connectedPlayers.getBytes(), id.getBytes());
				if(bytes != null) {
					ConnectedPlayer player = kryoPool.fromBytes(bytes, ConnectedPlayer.class);
					return player;
				}else {
					return null;
				}
			}
		}.get();
	}
	
	public void savePlayerPreDatabase(final ConnectedPlayer player) {
		new JedisPutTask(jedisPool) {
			@Override
			public void task(Jedis jedis) {
				jedis.hset(connectedPlayersPreDB.getBytes(), player.getUdid().getBytes(), kryoPool.toBytesWithClass(player));
			}
		}.put();
	}
	
	public void savePlayer(final ConnectedPlayer player) {
		new JedisPutTask(jedisPool) {
			@Override
			public void task(Jedis jedis) {
				jedis.hset(connectedPlayers.getBytes(), player.getUdid().getBytes(), kryoPool.toBytesWithClass(player));
			}
		}.put();
	}
	
	
	//TODO: need cleanup job for disconnected players
	
	
	
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}



	protected KryoPool getKryoPool() {
		return kryoPool;
	}



	public void setKryoPool(KryoPool kryoPool) {
		this.kryoPool = kryoPool;
	}



	protected JedisPool getJedisPool() {
		return jedisPool;
	}
}
