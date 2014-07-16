package com.lvl6.mobsters.services.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.dynamo.repository.UserCredentialRepository;
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository;
import com.lvl6.mobsters.dynamo.repository.UserRepository;
import com.lvl6.mobsters.dynamo.repository.UserRepositoryImpl;
import com.lvl6.mobsters.server.ControllerConstants;
import com.lvl6.mobsters.services.common.Lvl6MobstersException;
import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode;

@Component
@Transactional
public class UserServiceImpl implements UserService
{
	
	private static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserRepositoryImpl userRepo;

	@Autowired
	UserDataRarelyAccessedRepository userDraRepo;

	@Autowired
	UserCredentialRepository userCredentialRepository;

	/*
	@Override
	@Transactional(
		propagation = Propagation.SUPPORTS,
		isolation = Isolation.READ_COMMITTED,
		readOnly = true)
	public User findById( String id )
	{
		User retVal = userRepo.findOne(id);
		return retVal;
	}

	@Override
	@Transactional(
		propagation = Propagation.SUPPORTS,
		isolation = Isolation.READ_COMMITTED,
		readOnly = true)
	public User findByIdWithClan( String id )
	{
		final User retVal = userRepo.findByIdWithClan(id);

		return retVal;
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void updateUserResources(
		String id,
		int cashDelta,
		int experienceDelta,
		int gemsDelta,
		int oilDelta )
	{
		// TODO: This can be done better using a @Modifying / @Query annotation pair on a
		// single repository method, doing the update in-place without a fetch after all

		final User retVal = userRepo.findOne(id);
		if (cashDelta != 0) {
			retVal.setCash(retVal.getCash()
				+ cashDelta);
		}
		if (experienceDelta != 0) {
			retVal.setExperience(retVal.getExperience()
				+ experienceDelta);
		}
		if (gemsDelta != 0) {
			retVal.setGems(retVal.getGems()
				+ gemsDelta);
		}
		if (oilDelta != 0) {
			retVal.setOil(retVal.getOil()
				+ oilDelta);
		}

		userRepo.save(retVal);
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void updateUsersResources( Iterable<ChangeUserResourcesRequest> actions )
	{
		for (final ChangeUserResourcesRequest action : actions) {
			updateUserResources(action.getId(), action.getCashDelta(),
				action.getExperienceDelta(), action.getGemsDelta(), action.getOilDelta());
		}

	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void saveUser( User modifiedUser )
	{
		userRepo.save(modifiedUser);
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ)
	public void createUser( User newUser )
	{
		userRepo.save(newUser);
	}

	@Override
	public ChangeUserResourcesRequest getChangeUserResourcesRequest(
		String id,
		int cashDelta,
		int experienceDelta,
		int gemsDelta,
		int oilDelta )
	{
		final ChangeUserResourcesRequest retVal =
			new ChangeUserResourcesRequest(id, cashDelta, experienceDelta, gemsDelta, oilDelta);

		return retVal;
	}
	 */
	
	@Override
	public void createUser( String userId, String name, int cash,
		int oil, int gems ) {
		User u = new User();
		u.setId(userId);
		u.setName(name);
		u.setAdmin(false);
		u.setGems(gems);
		u.setCash(cash);
		u.setOil(oil);
		
		// TODO: Figure out correct amount
		u.setExperience((new Random()).nextInt(10));
		u.setLevel(ControllerConstants.USER_CREATE__START_LEVEL);
		
		userRepo.save(u);
	}

	@Override
	public User levelUpUser( String userId, int newLevel ) {
		User user = userRepo.load(userId);
		
		if (null == user) {
			throw new IllegalArgumentException(
			"no User for userId="
				+ userId);
		}
		
		user.setLevel(newLevel);
		userRepo.save(user);
		return user;
	}
	
	 @Override
	 public User modifyUser( String userId, ModifyUserSpec modifySpec ) {
		 final Set<UserFunc> userOps =
			 modifySpec.getUserModificationsSet();
	
		 User user = userRepo.load(userId);

			if (null == user) {
				throw new IllegalArgumentException(
				"no User for userId="
					+ userId);
			}

			// Mutate the object

			for (UserFunc userOp : userOps) {
				userOp.apply(user);
			}

			userRepo.save(user);
			return user;
	 }
	 
	 @Override
	 public User modifyUser( User user, ModifyUserSpec modifySpec ) {
		 final Set<UserFunc> userOps =
			 modifySpec.getUserModificationsSet();
	
			// Mutate the object

			for (UserFunc userOp : userOps) {
				userOp.apply(user);
			}

			userRepo.save(user);
			return user;
	 }
	
