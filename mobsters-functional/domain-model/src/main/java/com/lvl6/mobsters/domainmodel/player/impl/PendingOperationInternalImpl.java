/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.PendingOperationInternal;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;

import com.lvl6.mobsters.utility.common.TimeUtils;

import java.lang.reflect.InvocationTargetException;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Pending Operation Internal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PendingOperationInternalImpl#getPlayer
 * <em>Player</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PendingOperationInternalImpl#getOpStartTimer
 * <em>Op Start Timer</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PendingOperationInternalImpl#getOpEndTimer
 * <em>Op End Timer</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class PendingOperationInternalImpl extends
		MinimalEObjectImpl.Container implements PendingOperationInternal {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PendingOperationInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.PENDING_OPERATION_INTERNAL;
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
				MobstersPlayerPackage.Literals.PENDING_OPERATION_INTERNAL__PLAYER,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setPlayer(PlayerInternal newPlayer) {
		eSet(MobstersPlayerPackage.Literals.PENDING_OPERATION_INTERNAL__PLAYER,
				newPlayer);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getOpStartTimer() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.PENDING_OPERATION_INTERNAL__OP_START_TIMER,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setOpStartTimer(Date newOpStartTimer) {
		eSet(MobstersPlayerPackage.Literals.PENDING_OPERATION_INTERNAL__OP_START_TIMER,
				newOpStartTimer);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getOpEndTimer() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.PENDING_OPERATION_INTERNAL__OP_END_TIMER,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setOpEndTimer(Date newOpEndTimer) {
		eSet(MobstersPlayerPackage.Literals.PENDING_OPERATION_INTERNAL__OP_END_TIMER,
				newOpEndTimer);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void happen() {
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
	public boolean checkTimer() {
		Date now = TimeUtils.createNow();
		boolean retVal = false;
		Date _opEndTimer = this.getOpEndTimer();
		boolean _lessThan = (_opEndTimer.compareTo(now) < 0);
		if (_lessThan) {
			this.happen();
			retVal = true;
		}
		return retVal;
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
		case MobstersPlayerPackage.PENDING_OPERATION_INTERNAL___HAPPEN:
			happen();
			return null;
		case MobstersPlayerPackage.PENDING_OPERATION_INTERNAL___CHECK_TIMER:
			return checkTimer();
		}
		return super.eInvoke(operationID, arguments);
	}

} // PendingOperationInternalImpl
