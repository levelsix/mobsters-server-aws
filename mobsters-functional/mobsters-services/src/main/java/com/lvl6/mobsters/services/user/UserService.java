package com.lvl6.mobsters.services.user;

import com.lvl6.mobsters.info.User;

public interface UserService {
    public User findById(String id);

	public User findByIdWithClan(String id);
    
    /**
     * Update identified resource counters by incrementing/decrementing the appropriate amounts.
     * 
     * This method bypasses optimistic locking conflicts, but does so by reading, modifying, and then saving the
     * target rows in a single transaction.
     * 
     * @param id
     * @param cashDelta
     * @param experienceDelta
     * @param gemsDelta
     * @param oilDelta
     */
    public void updateUserResources(
    	String id, int cashDelta, int experienceDelta, int gemsDelta, int oilDelta);
    
    /**
     * Perform a sequence of relative user resource modifications within a single transaction.
     * 
     * Call {@link #getChangeUserResourcesRequest(String, int, int, int, int) getChangeUserResourceRequest}
     * to construct the members of the argument list.
     * 
     * This method bypasses optimistic locking conflicts, but does so by reading, modifying, and then saving the
     * targeted rows within a single transaction.
     * 
     * @param actions A list of relative user resource modification requests.
     */
    public void updateUsersResources(Iterable<ChangeUserResourcesRequest> actions);
    
    /**
     * Saves a modified user record, provided no optimistic locking conflicts occur.
     * 
     * This method attempts to save a modified user without reading a copy of its state first.
     * Unmodified user attributes must be set as they were to remain unmodified.  Optimistic 
     * locking check is performed at the RDBMS, enabling a single-update round trip.
     * 
     * @param modifiedUser
     */
    public void saveUser( User modifiedUser );
    
    public void createUser( User newUser );
    
    public ChangeUserResourcesRequest getChangeUserResourcesRequest(
    	String id, int cashDelta, int experienceDelta, int gemsDelta, int oilDelta);
    
    public final class ChangeUserResourcesRequest {
    	private final String id;
    	private final int cashDelta;
    	private final int experienceDelta;
    	private final int gemsDelta;
    	private final int oilDelta;

		ChangeUserResourcesRequest(final String id, final int cashDelta, final int experienceDelta,
			final int gemsDelta, final int oilDelta) {
			super();
			this.id = id;
			this.cashDelta = cashDelta;
			this.experienceDelta = experienceDelta;
			this.gemsDelta = gemsDelta;
			this.oilDelta = oilDelta;
		}

		String getId() {
			return id;
		}

		int getCashDelta() {
			return cashDelta;
		}

		int getExperienceDelta() {
			return experienceDelta;
		}

		int getGemsDelta() {
			return gemsDelta;
		}

		int getOilDelta() {
			return oilDelta;
		}
    }
}
