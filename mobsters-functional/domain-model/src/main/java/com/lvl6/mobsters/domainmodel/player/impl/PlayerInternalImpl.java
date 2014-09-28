/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.googlecode.cqengine.IndexedCollection;

import com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder;
import com.lvl6.mobsters.domainmodel.player.CompletedTask;
import com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal;
import com.lvl6.mobsters.domainmodel.player.ItemInternal;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.MonsterInternal;
import com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal;
import com.lvl6.mobsters.domainmodel.player.PendingOperationInternal;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;

import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.ITask;

import com.lvl6.mobsters.utility.lambda.Director;

import java.lang.reflect.InvocationTargetException;

import java.util.Date;
import java.util.UUID;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Player Internal</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getUserUuid
 * <em>User Uuid</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getGems
 * <em>Gems</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getCash
 * <em>Cash</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getOil
 * <em>Oil</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getExperience
 * <em>Experience</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getOngoingTask
 * <em>Ongoing Task</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getCompletedTasks
 * <em>Completed Tasks</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getItems
 * <em>Items</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getMonsters
 * <em>Monsters</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getTeamSlots
 * <em>Team Slots</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getPendingOperations
 * <em>Pending Operations</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getUdidForHistory
 * <em>Udid For History</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getDeviceToken
 * <em>Device Token</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#isFbIdSetOnUserCreate
 * <em>Fb Id Set On User Create</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getGameCenterId
 * <em>Game Center Id</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getAvatarMonsterMeta
 * <em>Avatar Monster Meta</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getLastLogin
 * <em>Last Login</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getLastLogout
 * <em>Last Logout</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getCreateTime
 * <em>Create Time</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getLastObstacleSpawnTime
 * <em>Last Obstacle Spawn Time</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getLastMiniJobGeneratedTime
 * <em>Last Mini Job Generated Time</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#isIndexed
 * <em>Indexed</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl#getCompletedTaskIndex
 * <em>Completed Task Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlayerInternalImpl extends MinimalEObjectImpl.Container implements
		PlayerInternal {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PlayerInternalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MobstersPlayerPackage.Literals.PLAYER_INTERNAL;
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
	public UUID getUserUuid() {
		return (UUID) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__USER_UUID, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setUserUuid(UUID newUserUuid) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__USER_UUID,
				newUserUuid);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getGems() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__GEMS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setGems(int newGems) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__GEMS, newGems);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getCash() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__CASH, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setCash(int newCash) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__CASH, newCash);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getOil() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__OIL, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setOil(int newOil) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__OIL, newOil);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getExperience() {
		return (Integer) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__EXPERIENCE,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setExperience(int newExperience) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__EXPERIENCE,
				newExperience);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public OngoingTaskInternal getOngoingTask() {
		return (OngoingTaskInternal) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__ONGOING_TASK,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setOngoingTask(OngoingTaskInternal newOngoingTask) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__ONGOING_TASK,
				newOngoingTask);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<CompletedTaskInternal> getCompletedTasks() {
		return (EList<CompletedTaskInternal>) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__COMPLETED_TASKS,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<ItemInternal> getItems() {
		return (EList<ItemInternal>) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__ITEMS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<MonsterInternal> getMonsters() {
		return (EList<MonsterInternal>) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__MONSTERS, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<MonsterInternal> getTeamSlots() {
		return (EList<MonsterInternal>) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__TEAM_SLOTS,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<PendingOperationInternal> getPendingOperations() {
		return (EList<PendingOperationInternal>) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__PENDING_OPERATIONS,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getUdidForHistory() {
		return (String) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__UDID_FOR_HISTORY,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setUdidForHistory(String newUdidForHistory) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__UDID_FOR_HISTORY,
				newUdidForHistory);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getDeviceToken() {
		return (String) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__DEVICE_TOKEN,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDeviceToken(String newDeviceToken) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__DEVICE_TOKEN,
				newDeviceToken);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isFbIdSetOnUserCreate() {
		return (Boolean) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__FB_ID_SET_ON_USER_CREATE,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setFbIdSetOnUserCreate(boolean newFbIdSetOnUserCreate) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__FB_ID_SET_ON_USER_CREATE,
				newFbIdSetOnUserCreate);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getGameCenterId() {
		return (String) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__GAME_CENTER_ID,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setGameCenterId(String newGameCenterId) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__GAME_CENTER_ID,
				newGameCenterId);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IMonster getAvatarMonsterMeta() {
		return (IMonster) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__AVATAR_MONSTER_META,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setAvatarMonsterMeta(IMonster newAvatarMonsterMeta) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__AVATAR_MONSTER_META,
				newAvatarMonsterMeta);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getLastLogin() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_LOGIN,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setLastLogin(Date newLastLogin) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_LOGIN,
				newLastLogin);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getLastLogout() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_LOGOUT,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setLastLogout(Date newLastLogout) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_LOGOUT,
				newLastLogout);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getCreateTime() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__CREATE_TIME,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setCreateTime(Date newCreateTime) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__CREATE_TIME,
				newCreateTime);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getLastObstacleSpawnTime() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_OBSTACLE_SPAWN_TIME,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setLastObstacleSpawnTime(Date newLastObstacleSpawnTime) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_OBSTACLE_SPAWN_TIME,
				newLastObstacleSpawnTime);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date getLastMiniJobGeneratedTime() {
		return (Date) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_MINI_JOB_GENERATED_TIME,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setLastMiniJobGeneratedTime(Date newLastMiniJobGeneratedTime) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__LAST_MINI_JOB_GENERATED_TIME,
				newLastMiniJobGeneratedTime);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isIndexed() {
		return (Boolean) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__INDEXED, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IndexedCollection<CompletedTask> getCompletedTaskIndex() {
		return (IndexedCollection<CompletedTask>) eGet(
				MobstersPlayerPackage.Literals.PLAYER_INTERNAL__COMPLETED_TASK_INDEX,
				true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setCompletedTaskIndex(
			IndexedCollection<CompletedTask> newCompletedTaskIndex) {
		eSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__COMPLETED_TASK_INDEX,
				newCompletedTaskIndex);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetCompletedTaskIndex() {
		eUnset(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__COMPLETED_TASK_INDEX);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetCompletedTaskIndex() {
		return eIsSet(MobstersPlayerPackage.Literals.PLAYER_INTERNAL__COMPLETED_TASK_INDEX);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void beginTask(final ITask taskMeta,
			final Director<BeginTaskStagesBuilder> director) {

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void checkForIndices() {
		boolean _isIndexed = this.isIndexed();
		boolean _equals = (_isIndexed == false);
		if (_equals) {
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public CompletedTask getCompletedTaskFor(final ITask taskMeta) {
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object eInvoke(int operationID, EList<?> arguments)
			throws InvocationTargetException {
		switch (operationID) {
		case MobstersPlayerPackage.PLAYER_INTERNAL___BEGIN_TASK__ITASK_DIRECTOR:
			beginTask((ITask) arguments.get(0),
					(Director<BeginTaskStagesBuilder>) arguments.get(1));
			return null;
		case MobstersPlayerPackage.PLAYER_INTERNAL___CHECK_FOR_INDICES:
			checkForIndices();
			return null;
		case MobstersPlayerPackage.PLAYER_INTERNAL___GET_COMPLETED_TASK_FOR__ITASK:
			return getCompletedTaskFor((ITask) arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} // PlayerInternalImpl
