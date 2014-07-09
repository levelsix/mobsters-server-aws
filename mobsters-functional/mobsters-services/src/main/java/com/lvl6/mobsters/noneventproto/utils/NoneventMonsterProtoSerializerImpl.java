package com.lvl6.mobsters.noneventproto.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.IMonsterLevelInfo;
import com.lvl6.mobsters.info.MonsterBattleDialogue;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Element;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Quality;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterBattleDialogueProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterBattleDialogueProto.DialogueType;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterLevelInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.MonsterProto.AnimationType;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserEnhancementItemProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserEnhancementProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterEvolutionProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterHealingProto;

public class NoneventMonsterProtoSerializerImpl implements NoneventMonsterProtoSerializer 
{

	private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());

	@Override
	public MonsterProto createMonsterProto(IMonster aMonster) {
		MonsterProto.Builder mpb = MonsterProto.newBuilder();
		
		mpb.setMonsterId(aMonster.getId());
	    String aStr = aMonster.getEvolutionGroup(); 
	    if (null != aStr) {
	    	mpb.setEvolutionGroup(aStr);
	    } else {
	    	log.error("monster has no evolutionGroup, aMonster=" + aMonster);
	    }
	    aStr = aMonster.getMonsterGroup();
	    if (null != aStr) {
	      mpb.setMonsterGroup(aStr);
	    }
	    
	    String monsterQuality = aMonster.getQuality(); 
	    try {
	    	Quality mq = Quality.valueOf(monsterQuality);
	    	mpb.setQuality(mq);
	    } catch (Exception e) {
	    	log.error("invalid monster quality. monster=" + aMonster);
	    }
	    
	    mpb.setEvolutionLevel(aMonster.getEvolutionLevel());
	    aStr = aMonster.getDisplayName(); 
	    if (null != aStr) {
	      mpb.setDisplayName(aStr);
	    }

	    String monsterElement = aMonster.getElement();
	    try {
	    	Element me = Element.valueOf(monsterElement);
	    	mpb.setMonsterElement(me);
	    } catch (Exception e){
	      log.error("invalid monster element. monster=" + aMonster);
	    }
	    
	    aStr = aMonster.getImagePrefix(); 
	    if (null != aStr) {
	      mpb.setImagePrefix(aStr);
	    }
	    
	    mpb.setNumPuzzlePieces(aMonster.getNumPuzzlePieces());
	    mpb.setMinutesToCombinePieces(aMonster.getMinutesToCombinePieces());
	    mpb.setMaxLevel(aMonster.getMaxLevel());

	    IMonster evol = aMonster.getEvolutionMonster();
	    if (null != evol) {
	      mpb.setEvolutionMonsterId(evol.getId());
	    }
	    aStr = aMonster.getCarrotRecruited();
	    if (null != aStr) {
	      mpb.setCarrotRecruited(aStr);
	    }
	    aStr = aMonster.getCarrotDefeated();
	    if (null != aStr) {
	      mpb.setCarrotDefeated(aStr);
	    }
	    aStr = aMonster.getCarrotEvolved();
	    if (null != aStr) {
	      mpb.setCarrotEvolved(aStr);
	    }
	    aStr = aMonster.getDescription();
	    if (null != aStr) {
	      mpb.setDescription(aStr);
	    }

	    IMonster evolutionCatalystMonster = aMonster.getEvolutionCatalystMonster();
	    if (null != evolutionCatalystMonster) {
	    	mpb.setEvolutionCatalystMonsterId(evolutionCatalystMonster.getId());
	    }
	    
	    mpb.setMinutesToEvolve(aMonster.getMinutesToEvolve());
	    mpb.setNumCatalystMonstersRequired(aMonster.getNumCatalystsRequired());

	    Collection<MonsterLevelInfoProto> lvlInfoProtos = createMonsterLevelInfoFromInfo(aMonster.getLvlInfo());
	    mpb.addAllLvlInfo(lvlInfoProtos);

	    mpb.setEvolutionCost(aMonster.getEvolutionCost());

	    aStr = aMonster.getAnimationType();
	    try {
	      AnimationType typ = AnimationType.valueOf(aStr);
	      mpb.setAttackAnimationType(typ);
	    } catch (Exception e) {
	      log.error("invalid animation type for monster. type=" + aStr + "\t monster=" + aMonster, e);
	    }
	    
	    mpb.setVerticalPixelOffset(aMonster.getVerticalPixelOffset());
	    
	    String atkSoundFile = aMonster.getAtkSoundFile();
	    if (null != atkSoundFile) {
	    	mpb.setAtkSoundFile(atkSoundFile);
	    }
	    
	    mpb.setAtkSoundAnimationFrame(aMonster.getAtkSoundAnimationFrame());
	    
	    mpb.setAtkAnimationRepeatedFramesStart(aMonster.getAtkAnimationRepeatedFramesStart());
	    
	    mpb.setAtkAnimationRepeatedFramesEnd(aMonster.getAtkAnimationRepeatedFramesEnd());
	    
	    String shorterName = aMonster.getShorterName();
	    if (null != shorterName) {
	    	mpb.setShorterName(shorterName);
	    }
	    
	    mpb.setShadowScaleFactor(aMonster.getShadowScaleFactor());
	    
		return mpb.build();
	}

	protected Collection<MonsterLevelInfoProto> createMonsterLevelInfoFromInfo(
		List<IMonsterLevelInfo> info)
	{
		// TODO: Figure out a way to not do explicit sorting every time this method is called
		TreeSet<MonsterLevelInfoProto> ascendingLvlInfo = new TreeSet<MonsterLevelInfoProto>(
			new Comparator<MonsterLevelInfoProto>() {

				@Override
				public int compare( MonsterLevelInfoProto o1, MonsterLevelInfoProto o2 )
				{
					if (o1.getLvl() < o2.getLvl()) {
						return -1;
					} else if (o1.getLvl() > o2.getLvl()) {
						return 1;
					} else {
						return 0;
					}
				}

			}
		);

		for (IMonsterLevelInfo mli : info) {
			MonsterLevelInfoProto mlip = createMonsterLevelInfoProto(mli);
			ascendingLvlInfo.add(mlip);
		}

		return ascendingLvlInfo;
	}
	
	@Override
	public MonsterLevelInfoProto createMonsterLevelInfoProto(IMonsterLevelInfo mli) {
		MonsterLevelInfoProto.Builder mlipb = MonsterLevelInfoProto.newBuilder();
		mlipb.setLvl(mli.getLevel());
		mlipb.setHp(mli.getHp());
		mlipb.setCurLvlRequiredExp(mli.getCurLvlRequiredExp());
		mlipb.setFeederExp(mli.getFeederExp());
		mlipb.setFireDmg(mli.getFireDmg());
		mlipb.setGrassDmg(mli.getGrassDmg());
		mlipb.setWaterDmg(mli.getWaterDmg());
		mlipb.setLightningDmg(mli.getLightningDmg());
		mlipb.setDarknessDmg(mli.getDarknessDmg());
		mlipb.setRockDmg(mli.getRockDmg());
		mlipb.setSpeed(mli.getSpeed());
		mlipb.setHpExponentBase(mli.getHpExponentBase());
		mlipb.setDmgExponentBase(mli.getDmgExponentBase());
		mlipb.setExpLvlDivisor(mli.getExpLvlDivisor());
		mlipb.setExpLvlExponent(mli.getExpLvlExponent());
		mlipb.setSellAmount(mli.getSellAmount());

		return mlipb.build();
	}
	

	@Override
	public MonsterBattleDialogueProto createMonsterBattleDialogueProto(MonsterBattleDialogue mbd) {
	    MonsterBattleDialogueProto.Builder mbdpb = MonsterBattleDialogueProto.newBuilder();
	    mbdpb.setMonsterId(mbd.getMonster().getId());

	    String aStr = mbd.getDialogueType();
	    try {
	      DialogueType type = DialogueType.valueOf(aStr);
	      mbdpb.setDialogueType(type);
	    } catch (Exception e) {
	      log.error("could not create DialogueType enum", e);
	    }

	    mbdpb.setDialogue(mbd.getDialogue());
	    mbdpb.setProbabilityUttered(mbd.getProbabilityUttered());

	    return mbdpb.build();
	  }
	
	//BEGIN USER DATA SERIALIZATION
	
	@Override
	public FullUserMonsterProto createFullUserMonsterProtoFromUserMonster(MonsterForUser mfu) {
		FullUserMonsterProto.Builder fumpb = FullUserMonsterProto.newBuilder();
	    fumpb.setUserMonsterUuid(mfu.getMonsterForUserUuid());
	    fumpb.setUserUuid(mfu.getUserId());
	    fumpb.setMonsterId(mfu.getMonsterId());
	    fumpb.setCurrentExp(mfu.getCurrentExp());
	    fumpb.setCurrentLvl(mfu.getCurrentLvl());
	    fumpb.setCurrentHealth(mfu.getCurrentHealth());
	    fumpb.setNumPieces(mfu.getNumPieces());
	    fumpb.setIsComplete(mfu.isComplete());

	    Date combineStartTime = mfu.getCombineStartTime();
	    if (null != combineStartTime) {
	      fumpb.setCombineStartTime(combineStartTime.getTime());
	    }

	    fumpb.setTeamSlotNum(mfu.getTeamSlotNum());
	    return fumpb.build();
	}
	
	@Override
	public UserMonsterHealingProto createUserMonsterHealingProto(MonsterHealingForUser mhfu) {
		UserMonsterHealingProto.Builder umhpb = UserMonsterHealingProto.newBuilder();
	    umhpb.setUserUuid(mhfu.getUserId());
	    umhpb.setUserMonsterUuid(mhfu.getMonsterForUserId());

	    Date aDate = mhfu.getQueuedTime();
	    if (null != aDate) {
	      umhpb.setQueuedTimeMillis(aDate.getTime());
	    }

	    //  	umhpb.setUserHospitalStructId(mhfu.getUserStructHospitalId());
	    umhpb.setHealthProgress(mhfu.getHealthProgress());
	    umhpb.setPriority(mhfu.getPriority());

	    return umhpb.build();
	}
	
	@Override
	public UserEnhancementItemProto createUserEnhancementItemProto(
		MonsterEnhancingForUser mefu)
	{
		UserEnhancementItemProto.Builder ueipb = UserEnhancementItemProto.newBuilder();
	    ueipb.setUserMonsterUuid(mefu.getMonsterForUserId());

	    Date startTime = mefu.getExpectedStartTime();
	    if (null != startTime) {
	      ueipb.setExpectedStartTimeMillis(startTime.getTime());
	    }

	    ueipb.setEnhancingCost(mefu.getEnhancingCost());

	    return ueipb.build();
	}
	
	@Override
	public UserEnhancementProto createUserEnhancementProto(
		String userId,
		UserEnhancementItemProto baseMonster,
		List<UserEnhancementItemProto> feeders )
	{

		UserEnhancementProto.Builder uepb = UserEnhancementProto.newBuilder();

		uepb.setUserUuid(userId);
		uepb.setBaseMonster(baseMonster);
		uepb.addAllFeeders(feeders);

		return uepb.build();
	}

	@Override
	public UserMonsterEvolutionProto createUserEvolutionProtoFromEvolution(
		MonsterEvolvingForUser mefu )
	{
		UserMonsterEvolutionProto.Builder uepb = UserMonsterEvolutionProto.newBuilder();

	    String catalystUserMonsterId = mefu.getCatalystMonsterForUserId();
	    String one = mefu.getMonsterForUserIdOne();
	    String two = mefu.getMonsterForUserIdTwo();
	    Date startTime = mefu.getStartTime();

	    uepb.setCatalystUserMonsterUuid(catalystUserMonsterId);

	    long startTimeMillis = startTime.getTime();
	    uepb.setStartTime(startTimeMillis);

	    List<String> userMonsterIds = new ArrayList<String>();
	    userMonsterIds.add(one);
	    userMonsterIds.add(two);
	    uepb.addAllUserMonsterUuids(userMonsterIds);

	    return uepb.build();
	}
	
}
