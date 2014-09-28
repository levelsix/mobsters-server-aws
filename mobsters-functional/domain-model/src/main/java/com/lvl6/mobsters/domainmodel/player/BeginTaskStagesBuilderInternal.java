/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Begin Task Stages Builder Internal</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getNewArtifact
 * <em>New Artifact</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getPlayer
 * <em>Player</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getBeginTaskStagesBuilderInternal()
 * @model
 * @generated
 */
public interface BeginTaskStagesBuilderInternal extends BeginTaskStagesBuilder {
	/**
	 * Returns the value of the '<em><b>New Artifact</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Artifact</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>New Artifact</em>' reference.
	 * @see #setNewArtifact(OngoingTaskInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getBeginTaskStagesBuilderInternal_NewArtifact()
	 * @model
	 * @generated
	 */
	OngoingTaskInternal getNewArtifact();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getNewArtifact
	 * <em>New Artifact</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>New Artifact</em>' reference.
	 * @see #getNewArtifact()
	 * @generated
	 */
	void setNewArtifact(OngoingTaskInternal value);

	/**
	 * Returns the value of the '<em><b>Player</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Player</em>' reference.
	 * @see #setPlayer(PlayerInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getBeginTaskStagesBuilderInternal_Player()
	 * @model
	 * @generated
	 */
	PlayerInternal getPlayer();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getPlayer
	 * <em>Player</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Player</em>' reference.
	 * @see #getPlayer()
	 * @generated
	 */
	void setPlayer(PlayerInternal value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model taskMetaDataType="com.lvl6.mobsters.domainmodel.player.ITask"
	 *        taskMetaUnique="false" playerUnique="false" annotation=
	 *        "http://www.eclipse.org/emf/2002/GenModel body='<%com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal%> _createOngoingTaskInternal = <%com.lvl6.mobsters.domainmodel.player.MobstersPlayerFactory%>.eINSTANCE.createOngoingTaskInternal();\nthis.setNewArtifact(_createOngoingTaskInternal);\n<%com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal%> _newArtifact = this.getNewArtifact();\n_newArtifact.setTaskMeta(taskMeta);'"
	 * @generated
	 */
	void init(ITask taskMeta, Player player);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model unique="false" stageNumUnique="false"
	 *        taskStageDataType="com.lvl6.mobsters.domainmodel.player.ITaskStage"
	 *        taskStageUnique="false" taskStageMonsterDataType=
	 *        "com.lvl6.mobsters.domainmodel.player.ITaskStageMonster"
	 *        taskStageMonsterUnique="false" cashRewardUnique="false"
	 *        oilRewardUnique="false" expRewardUnique="false"
	 *        itemDroppedDataType="com.lvl6.mobsters.domainmodel.player.IItem"
	 *        itemDroppedUnique="false" monsterPieceDroppedUnique="false"
	 *        annotation=
	 *        "http://www.eclipse.org/emf/2002/GenModel body='<%com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal%> _newArtifact = this.getNewArtifact();\n<%org.eclipse.emf.common.util.EList%><<%com.lvl6.mobsters.domainmodel.player.TaskStageInternal%>> _stages = _newArtifact.getStages();\n<%com.lvl6.mobsters.domainmodel.player.TaskStageInternal%> _createTaskStageInternal = <%com.lvl6.mobsters.domainmodel.player.MobstersPlayerFactory%>.eINSTANCE.createTaskStageInternal();\nfinal <%org.eclipse.xtext.xbase.lib.Procedures.Procedure1%><<%com.lvl6.mobsters.domainmodel.player.TaskStageInternal%>> _function = new <%org.eclipse.xtext.xbase.lib.Procedures.Procedure1%><<%com.lvl6.mobsters.domainmodel.player.TaskStageInternal%>>()\n{\n\tpublic void apply(final <%com.lvl6.mobsters.domainmodel.player.TaskStageInternal%> it)\n\t{\n\t\t<%java.util.UUID%> _randomUUID = <%java.util.UUID%>.randomUUID();\n\t\tit.setTaskStageUuid(_randomUUID);\n\t\tit.setStageNum(stageNum);\n\t\tit.setTaskStage(taskStage);\n\t\tit.setTaskStageMonster(taskStageMonster);\n\t\tit.setCashGained(cashReward);\n\t\tit.setOilGained(oilReward);\n\t\tit.setExpGained(expReward);\n\t\tit.setMonsterPieceDropped(monsterPieceDropped);\n\t\tit.setItemDropped(itemDropped);\n\t}\n};\n<%com.lvl6.mobsters.domainmodel.player.TaskStageInternal%> _doubleArrow = <%org.eclipse.xtext.xbase.lib.ObjectExtensions%>.<<%com.lvl6.mobsters.domainmodel.player.TaskStageInternal%>>operator_doubleArrow(_createTaskStageInternal, _function);\n_stages.add(_doubleArrow);\nreturn this;'"
	 * @generated
	 */
	BeginTaskStagesBuilder addStage(int stageNum, ITaskStage taskStage,
			ITaskStageMonster taskStageMonster, int cashReward, int oilReward,
			int expReward, IItem itemDropped, boolean monsterPieceDropped);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model annotation=
	 *        "http://www.eclipse.org/emf/2002/GenModel body='<%com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal%> _newArtifact = this.getNewArtifact();\n<%java.util.UUID%> _randomUUID = <%java.util.UUID%>.randomUUID();\n_newArtifact.setTaskUuid(_randomUUID);\n<%com.lvl6.mobsters.domainmodel.player.PlayerInternal%> _player = this.getPlayer();\n<%com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal%> _newArtifact_1 = this.getNewArtifact();\n_player.setOngoingTask(_newArtifact_1);'"
	 * @generated
	 */
	void build();

} // BeginTaskStagesBuilderInternal
