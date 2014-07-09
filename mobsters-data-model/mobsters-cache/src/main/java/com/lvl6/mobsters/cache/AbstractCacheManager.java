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
	
	final protected Class<T> type;
	
	// Ascii encoding for "ex", used to set a Jedis set() parameter indicating that TTL 
	// argument measures time in seconds.  If the first byte were 0x70, TTL times would be
	// measured in milliseconds.
    byte[] bex = { 0x65, 0x78 };
    
    // Placeholder for an unused Jedis set() argument.  If set to {0x6e, 0x78}, Jedis
    // will only update the cache if the given key doesn't already exist.  If set to
    // {0x78, 0x78}, Jedis will only update the cache if the given key already exists.
    byte[] binaryValue;
	
	public AbstractCacheManager(final Class<T> type) {
		super();
		this.type = type;
	}

	protected T getEntityFromCollection(final String fromCollection, final String withKey) {
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
	
	protected void saveEntityToCollection(final T entity, final String toCollection, final String withKey) {
		new JedisPutTask(jedisPool) {
			@Override
			public void task(Jedis jedis) {
				jedis.hset(toCollection.getBytes(), withKey.getBytes(), kryoPool.toBytesWithClass(entity) );				
			}
		}.put();
	}
	
	
	protected T getEntity(final String withKey) {
		return new JedisGetTask<T>(jedisPool) {
			@Override
			public T task(Jedis jedis) {
				byte[] bytes = jedis.get(withKey.getBytes());
				if(bytes != null) {
					T retVal = kryoPool.fromBytes(bytes, type);
					return retVal;
				}else {
					return null;
				}
			}
		}.get();
	}
	
	protected void saveEntity(final T entity, final String withKey, final Long ttlSeconds) {
		new JedisPutTask(jedisPool) {
			@Override
			public void task(Jedis jedis) {
				jedis.set(withKey.getBytes(), kryoPool.toBytesWithClass(entity),binaryValue, bex, ttlSeconds);
				
			}
		}.put();
	}

	protected void saveEntity(final T entity, final String withKey) {
		new JedisPutTask(jedisPool) {
			@Override
			public void task(Jedis jedis) {
				jedis.set(withKey.getBytes(), kryoPool.toBytesWithClass(entity));
				
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
