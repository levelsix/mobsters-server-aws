/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;

import com.lvl6.mobsters.info.ITask;

import java.util.Date;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Completed Task Internal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.CompletedTaskInternalImpl#getPlayer
 * <em>Player</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.CompletedTaskInternalImpl#getTaskMeta
 * <em>Task Meta</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.CompletedTaskInternalImpl#getTimeOfEntry
 * <em>Time Of Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CompletedTaskInternalImpl extends MinimalEObjectImpl.Container
		implements CompletedTaskInternal {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected CompletedTaskInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.COMPLETED_TASK_INTERNAL;
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
	public PlayerInternal getPlayer() {
		return (PlayerInternal) eGet(
				MobstersPlayerPackage.Literals.COMPLETED_TASK_INTERNAL__PLAYER,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setPlayer(PlayerInternal newPlayer) {
		eSet(MobstersPlayerPackage.Literals.COMPLETED_TASK_INTERNAL__PLAYER,
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
				MobstersPlayerPackage.Literals.COMPLETED_TASK_INTERNAL__TASK_META,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTaskMeta(ITask newTaskMeta) {
		eSet(MobstersPlayerPackage.Literals.COMPLETED_TASK_INTERNAL__TASK_META,
				newTaskMeta);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getTimeOfEntry() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.COMPLETED_TASK_INTERNAL__TIME_OF_ENTRY,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTimeOfEntry(Date newTimeOfEntry) {
		eSet(MobstersPlayerPackage.Literals.COMPLETED_TASK_INTERNAL__TIME_OF_ENTRY,
				newTimeOfEntry);
	}

} // CompletedTaskInternalImpl
