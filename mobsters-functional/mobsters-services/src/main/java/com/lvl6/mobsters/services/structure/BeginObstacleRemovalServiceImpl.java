package com.lvl6.mobsters.services.structure;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.services.common.Lvl6MobstersException;
import com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum;
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode;

@Component
public class BeginObstacleRemovalServiceImpl implements BeginObstacleRemovalService {
    
    private static Logger LOG = LoggerFactory.getLogger(BeginObstacleRemovalServiceImpl.class);
    
    // TODO: Use this or the service?
    @Autowired
    protected UserRepository userRepository;
    
    @Autowired
    protected ObstacleForUserRepository obstacleForUserRepository;
    
    //NON CRUD LOGIC
    
    /**************************************************************************/
    //CRUD LOGIC

    // BEGIN READ ONLY LOGIC

    // END READ ONLY LOGIC
    /**************************************************************************/

	@Override
	public void removeObstacle(
		String userId,
		String userObstacleId,
		Date clientTime,
		int gemsSpent,
		String resourceType,
		int resourceChange )
	{
		// TODO: TRANSACTIONIFY
		User user = userRepository.load(userId);
		ObstacleForUser ofu = obstacleForUserRepository.load(userId, userObstacleId);
			
		checkIfUserCanRemoveObstacle(userId, user, userObstacleId, ofu, gemsSpent, resourceType, resourceChange);
		
		// TODO: Write to currency history
		updateUserCurrency(user, gemsSpent, resourceType, resourceChange);
		ofu.setRemovalTime(clientTime);
		
	}

	private void checkIfUserCanRemoveObstacle(
		String userId,
		User user,
		String ofuId,
		ObstacleForUser ofu,
		int gemsSpent,
		String resourceType,
		int resourceChange )
	{
		if (null == user || null == ofu) {
			LOG.error("unexpected error: user or obstacle for user is null. user=" + user +
					"\t userId=" + userId + "\t obstacleForUser=" + ofu + "\t ofuId=" + ofuId);
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
		}
		
		
		 
	    if (!hasEnoughGems(user, gemsSpent)) {
	    	throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS);
	    }
	    
	    if (Lvl6MobstersResourceEnum.CASH.name().equals(resourceType)) {
	    	if (!hasEnoughCash(user, resourceChange)) {
	    		throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_RESOURCE);
	      }
	    }

	    if (Lvl6MobstersResourceEnum.OIL.name().equals(resourceType)) {
	      if (!hasEnoughOil(user, resourceChange)) {
	    	  throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_RESOURCE);
	      }
	    }
		
	}
	
	private boolean hasEnoughGems( User user, int gemsSpent )
	{
		int userGems = user.getGems();
		if (userGems < gemsSpent)
		{
			LOG.error("user error: user does not have enough gems. userGems=" + userGems +
  				"\t gemsSpent=" + gemsSpent);
			return false;
		} else {
			return true;
		}
	}
	
	private boolean hasEnoughCash( User user, int resourceChange )
	{
		int userCash = user.getCash();
		if (userCash < resourceChange)
		{
			LOG.error("user error: user does not have enough cash. userCash=" + userCash +
  				"\t cashSpent=" + resourceChange);
	  		return false;
		} else {
			return true;
		}
	}

	private boolean hasEnoughOil( User user, int resourceChange )
	{
		int userOil = user.getOil();
		if (userOil < resourceChange) {
	  		LOG.error("user error: user does not have enough oil. userOil=" + userOil +
	  				"\t oilSpent=" + resourceChange);
	  		return false;
	  	} else {
	  		return true;
	  	}
	}

	private void updateUserCurrency(
		User user,
		int gemsSpent,
		String resourceType,
		int resourceChange )
	{
		int gemsChange = -1 * Math.abs(gemsSpent);
	  	int cashChange = 0;
	  	int oilChange = 0;
		
	  	if (Lvl6MobstersResourceEnum.CASH.name().equals(resourceType)) {
	  		cashChange = resourceChange;
	  	} else if (Lvl6MobstersResourceEnum.OIL.name().equals(resourceType)) {
	  		oilChange = resourceChange;
	  	}
	  	
	  	user.setGems( user.getGems() + gemsChange );
	  	user.setCash( user.getCash() + cashChange );
	  	user.setOil( user.getOil() + oilChange );
	}



	public UserRepository getUserRepository()
	{
		return userRepository;
	}

	public void setUserRepository( UserRepository userRepository )
	{
		this.userRepository = userRepository;
	}

	public ObstacleForUserRepository getObstacleForUserRepository()
	{
		return obstacleForUserRepository;
	}

	public void setObstacleForUserRepository( ObstacleForUserRepository obstacleForUserRepository )
	{
		this.obstacleForUserRepository = obstacleForUserRepository;
	}
    
}
