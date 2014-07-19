package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.CepfuRaidHistory;
@Component public abstract class CepfuRaidHistoryRepository extends BaseDynamoItemRepositoryImpl<CepfuRaidHistory>{
	public CepfuRaidHistoryRepository(){
		super(CepfuRaidHistory.class);
	}

}