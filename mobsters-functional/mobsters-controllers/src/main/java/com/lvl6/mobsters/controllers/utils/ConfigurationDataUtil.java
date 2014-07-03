package com.lvl6.mobsters.controllers.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lvl6.mobsters.info.BaseIntPersistentObject;

public class ConfigurationDataUtil
{
	public static Map<Integer, BaseIntPersistentObject> mapifyConfigurationData(Collection<?> data) {
		Map<Integer, BaseIntPersistentObject> results = new HashMap<Integer, BaseIntPersistentObject>();
		
		for (Object datum : data) {
			results.put(((BaseIntPersistentObject)datum).getId(), (BaseIntPersistentObject) datum);
		}
		
		return results;
	}
}
