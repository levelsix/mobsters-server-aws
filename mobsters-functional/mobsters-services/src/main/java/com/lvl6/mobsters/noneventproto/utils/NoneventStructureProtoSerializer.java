package com.lvl6.mobsters.noneventproto.utils;

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
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.CoordinateProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.HospitalProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.LabProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.MiniJobCenterProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.MinimumObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ObstacleProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ResidenceProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ResourceGeneratorProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.ResourceStorageProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructureInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TownHallProto;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.TutorialStructProto;

public interface NoneventStructureProtoSerializer
{

	public abstract StructureInfoProto createStructureInfoProtoFromStructure( IStructure s );

	public abstract ResourceGeneratorProto createResourceGeneratorProto(
	      StructureResourceGenerator srg);
	
	public abstract ResourceStorageProto createResourceStorageProto(
	      StructureResourceStorage srs);

	public abstract HospitalProto createHospitalProto(StructureHospital sh);
	
	public abstract ResidenceProto createResidenceProto(StructureResidence sr);
	
	public abstract TownHallProto createTownHallProto(StructureTownHall sth);

	public abstract LabProto createLabProto( StructureLab sl );

	public abstract MiniJobCenterProto createMiniJobCenterProto( StructureMiniJob smj );
	
	public abstract ObstacleProto createObstacleProto( Obstacle o );
	
	public abstract MinimumObstacleProto createMinimumObstacleProto(
		int obstacleId,
		float posX,
		float posY,
		int orientation );

	public abstract CoordinateProto createCoordinateProtoFromCoordinatePair( CoordinatePair cp );

	public abstract TutorialStructProto createTutorialStructProto( int structId, float posX, float posY );
	
}
