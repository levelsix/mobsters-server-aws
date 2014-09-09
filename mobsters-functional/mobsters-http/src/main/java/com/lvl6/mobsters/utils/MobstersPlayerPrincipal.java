package com.lvl6.mobsters.utils;

import java.io.Serializable;
import java.security.Principal;

import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.google.common.base.Preconditions;

/**
 * Basic user principal used for HTTP authentication
 *
 * @since 4.0
 */
@Immutable
public final class MobstersPlayerPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = -2266305184969850467L;

    // First pattern is for a Mobsters User Id, next two are alternative forms of a Device UDID, and 
    // I still need to add a UUID pattern (TODO)
    @NotNull
    @Pattern(regexp="\\d{3}_-?\\d{9,10}|\\d{13}_\\d{10}|[A-Z0-9]{8}(-[A-Z0-9]{4}){3}-[A-Z0-9]{12}_\\d{8,10}")
    private final String username;
    
    private final UserIdentityType idType;

	private final int hashCode;

	public enum UserIdentityType {
    	USER_ID,
    	FACEBOOK_ID,
    	DEVICE_UDID;
    }

	/**
	 * Create a Principal for a Mobsters credential
	 * 
	 * TODO: RANDOM and HTTP_AUTH do not belong here.  THey have no analogous queue suffix for client messages,
	 * so the following will eventually change in the MobstersHandshakeHandler:
	 * <ul>
	 *   <li>determineUser() will have to treat it as an error if super.determineUser() actually returns a
	 *       non-null value</li>
	 *   <li>Instead of rolling a random fallback (useful during exploratory development), treat inability to
	 *       find some way of identifying the user from HTTP headers as an unrecoverable error.</li>
	 * </ul>
	 * 
	 * For now, fake queue suffixes that the client won't really know how to listen to are being constructed
	 * for these two fake (and temporary!) identity types.
	 * 
	 * NOTE: The username is constructed with a type prefix because it simplifies the work done in 
	 * MobstersUserResolver to build a queue name.  There are two options when implementing this class:
	 * 1)  Implement everything that DefaultUserResolver does, but provide more information from the headers
	 *     than is currently given to DefaultUserResolver's protected getTargetDestination(). 
	 * 2)  Just override getTargetDestination(), and generate a mapped queue name using only the username 
	 *     of the WebSocketSession's Principal() and its Spring-defined sessionId.
	 *     
	 * If (2) was given the Principal and not just Principal.getUsername(), we could be more flexible here...
	 * 
	 * @param username
	 * @param idType
	 */
    public MobstersPlayerPrincipal(final String username, UserIdentityType idType) {
        super();
        Preconditions.checkNotNull(username, "User name may not be null");
        Preconditions.checkNotNull(idType);
        
        switch(idType) {
        	case USER_ID : {
        		this.username = "userid_" + username;
        		break;
        	}
        	case DEVICE_UDID : {
        		this.username = "udid_" + username;
        		break;
        	}
        	case FACEBOOK_ID : {
        		this.username = "FacebookId_" + username;
        		break;
        	}
        	default : {
        		// Unreachable code, but it satisfies the compiler that username is given a value.
        		this.username = "unknown_" + username;
        		break;
        	}
        }
        
        this.idType = idType;
        
        final int prime = 31;
		int result = 1;
		result = prime * result + ((idType == null) ? 0 : idType.hashCode());
		this.hashCode = 
			(prime * result)
			+ ((username == null) ? 0 : username.hashCode());
		
    }

    public String getName() {
        return this.username;
    }
    
    public UserIdentityType getIdType() {
    	return this.idType;
    }

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MobstersPlayerPrincipal other = (MobstersPlayerPrincipal) obj;
		if (idType != other.idType) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}
    
    @Override
	public String toString() {
		return String.format(
			"MobstersPlayerPrincipal [username=%s, idType=%s]", this.username, this.idType);
	}
}