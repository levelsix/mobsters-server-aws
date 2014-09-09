/**
 */
package com.lvl6.mobsters.domainmodel.player;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pending Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * class Quest{
 * 
 * }
 * 
 * class QuestJob {
 * 	IQuestJob questJobMeta;
 * 
 * 	private boolean isComplete;
 * 	private int progress;
 * 
 * }
 * 
 * class BoosterPackHistory
 * 
 * class BoosterPackItemHistory
 * 
 * class EventPersistentForUser
 * 
 * class IapHistory // A Transaction
 * 
 * class IapItem // A line item.  Related to Item?
 * 
 * class MiniJobOngoing
 * 
 * class MiniJobCompleted
 * 
 * class MonsterHealingHistory
 * 
 * class Obstacle
 * 
 * class Structure
 * 
 * class Hospital
 * 
 * class Laboratory
 * 
 * class MiniJob
 * 
 * class Residence
 * 
 * class ResourceGenrator
 * 
 * class ResourceStoreage
 * 
 * class TeamCenter
 * 
 * class TownHall
 * 
 * class TaskStageHistory // ????
 * <!-- end-model-doc -->
 *
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPendingOperation()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface PendingOperation extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model unique="false"
	 * @generated
	 */
	boolean checkTimer();

} // PendingOperation
