package com.lvl6.mobsters.services.user

import com.lvl6.mobsters.utility.lambda.Director

interface UserService
{
	/* BEGIN READ-ONLY METHODS ************************************************************************/
	def String getUserCredentialByFacebookIdOrUdid(String facebookId, String udid)

	/* BEGIN TRANSACTIONAL METHODS ******************************************************************/
	
	/**
	 * Builder for collecting optional parameters and parameters with N-ary cardinality.
	 * In this case, the list of structures has N-ary cardinality, but it is required,
	 * so there is no createXXXUser() method without a Director<CreateUserOptionsBuilder>.
	 * @author jheinnic
	 */
	interface CreateUserOptionsBuilder
	{
		def UserService.CreateUserOptionsBuilder withStructure(int structureId, float xPosition, float yPosition)
	}

	interface CreateUserReplyBuilder
	{
		def UserService.CreateUserReplyBuilder resultOk()
	}

	def void createFacebookUser(UserService.CreateUserReplyBuilder replyBuilder, String facebookId,
		String udid, String name, String deviceToken, int cash, int oil, int gems,
		Director<UserService.CreateUserOptionsBuilder> options)

	def void createUdidUser(UserService.CreateUserReplyBuilder replyBuilder, String udid,
		String name, String deviceToken, int cash, int oil, int gems,
		Director<UserService.CreateUserOptionsBuilder> options)

	def void levelUpUser(String userId, int newLevel)

	def void modifyUser(String userId, Director<UserService.ModifyUserBuilder> director)

	// This is not a valid service method--it uses a domain class, User, in its call signature 
	// def User modifyUser(User user, ModifyUserSpec modifySpec)
	public interface ModifyUserBuilder
	{
		/**
		 * Reduces target user's gem count by gemsDelta iff that user has at least that many gems.
		 * Otherwise, throws a Lvl6Exception with INSUFFICIENT_GEMS as the cause.
		 * 
		 * @throws Lvl6Exception Throws INSUFFICIENT_GEMS if user has fewer than gemsDelta gems.
		 */
		def UserService.ModifyUserBuilder spendGems(int gemsDelta)
		
		/**
		 * Reduces target user's gem count by gemDelta.  If user has less than that many gems, reduces it
		 * to zero instead.
		 */
		def UserService.ModifyUserBuilder decrementGems(int gemsDelta)
		
		/**
		 * Increases target user's gem count by gemDelta.
		 */
		def UserService.ModifyUserBuilder incrementGems(int gemsDelta)

		/**
		 * Reduces target user's cash count by cashDelta iff that user has at least that much cash.
		 * Otherwise, throws a Lvl6Exception with INSUFFICIENT_CASH as the cause.
		 * 
		 * @throws Lvl6Exception Throws INSUFFICIENT_CASH if user has less than cashDelta cash.
		 */
		def UserService.ModifyUserBuilder spendCash(int cashDelta)
		
		/**
		 * Reduces target user's casg count by cashDelta.  If user has less than that much cash, reduces it
		 * to zero instead.
		 */
		def UserService.ModifyUserBuilder decrementCash(int cashDelta)

		def UserService.ModifyUserBuilder incrementCash(int cashDelta, int maxCash)

		/**
		 * Reduces target user's oil count by oilDelta iff that user has at least that many oil units.
		 * Otherwise, throws a Lvl6Exception with INSUFFICIENT_GEMS as the cause.
		 * 
		 * @throws Lvl6Exception Throws INSUFFICIENT_OIL if user has less than oilDelta units of oil.
		 */
		def UserService.ModifyUserBuilder spendOil(int oilDelta)
		
		/**
		 * Reduces target user's oil count by oilDelta.  If user has less than that much oil, reduces it
		 * to zero instead.
		 */
		def UserService.ModifyUserBuilder decrementOil(int oilDelta)

		def UserService.ModifyUserBuilder incrementOil(int oilDelta, int maxOil)

		def UserService.ModifyUserBuilder setExpRelative(int expDelta)
		
		def UserService.ModifyUserBuilder levelUpUser(int newLevel)
	}

	/** 
 	 */
	def void modifyUserDataRarelyAccessed(String userId,
		Director<UserService.ModifyUserDataRarelyAccessedBuilder> director)

	public interface ModifyUserDataRarelyAccessedBuilder
	{
		def UserService.ModifyUserDataRarelyAccessedBuilder setGameCenterIdNotNull(String nonNullGameCenterId)

		def UserService.ModifyUserDataRarelyAccessedBuilder setDeviceToken(String deviceToken)

		def UserService.ModifyUserDataRarelyAccessedBuilder setAvatarMonsterId(int monsterId)
	}
}
