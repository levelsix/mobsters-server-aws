package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.TaskForUserCompleted;

public interface TaskForUserCompletedRepository
{

	List<TaskForUserCompleted> findByUserId( String userId );

}