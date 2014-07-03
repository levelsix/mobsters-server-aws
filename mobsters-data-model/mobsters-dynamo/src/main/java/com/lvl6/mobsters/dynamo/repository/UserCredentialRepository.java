package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.UserCredential;

public interface UserCredentialRepository extends BaseDynamoItemRepository<UserCredential>
{
	public List<UserCredential> getUserCredentialByFacebook( final String facebookId );

	public List<UserCredential> getUserCredentialByUdid( final String udid );
}
