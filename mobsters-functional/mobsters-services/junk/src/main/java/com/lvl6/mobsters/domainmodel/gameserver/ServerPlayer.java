package com.lvl6.mobsters.domainmodel.gameserver;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;

import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.ITask;


public interface ServerPlayer {
	public List<? extends ServerPlayerTask> getOngoingPlayerTasks();
	
	public ServerPlayerTask getOngoingPlayerTask(ITask taskMeta);
	
	public boolean hasCompleted(ITask taskMeta);
	
	public ServerPlayerTask beginTask(
		ITask taskMeta, Iterable<IQuestJob> questJobs, String elementName, boolean mayGeneratePieces);

	public ServerPlayer checkCanSpendGems(
		int gemsToSpend, Logger log, Callable<String> spendPurposeLambda);
	
	public ServerPlayer checkCanSpendGems( int gemsToSpend, Logger log );
	
	public ServerPlayer checkCanSpendCash(
		int cashToSpend, Logger log, Callable<String> spendPurposeLambda);
	
	public ServerPlayer checkCanSpendCash( int cashToSpend, Logger log );
	
	public ServerPlayer checkCanSpendOil(
		int oilToSpend, Logger log, Callable<String> spendPurposeLambda
	);
	
	public ServerPlayer checkCanSpendOil( int oilToSpend, Logger log );
	
	public boolean canSpendGems( int gemsToSpend );
	
	public boolean canSpendCash( int cashToSpend );
	
	public boolean canSpendOil( int oilToSpend );
	
	public ServerPlayer spendGems( int gemsToSpend, Logger log );
	
	public boolean spendGems( int gemsToSpend );
	
	public ServerPlayer spendCash( int cashToSpend, Logger log);
	
	public boolean spendCash( int cashToSpend );
	
	public ServerPlayer spendOil(int oilToSpend, Logger log);
	
	boolean spendOil(int oilToSpend);

	//
	// Semantic Pass Through
	//
	
	public String getUuid();
	
	public int getGems();

}
