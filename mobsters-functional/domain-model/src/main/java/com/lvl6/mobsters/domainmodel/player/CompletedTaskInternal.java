/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.ITask;

import java.util.Date;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Completed Task Internal</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getPlayer
 * <em>Player</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTaskMeta
 * <em>Task Meta</em>}</li>
 * <li>
 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTimeOfEntry
 * <em>Time Of Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getCompletedTaskInternal()
 * @model
 * @generated
 */
public interface CompletedTaskInternal extends CompletedTask {
	/**
	 * Returns the value of the '<em><b>Player</b></em>' container reference. It
	 * is bidirectional and its opposite is '
	 * {@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTasks
	 * <em>Completed Tasks</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Player</em>' container reference.
	 * @see #setPlayer(PlayerInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getCompletedTaskInternal_Player()
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTasks
	 * @model opposite="completedTasks" transient="false"
	 * @generated
	 */
	PlayerInternal getPlayer();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getPlayer
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
	 * Returns the value of the '<em><b>Task Meta</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Meta</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Task Meta</em>' attribute.
	 * @see #setTaskMeta(ITask)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getCompletedTaskInternal_TaskMeta()
	 * @model unique="false"
	 *        dataType="com.lvl6.mobsters.domainmodel.player.ITask"
	 * @generated
	 */
	ITask getTaskMeta();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTaskMeta
	 * <em>Task Meta</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Task Meta</em>' attribute.
	 * @see #getTaskMeta()
	 * @generated
	 */
	void setTaskMeta(ITask value);

	/**
	 * Returns the value of the '<em><b>Time Of Entry</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Of Entry</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Time Of Entry</em>' attribute.
	 * @see #setTimeOfEntry(Date)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getCompletedTaskInternal_TimeOfEntry()
	 * @model unique="false"
	 * @generated
	 */
	Date getTimeOfEntry();

	/**
	 * Sets the value of the '
	 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTimeOfEntry
	 * <em>Time Of Entry</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Time Of Entry</em>' attribute.
	 * @see #getTimeOfEntry()
	 * @generated
	 */
	void setTimeOfEntry(Date value);

} // CompletedTaskInternal
