package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.PvpBattleHistory;
public class PvpBattleHistoryRepository extends BaseDynamoRepository<PvpBattleHistory>{
	public PvpBattleHistoryRepository(){
		super(PvpBattleHistory.class);
	}

}