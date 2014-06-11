package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.UserCredential;

public interface UserCredentialRepository
{
	public abstract List<UserCredential> getUserCredentialByFacebook( final String facebookId );

	public abstract List<UserCredential> getUserCredentialByUdid( final String udid );
}
