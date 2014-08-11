package com.lvl6.mobsters.noneventproto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.info.CoordinatePair;
import com.lvl6.mobsters.info.IStructure;
import com.lvl6.mobsters.info.Obstacle;
import com.lvl6.mobsters.info.StructureHospital;
import com.lvl6.mobsters.info.StructureLab;
import com.lvl6.mobsters.info.StructureMiniJob;
import com.lvl6.mobsters.info.StructureResidence;
import com.lvl6.mobsters.info.StructureResourceGenerator;
import com.lvl6.mobsters.info.StructureResourceStorage;
import com.lvl6.mobsters.info.StructureTownHall;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.ResourceType;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.CoordinateProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.HospitalProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.LabProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.MiniJobCenterProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.MinimumObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ResidenceProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ResourceGeneratorProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ResourceStorageProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructOrientation;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructureInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructureInfoProto.StructType;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TownHallProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TutorialStructProto;

public class NoneventStructureProtoSerializerImpl implements NoneventStructureProtoSerializer 
{

	private static Logger LOG = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());

	@Override
	public StructureInfoProto createStructureInfoProtoFromStructure(IStructure s) {
		StructureInfoProto.Builder builder = StructureInfoProto.newBuilder();
		builder.setStructId(s.getId());

		String aStr = s.getName();
		if (null != aStr) {
			builder.setName(s.getName());
		}

		builder.setLevel(s.getLevel());
		aStr = s.getStructType();
		try {
			StructType st = StructType.valueOf(aStr);
			builder.setStructType(st);
		} catch (Exception e) {
			LOG.error("can't create enum type. structType=" + aStr + ".\t structure=" + s);
		}

		aStr = s.getBuildResourceType();
		try {
			ResourceType rt = ResourceType.valueOf(aStr);
			builder.setBuildResourceType(rt);
		} catch (Exception e) {
			LOG.error("can't create enum type. resourceType=" + aStr + ". structure=" + s);
		}

		builder.setBuildCost(s.getBuildCost());
		builder.setMinutesToBuild(s.getMinutesToBuild());
		builder.setPrerequisiteTownHallLvl(s.getRequiredTownHallLvl());
		builder.setWidth(s.getWidth());
		builder.setHeight(s.getHeight());

		IStructure predecessorStruct = s.getPredecessorStruct(); 
		if (null != predecessorStruct) {
			builder.setPredecessorStructId(predecessorStruct.getId());
		}
		IStructure successorStruct = s.getSuccessorStruct();
		if (null != successorStruct) {
			builder.setSuccessorStructId(successorStruct.getId());
		}

		aStr = s.getImgName();
		if (null != aStr) {
			builder.setImgName(aStr);
		}

		builder.setImgVerticalPixelOffset(s.getImgVerticalPixelOffset());
		builder.setImgHorizontalPixelOffset(s.getImgHorizontalPixelOffset());

		aStr = s.getDescription();
		if (null != aStr) {
			builder.setDescription(aStr);
		}

		aStr = s.getShortDescription();
		if (null != aStr) {
			builder.setShortDescription(aStr);
		}

		aStr = s.getShadowImgName();
		if (null != aStr) {
			builder.setShadowImgName(aStr);
		}

		builder.setShadowVerticalOffset(s.getShadowVerticalOffset());
		builder.setShadowHorizontalOfffset(s.getShadowHorizontalOffset());
		builder.setShadowScale(s.getShadowScale());

		return builder.build();
	}

	@Override
	public ResourceGeneratorProto createResourceGeneratorProto( StructureResourceGenerator srg )
	{
		StructureInfoProto sip = createStructureInfoProtoFromStructure(srg.getStruct());
		
		ResourceGeneratorProto.Builder rgpb = ResourceGeneratorProto.newBuilder();
		rgpb.setStructInfo(sip);

		String aStr = srg.getResourceTypeGenerated();
		try {
			ResourceType rt = ResourceType.valueOf(aStr);
			rgpb.setResourceType(rt);
		} catch (Exception e) {
			LOG.error("can't create enum type. resourceType=" + aStr +
				". resourceGenerator=" + srg);
		}

		rgpb.setProductionRate(srg.getProductionRate());
		rgpb.setCapacity(srg.getCapacity());

		return rgpb.build();
	}

	@Override
	public ResourceStorageProto createResourceStorageProto( StructureResourceStorage srs )
	{
		StructureInfoProto sip = createStructureInfoProtoFromStructure(srs.getStruct());

		ResourceStorageProto.Builder rspb = ResourceStorageProto.newBuilder();
		rspb.setStructInfo(sip);

		String aStr = srs.getResourceTypeStored();
		try {
			ResourceType rt = ResourceType.valueOf(aStr);
			rspb.setResourceType(rt);
		} catch (Exception e) {
			LOG.error("can't create enum type. resourceType=" + aStr +
				". resourceStorage=" + srs);
		}

		rspb.setCapacity(srs.getCapacity());

		return rspb.build();
		
	}

	@Override
	public HospitalProto createHospitalProto( StructureHospital sh )
	{
		StructureInfoProto sip = createStructureInfoProtoFromStructure(sh.getStruct());

		HospitalProto.Builder hpb = HospitalProto.newBuilder();
		hpb.setStructInfo(sip);
		hpb.setQueueSize(sh.getQueueSize());
		hpb.setHealthPerSecond(sh.getHealthPerSecond());

		return hpb.build();
	}

	@Override
	public ResidenceProto createResidenceProto( StructureResidence sr )
	{
		StructureInfoProto sip = createStructureInfoProtoFromStructure(sr.getStruct());

	    ResidenceProto.Builder rpb = ResidenceProto.newBuilder();
	    rpb.setStructInfo(sip);
	    rpb.setNumMonsterSlots(sr.getNumMonsterSlots());
	    rpb.setNumBonusMonsterSlots(sr.getNumBonusMonsterSlots());
	    rpb.setNumGemsRequired(sr.getNumGemsRequired());
	    rpb.setNumAcceptedFbInvites(sr.getNumAcceptedFbInvites());
	    String str = sr.getOccupationName();
	    if (null != str) {
	      rpb.setOccupationName(str);
	    }
	    str = sr.getImgSuffix();
	    if (null != str) {
	    	rpb.setImgSuffix(str);
	    }
	    
	    return rpb.build();
	}

	@Override
	public TownHallProto createTownHallProto( StructureTownHall sth )
	{
		StructureInfoProto sip = createStructureInfoProtoFromStructure(sth.getStruct());

		TownHallProto.Builder thpb = TownHallProto.newBuilder();
	    thpb.setStructInfo(sip);
	    thpb.setNumResourceOneGenerators(sth.getNumResourceOneGenerators());
	    thpb.setNumResourceOneStorages(sth.getNumResourceOneStorages());
	    thpb.setNumResourceTwoGenerators(sth.getNumResourceTwoGenerators());
	    thpb.setNumResourceTwoStorages(sth.getNumResourceTwoStorages());
	    thpb.setNumHospitals(sth.getNumHospitals());
	    thpb.setNumResidences(sth.getNumResidences());
	    thpb.setNumMonsterSlots(sth.getNumMonsterSlots());
	    thpb.setNumLabs(sth.getNumLabs());
	    thpb.setPvpQueueCashCost(sth.getPvpQueueCashCost());
	    thpb.setResourceCapacity(sth.getResourceCapacity());

	    return thpb.build();
	}

	@Override
	public LabProto createLabProto( StructureLab sl )
	{
		StructureInfoProto sip = createStructureInfoProtoFromStructure(sl.getStruct());

	    LabProto.Builder lpb = LabProto.newBuilder();
	    lpb.setStructInfo(sip);
	    lpb.setQueueSize(sl.getQueueSize());
	    lpb.setPointsPerSecond(sl.getPointsPerSecond());

	    return lpb.build();
	}

	@Override
	public MiniJobCenterProto createMiniJobCenterProto( StructureMiniJob smj )
	{
		StructureInfoProto sip = createStructureInfoProtoFromStructure(smj.getStruct());

		MiniJobCenterProto.Builder smjcpb = MiniJobCenterProto.newBuilder();
		smjcpb.setStructInfo(sip);
		smjcpb.setGeneratedJobLimit(smj.getGeneratedJobLimit());
		smjcpb.setHoursBetweenJobGeneration(smj.getHoursBetweenJobGeneration());

		return smjcpb.build();
	}

	@Override
	public ObstacleProto createObstacleProto( Obstacle o )
	{
		ObstacleProto.Builder ob = ObstacleProto.newBuilder();
	  	
	  	ob.setObstacleId(o.getId());
	  	String aStr = o.getName();
	  	if (null != aStr) {
	  		ob.setName(aStr);
	  	}
	  	
	  	aStr = o.getRemovalCostType();
	  	try {
	  		ResourceType rt = ResourceType.valueOf(aStr);
	  		ob.setRemovalCostType(rt);
	  	} catch (Exception e) {
	  		LOG.info("incorrect resource type name in db. name=" + aStr, e);
	  	}
	  	
	  	ob.setCost(o.getCost());
	  	ob.setSecondsToRemove(o.getSecondsToRemove());
	  	ob.setWidth(o.getWidth());
	  	ob.setHeight(o.getHeight());
	  	
	  	aStr = o.getImgName();
	  	if (null != aStr) {
	  		ob.setImgName(aStr);
	  	}
	  	
	  	ob.setImgVerticalPixelOffset(o.getImgVerticalPixelOffset());
	  	
	  	aStr = o.getDescription();
	  	if (null != aStr) {
	  		ob.setDescription(aStr);
	  	}
	  	ob.setChanceToAppear(o.getChanceToAppear());
	  	
	  	aStr = o.getShadowImgName();
	  	if (null != aStr) {
	  		ob.setShadowImgName(o.getShadowImgName());
	  	}
	  	
	  	ob.setShadowVerticalOffset(o.getShadowVerticalOffset());
	  	ob.setShadowHorizontalOfffset(o.getShadowHorizontalOffset());
	  	
	  	return ob.build();
	}

	@Override
	public MinimumObstacleProto createMinimumObstacleProto(int obstacleId,
		float posX, float posY, int orientation) {

		MinimumObstacleProto.Builder mopb = MinimumObstacleProto.newBuilder();
		mopb.setObstacleId(obstacleId);

		CoordinatePair cp = new CoordinatePair(posX, posY);
		CoordinateProto cProto = createCoordinateProtoFromCoordinatePair(cp); 
		mopb.setCoordinate(cProto);

		try {
			StructOrientation structOrientation = StructOrientation.valueOf(orientation);
			mopb.setOrientation(structOrientation);
		} catch (Exception e) {
			LOG.info("incorrect struct orientation. obstacleId=" + obstacleId
				+ ", posX=" + posX + ", posY=" + posY + ", orientation" + orientation);
		}

		return mopb.build();
	}
	
	@Override
	public CoordinateProto createCoordinateProtoFromCoordinatePair(CoordinatePair cp) {
		CoordinateProto.Builder cpb = CoordinateProto.newBuilder();
		cpb.setX(cp.getX());
		cpb.setY(cp.getY());

		return cpb.build();
	}
	
	@Override
	public TutorialStructProto createTutorialStructProto(int structId, float posX,
	      float posY)
	{
		TutorialStructProto.Builder tspb = TutorialStructProto.newBuilder();

		tspb.setStructId(structId);
		CoordinatePair cp = new CoordinatePair(posX, posY);
		CoordinateProto cpp = createCoordinateProtoFromCoordinatePair(cp);
		tspb.setCoordinate(cpp);
		return tspb.build();
	}
	
}
