package com.lvl6.mobsters.domain.game.model;

import com.lvl6.mobsters.dynamo.TaskStageForUser;


interface IPlayerTaskStageInternal {
	// Server Unwrap Methods -- TODO: Should this not be required?
	TaskStageForUser getTaskStageForUser();
}
