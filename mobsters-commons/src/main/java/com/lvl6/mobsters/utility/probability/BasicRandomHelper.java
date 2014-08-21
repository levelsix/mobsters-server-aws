package com.lvl6.mobsters.utility.probability;

import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("basicRandomHelper")
@Qualifier("basic")
public class BasicRandomHelper implements IRandomHelper {
	@SuppressWarnings("unused")
	private static final Logger LOG = 
		LoggerFactory.getLogger(BasicRandomHelper.class);
	
    private final Random stdRandomSrc;

    public BasicRandomHelper() {
    	SecureRandom cryptoRandomSrc = CryptoRandomHelper.getCryptoPRNG();
    	
    	// Seed well if possible.
    	if (cryptoRandomSrc != null) {
    		long seed = 0;
    		for( byte nextByte : cryptoRandomSrc.generateSeed(8)) {
    			seed = (seed << 8) + nextByte;
    		}
    		stdRandomSrc = new Random(seed);    		
    	} else {
    		stdRandomSrc = new Random();
    	}
    }

	@Override
	public void nextBytes(byte[] bytes) {
		stdRandomSrc.nextBytes(bytes);
	}

	@Override
	public int nextInt() {
		return stdRandomSrc.nextInt();
	}

	@Override
	public int nextInt(int n) {
		return stdRandomSrc.nextInt(n);
	}

	@Override
	public long nextLong() {
		return stdRandomSrc.nextLong();
	}

	@Override
	public boolean nextBoolean() {
		return stdRandomSrc.nextBoolean();
	}

	@Override
	public float nextFloat() {
		return stdRandomSrc.nextFloat();
	}

	@Override
	public double nextDouble() {
		return stdRandomSrc.nextDouble();
	}

	@Override
	public double nextGaussian() {
		return stdRandomSrc.nextGaussian();
	}
}
