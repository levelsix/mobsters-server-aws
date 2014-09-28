/**
 */
package com.lvl6.mobsters.domainmodel.player;

import com.lvl6.mobsters.info.ITask;

import com.lvl6.mobsters.utility.lambda.Director;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Player</b></em>'. <!-- end-user-doc -->
 *
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPlayer()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface Player extends EObject {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model taskMetaDataType="com.lvl6.mobsters.domainmodel.player.ITask"
	 *        taskMetaUnique="false" directorType=
	 *        "com.lvl6.mobsters.domainmodel.player.Director<com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder>"
	 *        directorUnique="false"
	 * @generated
	 */
	void beginTask(ITask taskMeta, Director<BeginTaskStagesBuilder> director);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model unique="false"
	 *        taskMetaDataType="com.lvl6.mobsters.domainmodel.player.ITask"
	 *        taskMetaUnique="false"
	 * @generated
	 */
	CompletedTask getCompletedTaskFor(ITask taskMeta);

} // Player
