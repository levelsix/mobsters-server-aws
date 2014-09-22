/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.googlecode.cqengine.IndexedCollection;

import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.ITask;

import com.lvl6.mobsters.utility.lambda.Director;

import java.util.Date;
import java.util.UUID;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Player Internal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserUuid <em>User Uuid</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGems <em>Gems</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCash <em>Cash</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOil <em>Oil</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getExperience <em>Experience</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOngoingTask <em>Ongoing Task</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTasks <em>Completed Tasks</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getItems <em>Items</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getMonsters <em>Monsters</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getTeamSlots <em>Team Slots</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getPendingOperations <em>Pending Operations</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUdidForHistory <em>Udid For History</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getDeviceToken <em>Device Token</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#isFbIdSetOnUserCreate <em>Fb Id Set On User Create</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGameCenterId <em>Game Center Id</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getAvatarMonsterMeta <em>Avatar Monster Meta</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogin <em>Last Login</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogout <em>Last Logout</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCreateTime <em>Create Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastObstacleSpawnTime <em>Last Obstacle Spawn Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastMiniJobGeneratedTime <em>Last Mini Job Generated Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#isIndexed <em>Indexed</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskIndex <em>Completed Task Index</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal()
 * @model
 * @generated
 */
public interface PlayerInternal extends Player {
	/**
	 * Returns the value of the '<em><b>User Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Uuid</em>' attribute.
	 * @see #setUserUuid(UUID)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_UserUuid()
	 * @model unique="false" id="true" dataType="com.lvl6.mobsters.domainmodel.player.UUID"
	 * @generated
	 */
	UUID getUserUuid();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserUuid <em>User Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Uuid</em>' attribute.
	 * @see #getUserUuid()
	 * @generated
	 */
	void setUserUuid(UUID value);

	/**
	 * Returns the value of the '<em><b>Gems</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Gems</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Gems</em>' attribute.
	 * @see #setGems(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_Gems()
	 * @model unique="false"
	 * @generated
	 */
	int getGems();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGems <em>Gems</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Gems</em>' attribute.
	 * @see #getGems()
	 * @generated
	 */
	void setGems(int value);

	/**
	 * Returns the value of the '<em><b>Cash</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cash</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cash</em>' attribute.
	 * @see #setCash(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_Cash()
	 * @model unique="false"
	 * @generated
	 */
	int getCash();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCash <em>Cash</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cash</em>' attribute.
	 * @see #getCash()
	 * @generated
	 */
	void setCash(int value);

	/**
	 * Returns the value of the '<em><b>Oil</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oil</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oil</em>' attribute.
	 * @see #setOil(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_Oil()
	 * @model unique="false"
	 * @generated
	 */
	int getOil();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOil <em>Oil</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oil</em>' attribute.
	 * @see #getOil()
	 * @generated
	 */
	void setOil(int value);

	/**
	 * Returns the value of the '<em><b>Experience</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Experience</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Experience</em>' attribute.
	 * @see #setExperience(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_Experience()
	 * @model unique="false"
	 * @generated
	 */
	int getExperience();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getExperience <em>Experience</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Experience</em>' attribute.
	 * @see #getExperience()
	 * @generated
	 */
	void setExperience(int value);

	/**
	 * Returns the value of the '<em><b>Ongoing Task</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ongoing Task</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ongoing Task</em>' containment reference.
	 * @see #setOngoingTask(OngoingTaskInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_OngoingTask()
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getPlayer
	 * @model opposite="player" containment="true"
	 * @generated
	 */
	OngoingTaskInternal getOngoingTask();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOngoingTask <em>Ongoing Task</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ongoing Task</em>' containment reference.
	 * @see #getOngoingTask()
	 * @generated
	 */
	void setOngoingTask(OngoingTaskInternal value);

	/**
	 * Returns the value of the '<em><b>Completed Tasks</b></em>' containment reference list.
	 * The list contents are of type {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal}.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Completed Tasks</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Completed Tasks</em>' containment reference list.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_CompletedTasks()
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getPlayer
	 * @model opposite="player" containment="true"
	 * @generated
	 */
	EList<CompletedTaskInternal> getCompletedTasks();

