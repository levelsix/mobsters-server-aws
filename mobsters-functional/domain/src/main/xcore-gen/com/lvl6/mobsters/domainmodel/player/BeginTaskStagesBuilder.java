/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Begin Task Stages Builder</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getBeginTaskStagesBuilder()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface BeginTaskStagesBuilder extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model unique="false" stageNumUnique="false" stageMetaDataType="com.lvl6.mobsters.domainmodel.player.ITaskStage" stageMetaUnique="false" stageMonsterDataType="com.lvl6.mobsters.domainmodel.player.ITaskStageMonster" stageMonsterUnique="false" cashRewardUnique="false" oilRewardUnique="false" expRewardUnique="false" rewardsMonsterPieceUnique="false" droppedItemDataType="com.lvl6.mobsters.domainmodel.player.IItem" droppedItemUnique="false"
	 * @generated
	 */
	BeginTaskStagesBuilder addStage(int stageNum, ITaskStage stageMeta,
			ITaskStageMonster stageMonster, int cashReward, int oilReward,
			int expReward, boolean rewardsMonsterPiece, IItem droppedItem);

} // BeginTaskStagesBuilder
