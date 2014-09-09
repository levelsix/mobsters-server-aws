/**
 */
package com.lvl6.mobsters.domainmodel.player;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage
 * @generated
 */
public interface MobstersPlayerFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MobstersPlayerFactory eINSTANCE = com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Player Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Player Internal</em>'.
	 * @generated
	 */
	PlayerInternal createPlayerInternal();

	/**
	 * Returns a new object of class '<em>User Data Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>User Data Internal</em>'.
	 * @generated
	 */
	UserDataInternal createUserDataInternal();

	/**
	 * Returns a new object of class '<em>Ongoing Task Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ongoing Task Internal</em>'.
	 * @generated
	 */
	OngoingTaskInternal createOngoingTaskInternal();

	/**
	 * Returns a new object of class '<em>Task Stage Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Task Stage Internal</em>'.
	 * @generated
	 */
	TaskStageInternal createTaskStageInternal();

	/**
	 * Returns a new object of class '<em>Begin Task Stages Builder Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Begin Task Stages Builder Internal</em>'.
	 * @generated
	 */
	BeginTaskStagesBuilderInternal createBeginTaskStagesBuilderInternal();

	/**
	 * Returns a new object of class '<em>Completed Task Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Completed Task Internal</em>'.
	 * @generated
	 */
	CompletedTaskInternal createCompletedTaskInternal();

	/**
	 * Returns a new object of class '<em>Monster Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Monster Internal</em>'.
	 * @generated
	 */
	MonsterInternal createMonsterInternal();

	/**
	 * Returns a new object of class '<em>Item Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Item Internal</em>'.
	 * @generated
	 */
	ItemInternal createItemInternal();

	/**
	 * Returns a new object of class '<em>Combine Monster Pieces Internal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Combine Monster Pieces Internal</em>'.
	 * @generated
	 */
	CombineMonsterPiecesInternal createCombineMonsterPiecesInternal();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MobstersPlayerPackage getMobstersPlayerPackage();

} //MobstersPlayerFactory
