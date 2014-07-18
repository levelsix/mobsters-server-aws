package com.lvl6.mobsters.services.structure.composite;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.info.IStructure;
import com.lvl6.mobsters.info.Structure;
import com.lvl6.mobsters.info.repository.StructureRepository;
import com.lvl6.mobsters.services.common.Lvl6MobstersException;
import com.lvl6.mobsters.services.common.Lvl6MobstersResourceEnum;
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode;
import com.lvl6.mobsters.services.common.TimeUtils;
import com.lvl6.mobsters.services.structure.StructureService;
import com.lvl6.mobsters.services.user.UserService;
import com.lvl6.mobsters.services.user.UserService.ModifyUserSpec;
import com.lvl6.mobsters.services.user.UserService.ModifyUserSpecBuilder;

@Component
public class UpgradeNormStructureServiceImpl implements
		UpgradeNormStructureService {

	private static Logger LOG = LoggerFactory
			.getLogger(UpgradeNormStructureServiceImpl.class);

	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected UserService userService;

	@Autowired
	protected StructureForUserRepository structureForUserRepository;
	
	@Autowired
	protected StructureService structureService;
	
	@Autowired
	protected StructureRepository structureRepository;

	// NON CRUD LOGIC
	
	private boolean hasEnoughCash( User user, int resourceChange ) {
		int userCash = user.getCash();
		if (userCash < resourceChange) {
			LOG.error("user error: user does not have enough cash. userCash="
					+ userCash + "\t cashSpent=" + resourceChange);
			return false;
		} else {
			return true;
		}
	}

	private boolean hasEnoughOil( User user, int resourceChange ) {
		int userOil = user.getOil();
		if (userOil < resourceChange) {
			LOG.error("user error: user does not have enough oil. userOil="
					+ userOil + "\t oilSpent=" + resourceChange);
			return false;
		} else {
			return true;
		}
	}
	
	private boolean hasEnoughGems( User user, int gemsSpent )
	{
		int userGems = user.getGems();
		if (gemsSpent > 0 && userGems < gemsSpent) {
			LOG.error("user has " + userGems + " gems; trying to spend " + gemsSpent);
			return false;
		} else {
			return true;
		}
	}

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// CRUD LOGIC

	@Override
	public User upgradeNormStructure(String userId, String userStructId,
			int gemsSpent, String resourceType, int resourceChange,
			Date timeOfUpgrade)
	{
		// TODO: TRANSACTIONIFY
		User user = userRepository.load(userId);
		StructureForUser sfu = structureForUserRepository.findByUserIdAndStructureForUserId(userId, userStructId);

		resourceChange = Math.abs(resourceChange);
		checkIfUserCanUpgradeStructure(userId, user, userStructId, sfu,
				gemsSpent, resourceType, resourceChange, timeOfUpgrade);

		// TODO: Write to currency history
		updateUserCurrency(gemsSpent, resourceType, resourceChange, user);
		structureService.beginUpgradingUserStruct(sfu, timeOfUpgrade);
		
		return user;
	}
	
	private void checkIfUserCanUpgradeStructure(String userId, User user,
			String userStructId, StructureForUser sfu, int gemsSpent,
			String resourceType, int resourceChange, Date timeOfUpgrade)
	{
		if ( null == user || null == sfu || sfu.getLastRetrieved() == null ) {
			LOG.error("parameter passed in is null. user=" + user + ", user struct=" + sfu + 
			         ", userStruct's last retrieve time=" + sfu.getLastRetrieved());
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
		}

		if (!sfu.isComplete()) {
			LOG.error("user struct is not complete yet");
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_CONSTRUCTION_INCOMPLETE);
		}

		Structure currentStruct = structureRepository.findOne(sfu.getStructId());
		IStructure nextLevelStruct = currentStruct.getSuccessorStruct(); 
		if (null == nextLevelStruct ) {
			//can't upgrade since there is no further structure to upgrade to
			LOG.error("user struct at max level already. struct is " + currentStruct);
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_STRUCTURE_AT_MAX_LEVEL);
		}
		
		if (TimeUtils.isFirstEarlierThanSecond(timeOfUpgrade, sfu.getLastRetrieved())) {
			LOG.error("the upgrade time " + timeOfUpgrade + " is before the last time the building was retrieved:"
			          + sfu.getLastRetrieved());
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_CONSTRUCTION_INCOMPLETE);
		}
			
		if (gemsSpent < 0) {
			LOG.warn("gemsSpent is negative! gemsSpent=" + gemsSpent);
	    	gemsSpent = Math.abs(gemsSpent);
		}

		if (gemsSpent > 0 && !hasEnoughGems(user, gemsSpent)) {
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS);
		}
		
		if (Lvl6MobstersResourceEnum.CASH.name().equals(resourceType)) {
			if (!hasEnoughCash(user, resourceChange)) {
				throw new Lvl6MobstersException(
						Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_RESOURCE);
			}
		}

		if (Lvl6MobstersResourceEnum.OIL.name().equals(resourceType)) {
			if (!hasEnoughOil(user, resourceChange)) {
				throw new Lvl6MobstersException(
						Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_RESOURCE);
			}
		}

	}

	private void updateUserCurrency(int gemsSpent, String resourceType,
			int resourceChange, User user) {
		ModifyUserSpecBuilder modifyUserSpecBuilder = ModifyUserSpec.builder();
		
		if (gemsSpent > 0) {
			modifyUserSpecBuilder.decrementGems(gemsSpent);
		}
		if (Lvl6MobstersResourceEnum.CASH.name().equals(resourceType)) {
			modifyUserSpecBuilder.decrementCash(resourceChange);
		} else if (Lvl6MobstersResourceEnum.OIL.name().equals(resourceType)) {
			modifyUserSpecBuilder.decrementOil(resourceChange);
		}
		userService.modifyUser(user, modifyUserSpecBuilder.build());
	}

	@Override
	public User speedUpConstructingUserStruct( String userId, String userStructId,
			int gemCost, Date now )
	{
		// TODO: TRANSACTIONIFY
		User user = userRepository.load(userId);
		StructureForUser sfu = structureForUserRepository.findByUserIdAndStructureForUserId(userId, userStructId);

		checkIfUserCanSpeedUpUpgrade(gemCost, user, sfu);
		
		ModifyUserSpecBuilder modifyUserSpecBuilder = ModifyUserSpec.builder();
		modifyUserSpecBuilder.decrementGems(gemCost);
		userService.modifyUser(user, modifyUserSpecBuilder.build());
		
		
		return user;
	}

	private void checkIfUserCanSpeedUpUpgrade(int gemCost, User user,
			StructureForUser sfu) {
		if ( null == user || null == sfu )
		{
			LOG.error("parameter passed in is null. user=" + user + ", user struct=" + sfu);
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
		}
		
		if (!hasEnoughGems(user, gemCost)) {
			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS);
		}
		
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public StructureForUserRepository getStructureForUserRepository() {
		return structureForUserRepository;
	}

	public void setStructureForUserRepository(
			StructureForUserRepository structureForUserRepository) {
		this.structureForUserRepository = structureForUserRepository;
	}

	public StructureService getStructureService() {
		return structureService;
	}

	public void setStructureService(StructureService StructureService) {
		this.structureService = StructureService;
	}

	public StructureRepository getStructureRepository() {
		return structureRepository;
	}

	public void setStructureRepository(StructureRepository structureRepository) {
		this.structureRepository = structureRepository;
	}

}
