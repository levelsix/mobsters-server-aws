/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.MonsterType;
import com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal;
import com.lvl6.mobsters.domainmodel.player.TaskStageInternal;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

import java.util.UUID;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Task Stage Internal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getTaskStageUuid <em>Task Stage Uuid</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getTask <em>Task</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getTaskStage <em>Task Stage</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getStageNum <em>Stage Num</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getTaskStageMonster <em>Task Stage Monster</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getDmgMultiplier <em>Dmg Multiplier</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getMonsterType <em>Monster Type</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#isMonsterPieceDropped <em>Monster Piece Dropped</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getItemDropped <em>Item Dropped</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getExpGained <em>Exp Gained</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getCashGained <em>Cash Gained</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl#getOilGained <em>Oil Gained</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TaskStageInternalImpl extends MinimalEObjectImpl.Container
		implements TaskStageInternal {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TaskStageInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUID getTaskStageUuid() {
		return (UUID) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK_STAGE_UUID,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaskStageUuid(UUID newTaskStageUuid) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK_STAGE_UUID,
				newTaskStageUuid);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OngoingTaskInternal getTask() {
		return (OngoingTaskInternal) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTask(OngoingTaskInternal newTask) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK, newTask);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITaskStage getTaskStage() {
		return (ITaskStage) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK_STAGE,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaskStage(ITaskStage newTaskStage) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK_STAGE,
				newTaskStage);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStageNum() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__STAGE_NUM,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStageNum(int newStageNum) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__STAGE_NUM,
				newStageNum);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITaskStageMonster getTaskStageMonster() {
		return (ITaskStageMonster) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK_STAGE_MONSTER,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaskStageMonster(ITaskStageMonster newTaskStageMonster) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__TASK_STAGE_MONSTER,
				newTaskStageMonster);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getDmgMultiplier() {
		ITaskStageMonster _taskStageMonster = this.getTaskStageMonster();
		return _taskStageMonster.getDmgMultiplier();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MonsterType getMonsterType() {
		ITaskStageMonster _taskStageMonster = this.getTaskStageMonster();
		String _monsterType = _taskStageMonster.getMonsterType();
		return MonsterType.<MonsterType> valueOf(MonsterType.class,
				_monsterType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMonsterPieceDropped() {
		return (Boolean) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__MONSTER_PIECE_DROPPED,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMonsterPieceDropped(boolean newMonsterPieceDropped) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__MONSTER_PIECE_DROPPED,
				newMonsterPieceDropped);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IItem getItemDropped() {
		return (IItem) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__ITEM_DROPPED,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setItemDropped(IItem newItemDropped) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__ITEM_DROPPED,
				newItemDropped);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getExpGained() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__EXP_GAINED,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpGained(int newExpGained) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__EXP_GAINED,
				newExpGained);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCashGained() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__CASH_GAINED,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCashGained(int newCashGained) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__CASH_GAINED,
				newCashGained);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getOilGained() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__OIL_GAINED,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOilGained(int newOilGained) {
		eSet(MobstersPlayerPackage.Literals.TASK_STAGE_INTERNAL__OIL_GAINED,
				newOilGained);
	}

} //TaskStageInternalImpl
