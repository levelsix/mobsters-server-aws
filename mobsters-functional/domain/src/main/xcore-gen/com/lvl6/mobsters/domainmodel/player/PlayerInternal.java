/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.googlecode.cqengine.IndexedCollection;

import com.lvl6.mobsters.info.ITask;

import com.lvl6.mobsters.utility.lambda.Director;

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
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserData <em>User Data</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOngoingTask <em>Ongoing Task</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTasks <em>Completed Tasks</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getItems <em>Items</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getMonsters <em>Monsters</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getTeamSlots <em>Team Slots</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getPendingOperations <em>Pending Operations</em>}</li>
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
	 * Returns the value of the '<em><b>User Data</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.UserDataInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Data</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Data</em>' containment reference.
	 * @see #setUserData(UserDataInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayerInternal_UserData()
	 * @see com.lvl6.mobsters.domainmodel.player.UserDataInternal#getPlayer
	 * @model opposite="player" containment="true"
	 * @generated
	 */
	UserDataInternal getUserData();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserData <em>User Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Data</em>' containment reference.
	 * @see #getUserData()
	 * @generated
	 */
	void setUserData(UserDataInternal value);

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
	 * @model taskMetaDataType="com.lvl6.mobsters.domainmodel.metadata.ITask" taskMetaUnique="false" directorType="com.lvl6.mobsters.domainmodel.player.Director<com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder>" directorUnique="false"
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
	 * @model unique="false" taskMetaDataType="com.lvl6.mobsters.domainmodel.metadata.ITask" taskMetaUnique="false"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return null;'"
	 * @generated
	 */
	CompletedTask getCompletedTaskFor(ITask taskMeta);

} // PlayerInternal
