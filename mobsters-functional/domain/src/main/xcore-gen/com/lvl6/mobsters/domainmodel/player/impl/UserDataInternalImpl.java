/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;
import com.lvl6.mobsters.domainmodel.player.UserDataInternal;

import com.lvl6.mobsters.info.IMonster;

import java.util.Date;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User Data Internal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getPlayer <em>Player</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getUdidForHistory <em>Udid For History</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getDeviceToken <em>Device Token</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#isFbIdSetOnUserCreate <em>Fb Id Set On User Create</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getGameCenterId <em>Game Center Id</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getAvatarMonsterMeta <em>Avatar Monster Meta</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getLastLogin <em>Last Login</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getLastLogout <em>Last Logout</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getCreateTime <em>Create Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getLastObstacleSpawnTime <em>Last Obstacle Spawn Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.impl.UserDataInternalImpl#getLastMiniJobGeneratedTime <em>Last Mini Job Generated Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserDataInternalImpl extends MinimalEObjectImpl.Container
		implements UserDataInternal {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UserDataInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.USER_DATA_INTERNAL;
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
	public PlayerInternal getPlayer() {
		return (PlayerInternal) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__PLAYER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPlayer(PlayerInternal newPlayer) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__PLAYER,
				newPlayer);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUdidForHistory() {
		return (String) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__UDID_FOR_HISTORY,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUdidForHistory(String newUdidForHistory) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__UDID_FOR_HISTORY,
				newUdidForHistory);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDeviceToken() {
		return (String) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__DEVICE_TOKEN,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeviceToken(String newDeviceToken) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__DEVICE_TOKEN,
				newDeviceToken);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFbIdSetOnUserCreate() {
		return (Boolean) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__FB_ID_SET_ON_USER_CREATE,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFbIdSetOnUserCreate(boolean newFbIdSetOnUserCreate) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__FB_ID_SET_ON_USER_CREATE,
				newFbIdSetOnUserCreate);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getGameCenterId() {
		return (String) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__GAME_CENTER_ID,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGameCenterId(String newGameCenterId) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__GAME_CENTER_ID,
				newGameCenterId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMonster getAvatarMonsterMeta() {
		return (IMonster) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__AVATAR_MONSTER_META,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvatarMonsterMeta(IMonster newAvatarMonsterMeta) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__AVATAR_MONSTER_META,
				newAvatarMonsterMeta);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLastLogin() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_LOGIN,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastLogin(Date newLastLogin) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_LOGIN,
				newLastLogin);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLastLogout() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_LOGOUT,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastLogout(Date newLastLogout) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_LOGOUT,
				newLastLogout);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getCreateTime() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__CREATE_TIME,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCreateTime(Date newCreateTime) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__CREATE_TIME,
				newCreateTime);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLastObstacleSpawnTime() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_OBSTACLE_SPAWN_TIME,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastObstacleSpawnTime(Date newLastObstacleSpawnTime) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_OBSTACLE_SPAWN_TIME,
				newLastObstacleSpawnTime);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLastMiniJobGeneratedTime() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_MINI_JOB_GENERATED_TIME,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastMiniJobGeneratedTime(Date newLastMiniJobGeneratedTime) {
		eSet(MobstersPlayerPackage.Literals.USER_DATA_INTERNAL__LAST_MINI_JOB_GENERATED_TIME,
				newLastMiniJobGeneratedTime);
	}

} //UserDataInternalImpl
