/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder;
import com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerFactory;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal;
import com.lvl6.mobsters.domainmodel.player.Player;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;
import com.lvl6.mobsters.domainmodel.player.TaskStageInternal;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

import java.lang.reflect.InvocationTargetException;

import java.util.UUID;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.xtext.xbase.lib.ObjectExtensions;

import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Begin Task Stages Builder Internal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.BeginTaskStagesBuilderInternalImpl#getNewArtifact
 * <em>New Artifact</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.BeginTaskStagesBuilderInternalImpl#getPlayer
 * <em>Player</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BeginTaskStagesBuilderInternalImpl extends
		MinimalEObjectImpl.Container implements BeginTaskStagesBuilderInternal {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BeginTaskStagesBuilderInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.BEGIN_TASK_STAGES_BUILDER_INTERNAL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public OngoingTaskInternal getNewArtifact() {
		return (OngoingTaskInternal) eGet(
				MobstersPlayerPackage.Literals.BEGIN_TASK_STAGES_BUILDER_INTERNAL__NEW_ARTIFACT,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setNewArtifact(OngoingTaskInternal newNewArtifact) {
		eSet(MobstersPlayerPackage.Literals.BEGIN_TASK_STAGES_BUILDER_INTERNAL__NEW_ARTIFACT,
				newNewArtifact);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PlayerInternal getPlayer() {
		return (PlayerInternal) eGet(
				MobstersPlayerPackage.Literals.BEGIN_TASK_STAGES_BUILDER_INTERNAL__PLAYER,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setPlayer(PlayerInternal newPlayer) {
		eSet(MobstersPlayerPackage.Literals.BEGIN_TASK_STAGES_BUILDER_INTERNAL__PLAYER,
				newPlayer);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void init(final ITask taskMeta, final Player player) {
		OngoingTaskInternal _createOngoingTaskInternal = MobstersPlayerFactory.eINSTANCE
				.createOngoingTaskInternal();
		this.setNewArtifact(_createOngoingTaskInternal);
		OngoingTaskInternal _newArtifact = this.getNewArtifact();
		_newArtifact.setTaskMeta(taskMeta);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public BeginTaskStagesBuilder addStage(final int stageNum,
			final ITaskStage taskStage,
			final ITaskStageMonster taskStageMonster, final int cashReward,
			final int oilReward, final int expReward, final IItem itemDropped,
			final boolean monsterPieceDropped) {
		OngoingTaskInternal _newArtifact = this.getNewArtifact();
		EList<TaskStageInternal> _stages = _newArtifact.getStages();
		TaskStageInternal _createTaskStageInternal = MobstersPlayerFactory.eINSTANCE
				.createTaskStageInternal();
		final Procedure1<TaskStageInternal> _function = new Procedure1<TaskStageInternal>() {
			@Override
			public void apply(final TaskStageInternal it) {
				UUID _randomUUID = UUID.randomUUID();
				it.setTaskStageUuid(_randomUUID);
				it.setStageNum(stageNum);
				it.setTaskStage(taskStage);
				it.setTaskStageMonster(taskStageMonster);
				it.setCashGained(cashReward);
				it.setOilGained(oilReward);
				it.setExpGained(expReward);
				it.setMonsterPieceDropped(monsterPieceDropped);
				it.setItemDropped(itemDropped);
			}
		};
		TaskStageInternal _doubleArrow = ObjectExtensions
				.<TaskStageInternal> operator_doubleArrow(
						_createTaskStageInternal, _function);
		_stages.add(_doubleArrow);
		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void build() {
		OngoingTaskInternal _newArtifact = this.getNewArtifact();
		UUID _randomUUID = UUID.randomUUID();
		_newArtifact.setTaskUuid(_randomUUID);
		PlayerInternal _player = this.getPlayer();
		OngoingTaskInternal _newArtifact_1 = this.getNewArtifact();
		_player.setOngoingTask(_newArtifact_1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public BeginTaskStagesBuilder addStage(int stageNum, ITaskStage stageMeta,
			ITaskStageMonster stageMonster, int cashReward, int oilReward,
			int expReward, boolean rewardsMonsterPiece, IItem droppedItem) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments)
			throws InvocationTargetException {
		switch (operationID) {
		case MobstersPlayerPackage.BEGIN_TASK_STAGES_BUILDER_INTERNAL___INIT__ITASK_PLAYER:
			init((ITask) arguments.get(0), (Player) arguments.get(1));
			return null;
		case MobstersPlayerPackage.BEGIN_TASK_STAGES_BUILDER_INTERNAL___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_IITEM_BOOLEAN:
			return addStage((Integer) arguments.get(0),
					(ITaskStage) arguments.get(1),
					(ITaskStageMonster) arguments.get(2),
					(Integer) arguments.get(3), (Integer) arguments.get(4),
					(Integer) arguments.get(5), (IItem) arguments.get(6),
					(Boolean) arguments.get(7));
		case MobstersPlayerPackage.BEGIN_TASK_STAGES_BUILDER_INTERNAL___BUILD:
			build();
			return null;
		case MobstersPlayerPackage.BEGIN_TASK_STAGES_BUILDER_INTERNAL___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_BOOLEAN_IITEM:
			return addStage((Integer) arguments.get(0),
					(ITaskStage) arguments.get(1),
					(ITaskStageMonster) arguments.get(2),
					(Integer) arguments.get(3), (Integer) arguments.get(4),
					(Integer) arguments.get(5), (Boolean) arguments.get(6),
					(IItem) arguments.get(7));
		}
		return super.eInvoke(operationID, arguments);
	}

} // BeginTaskStagesBuilderInternalImpl
