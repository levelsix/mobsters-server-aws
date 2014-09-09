/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.ITask;

import java.util.UUID;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ongoing Task Internal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskUuid <em>Task Uuid</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getPlayer <em>Player</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskMeta <em>Task Meta</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getStages <em>Stages</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getOngoingTaskInternal()
 * @model
 * @generated
 */
public interface OngoingTaskInternal extends OngoingTask {
	/**
	 * Returns the value of the '<em><b>Task Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Uuid</em>' attribute.
	 * @see #setTaskUuid(UUID)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getOngoingTaskInternal_TaskUuid()
	 * @model unique="false" id="true" dataType="com.lvl6.mobsters.domainmodel.player.UUID"
	 * @generated
	 */
	UUID getTaskUuid();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskUuid <em>Task Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Uuid</em>' attribute.
	 * @see #getTaskUuid()
	 * @generated
	 */
	void setTaskUuid(UUID value);

	/**
	 * Returns the value of the '<em><b>Player</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOngoingTask <em>Ongoing Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Player</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Player</em>' container reference.
	 * @see #setPlayer(PlayerInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getOngoingTaskInternal_Player()
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOngoingTask
	 * @model opposite="ongoingTask" transient="false"
	 * @generated
	 */
	PlayerInternal getPlayer();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getPlayer <em>Player</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Player</em>' container reference.
	 * @see #getPlayer()
	 * @generated
	 */
	void setPlayer(PlayerInternal value);

	/**
	 * Returns the value of the '<em><b>Task Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Meta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Meta</em>' attribute.
	 * @see #setTaskMeta(ITask)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getOngoingTaskInternal_TaskMeta()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.metadata.ITask"
	 * @generated
	 */
	ITask getTaskMeta();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskMeta <em>Task Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Meta</em>' attribute.
	 * @see #getTaskMeta()
	 * @generated
	 */
	void setTaskMeta(ITask value);

	/**
	 * Returns the value of the '<em><b>Stages</b></em>' containment reference list.
	 * The list contents are of type {@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal}.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stages</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stages</em>' containment reference list.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getOngoingTaskInternal_Stages()
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTask
	 * @model opposite="task" containment="true"
	 * @generated
	 */
	EList<TaskStageInternal> getStages();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='<%com.lvl6.mobsters.domainmodel.player.PlayerInternal%> _player = this.getPlayer();\n<%com.lvl6.mobsters.info.ITask%> _taskMeta = this.getTaskMeta();\n<%com.lvl6.mobsters.domainmodel.player.CompletedTask%> _completedTaskFor = _player.getCompletedTaskFor(_taskMeta);\nboolean _equals = <%com.google.common.base.Objects%>.equal(_completedTaskFor, null);\nif (_equals)\n{\n}'"
	 * @generated
	 */
	void completeTask();

} // OngoingTaskInternal
