package com.lvl6.mobsters.services.user;

import java.util.Date;
import java.util.Set;

import com.lvl6.mobsters.common.utils.Function;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.services.user.UserServiceImpl.ModifyUserDataRarelyAccessedSpecBuilderImpl;
import com.lvl6.mobsters.services.user.UserServiceImpl.ModifyUserSpecBuilderImpl;

public interface UserService
{
	/*
	public User findById( String id );

	public User findByIdWithClan( String id );

	/**
	 * Update identified resource counters by incrementing/decrementing the appropriate amounts.
	 * 
	 * This method bypasses optimistic locking conflicts, but does so by reading, modifying, and then
	 * saving the target rows in a single transaction.
	 * 
	 * @param id
	 * @param cashDelta
	 * @param experienceDelta
	 * @param gemsDelta
	 * @param oilDelta
	 */
	/*public void updateUserResources(
		String id,
		int cashDelta,
		int experienceDelta,
		int gemsDelta,
		int oilDelta );

	/*
	 * Perform a sequence of relative user resource modifications within a single transaction.
	 * 
	 * Call {@link #getChangeUserResourcesRequest(String, int, int, int, int)
	 * getChangeUserResourceRequest} to construct the members of the argument list.
	 * 
	 * This method bypasses optimistic locking conflicts, but does so by reading, modifying, and then
	 * saving the targeted rows within a single transaction.
	 * 
	 * @param actions
	 *        A list of relative user resource modification requests.
	 */
	/*public void updateUsersResources( Iterable<ChangeUserResourcesRequest> actions );

	/**
	 * Saves a modified user record, provided no optimistic locking conflicts occur.
	 * 
	 * This method attempts to save a modified user without reading a copy of its state first. Unmodified
	 * user attributes must be set as they were to remain unmodified. Optimistic locking check is
	 * performed at the RDBMS, enabling a single-update round trip.
	 * 
	 * @param modifiedUser
	 */
	/*public void saveUser( User modifiedUser );

	public void createUser( User newUser );

	public ChangeUserResourcesRequest getChangeUserResourcesRequest(
		String id,
		int cashDelta,
		int experienceDelta,
		int gemsDelta,
		int oilDelta );

	public final class ChangeUserResourcesRequest
	{
		private final String id;

		private final int cashDelta;

		private final int experienceDelta;

		private final int gemsDelta;

		private final int oilDelta;

		ChangeUserResourcesRequest(
			final String id,
			final int cashDelta,
			final int experienceDelta,
			final int gemsDelta,
			final int oilDelta )
		{
			super();
			this.id = id;
			this.cashDelta = cashDelta;
			this.experienceDelta = experienceDelta;
			this.gemsDelta = gemsDelta;
			this.oilDelta = oilDelta;
		}

		String getId()
		{
			return id;
		}

		int getCashDelta()
		{
			return cashDelta;
		}

		int getExperienceDelta()
		{
			return experienceDelta;
		}

		int getGemsDelta()
		{
			return gemsDelta;
		}

		int getOilDelta()
		{
			return oilDelta;
		}
	}
	*/

	public void createUser( String userId, String name, int cash,
		int oil, int gems );
	
	public abstract User modifyUser( String userId, ModifyUserSpec modifySpec );

	public interface ModifyUserSpecBuilder
	{
		// specify the modifications (to the users' columns) that will be made

		public ModifyUserSpec build();

		public ModifyUserSpecBuilder decrementGems( int gemsDelta );
		
		public ModifyUserSpecBuilder decrementCash( int cashDelta );
		
		public ModifyUserSpecBuilder decrementOil( int oilDelta );
		
		public ModifyUserSpecBuilder setExpRelative( int expDelta );
		
	}

	// cuts down the amount of typing
	interface UserFunc extends Function<User>
	{}

	// implicitly static
	public class ModifyUserSpec
	{
		final private Set<UserFunc> userModificationsSet;

