/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.IMonster;

import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Data Internal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getPlayer <em>Player</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getUdidForHistory <em>Udid For History</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getDeviceToken <em>Device Token</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#isFbIdSetOnUserCreate <em>Fb Id Set On User Create</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getGameCenterId <em>Game Center Id</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getAvatarMonsterMeta <em>Avatar Monster Meta</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastLogin <em>Last Login</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastLogout <em>Last Logout</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getCreateTime <em>Create Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastObstacleSpawnTime <em>Last Obstacle Spawn Time</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastMiniJobGeneratedTime <em>Last Mini Job Generated Time</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal()
 * @model
 * @generated
 */
public interface UserDataInternal extends UserData {
	/**
	 * Returns the value of the '<em><b>Player</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserData <em>User Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Player</em>' container reference.
	 * @see #setPlayer(PlayerInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_Player()
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserData
	 * @model opposite="userData" transient="false"
	 * @generated
	 */
	PlayerInternal getPlayer();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getPlayer <em>Player</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Player</em>' container reference.
	 * @see #getPlayer()
	 * @generated
	 */
	void setPlayer(PlayerInternal value);

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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_UdidForHistory()
	 * @model unique="false"
	 * @generated
	 */
	String getUdidForHistory();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getUdidForHistory <em>Udid For History</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_DeviceToken()
	 * @model unique="false"
	 * @generated
	 */
	String getDeviceToken();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getDeviceToken <em>Device Token</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_FbIdSetOnUserCreate()
	 * @model unique="false"
	 * @generated
	 */
	boolean isFbIdSetOnUserCreate();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#isFbIdSetOnUserCreate <em>Fb Id Set On User Create</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_GameCenterId()
	 * @model unique="false"
	 * @generated
	 */
	String getGameCenterId();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getGameCenterId <em>Game Center Id</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_AvatarMonsterMeta()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.metadata.IMonster"
	 * @generated
	 */
	IMonster getAvatarMonsterMeta();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getAvatarMonsterMeta <em>Avatar Monster Meta</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_LastLogin()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastLogin();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastLogin <em>Last Login</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_LastLogout()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastLogout();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastLogout <em>Last Logout</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_CreateTime()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getCreateTime();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getCreateTime <em>Create Time</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_LastObstacleSpawnTime()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastObstacleSpawnTime();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastObstacleSpawnTime <em>Last Obstacle Spawn Time</em>}' attribute.
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
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getUserDataInternal_LastMiniJobGeneratedTime()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.Date"
	 * @generated
	 */
	Date getLastMiniJobGeneratedTime();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getLastMiniJobGeneratedTime <em>Last Mini Job Generated Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Mini Job Generated Time</em>' attribute.
	 * @see #getLastMiniJobGeneratedTime()
	 * @generated
	 */
	void setLastMiniJobGeneratedTime(Date value);

} // UserDataInternal
