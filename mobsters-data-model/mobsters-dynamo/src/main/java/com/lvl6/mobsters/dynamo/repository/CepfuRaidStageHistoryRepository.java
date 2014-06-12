package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.CepfuRaidStageHistory;
@Component public class CepfuRaidStageHistoryRepository extends BaseDynamoRepositoryImpl<CepfuRaidStageHistory>{
	public CepfuRaidStageHistoryRepository(){
		super(CepfuRaidStageHistory.class);
	}

}