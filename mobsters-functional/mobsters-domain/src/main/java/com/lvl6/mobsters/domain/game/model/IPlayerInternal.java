package com.lvl6.mobsters.domain.game.model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;

import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.ITask;


interface IPlayerInternal {
	public List<? extends IPlayerTaskInternal> getOngoingPlayerTasks();
	
	public IPlayerTaskInternal getOngoingPlayerTask(ITask taskMeta);
	
	public boolean hasCompleted(ITask taskMeta);
	
	public IPlayerTaskInternal beginTask(
		ITask taskMeta, Set<IQuestJob> questJobs, String elementName, boolean mayGeneratePieces);

	public IPlayerInternal checkCanSpendGems(
		int gemsToSpend, Logger log, Callable<String> spendPurposeLambda);
	
	public IPlayerInternal checkCanSpendGems( int gemsToSpend, Logger log );
	
	public IPlayerInternal checkCanSpendCash(
		int cashToSpend, Logger log, Callable<String> spendPurposeLambda);
	
	public IPlayerInternal checkCanSpendCash( int cashToSpend, Logger log );
	
	public IPlayerInternal checkCanSpendOil(
		int oilToSpend, Logger log, Callable<String> spendPurposeLambda
	);
	
	public IPlayerInternal checkCanSpendOil( int oilToSpend, Logger log );
	
	public boolean canSpendGems( int gemsToSpend );
	
	public boolean canSpendCash( int cashToSpend );
	
	public boolean canSpendOil( int oilToSpend );
	
	public IPlayerInternal spendGems( int gemsToSpend, Logger log );
	
	public boolean spendGems( int gemsToSpend );
	
	public IPlayerInternal spendCash( int cashToSpend, Logger log);
	
	public boolean spendCash( int cashToSpend );
	
	public IPlayerInternal spendOil(int oilToSpend, Logger log);
	
	boolean spendOil(int oilToSpend);

	//
	// Semantic Pass Through
	//
	
	public String getUuid();
	
	public int getGems();

}
