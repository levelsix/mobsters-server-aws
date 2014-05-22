package com.lvl6.mobsters.cache;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

abstract public class JedisPutTask {
	
	
	private static final Logger log = LoggerFactory.getLogger(JedisPutTask.class);
	
	protected JedisPool jedisPool;
	
	public JedisPutTask(JedisPool jedisPool) {
		super();
		this.jedisPool = jedisPool;
	}

	abstract public void task(Jedis jedis);
	
	public void put() {
		Jedis jedis = jedisPool.getResource();
		try {
			task(jedis);
		}catch(Throwable e) {
			log.error("Error in jedis task", e);
			if (null != jedis) {
		        jedisPool.returnBrokenResource(jedis);
		        jedis = null;
		    }
			throw new RuntimeException(e);
		}finally {
			if (null != jedis) {
			    jedisPool.returnResource(jedis);
			}
		}
	}
	
}
