package com.lvl6.mobsters.info.xtension

import com.lvl6.mobsters.info.IMonsterLevelInfo
import com.lvl6.mobsters.info.Monster

public class ConfigExtensions {
	/** Monster Extensions *********************/
	public def IMonsterLevelInfo getFirstLevelInfo( Monster it ) {
		return
			it.lvlInfo.reduce[min, next|
				if (next.level < min.level)
					return next
				else
					return min
			]
	}
}