	static class ModifyUserSpecBuilderImpl implements ModifyUserSpecBuilder
	{
		final Set<UserFunc> usersModificationsSet;

		ModifyUserSpecBuilderImpl()
		{
			usersModificationsSet = new HashSet<UserFunc>();
		}

		@Override
		public ModifyUserSpec build()
		{
			return new ModifyUserSpec( usersModificationsSet );
		}

		@Override
		public ModifyUserSpecBuilder decrementGems( int gemsDelta )
		{
			usersModificationsSet.add( new DecrementGems( gemsDelta ));
			return this;
		}
		
		@Override
		public ModifyUserSpecBuilder incrementGems( int gemsDelta )
		{
			usersModificationsSet.add( new IncrementGems( gemsDelta ));
			return this;
		}

		@Override
		public ModifyUserSpecBuilder decrementCash( int cashDelta )
		{
			usersModificationsSet.add( new DecrementCash( cashDelta ) );
			return this;
		}
		
		@Override
		public ModifyUserSpecBuilder incrementCash( int cashDelta, int maxCash )
		{
			usersModificationsSet.add( new IncrementCash( cashDelta, maxCash ));
			return this;
		}

		@Override
		public ModifyUserSpecBuilder decrementOil( int oilDelta )
		{
			usersModificationsSet.add( new DecrementOil( oilDelta ));
			return this;
		}
		
		@Override
		public ModifyUserSpecBuilder incrementOil( int oilDelta, int maxOil )
		{
			usersModificationsSet.add( new IncrementOil( oilDelta, maxOil ));
			return this;
		}

		@Override
		public ModifyUserSpecBuilder setExpRelative( int expDelta )
		{
			usersModificationsSet.add( new SetExpRelative( expDelta ));
			return this;
		}

	}
	
	static class DecrementGems implements UserFunc {
		private int gemsDelta; //should be a positive number

		public DecrementGems( int gemsDelta )
		{
			super();
			this.gemsDelta = gemsDelta;
		}

