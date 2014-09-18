/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

import java.util.UUID;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task Stage Internal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageUuid <em>Task Stage Uuid</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTask <em>Task</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStage <em>Task Stage</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getStageNum <em>Stage Num</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageMonster <em>Task Stage Monster</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getDmgMultiplier <em>Dmg Multiplier</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getMonsterType <em>Monster Type</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#isMonsterPieceDropped <em>Monster Piece Dropped</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getItemDropped <em>Item Dropped</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getExpGained <em>Exp Gained</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getCashGained <em>Cash Gained</em>}</li>
 *   <li>{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getOilGained <em>Oil Gained</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal()
 * @model
 * @generated
 */
public interface TaskStageInternal extends TaskStage {
	/**
	 * Returns the value of the '<em><b>Task Stage Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Stage Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Stage Uuid</em>' attribute.
	 * @see #setTaskStageUuid(UUID)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_TaskStageUuid()
	 * @model unique="false" id="true" dataType="com.lvl6.mobsters.domainmodel.player.UUID"
	 * @generated
	 */
	UUID getTaskStageUuid();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageUuid <em>Task Stage Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Stage Uuid</em>' attribute.
	 * @see #getTaskStageUuid()
	 * @generated
	 */
	void setTaskStageUuid(UUID value);

	/**
	 * Returns the value of the '<em><b>Task</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getStages <em>Stages</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task</em>' container reference.
	 * @see #setTask(OngoingTaskInternal)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_Task()
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getStages
	 * @model opposite="stages" required="true" transient="false"
	 * @generated
	 */
	OngoingTaskInternal getTask();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTask <em>Task</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task</em>' container reference.
	 * @see #getTask()
	 * @generated
	 */
	void setTask(OngoingTaskInternal value);

	/**
	 * Returns the value of the '<em><b>Task Stage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Stage</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Stage</em>' attribute.
	 * @see #setTaskStage(ITaskStage)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_TaskStage()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.ITaskStage"
	 * @generated
	 */
	ITaskStage getTaskStage();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStage <em>Task Stage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Stage</em>' attribute.
	 * @see #getTaskStage()
	 * @generated
	 */
	void setTaskStage(ITaskStage value);

	/**
	 * Returns the value of the '<em><b>Stage Num</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stage Num</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stage Num</em>' attribute.
	 * @see #setStageNum(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_StageNum()
	 * @model unique="false"
	 * @generated
	 */
	int getStageNum();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getStageNum <em>Stage Num</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Stage Num</em>' attribute.
	 * @see #getStageNum()
	 * @generated
	 */
	void setStageNum(int value);

	/**
	 * Returns the value of the '<em><b>Task Stage Monster</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Stage Monster</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Stage Monster</em>' attribute.
	 * @see #setTaskStageMonster(ITaskStageMonster)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_TaskStageMonster()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.ITaskStageMonster"
	 * @generated
	 */
	ITaskStageMonster getTaskStageMonster();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageMonster <em>Task Stage Monster</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Stage Monster</em>' attribute.
	 * @see #getTaskStageMonster()
	 * @generated
	 */
	void setTaskStageMonster(ITaskStageMonster value);

	/**
	 * Returns the value of the '<em><b>Dmg Multiplier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dmg Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dmg Multiplier</em>' attribute.
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_DmgMultiplier()
	 * @model unique="false" transient="true" changeable="false" volatile="true" derived="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel get='<%com.lvl6.mobsters.info.ITaskStageMonster%> _taskStageMonster = this.getTaskStageMonster();\nreturn _taskStageMonster.getDmgMultiplier();'"
	 * @generated
	 */
	float getDmgMultiplier();

	/**
	 * Returns the value of the '<em><b>Monster Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.lvl6.mobsters.domainmodel.player.MonsterType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Monster Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Monster Type</em>' attribute.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterType
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_MonsterType()
	 * @model unique="false" transient="true" changeable="false" volatile="true" derived="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel get='<%com.lvl6.mobsters.info.ITaskStageMonster%> _taskStageMonster = this.getTaskStageMonster();\n<%java.lang.String%> _monsterType = _taskStageMonster.getMonsterType();\nreturn <%com.lvl6.mobsters.domainmodel.player.MonsterType%>.<<%com.lvl6.mobsters.domainmodel.player.MonsterType%>>valueOf(\n\t<%com.lvl6.mobsters.domainmodel.player.MonsterType%>.class, _monsterType);'"
	 * @generated
	 */
	MonsterType getMonsterType();

	/**
	 * Returns the value of the '<em><b>Monster Piece Dropped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Monster Piece Dropped</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Monster Piece Dropped</em>' attribute.
	 * @see #setMonsterPieceDropped(boolean)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_MonsterPieceDropped()
	 * @model unique="false"
	 * @generated
	 */
	boolean isMonsterPieceDropped();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#isMonsterPieceDropped <em>Monster Piece Dropped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Monster Piece Dropped</em>' attribute.
	 * @see #isMonsterPieceDropped()
	 * @generated
	 */
	void setMonsterPieceDropped(boolean value);

	/**
	 * Returns the value of the '<em><b>Item Dropped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Item Dropped</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Item Dropped</em>' attribute.
	 * @see #setItemDropped(IItem)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_ItemDropped()
	 * @model unique="false" dataType="com.lvl6.mobsters.domainmodel.player.IItem"
	 * @generated
	 */
	IItem getItemDropped();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getItemDropped <em>Item Dropped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Item Dropped</em>' attribute.
	 * @see #getItemDropped()
	 * @generated
	 */
	void setItemDropped(IItem value);

	/**
	 * Returns the value of the '<em><b>Exp Gained</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exp Gained</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exp Gained</em>' attribute.
	 * @see #setExpGained(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_ExpGained()
	 * @model unique="false"
	 * @generated
	 */
	int getExpGained();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getExpGained <em>Exp Gained</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exp Gained</em>' attribute.
	 * @see #getExpGained()
	 * @generated
	 */
	void setExpGained(int value);

	/**
	 * Returns the value of the '<em><b>Cash Gained</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cash Gained</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cash Gained</em>' attribute.
	 * @see #setCashGained(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_CashGained()
	 * @model unique="false"
	 * @generated
	 */
	int getCashGained();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getCashGained <em>Cash Gained</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cash Gained</em>' attribute.
	 * @see #getCashGained()
	 * @generated
	 */
	void setCashGained(int value);

	/**
	 * Returns the value of the '<em><b>Oil Gained</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oil Gained</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oil Gained</em>' attribute.
	 * @see #setOilGained(int)
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getTaskStageInternal_OilGained()
	 * @model unique="false"
	 * @generated
	 */
	int getOilGained();

	/**
	 * Sets the value of the '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getOilGained <em>Oil Gained</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oil Gained</em>' attribute.
	 * @see #getOilGained()
	 * @generated
	 */
	void setOilGained(int value);

} // TaskStageInternal
