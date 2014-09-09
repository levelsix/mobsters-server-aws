package com.lvl6.mobsters.domain.svcreg;

import com.lvl6.mobsters.domain.config.IConfigurationRegistry;
import com.lvl6.mobsters.domain.config.ConfigExtensions;

/**
 * An interface defining all the singleton objects that are required to provide
 * service to domain model instances.
 * 
 * Domain Driven Development authority Udi Dahan has contemplated the issue of
 * providing access to a rich domain model that that necessarily increases the
 * number of objects that need access to the singletons that provide it
 * supporting services and concluded that given facilities to interface with or
 * suitably replace elements of such a static registry during a test run, static
 * utilities need not constitute an obstacle to unit testing and is preferable
 * here to the alternative of widely replicating pointers to the same finite set
 * of objects.
 * 
 * An Initialization on Holder Load pattern is used here to provide components
 * by dependency injection through a static registry while retaining a test seam
 * around such objects. The facility works by using an outer and an inner class.
 * Whatever is set on a public static field of the parent becomes the permanent
 * and efficiently thread safe binding for the remainder of the host process's
 * lifetime. The first thread to request use of the library triggers
 * class-loading of the inner class that populates its static final field from
 * the mutable public static. Under normal circumstances, a properly wired
 * singleton will wire itself into this place during its initialization handler,
 * and a test case can squelch this behavior and supply its own alternative
 * before any other threads attempt to wire test mocks instead.
 * 
 * @author jheinnic
 * 
 */
public interface IServiceRegistry {
    ConfigExtensions getConfigExtensions();

	IConfigurationRegistry getConfigurationRegistry();
}
