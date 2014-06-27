package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.info.Task;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.FullTaskProto;

public interface NoneventTaskProtoSerializer
{

	public FullTaskProto createFullTaskProtoFromTask(Task task);
}
