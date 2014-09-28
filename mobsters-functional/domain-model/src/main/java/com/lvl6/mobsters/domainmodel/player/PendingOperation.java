/**
 */
package com.lvl6.mobsters.domainmodel.player;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Pending Operation</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> class Quest{
 * 
 * }
 * 
 * class QuestJob { IQuestJob questJobMeta;
 * 
 * private boolean isComplete; private int progress;
 * 
 * }
 * 
 * // // Game State Subunit Models: Booster ? // class BoosterPackHistory
 * 
 * class BoosterPackItemHistory
 * 
 * 
 * // // Game State Subunit Models: ??? //
 * 
 * class EventPersistentForUser
 * 
 * 
 * // // Game State Subunit Models: In App Purchase // class IapHistory // A
 * Transaction
 * 
 * class IapItem // A line item. Related to Item?
 * 
 * 
 * // // Game State Subunit Models: MiniJob // class MiniJob
 * 
 * class MiniJobOngoing
 * 
 * class MiniJobCompleted
 * 
 * 
 * // // Game State Subunit Models: Monster <Activity> ??? // class
 * MonsterHealingHistory
 * 
 * 
 * // // Game State Subunit Models: Obstacle // class Obstacle
 * 
 * 
 * // // Game State Subunit Models: Structure // class Structure
 * 
 * class Hospital
 * 
 * class Laboratory
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
 * // // New abstractions //
 * 
 * /** A Pending Operation is a record of a game event that was scheduled as a
 * result of a game-play action (e.g. buying a structure, healing a monster, or
 * committing to complete a MiniJob) and then generates an effect some fixed
 * amount of time later.
 * 
 * The purpose of this class is storage for time stamps that are both: 1)
 * Necessary while creation, modification, or removal of some user artifact is
 * scheduled but unresolved. 2) Serve no functional role after the scheduled
 * creation, modification, or removal is resolved.
 * 
 * NOTE: Copying the state to historical auditing tables goes not constitute a
 * functional role from the perspective (and, hence, domain model) for user
 * state. NOTE: The above also alleviates PendingOperations from needing to
 * model state that would be useful in a historical record, but is unnecessary
 * when processing. For example: 1) If resources spent are deducted in the same
 * request handler that creates and attaches a PendingOperation, not the one
 * that resolves it, then neither cashCost, oilCost, or gemCost is needed in
 * that PendingOperation's model. 2) Decisions that affected the price paid and,
 * more importantly, the outcome on resolution, are what PendingOperations are
 * meant to capture. If our example were a "Buy Structure" or "Heal Monster"
 * request, we would see "structure id" or "monster id" storage for information
 * about what is being created. 3) If the game were later changed to add a
 * "Cancel" button that could be used to prevent the new structure creation or
 * monster piece combination result from happening, and refunded the initial
 * resource costs, this would be good reason to change the original design to
 * include attributes for costs paid. <!-- end-model-doc -->
 *
 *
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#getPendingOperation()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface PendingOperation extends EObject {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model unique="false"
	 * @generated
	 */
	boolean checkTimer();

} // PendingOperation
