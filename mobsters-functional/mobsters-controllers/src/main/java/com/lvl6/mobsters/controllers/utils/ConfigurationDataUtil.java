package com.lvl6.mobsters.controllers.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lvl6.mobsters.info.BaseIntPersistentObject;

// static helper utility class convention #1 -- no sub-classing; declare final.
public final class ConfigurationDataUtil
{
	// Static helper utility class convention #2 -- Only constructor is unused and private.
	private ConfigurationDataUtil()
	{}
	
	public static Map<Integer, BaseIntPersistentObject> mapifyConfigurationData(Collection<?> data) {
		Map<Integer, BaseIntPersistentObject> results = new HashMap<Integer, BaseIntPersistentObject>();
		
		for (Object datum : data) {
			results.put(((BaseIntPersistentObject)datum).getId(), (BaseIntPersistentObject) datum);
		}
		
		return results;
	}
}
