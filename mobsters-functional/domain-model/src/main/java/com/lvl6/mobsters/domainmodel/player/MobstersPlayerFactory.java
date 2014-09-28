/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.googlecode.cqengine.IndexedCollection;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.IQuest;
import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.IQuestJobMonsterItem;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

import java.util.Date;
import java.util.UUID;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage
 * @generated
 */
public interface MobstersPlayerFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	MobstersPlayerFactory eINSTANCE = com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Player Internal</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Player Internal</em>'.
	 * @generated
	 */
	PlayerInternal createPlayerInternal();

	/**
	 * Returns a new object of class '<em>Ongoing Task Internal</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Ongoing Task Internal</em>'.
	 * @generated
	 */
	OngoingTaskInternal createOngoingTaskInternal();

	/**
	 * Returns a new object of class '<em>Task Stage Internal</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Task Stage Internal</em>'.
	 * @generated
	 */
	TaskStageInternal createTaskStageInternal();

	/**
	 * Returns a new object of class '
	 * <em>Begin Task Stages Builder Internal</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '
	 *         <em>Begin Task Stages Builder Internal</em>'.
	 * @generated
	 */
	BeginTaskStagesBuilderInternal createBeginTaskStagesBuilderInternal();

	/**
	 * Returns a new object of class '<em>Completed Task Internal</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Completed Task Internal</em>'.
	 * @generated
	 */
	CompletedTaskInternal createCompletedTaskInternal();

	/**
	 * Returns a new object of class '<em>Monster Internal</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Monster Internal</em>'.
	 * @generated
	 */
	MonsterInternal createMonsterInternal();

	/**
	 * Returns a new object of class '<em>Item Internal</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Item Internal</em>'.
	 * @generated
	 */
	ItemInternal createItemInternal();

	/**
	 * Returns a new object of class '<em>Combine Monster Pieces Internal</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Combine Monster Pieces Internal</em>'.
	 * @generated
	 */
	CombineMonsterPiecesInternal createCombineMonsterPiecesInternal();

	/**
	 * Returns an instance of data type '<em>Monster Type</em>' corresponding
	 * the given literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	MonsterType createMonsterType(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>Monster Type</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertMonsterType(MonsterType instanceValue);

	/**
	 * Returns an instance of data type '<em>Element Type</em>' corresponding
	 * the given literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	ElementType createElementType(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>Element Type</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertElementType(ElementType instanceValue);

	/**
	 * Returns an instance of data type '<em>IMonster</em>' corresponding the
	 * given literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	IMonster createIMonster(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>IMonster</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertIMonster(IMonster instanceValue);

	/**
	 * Returns an instance of data type '<em>IItem</em>' corresponding the given
	 * literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	IItem createIItem(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>IItem</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertIItem(IItem instanceValue);

	/**
	 * Returns an instance of data type '<em>ITask</em>' corresponding the given
	 * literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	ITask createITask(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>ITask</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertITask(ITask instanceValue);

	/**
	 * Returns an instance of data type '<em>ITask Stage</em>' corresponding the
	 * given literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	ITaskStage createITaskStage(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>ITask Stage</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertITaskStage(ITaskStage instanceValue);

	/**
	 * Returns an instance of data type '<em>ITask Stage Monster</em>'
	 * corresponding the given literal. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	ITaskStageMonster createITaskStageMonster(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>ITask Stage Monster</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertITaskStageMonster(ITaskStageMonster instanceValue);

	/**
	 * Returns an instance of data type '<em>IQuest</em>' corresponding the
	 * given literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	IQuest createIQuest(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>IQuest</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertIQuest(IQuest instanceValue);

	/**
	 * Returns an instance of data type '<em>IQuest Job</em>' corresponding the
	 * given literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	IQuestJob createIQuestJob(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>IQuest Job</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertIQuestJob(IQuestJob instanceValue);

	/**
	 * Returns an instance of data type '<em>IQuest Job Monster Item</em>'
	 * corresponding the given literal. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	IQuestJobMonsterItem createIQuestJobMonsterItem(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>IQuest Job Monster Item</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertIQuestJobMonsterItem(IQuestJobMonsterItem instanceValue);

	/**
	 * Returns an instance of data type '<em>Date</em>' corresponding the given
	 * literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	Date createDate(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>Date</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertDate(Date instanceValue);

	/**
	 * Returns an instance of data type '<em>UUID</em>' corresponding the given
	 * literal. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	UUID createUUID(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>UUID</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertUUID(UUID instanceValue);

	/**
	 * Returns an instance of data type '<em>Completed Task Index</em>'
	 * corresponding the given literal. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param literal
	 *            a literal of the data type.
	 * @return a new instance value of the data type.
	 * @generated
	 */
	IndexedCollection<CompletedTask> createCompletedTaskIndex(String literal);

	/**
	 * Returns a literal representation of an instance of data type '
	 * <em>Completed Task Index</em>'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param instanceValue
	 *            an instance value of the data type.
	 * @return a literal representation of the instance value.
	 * @generated
	 */
	String convertCompletedTaskIndex(
			IndexedCollection<CompletedTask> instanceValue);

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	MobstersPlayerPackage getMobstersPlayerPackage();

} // MobstersPlayerFactory
