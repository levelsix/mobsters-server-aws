package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.CepfuRaidStageHistory;
@Component public abstract class CepfuRaidStageHistoryRepository extends BaseDynamoItemRepositoryImpl<CepfuRaidStageHistory>{
	public CepfuRaidStageHistoryRepository(){
		super(CepfuRaidStageHistory.class);
	}

}