package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.CepfuRaidStageHistory;
@Component public class CepfuRaidStageHistoryRepository extends BaseDynamoRepository<CepfuRaidStageHistory>{
	public CepfuRaidStageHistoryRepository(){
		super(CepfuRaidStageHistory.class);
	}

}