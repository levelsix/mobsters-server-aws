/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.IMonster;

import java.util.Date;
import java.util.UUID;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Monster Internal</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterUuid
 * <em>Monster Uuid</em>}</li>
 * <li>{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getPlayer
 * <em>Player</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterMeta
 * <em>Monster Meta</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentExp
 * <em>Current Exp</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentLvl
 * <em>Current Lvl</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentHealth
 * <em>Current Health</em>}</li>
 * <li>{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getNumPieces
 * <em>Num Pieces</em>}</li>
 * <li>{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#isIsComplete
 * <em>Is Complete</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCombineStartTime
 * <em>Combine Start Time</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getTeamSlotNum
 * <em>Team Slot Num</em>}</li>
 * <li>{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#isRestricted
 * <em>Restricted</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal()
 * @model
 * @generated
 */
public interface MonsterInternal extends Monster {
	/**
	 * Returns the value of the '<em><b>Monster Uuid</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Monster Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Monster Uuid</em>' attribute.
	 * @see #setMonsterUuid(UUID)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_MonsterUuid()
	 * @model unique="false" id="true"
	 *        dataType="com.lvl6.mobsters.domainmodel.player.UUID"
	 * @generated
	 */
	UUID getMonsterUuid();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterUuid
	 * <em>Monster Uuid</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Monster Uuid</em>' attribute.
	 * @see #getMonsterUuid()
	 * @generated
	 */
	void setMonsterUuid(UUID value);

	/**
	 * Returns the value of the '<em><b>Player</b></em>' container reference. It
	 * is bidirectional and its opposite is '
	 * {@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getMonsters
	 * <em>Monsters</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Player</em>' container reference.
	 * @see #setPlayer(PlayerInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_Player()
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getMonsters
	 * @model opposite="monsters" transient="false"
	 * @generated
	 */
	PlayerInternal getPlayer();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getPlayer
	 * <em>Player</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Player</em>' container reference.
	 * @see #getPlayer()
	 * @generated
	 */
	void setPlayer(PlayerInternal value);

	/**
	 * Returns the value of the '<em><b>Monster Meta</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Monster Meta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Monster Meta</em>' attribute.
	 * @see #setMonsterMeta(IMonster)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_MonsterMeta()
	 * @model unique="false"
	 *        dataType="com.lvl6.mobsters.domainmodel.player.IMonster"
	 * @generated
	 */
	IMonster getMonsterMeta();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterMeta
	 * <em>Monster Meta</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Monster Meta</em>' attribute.
	 * @see #getMonsterMeta()
	 * @generated
	 */
	void setMonsterMeta(IMonster value);

	/**
	 * Returns the value of the '<em><b>Current Exp</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Exp</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Current Exp</em>' attribute.
	 * @see #setCurrentExp(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_CurrentExp()
	 * @model unique="false"
	 * @generated
	 */
	int getCurrentExp();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentExp
	 * <em>Current Exp</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Current Exp</em>' attribute.
	 * @see #getCurrentExp()
	 * @generated
	 */
	void setCurrentExp(int value);

	/**
	 * Returns the value of the '<em><b>Current Lvl</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Lvl</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Current Lvl</em>' attribute.
	 * @see #setCurrentLvl(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_CurrentLvl()
	 * @model unique="false"
	 * @generated
	 */
	int getCurrentLvl();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentLvl
	 * <em>Current Lvl</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Current Lvl</em>' attribute.
	 * @see #getCurrentLvl()
	 * @generated
	 */
	void setCurrentLvl(int value);

	/**
	 * Returns the value of the '<em><b>Current Health</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Health</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Current Health</em>' attribute.
	 * @see #setCurrentHealth(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_CurrentHealth()
	 * @model unique="false"
	 * @generated
	 */
	int getCurrentHealth();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentHealth
	 * <em>Current Health</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Current Health</em>' attribute.
	 * @see #getCurrentHealth()
	 * @generated
	 */
	void setCurrentHealth(int value);

	/**
	 * Returns the value of the '<em><b>Num Pieces</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Num Pieces</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Num Pieces</em>' attribute.
	 * @see #setNumPieces(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_NumPieces()
	 * @model unique="false"
	 * @generated
	 */
	int getNumPieces();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getNumPieces
	 * <em>Num Pieces</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Num Pieces</em>' attribute.
	 * @see #getNumPieces()
	 * @generated
	 */
	void setNumPieces(int value);

	/**
	 * Returns the value of the '<em><b>Is Complete</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Complete</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Is Complete</em>' attribute.
	 * @see #setIsComplete(boolean)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_IsComplete()
	 * @model unique="false"
	 * @generated
	 */
	boolean isIsComplete();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#isIsComplete
	 * <em>Is Complete</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Is Complete</em>' attribute.
	 * @see #isIsComplete()
	 * @generated
	 */
	void setIsComplete(boolean value);

	/**
	 * Returns the value of the '<em><b>Combine Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Combine Start Time</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Combine Start Time</em>' attribute.
	 * @see #setCombineStartTime(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_CombineStartTime()
	 * @model unique="false"
	 * @generated
	 */
	Date getCombineStartTime();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCombineStartTime
	 * <em>Combine Start Time</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Combine Start Time</em>' attribute.
	 * @see #getCombineStartTime()
	 * @generated
	 */
	void setCombineStartTime(Date value);

	/**
	 * Returns the value of the '<em><b>Team Slot Num</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Team Slot Num</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Team Slot Num</em>' attribute.
	 * @see #setTeamSlotNum(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_TeamSlotNum()
	 * @model unique="false"
	 * @generated
	 */
	int getTeamSlotNum();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getTeamSlotNum
	 * <em>Team Slot Num</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Team Slot Num</em>' attribute.
	 * @see #getTeamSlotNum()
	 * @generated
	 */
	void setTeamSlotNum(int value);

	/**
	 * Returns the value of the '<em><b>Restricted</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Restricted</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Restricted</em>' attribute.
	 * @see #setRestricted(boolean)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getMonsterInternal_Restricted()
	 * @model unique="false"
	 * @generated
	 */
	boolean isRestricted();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#isRestricted
	 * <em>Restricted</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Restricted</em>' attribute.
	 * @see #isRestricted()
	 * @generated
	 */
	void setRestricted(boolean value);

} // MonsterInternal
