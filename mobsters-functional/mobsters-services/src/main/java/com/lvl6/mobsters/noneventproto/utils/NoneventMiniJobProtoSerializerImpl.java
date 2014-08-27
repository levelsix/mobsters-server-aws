package com.lvl6.mobsters.noneventproto.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.info.MiniJob;
import com.lvl6.mobsters.info.repository.MiniJobRepository;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Quality;
import com.lvl6.mobsters.noneventproto.NoneventMiniJobProto.MiniJobProto;
import com.lvl6.mobsters.noneventproto.NoneventMiniJobProto.UserMiniJobProto;

@Component
public class NoneventMiniJobProtoSerializerImpl implements NoneventMiniJobProtoSerializer 
{

	private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());
	
	@Autowired
	protected MiniJobRepository miniJobRepository;
	
	@Override
	public MiniJobProto createMiniJobProto(MiniJob mj) {
		MiniJobProto.Builder mjpb = MiniJobProto.newBuilder();

		mjpb.setMiniJobId(mj.getId());
		mjpb.setRequiredStructId(mj.getRequiredStruct().getId());

		String str = mj.getName();
		if (null != str) {
			mjpb.setName(str);
		}

		mjpb.setCashReward(mj.getCashReward());
		mjpb.setOilReward(mj.getOilReward());
		mjpb.setGemReward(mj.getGemReward());
		mjpb.setMonsterIdReward(mj.getMonsterReward().getId());

		str = mj.getQuality();
		if (null != str) {
			try {
				Quality q = Quality.valueOf(str);
				mjpb.setQuality(q);
			} catch(Exception e) {
				log.error("invalid quality. MiniJob=" + mj);
			}
		}

		mjpb.setMaxNumMonstersAllowed(mj.getMaxNumMonstersAllowed());
		mjpb.setChanceToAppear(mj.getChanceToAppear());
		mjpb.setHpRequired(mj.getHpRequired());
		mjpb.setAtkRequired(mj.getAtkRequired());
		mjpb.setMinDmgDealt(mj.getMinDmgDealt());
		mjpb.setMaxDmgDealt(mj.getMaxDmgDealt());
		mjpb.setDurationMaxMinutes(mj.getDurationMaxMinutes());
		mjpb.setDurationMinMinutes(mj.getDurationMinMinutes());

		return mjpb.build();
	}

	
	//BEGIN USER DATA SERIALIZATION
	

	protected UserMiniJobProto createUserMiniJobProto(
		MiniJobForUser mjfu, MiniJob mj) {
		UserMiniJobProto.Builder umjpb = UserMiniJobProto.newBuilder();

		umjpb.setUserMiniJobUuid(mjfu.getMiniJobForUserId());
		umjpb.setBaseDmgReceived(mjfu.getBaseDmgReceived());
		umjpb.setDurationMinutes(mjfu.getDurationMinutes());
		Date time = mjfu.getTimeStarted();
		if (null != time) {
			umjpb.setTimeStarted(time.getTime());
		}

		Set<String> userMonsterIds = mjfu.getUserMonsterIds();
		if (null != userMonsterIds) {
			umjpb.addAllUserMonsterUuids(userMonsterIds);
		}

		time = mjfu.getTimeCompleted();
		if (null != time) {
			umjpb.setTimeCompleted(time.getTime());
		}

		MiniJobProto mjp = createMiniJobProto(mj);
		umjpb.setMiniJob(mjp);

		return umjpb.build();
	} 
	
	@Override
	public List<UserMiniJobProto> createUserMiniJobProtos(
		List<MiniJobForUser> mjfuList, Map<Integer,
		MiniJob> miniJobIdToMiniJob) {
		List<UserMiniJobProto> umjpList = new ArrayList<UserMiniJobProto>();

		for (MiniJobForUser mjfu : mjfuList) {
			int miniJobId = mjfu.getMiniJobId();

			MiniJob mj = null;
			if (null == miniJobIdToMiniJob ||
				!miniJobIdToMiniJob.containsKey(miniJobId)) {
				mj = miniJobRepository.findOne(miniJobId);
			} else {
				mj = miniJobIdToMiniJob.get(miniJobId);
			}

			UserMiniJobProto umjp = createUserMiniJobProto(mjfu, mj);
			umjpList.add(umjp);
		}

		return umjpList;
	}


	public MiniJobRepository getMiniJobRepository()
	{
		return miniJobRepository;
	}


	public void setMiniJobRepository( MiniJobRepository miniJobRepository )
	{
		this.miniJobRepository = miniJobRepository;
	}
	
}