	/**
	 * Returns the value of the '<em><b>Items</b></em>' containment reference list.
	 * The list contents are of type {@link com.lvl6.mobsters.domainmodel.player.ItemInternal}.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Items</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Items</em>' containment reference list.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_Items()
	 * @see com.lvl6.mobsters.domainmodel.player.ItemInternal#getPlayer
	 * @model opposite="player" containment="true"
	 * @generated
	 */
	EList<ItemInternal> getItems();

	/**
	 * Returns the value of the '<em><b>Monsters</b></em>' containment reference list.
	 * The list contents are of type {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal}.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Monsters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Monsters</em>' containment reference list.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_Monsters()
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getPlayer
	 * @model opposite="player" containment="true"
	 * @generated
	 */
	EList<MonsterInternal> getMonsters();

	/**
	 * Returns the value of the '<em><b>Team Slots</b></em>' reference list.
	 * The list contents are of type {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Team Slots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Team Slots</em>' reference list.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_TeamSlots()
	 * @model upper="3"
	 * @generated
	 */
	EList<MonsterInternal> getTeamSlots();

	/**
	 * Returns the value of the '<em><b>Pending Operations</b></em>' containment reference list.
	 * The list contents are of type {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal}.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pending Operations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pending Operations</em>' containment reference list.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_PendingOperations()
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getPlayer
	 * @model opposite="player" containment="true"
	 * @generated
	 */
	EList<PendingOperationInternal> getPendingOperations();

	/**
	 * Returns the value of the '<em><b>Udid For History</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Udid For History</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Udid For History</em>' attribute.
	 * @see #setUdidForHistory(String)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_UdidForHistory()
	 * @model unique="false"
	 * @generated
	 */
	String getUdidForHistory();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUdidForHistory <em>Udid For History</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Udid For History</em>' attribute.
	 * @see #getUdidForHistory()
	 * @generated
	 */
	void setUdidForHistory(String value);

	/**
	 * Returns the value of the '<em><b>Device Token</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Device Token</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Device Token</em>' attribute.
	 * @see #setDeviceToken(String)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_DeviceToken()
	 * @model unique="false"
	 * @generated
	 */
	String getDeviceToken();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getDeviceToken <em>Device Token</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Device Token</em>' attribute.
	 * @see #getDeviceToken()
	 * @generated
	 */
	void setDeviceToken(String value);

	/**
	 * Returns the value of the '<em><b>Fb Id Set On User Create</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fb Id Set On User Create</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fb Id Set On User Create</em>' attribute.
	 * @see #setFbIdSetOnUserCreate(boolean)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_FbIdSetOnUserCreate()
	 * @model unique="false"
	 * @generated
	 */
	boolean isFbIdSetOnUserCreate();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#isFbIdSetOnUserCreate <em>Fb Id Set On User Create</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fb Id Set On User Create</em>' attribute.
	 * @see #isFbIdSetOnUserCreate()
	 * @generated
	 */
	void setFbIdSetOnUserCreate(boolean value);

	/**
	 * Returns the value of the '<em><b>Game Center Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Game Center Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Game Center Id</em>' attribute.
	 * @see #setGameCenterId(String)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_GameCenterId()
	 * @model unique="false"
	 * @generated
	 */
	String getGameCenterId();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGameCenterId <em>Game Center Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Game Center Id</em>' attribute.
	 * @see #getGameCenterId()
	 * @generated
	 */
	void setGameCenterId(String value);

	/**
	 * Returns the value of the '<em><b>Avatar Monster Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Avatar Monster Meta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Avatar Monster Meta</em>' attribute.
	 * @see #setAvatarMonsterMeta(IMonster)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_AvatarMonsterMeta()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.IMonster"
	 * @generated
	 */
	IMonster getAvatarMonsterMeta();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getAvatarMonsterMeta <em>Avatar Monster Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Avatar Monster Meta</em>' attribute.
	 * @see #getAvatarMonsterMeta()
	 * @generated
	 */
	void setAvatarMonsterMeta(IMonster value);

	/**
	 * Returns the value of the '<em><b>Last Login</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Login</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Login</em>' attribute.
	 * @see #setLastLogin(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_LastLogin()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastLogin();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogin <em>Last Login</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Login</em>' attribute.
	 * @see #getLastLogin()
	 * @generated
	 */
	void setLastLogin(Date value);

