package com.lvl6.mobsters.utility.probability;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import sun.security.jca.Providers;

@Component("cryptoRandomHelper")
@Qualifier("crypto")
@SuppressWarnings("restriction")
public class CryptoRandomHelper implements IRandomHelper
{
	private static final Logger LOG = LoggerFactory.getLogger(CryptoRandomHelper.class);
	
    // private final Random stdRandomSrc;
    private final SecureRandom cryptoRandomSrc;

    public CryptoRandomHelper( ) 
    {
    	cryptoRandomSrc = getCryptoPRNG();
    }

	@Override
	public void nextBytes(byte[] bytes) 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		cryptoRandomSrc.nextBytes(bytes);
	}

	@Override
	public boolean nextBoolean() 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.nextBoolean();
	}

	@Override
	public int nextInt() 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.nextInt();
	}

	@Override
	public int nextInt(int n) 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.nextInt(n);
	}

	@Override
	public long nextLong() 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.nextLong();
	}

	@Override
	public float nextFloat() 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.nextFloat();
	}

	@Override
	public double nextDouble() 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.nextDouble();
	}

	@Override
	public double nextGaussian() 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.nextGaussian();
	}

	public byte[] generateSeed(int numBytes) 
	{
		if (this.cryptoRandomSrc == null) {
			throw new UnsupportedOperationException("No suitable cryptographic strength PRNG found at bootstrap.");
		}
		
		return cryptoRandomSrc.generateSeed(numBytes);
	}
   
    /**
     * Gets a default PRNG algorithm by looking through all registered
     * providers. Returns the first PRNG algorithm of the first provider that
     * has registered a SecureRandom implementation, or null if none of the
     * registered providers supplies a SecureRandom implementation.
     */
    private static String getPrngAlgorithm() 
    {
        for (Provider p : Providers.getProviderList().providers()) {
            for (Service s : p.getServices()) {
                if (s.getType().equals("SecureRandom")) {
                    return s.getAlgorithm();
                }
            }
        }
        return null;
    }
    
    static SecureRandom getCryptoPRNG() 
    {
        SecureRandom retVal = null;
        
        String prng = getPrngAlgorithm();
        if (prng == null) {
            // bummer, get the SUN implementation
            try {
				retVal = SecureRandom.getInstance("SHA1PRNG");
    		} catch (NoSuchAlgorithmException e) {
    			LOG.warn(
    				String.format(
    					"Cryptographic strength PRNG not available: %s", 
    					e.getStackTrace().toString()
    				), e
    			);
    		}
        } else {
            try {
                retVal = SecureRandom.getInstance(prng);
            } catch (NoSuchAlgorithmException nsae) {
                // never happens, because we made sure the algorithm exists
                throw new RuntimeException(nsae);
            }
        }
        
        return retVal;
    }
}
