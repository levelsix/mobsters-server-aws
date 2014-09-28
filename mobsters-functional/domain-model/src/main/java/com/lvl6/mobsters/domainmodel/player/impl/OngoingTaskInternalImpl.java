/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.google.common.base.Objects;

import com.lvl6.mobsters.domainmodel.player.CompletedTask;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;
import com.lvl6.mobsters.domainmodel.player.TaskStageInternal;

import com.lvl6.mobsters.info.ITask;

import java.lang.reflect.InvocationTargetException;

import java.util.UUID;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Ongoing Task Internal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl#getTaskUuid
 * <em>Task Uuid</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl#getPlayer
 * <em>Player</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl#getTaskMeta
 * <em>Task Meta</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl#getStages
 * <em>Stages</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OngoingTaskInternalImpl extends MinimalEObjectImpl.Container
		implements OngoingTaskInternal {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected OngoingTaskInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL;
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
	public UUID getTaskUuid() {
		return (UUID) eGet(
				MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL__TASK_UUID,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTaskUuid(UUID newTaskUuid) {
		eSet(MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL__TASK_UUID,
				newTaskUuid);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PlayerInternal getPlayer() {
		return (PlayerInternal) eGet(
				MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL__PLAYER,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setPlayer(PlayerInternal newPlayer) {
		eSet(MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL__PLAYER,
				newPlayer);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ITask getTaskMeta() {
		return (ITask) eGet(
				MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL__TASK_META,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTaskMeta(ITask newTaskMeta) {
		eSet(MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL__TASK_META,
				newTaskMeta);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<TaskStageInternal> getStages() {
		return (EList<TaskStageInternal>) eGet(
				MobstersPlayerPackage.Literals.ONGOING_TASK_INTERNAL__STAGES,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void completeTask() {
		PlayerInternal _player = this.getPlayer();
		ITask _taskMeta = this.getTaskMeta();
		CompletedTask _completedTaskFor = _player
				.getCompletedTaskFor(_taskMeta);
		boolean _equals = Objects.equal(_completedTaskFor, null);
		if (_equals) {
		}
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
		case MobstersPlayerPackage.ONGOING_TASK_INTERNAL___COMPLETE_TASK:
			completeTask();
			return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} // OngoingTaskInternalImpl