	/**
	 * Returns the value of the '<em><b>Last Logout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Logout</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Logout</em>' attribute.
	 * @see #setLastLogout(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_LastLogout()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastLogout();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogout <em>Last Logout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Logout</em>' attribute.
	 * @see #getLastLogout()
	 * @generated
	 */
	void setLastLogout(Date value);

	/**
	 * Returns the value of the '<em><b>Create Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Create Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Create Time</em>' attribute.
	 * @see #setCreateTime(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_CreateTime()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getCreateTime();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCreateTime <em>Create Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Create Time</em>' attribute.
	 * @see #getCreateTime()
	 * @generated
	 */
	void setCreateTime(Date value);

	/**
	 * Returns the value of the '<em><b>Last Obstacle Spawn Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Obstacle Spawn Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Obstacle Spawn Time</em>' attribute.
	 * @see #setLastObstacleSpawnTime(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_LastObstacleSpawnTime()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastObstacleSpawnTime();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastObstacleSpawnTime <em>Last Obstacle Spawn Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Obstacle Spawn Time</em>' attribute.
	 * @see #getLastObstacleSpawnTime()
	 * @generated
	 */
	void setLastObstacleSpawnTime(Date value);

	/**
	 * Returns the value of the '<em><b>Last Mini Job Generated Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Mini Job Generated Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Mini Job Generated Time</em>' attribute.
	 * @see #setLastMiniJobGeneratedTime(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_LastMiniJobGeneratedTime()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastMiniJobGeneratedTime();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastMiniJobGeneratedTime <em>Last Mini Job Generated Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Mini Job Generated Time</em>' attribute.
	 * @see #getLastMiniJobGeneratedTime()
	 * @generated
	 */
	void setLastMiniJobGeneratedTime(Date value);

	/**
	 * Returns the value of the '<em><b>Indexed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Indexed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Indexed</em>' attribute.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_Indexed()
	 * @model unique="false" required="true" transient="true" changeable="false"
	 * @generated
	 */
	boolean isIndexed();

	/**
	 * Returns the value of the '<em><b>Completed Task Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Completed Task Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Completed Task Index</em>' attribute.
	 * @see #isSetCompletedTaskIndex()
	 * @see #unsetCompletedTaskIndex()
	 * @see #setCompletedTaskIndex(IndexedCollection)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_CompletedTaskIndex()
	 * @model unique="false" unsettable="true" dataType="com.lvl6.mobsters.domainmodel.player.CompletedTaskIndex" transient="true"
	 * @generated
	 */
	IndexedCollection<CompletedTask> getCompletedTaskIndex();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskIndex <em>Completed Task Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Completed Task Index</em>' attribute.
	 * @see #isSetCompletedTaskIndex()
	 * @see #unsetCompletedTaskIndex()
	 * @see #getCompletedTaskIndex()
	 * @generated
	 */
	void setCompletedTaskIndex(IndexedCollection<CompletedTask> value);

	/**
	 * Unsets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskIndex <em>Completed Task Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCompletedTaskIndex()
	 * @see #getCompletedTaskIndex()
	 * @see #setCompletedTaskIndex(IndexedCollection)
	 * @generated
	 */
	void unsetCompletedTaskIndex();

	/**
	 * Returns whether the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskIndex <em>Completed Task Index</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Completed Task Index</em>' attribute is set.
	 * @see #unsetCompletedTaskIndex()
	 * @see #getCompletedTaskIndex()
	 * @see #setCompletedTaskIndex(IndexedCollection)
	 * @generated
	 */
	boolean isSetCompletedTaskIndex();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model taskMetaDataType="com.lvl6.mobsters.domainmodel.player.ITask" taskMetaUnique="false" directorType="com.lvl6.mobsters.domainmodel.player.Director<com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder>" directorUnique="false"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
	 * @generated
	 */
	void beginTask(ITask taskMeta, Director<BeginTaskStagesBuilder> director);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='boolean _isIndexed = this.isIndexed();\nboolean _equals = (_isIndexed == false);\nif (_equals)\n{\n}'"
	 * @generated
	 */
	void checkForIndices();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model unique="false" taskMetaDataType="com.lvl6.mobsters.domainmodel.player.ITask" taskMetaUnique="false"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return null;'"
	 * @generated
	 */
	CompletedTask getCompletedTaskFor(ITask taskMeta);

} // PlayerInternal
