package com.lvl6.mobsters.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.mobsters.info.User;
import com.lvl6.mobsters.info.repository.UserRepository;

@Component
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepo;

	@Override
	@Transactional(propagation=Propagation.SUPPORTS, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public User findById(String id) {
		User retVal = userRepo.findOne(id);
		return retVal;
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public User findByIdWithClan(String id) {
		final User retVal = userRepo.findByIdWithClan(id);
		
		return retVal;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.REPEATABLE_READ)
	public void updateUserResources(String id, int cashDelta,
			int experienceDelta, int gemsDelta, int oilDelta) {
		// TODO: This can be done better using a @Modifying / @Query annotation pair on a
		//       single repository method, doing the update in-place without a fetch after all
		
		final User retVal = userRepo.findOne(id);
        if (cashDelta != 0) {
        	retVal.setCash(
        		retVal.getCash() + cashDelta
        	);
        }
        if (experienceDelta != 0) {
        	retVal.setExperience(
        		retVal.getExperience() + experienceDelta
        	);
        }
        if (gemsDelta != 0) {
        	retVal.setGems(
        		retVal.getGems() + gemsDelta
        	);
        }
        if (oilDelta != 0) {
        	retVal.setOil(
        		retVal.getOil() + oilDelta
        	);
        }
        
        userRepo.save(retVal);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.REPEATABLE_READ)
	public void updateUsersResources(
			Iterable<ChangeUserResourcesRequest> actions) {
		for (final ChangeUserResourcesRequest action : actions) {
			updateUserResources(
				action.getId(), action.getCashDelta(), action.getExperienceDelta(),
				action.getGemsDelta(), action.getOilDelta()
			);
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.REPEATABLE_READ)
	public void saveUser(User modifiedUser) {
		userRepo.save(modifiedUser);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.REPEATABLE_READ)
	public void createUser(User newUser) {
        userRepo.save(newUser);
	}

	@Override
	public ChangeUserResourcesRequest getChangeUserResourcesRequest(String id,
			int cashDelta, int experienceDelta, int gemsDelta, int oilDelta) {
		final ChangeUserResourcesRequest retVal = 
			new ChangeUserResourcesRequest(id, cashDelta, experienceDelta, gemsDelta, oilDelta);
		
		return retVal;
	}
}