		ModifyUserSpec( Set<UserFunc> userModificationsSet )
		{
			this.userModificationsSet = userModificationsSet;
		}

		Set<UserFunc> getUserModificationsSet()
		{
			return userModificationsSet;
		}

		public static ModifyUserSpecBuilder builder()
		{
			ModifyUserSpecBuilder builder = new ModifyUserSpecBuilderImpl();
			return builder;
		}
	}

	/**************************************************************************/
	
	public void createUserDataRarelyAccessed( String userId, String udidForHistory,
		Date createTime, String deviceToken, boolean fbIdSetOnUserCreate);

	public void modifyUserDataRarelyAccessed(
		String userId,
		ModifyUserDataRarelyAccessedSpec modifySpec );

	/*
	 * public interface ModifyUserDataRarelyAccessedSpecBuilder { // specify the modifications (to the
	 * users' columns) that will be made public ModifyUserDataRarelyAccessedSpec build(); public
	 * ModifyUserDataRarelyAccessedSpecBuilder setGameCenterIdNotNull( String userId, String
	 * nonNullGameCenterId ); } interface UserDataRarelyAccessedFunc extends
	 * Function<UserDataRarelyAccessed> {} public class ModifyUserDataRarelyAccessedSpec { // keys are
	 * userIds, should only be one key since UserDataRarelyAccessed will only be accessed by // one user
	 * final private Multimap<String, UserDataRarelyAccessedFunc> usersDraModificationsMap;
	 * ModifyUserDataRarelyAccessedSpec( Multimap<String, UserDataRarelyAccessedFunc>
	 * usersDraModificationsMap ) { this.usersDraModificationsMap = usersDraModificationsMap; }
	 * Multimap<String, UserDataRarelyAccessedFunc> getUsersDraModificationsMap() { return
	 * usersDraModificationsMap; } public static ModifyUserDataRarelyAccessedSpecBuilder builder() {
	 * ModifyUserDataRarelyAccessedSpecBuilder builder = new
	 * ModifyUserDataRarelyAccessedSpecBuilderImpl(); return builder; } }
	 */
	public interface ModifyUserDataRarelyAccessedSpecBuilder
	{
		// specify the modifications (to the users' columns) that will be made

		public ModifyUserDataRarelyAccessedSpec build();

		public ModifyUserDataRarelyAccessedSpecBuilder setGameCenterIdNotNull(
			String nonNullGameCenterId );
		
		public ModifyUserDataRarelyAccessedSpecBuilder setDeviceToken( String deviceToken );
		
		public ModifyUserDataRarelyAccessedSpecBuilder setAvatarMonsterId( int monsterId );

	}

	interface UserDataRarelyAccessedFunc extends Function<UserDataRarelyAccessed>
	{}

	public class ModifyUserDataRarelyAccessedSpec
	{
		// only one key (userId), should only be one key since one UserDataRarelyAccessed row will only
		// be accessed by one user

		final private Set<UserDataRarelyAccessedFunc> usersDraModificationsSet;

		ModifyUserDataRarelyAccessedSpec(
			Set<UserDataRarelyAccessedFunc> usersDraModificationsSet )
		{
			this.usersDraModificationsSet = usersDraModificationsSet;
		}

		Set<UserDataRarelyAccessedFunc> getUsersDraModificationsSet()
		{
			return usersDraModificationsSet;
		}

		public static ModifyUserDataRarelyAccessedSpecBuilder builder()
		{
			ModifyUserDataRarelyAccessedSpecBuilder builder =
				new ModifyUserDataRarelyAccessedSpecBuilderImpl();
			return builder;
		}
	}

	
	public UserCredential getUserCredentialByFacebookIdOrUdid( String facebookId, String udid );
	/**
	 * @throws Exception
	 */

	public UserCredential createUserCredential( String facebookId, String udid ) throws Exception;


}
