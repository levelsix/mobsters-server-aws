package com.lvl6.mobsters.services.structure;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository;
import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.info.Structure;
import com.lvl6.mobsters.info.repository.StructureRepository;
import com.lvl6.mobsters.services.structure.StructureService.ConstructionStatusKind;
import com.lvl6.mobsters.services.structure.StructureService.CreateStructureCollectionBuilder;
import com.lvl6.mobsters.services.structure.StructureService.CreateStructureOptionsBuilder;

@Component
public class StructureServiceImpl implements StructureService {
	private static final Logger LOG = LoggerFactory.getLogger(StructureServiceImpl.class);

	@Autowired
	private ObstacleForUserRepository obstacleForUserRepository;

	@Autowired
	private StructureForUserRepository structureForUserRepository;

	@Autowired
	private DataServiceTxManager txManager;

	@Autowired
	private StructureRepository structureRepository;

	/* BEGIN NON CRUD LOGIC ***************************************************/
	/* END NON CRUD LOGIC *****************************************************/
	/* BEGIN READ ONLY LOGIC **************************************************/
	/* END READ ONLY LOGIC ****************************************************/
	/* BEGIN TRANSACTIONAL LOGIC **********************************************/

	/**************************************************************************/

	@Override
	public void beginUpgradingUserStruct(final StructureForUser sfu,
			final Date upgradeTime) {
		final Structure currentStruct = structureRepository.findOne(sfu
				.getStructId());
		final int nextLevelStructId = currentStruct.getSuccessorStruct()
				.getId();

		sfu.setStructId(nextLevelStructId);
		sfu.setPurchaseTime(upgradeTime);
		sfu.setComplete(false);
		structureForUserRepository.save(sfu);
	}

	/**************************************************************************/
	
	@Override
	public void createObstaclesForUser(final CreateObstaclesReplyBuilder replyBuilder, 
		final String userId, final Director<CreateObstacleCollectionBuilder> director)
	{
		final CreateObstacleCollectionBuilderImpl builder = 
			new CreateObstacleCollectionBuilderImpl(userId);
		director.apply(builder);

		final boolean success = false;
		txManager.requireTransaction();
		try {
			// save whatever the callback created through the builder
			obstacleForUserRepository.saveEach(builder.build());
		} finally {
			if (success) {
				txManager.commit();
				replyBuilder.resultOk();
			} else {
				txManager.rollback();
			}
		}
	}

	static class CreateObstacleCollectionBuilderImpl 
		implements CreateObstacleCollectionBuilder 
	{
		// the end state: objects to be saved to db
		private final ArrayList<ObstacleForUser> retVal;
		private final String userId;

		CreateObstacleCollectionBuilderImpl(final String userId) {
			this.userId = userId;
			this.retVal = new ArrayList<ObstacleForUser>();
		}

		@Override
		public CreateObstacleCollectionBuilder addStructure(
				final int obstacleId, final float xCoord, final float yCoord,
				final String orientation) {
			return this;
		}

		ArrayList<ObstacleForUser> build() {
			return retVal;
		}
	}

	/**************************************************************************/

	@Override
	public void createStructuresForUser(final CreateStructuresReplyBuilder replyBuilder,
		final String userId, final Director<CreateStructureCollectionBuilder> director) {
		final CreateStructureCollectionBuilderImpl builder = 
			new CreateStructureCollectionBuilderImpl(userId);
		director.apply(builder);

		final boolean success = false;
		txManager.requireTransaction();
		try {
			// save whatever the callback created through the builder
			structureForUserRepository.saveEach(builder.build());
		} finally {
			if (success) {
				txManager.commit();
				replyBuilder.resultOk();
			} else {
				txManager.rollback();
			}
		}
	}
	class CreateStructureCollectionBuilderImpl implements CreateStructureCollectionBuilder 
	{
		private final ArrayList<CreateStructureOptionsBuilderImpl> builders = 
			new ArrayList<CreateStructureOptionsBuilderImpl>();
		private final String userId;

		CreateStructureCollectionBuilderImpl(final String userId)
		{
			this.userId = userId;
		}
		
		@Override
		public CreateStructureCollectionBuilder addStructure(
			final int structureId, final float xCoord, final float yCoord)
		{
			builders.add(
				new CreateStructureOptionsBuilderImpl(userId, structureId, xCoord, yCoord)
			);
			return this;
		}

		@Override
		public CreateStructureCollectionBuilder addStructure(
			int structureId, float xCoord, float yCoord, 
			Director<CreateStructureOptionsBuilder> director)
		{
			final CreateStructureOptionsBuilderImpl retVal = 
				new CreateStructureOptionsBuilderImpl(userId, structureId, xCoord, yCoord);
			director.apply(retVal);
			builders.add(retVal);
			return this;
		}
		
		ArrayList<StructureForUser> build()
		{
			// TODO
			return null;
		}
	}
	
	class CreateStructureOptionsBuilderImpl 
		implements CreateStructureOptionsBuilder
	{
		private StructureForUser retVal;
		private ConstructionStatusKind constructionStatus;
		private int fbInviteStructLvl;

		CreateStructureOptionsBuilderImpl(
			final String userUuid, final int structureId, final float xCoord, final float yCoord)
		{
			retVal = new StructureForUser();
//			this.userUuid = userUuid;
//			this.structureId = structureId;
//			this.xCoord = xCoord;
//			this.yCoord = yCoord;
//			this.purchaseTime = TimeUtils.createNow();
			this.constructionStatus = ConstructionStatusKind.INCOMPLETE;
			this.fbInviteStructLvl = fbInviteStructLvl;
		}
		
		@Override
		public CreateStructureOptionsBuilder purchaseTime( final Date purchaseTime ) 
		{
			retVal.setPurchaseTime(purchaseTime);
			return this;
		}

		@Override
		public CreateStructureOptionsBuilder constructionStatus(
			final ConstructionStatusKind constructionStatus)
		{
			this.constructionStatus = constructionStatus;
			return this;
		}

		@Override
		public CreateStructureOptionsBuilder fbInviteStructLvl(int fbInviteStructLvl)
		{
			retVal.setFbInviteStructLvl(fbInviteStructLvl);
			return this;
		}
		
		StructureForUser build()
		{
			return retVal;
		}
	}
	
	/**************************************************************************/

	/* END TRANSACTIONAL LOGIC ************************************************/
	/* BEGIN DEPENDENCY INJECTION *********************************************/

	public StructureRepository getStructureRepository() 
	{
		return structureRepository;
	}

	// for the dependency injection
	void setObstacleForUserRepository(
		final ObstacleForUserRepository obstacleForUserRepository) 
	{
		this.obstacleForUserRepository = obstacleForUserRepository;
	}

	void setStructureForUserRepository(
		final StructureForUserRepository structureForUserRepository) 
	{
		this.structureForUserRepository = structureForUserRepository;
	}

	public void setStructureRepository(
		final StructureRepository structureRepository) 
	{
		this.structureRepository = structureRepository;
	}

	/* END DEPENDENCY INJECTION *******************************************/
}