package com.lvl6.mobsters.utility.probability;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("concurrent")
public class ConcurrentRandomHelper implements IRandomHelper {
	@SuppressWarnings("unused")
	private static final Logger LOG = 
		LoggerFactory.getLogger(ConcurrentRandomHelper.class);
	
    public ConcurrentRandomHelper() {
    }
    
	@Override
	public void nextBytes(byte[] bytes) {
		ThreadLocalRandom.current().nextBytes(bytes);
	}

	@Override
	public int nextInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	@Override
	public int nextInt(int n) {
		return ThreadLocalRandom.current().nextInt(n);
	}

	@Override
	public long nextLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	@Override
	public boolean nextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	@Override
	public float nextFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}

	@Override
	public double nextDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	@Override
	public double nextGaussian() {
		return ThreadLocalRandom.current().nextGaussian();
	}
}
