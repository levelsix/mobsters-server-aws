package com.lvl6.mobsters.domain.game.api;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;

import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.ITask;


public interface IPlayer {
	public List<? extends IPlayerTask> getOngoingPlayerTasks();
	
	public IPlayerTask getOngoingPlayerTask(ITask taskMeta);
	
	public boolean hasCompleted(ITask taskMeta);
	
	public IPlayerTask beginTask(
		ITask taskMeta, List<IQuestJob> questJobs, String elementName, boolean mayGeneratePieces);

	public IPlayer checkCanSpendGems(
		int gemsToSpend, Logger log, Callable<String> spendPurposeLambda);

	public IPlayer checkCanSpendGems( int gemsToSpend, Logger log );
	
	public IPlayer checkCanSpendCash(
		int cashToSpend, Logger log, Callable<String> spendPurposeLambda);
	
	public IPlayer checkCanSpendCash( int cashToSpend, Logger log );
	
	public IPlayer checkCanSpendOil(
		int oilToSpend, Logger log, Callable<String> spendPurposeLambda
	);
	
	public IPlayer checkCanSpendOil( int oilToSpend, Logger log );
	
	public boolean canSpendGems( int gemsToSpend );
	
	public boolean canSpendCash( int cashToSpend );
	
	public boolean canSpendOil( int oilToSpend );
	
	//
	// Semantic Pass Through
	//
	public String getUuid();
	
	public int getGems();
}
