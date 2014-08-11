package com.lvl6.mobsters.domainmodel.gameserver;

import com.lvl6.mobsters.domainmodel.gameimpl.ServerUserResource;

public interface ServerUserResourceFactory {
	public ServerUserResource getUserResourceFor(String userUuid);
}
