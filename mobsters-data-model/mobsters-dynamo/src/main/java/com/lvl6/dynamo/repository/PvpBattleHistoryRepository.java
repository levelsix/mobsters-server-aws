package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.PvpBattleHistory;
@Component public class PvpBattleHistoryRepository extends BaseDynamoRepository<PvpBattleHistory>{
	public PvpBattleHistoryRepository(){
		super(PvpBattleHistory.class);
	}

}