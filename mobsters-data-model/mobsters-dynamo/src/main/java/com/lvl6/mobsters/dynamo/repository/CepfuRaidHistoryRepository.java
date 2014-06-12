package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.CepfuRaidHistory;
@Component public class CepfuRaidHistoryRepository extends BaseDynamoRepositoryImpl<CepfuRaidHistory>{
	public CepfuRaidHistoryRepository(){
		super(CepfuRaidHistory.class);
	}

}