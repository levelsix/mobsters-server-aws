/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.MonsterInternal;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Combine Monster Pieces Internal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.CombineMonsterPiecesInternalImpl#getNewMonster <em>New Monster</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CombineMonsterPiecesInternalImpl extends
		PendingOperationInternalImpl implements CombineMonsterPiecesInternal {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CombineMonsterPiecesInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.COMBINE_MONSTER_PIECES_INTERNAL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MonsterInternal getNewMonster() {
		return (MonsterInternal) eGet(
				MobstersPlayerPackage.Literals.COMBINE_MONSTER_PIECES_INTERNAL__NEW_MONSTER,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewMonster(MonsterInternal newNewMonster) {
		eSet(MobstersPlayerPackage.Literals.COMBINE_MONSTER_PIECES_INTERNAL__NEW_MONSTER,
				newNewMonster);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void happen() {
		PlayerInternal _player = this.getPlayer();
		EList<MonsterInternal> _monsters = _player.getMonsters();
		MonsterInternal _newMonster = this.getNewMonster();
		_monsters.add(_newMonster);
		return;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments)
			throws InvocationTargetException {
		switch (operationID) {
		case MobstersPlayerPackage.COMBINE_MONSTER_PIECES_INTERNAL___HAPPEN:
			happen();
			return null;
		}
		return super.eInvoke(operationID, arguments);
	}

} //CombineMonsterPiecesInternalImpl