		@Override
		public void apply( User u )
		{
			if (0 > gemsDelta && u.isAdmin()) {
				LOG.info("admin wants to add gems when should be decrementing, whatevs. unsignedGemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
			} else {
				LOG.error("gemsDelta is illegally negative. gemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newGems = u.getGems() - gemsDelta;
			
			if (0 > newGems) {
				LOG.error("user does not have enough gems to spend. gemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_GEMS);
			}
			
			u.setGems(newGems);
		}
	}
	
	static class IncrementGems implements UserFunc {
		private int gemsDelta; //should be a positive number
		
		public IncrementGems( int gemsDelta )
		{
			super();
			this.gemsDelta = gemsDelta;
		}
		
		@Override
		public void apply( User u )
		{
			if ( 0 > gemsDelta )
			{
				LOG.error("gemsDelta is illegally negative. gemsDelta="
					+ gemsDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			u.setGems( u.getGems() + gemsDelta );
		}
	}
	

	static class DecrementCash implements UserFunc {
		private int cashDelta; //should be a positive number

		public DecrementCash( int cashDelta )
		{
			super();
			this.cashDelta = cashDelta;
		}

		@Override
		public void apply( User u )
		{
			if (0 > cashDelta && u.isAdmin()) {
				LOG.info("admin wants to add cash when should be decrementing, whatevs. unsignedCashDelta="
					+ cashDelta
					+ ", user="
					+ u);
			} else {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newCash = u.getCash() - cashDelta;
			
			if (0 > newCash) {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_CASH);
			}
			
			u.setCash(newCash);
		}
	}
	
	static class IncrementCash implements UserFunc {
		private int cashDelta; //should be a positive number
		private int maxCash;
		
		public IncrementCash( int cashDelta, int maxCash )
		{
			super();
			this.cashDelta = cashDelta;
			this.maxCash = maxCash;
		}
		
		@Override
		public void apply( User u )
		{
			if (0 > cashDelta) {
				LOG.error("cashDelta is illegally negative. cashDelta="
					+ cashDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newCash = u.getCash() + cashDelta;
			
			if ( newCash > maxCash ) {
				LOG.warn("cashDelta gives user more than maxCash. limiting to maxCash. cashDelta="
					+ cashDelta
					+ ", user="
					+ u
					+ ", maxCash="
					+ maxCash);
				newCash = maxCash;
			}
			
			u.setCash(newCash);
		}
	}
	
	static class DecrementOil implements UserFunc {
		private int oilDelta; //should be a positive number

		public DecrementOil( int oilDelta )
		{
			super();
			this.oilDelta = oilDelta;
		}

		@Override
		public void apply( User u )
		{
			if (0 > oilDelta && u.isAdmin()) {
				LOG.info("admin wants to add oil when should be decrementing, whatevs. unsignedOilDelta="
					+ oilDelta
					+ ", user="
					+ u);
			} else {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newOil = u.getOil() + oilDelta;
			
			if (0 > newOil) {
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_INSUFFICIENT_OIL);
			}
			
			u.setOil(newOil);
		}
	}
	
	static class IncrementOil implements UserFunc {
		private int oilDelta; //should be a positive number
		private int maxOil;
		
		public IncrementOil( int oilDelta, int maxOil )
		{
			super();
			this.oilDelta = oilDelta;
			this.maxOil = maxOil;
		}
		
		@Override
		public void apply( User u )
		{
			if (0 > oilDelta) {
				LOG.error("oilDelta is illegally negative. oilDelta="
					+ oilDelta
					+ ", user="
					+ u);
				throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
			}
			
			int newOil = u.getOil() + oilDelta;
			
			if ( newOil > maxOil ) {
				LOG.warn("oilDelta gives user more than maxOil. limiting to maxOil. oilDelta="
					+ oilDelta
					+ ", user="
					+ u
					+ ", maxOil="
					+ maxOil);
				newOil = maxOil;
			}
			
			u.setOil(newOil);
		}
	}
	
	static class SetExpRelative implements UserFunc {
		private int expDelta;

		public SetExpRelative( int expDelta )
		{
			super();
			this.expDelta = expDelta;
		}

		@Override
		public void apply( User u )
		{
			int newExp = u.getExperience() + expDelta;
			u.setExperience(newExp);
		}
	}	
	
	/**************************************************************************/

	/**
	 * At the moment, only UserCreateController uses this. No checks are made before
	 * saving this data to the db.
	 */
	@Override
	public void createUserDataRarelyAccessed(
		String userId,
		String udidForHistory,
		Date createTime,
		String deviceToken,
		boolean fbIdSetOnUserCreate )
	{
		UserDataRarelyAccessed udra = new UserDataRarelyAccessed();
		udra.setUserId(userId);
		udra.setUdidForHistory(udidForHistory);
		udra.setLastLogin(createTime);
		udra.setDeviceToken(deviceToken);
		udra.setCreateTime(createTime);
		udra.setFbIdSetOnUserCreate(fbIdSetOnUserCreate);
		udra.setLastObstacleSpawnTime(createTime);
		
		userDraRepo.save(udra);
	}
	
	@Override
	public void modifyUserDataRarelyAccessed(
		String userId,
		ModifyUserDataRarelyAccessedSpec modifySpec )
	{
		// get whatever we need from the database
		final Set<UserDataRarelyAccessedFunc> userOps =
			modifySpec.getUsersDraModificationsSet();

		UserDataRarelyAccessed udra = userDraRepo.load(userId);

		if (null == udra) { throw new IllegalArgumentException(
			"no UserDataRarelyAccessed for userId="
				+ userId); }

		// Mutate the object

		for (UserDataRarelyAccessedFunc userOp : userOps) {
			userOp.apply(udra);
		}

		userDraRepo.save(udra);
	}

	static class ModifyUserDataRarelyAccessedSpecBuilderImpl
		implements
			ModifyUserDataRarelyAccessedSpecBuilder
	{
		final Set<UserDataRarelyAccessedFunc> usersDraModificationsSet;

		ModifyUserDataRarelyAccessedSpecBuilderImpl()
		{
			usersDraModificationsSet = new HashSet<UserDataRarelyAccessedFunc>();
		}

		@Override
		public ModifyUserDataRarelyAccessedSpec build()
		{
			return new ModifyUserDataRarelyAccessedSpec(usersDraModificationsSet);
		}

		@Override
		public ModifyUserDataRarelyAccessedSpecBuilder setGameCenterIdNotNull(
			String nonNullGameCenterId )
		{
			usersDraModificationsSet.add(new SetGameCenterIdNotNull(nonNullGameCenterId));
			return this;
		}

		@Override
		public ModifyUserDataRarelyAccessedSpecBuilder setDeviceToken( String deviceToken )
		{
			usersDraModificationsSet.add(new SetDeviceToken(deviceToken));
			return this;
		}

		@Override
		public ModifyUserDataRarelyAccessedSpecBuilder setAvatarMonsterId( int monsterId )
		{
			usersDraModificationsSet.add(new SetAvatarMonsterId(monsterId));
			return null;
		}
	}

	static class SetGameCenterIdNotNull implements UserDataRarelyAccessedFunc
	{
		private String gameCenterId;

		SetGameCenterIdNotNull( String gameCenterId )
		{
			this.gameCenterId = gameCenterId;
		}

		@Override
		public void apply( UserDataRarelyAccessed udra )
		{
			udra.setGameCenterId(gameCenterId);
		}
	}

	static class SetDeviceToken implements UserDataRarelyAccessedFunc
	{
		private String deviceToken;

		SetDeviceToken( String deviceToken )
		{
			this.deviceToken = deviceToken;
		}

		@Override
		public void apply( UserDataRarelyAccessed udra )
		{
			udra.setDeviceToken(deviceToken);
		}
	}
	
	static class SetAvatarMonsterId implements UserDataRarelyAccessedFunc
	{
		private int monsterId;
		
		SetAvatarMonsterId( int monsterId ) {
			this.monsterId = monsterId;
		}
		
		@Override
		public void apply( UserDataRarelyAccessed udra ) {
			udra.setAvatarMonsterId(monsterId);
		}
	}

	/**************************************************************************/

	/**
	 * At the moment, only UserCreateController uses this. No checks are made before
	 * saving this data to the db.
	 */
	@Override
	public UserCredential getUserCredentialByFacebookIdOrUdid(String facebookId, String udid) {
		
		List<UserCredential> ucList = userCredentialRepository.findByFacebookId(facebookId);
		
		
		if (ucList.size() > 1) {
			ucList = new ArrayList<UserCredential>(ucList);
			LOG.warn("multiple UserCredentials for facebookId=" + facebookId + ". list=" +
				ucList + " choosing the one with the lowest userId.");
			
			// TODO: Figure out if sorting is necessary.
			Collections.sort(ucList, new Comparator<UserCredential>() {
				@Override
				public int compare(UserCredential o1, UserCredential o2) {
					return o1.getUserId().compareTo(o2.getUserId());
				}
			});
			
			return ucList.get(0);
			
		} else if (ucList.size() == 1) {
			return ucList.get(0);
		}
		
		ucList = userCredentialRepository.findByUdid(udid);
		
		if (ucList.size() > 1) {
			LOG.warn("wtf, multiple UserCredentials for udid=" + udid + ". list=" +
				ucList + ", consider making client generate another udid.");

			// TODO: Figure out if sorting is necessary.
			Collections.sort(ucList, new Comparator<UserCredential>() {
				@Override
				public int compare(UserCredential o1, UserCredential o2) {
					return o1.getUserId().compareTo(o2.getUserId());
				}
			});

			return ucList.get(0);
		} else if (ucList.size() == 1) {
			return ucList.get(0);
		}
		
		return null;
	}

	@Override
	public UserCredential createUserCredential( String facebookId, String udid ) throws Exception
	{
		UserCredential uc = new UserCredential();

		// if facebook id is provided, use that to try creating account, else use udid
		if (StringUtils.hasText(facebookId)) {
			List<UserCredential> userCredentials =
				userCredentialRepository.findByFacebookId(facebookId);

			if (!CollectionUtils.lacksSubstance(userCredentials)) { throw new Exception(
				"User(s) already exist with facebookId="
					+ facebookId
					+ " users="
					+ userCredentials); }

			uc.setFacebookId(facebookId);

		} else {
			List<UserCredential> userCredentials =
				userCredentialRepository.findByUdid(udid);

			if (!CollectionUtils.lacksSubstance(userCredentials)) { throw new Exception(
				"User(s) already exist with udid="
					+ udid
					+ " users="
					+ userCredentials); }

			uc.setUdid(udid);
		}

		userCredentialRepository.save(uc);
		return uc;
	}

	// for the dependency injection
	public UserRepository getUserRepo()
	{
		return userRepo;
	}

	public void setUserRepo( UserRepositoryImpl userRepo )
	{
		this.userRepo = userRepo;
	}

	public UserDataRarelyAccessedRepository getUserDraRepo()
	{
		return userDraRepo;
	}

	public void setUserDraRepo( UserDataRarelyAccessedRepository userDraRepo )
	{
		this.userDraRepo = userDraRepo;
	}

	public UserCredentialRepository getUserCredentialRepository()
	{
		return userCredentialRepository;
	}

	public void setUserCredentialRepository( UserCredentialRepository userCredentialRepository )
	{
		this.userCredentialRepository = userCredentialRepository;
	}

}
