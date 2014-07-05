package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.UserCredential;

public interface UserCredentialRepository extends BaseDynamoRepository<UserCredential>
{
	public List<UserCredential> findByFacebookId( final String facebookId );

	public List<UserCredential> findByUdid( final String udid );
}
