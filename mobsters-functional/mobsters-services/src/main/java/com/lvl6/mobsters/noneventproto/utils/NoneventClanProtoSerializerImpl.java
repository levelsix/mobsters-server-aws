package com.lvl6.mobsters.noneventproto.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.FullUserClanProto;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;

@Component
public class NoneventClanProtoSerializerImpl implements NoneventClanProtoSerializer
{
	private static final Logger LOG =
		LoggerFactory.getLogger(NoneventClanProtoSerializerImpl.class);

	@Override
	public FullUserClanProto createFullUserClanProtoFromUserClan( ClanForUser cfu )
	{
		FullUserClanProto.Builder fucpb = FullUserClanProto.newBuilder();
		fucpb.setClanUuid(cfu.getClanId());
		fucpb.setUserUuid(cfu.getUserId());
		String userClanStatus = cfu.getStatus();

		try {
			UserClanStatus ucs = UserClanStatus.valueOf(userClanStatus);
			fucpb.setStatus(ucs);
		} catch (Throwable e) {
			LOG.error(
				String.format("incorrect user clan status. userClan=%s", cfu),
				e
			);
		}

		Date aTime = cfu.getRequestTime();
		if (null != aTime) {
			fucpb.setRequestTime(aTime.getTime());
		}

		return fucpb.build();
	}
}
