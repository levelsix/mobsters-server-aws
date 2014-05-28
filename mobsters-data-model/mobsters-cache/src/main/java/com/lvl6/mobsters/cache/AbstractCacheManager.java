package com.lvl6.mobsters.cache;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.twitter.chill.KryoPool;



abstract class AbstractCacheManager<T> {
	
	@Autowired
	protected JedisPool jedisPool;
	
	@Resource(name="kryoPool")
	protected KryoPool kryoPool;
	
	protected Class<T> type;
	
	
	
	public AbstractCacheManager(Class<T> type) {
		super();
		this.type = type;
	}

	public T getEntity(final String fromCollection, final String withKey) {
		return new JedisGetTask<T>(jedisPool) {
			@Override
			public T task(Jedis jedis) {
				byte[] bytes = jedis.hget(fromCollection.getBytes(), withKey.getBytes());
				if(bytes != null) {
					T retVal = kryoPool.fromBytes(bytes, type);
					return retVal;
				}else {
					return null;
				}
			}
		}.get();
	}
	
	public void saveEntity(final T entity, final String toCollection, final String withKey) {
		new JedisPutTask(jedisPool) {
			@Override
			public void task(Jedis jedis) {
				jedis.hset(toCollection.getBytes(), withKey.getBytes(), kryoPool.toBytesWithClass(entity));
			}
		}.put();
	}
	

	
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
