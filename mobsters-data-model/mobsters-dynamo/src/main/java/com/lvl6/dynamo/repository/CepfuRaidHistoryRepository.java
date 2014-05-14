package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.CepfuRaidHistory;
@Component public class CepfuRaidHistoryRepository extends BaseDynamoRepository<CepfuRaidHistory>{
	public CepfuRaidHistoryRepository(){
		super(CepfuRaidHistory.class);
	}

}