package com.lvl6.mobsters.domainmodel.gameclient;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;

import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.ITask;


public interface Player {
	public List<? extends PlayerTask> getOngoingPlayerTasks();
	
	public PlayerTask getOngoingPlayerTask(ITask taskMeta);
	
	public boolean hasCompleted(ITask taskMeta);
	
	public PlayerTask beginTask(
		ITask taskMeta, Iterable<IQuestJob> questJobs, String elementName, boolean mayGeneratePieces);

	public Player checkCanSpendGems(
		int gemsToSpend, Logger log, Callable<String> spendPurposeLambda);

	public Player checkCanSpendGems( int gemsToSpend, Logger log );
	
	public Player checkCanSpendCash(
		int cashToSpend, Logger log, Callable<String> spendPurposeLambda);
	
	public Player checkCanSpendCash( int cashToSpend, Logger log );
	
	public Player checkCanSpendOil(
		int oilToSpend, Logger log, Callable<String> spendPurposeLambda
	);
	
	public Player checkCanSpendOil( int oilToSpend, Logger log );
	
	public boolean canSpendGems( int gemsToSpend );
	
	public boolean canSpendCash( int cashToSpend );
	
	public boolean canSpendOil( int oilToSpend );
	
	//
	// Semantic Pass Through
	//
	public String getUuid();
	
	public int getGems();
}
