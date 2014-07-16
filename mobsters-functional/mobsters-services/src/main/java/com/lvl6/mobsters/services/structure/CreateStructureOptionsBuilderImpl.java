package com.lvl6.mobsters.services.structure;

import java.util.Date;

import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.services.common.TimeUtils;
import com.lvl6.mobsters.services.structure.StructureService.ConstructionStatusKind;
import com.lvl6.mobsters.services.structure.StructureService.CreateStructureOptionsBuilder;

class CreateStructureOptionsBuilderImpl implements CreateStructureOptionsBuilder {
	private StructureForUser retVal;
	private ConstructionStatusKind constructionStatus;
	private int fbInviteStructLvl;

	public CreateStructureOptionsBuilderImpl(
		int structureId, float xCoord, float yCoord
	) {
		retVal = new StructureForUser();
//		this.structureId = structureId;
//		this.xCoord = xCoord;
//		this.yCoord = yCoord;
//		this.purchaseTime = TimeUtils.createNow();
		this.constructionStatus = ConstructionStatusKind.INCOMPLETE;
		this.fbInviteStructLvl = fbInviteStructLvl;
	}

	int getFbInviteStructLvl() {
		return fbInviteStructLvl;
	}
	
	@Override
	public CreateStructureOptionsBuilder purchaseTime( final Date purchaseTime ) 
	{
		retVal.setPurchaseTime(purchaseTime);
		return this;
	}

	@Override
	public CreateStructureOptionsBuilder constructionStatus(
			ConstructionStatusKind constructionStatus)
	{
		this.constructionStatus = constructionStatus;
		return this;
	}

	@Override
	public CreateStructureOptionsBuilder fbInviteStructLvl(int fbInviteStructLvl) {
		retVal.setFbInviteStructLvl(fbInviteStructLvl);
		return this;
	}
	
//	public void build(CreateStructureCollectionCallBuilder delegate) {
//		delegate.addStructure(
//			structureId, xCoord, yCoord, purchaseTime, fbInviteStructLvl, 
//			new Director<CreateStructureOptionsBuilder>() {
//				public void apply(CreateStructureOptionsBuilder bldr) {
//					bldr.lastRetrievedTime(lastRetrievedTime);
//				}
//			}
//		);
//	}

	StructureForUser build() {
		// TODO: Parse constructionStatus.
		return retVal;
	}
}