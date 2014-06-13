package com.lvl6.mobsters.services.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.dynamo.repository.UserCredentialRepository;
import com.lvl6.mobsters.dynamo.repository.UserDataRarelyAccessedRepository;
import com.lvl6.mobsters.info.User;
import com.lvl6.mobsters.info.repository.UserRepository;

@Component
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    UserDataRarelyAccessedRepository userDraRepo;
    
    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        readOnly = true)
    public User findById( String id ) {
        User retVal = userRepo.findOne(id);
        return retVal;
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        readOnly = true)
    public User findByIdWithClan( String id ) {
        final User retVal = userRepo.findByIdWithClan(id);

        return retVal;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
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
            retVal.setCash(retVal.getCash() + cashDelta);
        }
        if (experienceDelta != 0) {
            retVal.setExperience(retVal.getExperience() + experienceDelta);
        }
        if (gemsDelta != 0) {
            retVal.setGems(retVal.getGems() + gemsDelta);
        }
        if (oilDelta != 0) {
            retVal.setOil(retVal.getOil() + oilDelta);
        }

        userRepo.save(retVal);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void updateUsersResources( Iterable<ChangeUserResourcesRequest> actions ) {
        for (final ChangeUserResourcesRequest action : actions) {
            updateUserResources(
                action.getId(),
                action.getCashDelta(),
                action.getExperienceDelta(),
                action.getGemsDelta(),
                action.getOilDelta());
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void saveUser( User modifiedUser ) {
        userRepo.save(modifiedUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void createUser( User newUser ) {
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

    // @Override
    // public void modifyUser( ModifyUserSpec modifySpec ) {
    // final Multimap<String, UserFunc> usersModificationsMap =
    // modifySpec.getUsersModificationsMap();
    //
    // }

    static class ModifyUserSpecBuilderImpl implements ModifyUserSpecBuilder {
        // keys are userIds
        final Multimap<String, UserFunc> usersModificationsMap;

        ModifyUserSpecBuilderImpl() {
            usersModificationsMap = ArrayListMultimap.create();
        }

        @Override
        public ModifyUserSpec build() {
            return new ModifyUserSpec(usersModificationsMap);
        }

    }

    /**************************************************************************/

    @Override
    public void modifyUserDataRarelyAccessed(
        String userId,
        ModifyUserDataRarelyAccessedSpec modifySpec )
    {
        // get whatever we need from the database
        final Set<UserDataRarelyAccessedFunc> userOps = modifySpec.getUsersDraModificationsSet();

        UserDataRarelyAccessed udra = userDraRepo.load(userId);

        if (null == udra) {
            throw new IllegalArgumentException("no UserDataRarelyAccessed for userId=" + userId);
        }

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

        ModifyUserDataRarelyAccessedSpecBuilderImpl() {
            usersDraModificationsSet = new HashSet<UserDataRarelyAccessedFunc>();
        }

        @Override
        public ModifyUserDataRarelyAccessedSpec build() {
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
        public ModifyUserDataRarelyAccessedSpecBuilder setDeviceToken(
            String deviceToken )
        {
            usersDraModificationsSet.add(new SetDeviceToken(deviceToken));
            return this;
        }
    }

    static class SetGameCenterIdNotNull implements UserDataRarelyAccessedFunc {
        private String gameCenterId;

        SetGameCenterIdNotNull( String gameCenterId ) {
            this.gameCenterId = gameCenterId;
        }

        @Override
        public void apply( UserDataRarelyAccessed udra ) {
            udra.setGameCenterId(gameCenterId);
        }
    }
    
    static class SetDeviceToken implements UserDataRarelyAccessedFunc {
        private String deviceToken;

        SetDeviceToken( String deviceToken ) {
            this.deviceToken = deviceToken;
        }

        @Override
        public void apply( UserDataRarelyAccessed udra ) {
            udra.setDeviceToken(deviceToken);
        }
    }
    
    /**************************************************************************/
    
    @Override
    public UserCredential createUserCredential( String facebookId, String udid ) throws Exception {
    	UserCredential uc = new UserCredential();
    	
    	//if facebook id is provided, use that to try creating account, else use udid
    	if (StringUtils.hasText(facebookId)) {
    		List<UserCredential> userCredentials = userCredentialRepository.getUserCredentialByFacebook(facebookId);

    		if (!CollectionUtils.lacksSubstance(userCredentials)) {
    			throw new Exception("User(s) already exist with facebookId=" + facebookId + " users=" + userCredentials); 
    		}

    		uc.setFacebookId(facebookId);

    	} else {
    		List<UserCredential> userCredentials = userCredentialRepository.getUserCredentialByUdid(udid);

    		if (!CollectionUtils.lacksSubstance(userCredentials)) {
    			throw new Exception("User(s) already exist with udid=" + udid + " users=" + userCredentials); 
    		}

    		uc.setUdid(udid);
    	}
    	
    	userCredentialRepository.save(uc);
    	return uc;
    }

    //for the dependency injection
    public UserRepository getUserRepo()
    {
    	return userRepo;
    }
    
    public void setUserRepo( UserRepository userRepo )
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
