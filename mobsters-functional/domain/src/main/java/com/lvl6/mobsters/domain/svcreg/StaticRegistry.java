package com.lvl6.mobsters.domain.svcreg;

import com.google.common.base.Preconditions;
import com.lvl6.mobsters.domain.config.IConfigurationRegistry;
import com.lvl6.mobsters.domain.config.ConfigExtensions;

public final class StaticRegistry  {
    private StaticRegistry()
    { }
    
    static IServiceRegistry preInitInjection;
    
    static class Holder 
    {
    	private static final IServiceRegistry INSTANCE = StaticRegistry.preInitInjection;
    }
    
    static final void wireAndInit(IServiceRegistry rootRegistry) {
    	Preconditions.checkNotNull(rootRegistry);
    	preInitInjection = rootRegistry;
    	if (Holder.INSTANCE != rootRegistry) {
    		throw new IllegalStateException(
    			"Another thread has already wired a different instance of ServiceRegistry for the lifetime of this process."
    		);
    	}
    }
    
    public static final ConfigExtensions getConfigExtensions() {
    	return Holder.INSTANCE.getConfigExtensions();
    }
    
    public static final IConfigurationRegistry getConfigurationRegistry() {
    	return Holder.INSTANCE.getConfigurationRegistry();
    }
}
