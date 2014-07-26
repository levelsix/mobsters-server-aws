package com.lvl6.mobsters.info.xtension

import com.lvl6.mobsters.info.IMonsterLevelInfo
import com.lvl6.mobsters.info.Monster
import org.springframework.stereotype.Component

@Component
public class ConfigExtensions {
	/** Monster Extensions *********************/
	public def IMonsterLevelInfo getFirstLevelInfo( Monster m ) {
		// TODO: This should be as simple as it.lvlInfo.get(0).  
		//        If that didn't work, lets figure out why.
		return
			m.lvlInfo.reduce[min, next|
				if (next.level < min.level)
					return next
				else
					return min
			]
	}
}