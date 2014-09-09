/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.MonsterInternal;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;

import com.lvl6.mobsters.info.IMonster;

import java.util.Date;
import java.util.UUID;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Monster Internal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getMonsterUuid <em>Monster Uuid</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getPlayer <em>Player</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getMonsterMeta <em>Monster Meta</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getCurrentExp <em>Current Exp</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getCurrentLvl <em>Current Lvl</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getCurrentHealth <em>Current Health</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getNumPieces <em>Num Pieces</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#isIsComplete <em>Is Complete</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getCombineStartTime <em>Combine Start Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#getTeamSlotNum <em>Team Slot Num</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl#isRestricted <em>Restricted</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MonsterInternalImpl extends MinimalEObjectImpl.Container implements
		MonsterInternal {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonsterInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.MONSTER_INTERNAL;
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
	public UUID getMonsterUuid() {
		return (UUID) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__MONSTER_UUID,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMonsterUuid(UUID newMonsterUuid) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__MONSTER_UUID,
				newMonsterUuid);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlayerInternal getPlayer() {
		return (PlayerInternal) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__PLAYER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPlayer(PlayerInternal newPlayer) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__PLAYER, newPlayer);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMonster getMonsterMeta() {
		return (IMonster) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__MONSTER_META,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMonsterMeta(IMonster newMonsterMeta) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__MONSTER_META,
				newMonsterMeta);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCurrentExp() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__CURRENT_EXP,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentExp(int newCurrentExp) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__CURRENT_EXP,
				newCurrentExp);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCurrentLvl() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__CURRENT_LVL,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentLvl(int newCurrentLvl) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__CURRENT_LVL,
				newCurrentLvl);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCurrentHealth() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__CURRENT_HEALTH,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentHealth(int newCurrentHealth) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__CURRENT_HEALTH,
				newCurrentHealth);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumPieces() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__NUM_PIECES,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumPieces(int newNumPieces) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__NUM_PIECES,
				newNumPieces);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsComplete() {
		return (Boolean) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__IS_COMPLETE,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsComplete(boolean newIsComplete) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__IS_COMPLETE,
				newIsComplete);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getCombineStartTime() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__COMBINE_START_TIME,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCombineStartTime(Date newCombineStartTime) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__COMBINE_START_TIME,
				newCombineStartTime);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTeamSlotNum() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__TEAM_SLOT_NUM,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTeamSlotNum(int newTeamSlotNum) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__TEAM_SLOT_NUM,
				newTeamSlotNum);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRestricted() {
		return (Boolean) eGet(
				MobstersPlayerPackage.Literals.MONSTER_INTERNAL__RESTRICTED,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRestricted(boolean newRestricted) {
		eSet(MobstersPlayerPackage.Literals.MONSTER_INTERNAL__RESTRICTED,
				newRestricted);
	}

} //MonsterInternalImpl
