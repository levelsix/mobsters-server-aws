/**
 */
package com.lvl6.mobsters.domainmodel.player.util;

import com.lvl6.mobsters.domainmodel.player.*;

import com.lvl6.mobsters.utility.lambda.Director;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage
 * @generated
 */
public class MobstersPlayerSwitch<T1> extends Switch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MobstersPlayerPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MobstersPlayerSwitch() {
		if (modelPackage == null) {
			modelPackage = MobstersPlayerPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case MobstersPlayerPackage.DIRECTOR: {
			Director<?> director = (Director<?>) theEObject;
			T1 result = caseDirector(director);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.PLAYER: {
			Player player = (Player) theEObject;
			T1 result = casePlayer(player);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.PLAYER_INTERNAL: {
			PlayerInternal playerInternal = (PlayerInternal) theEObject;
			T1 result = casePlayerInternal(playerInternal);
			if (result == null)
				result = casePlayer(playerInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.USER_DATA: {
			UserData userData = (UserData) theEObject;
			T1 result = caseUserData(userData);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.USER_DATA_INTERNAL: {
			UserDataInternal userDataInternal = (UserDataInternal) theEObject;
			T1 result = caseUserDataInternal(userDataInternal);
			if (result == null)
				result = caseUserData(userDataInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.ONGOING_TASK: {
			OngoingTask ongoingTask = (OngoingTask) theEObject;
			T1 result = caseOngoingTask(ongoingTask);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.ONGOING_TASK_INTERNAL: {
			OngoingTaskInternal ongoingTaskInternal = (OngoingTaskInternal) theEObject;
			T1 result = caseOngoingTaskInternal(ongoingTaskInternal);
			if (result == null)
				result = caseOngoingTask(ongoingTaskInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.TASK_STAGE: {
			TaskStage taskStage = (TaskStage) theEObject;
			T1 result = caseTaskStage(taskStage);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.TASK_STAGE_INTERNAL: {
			TaskStageInternal taskStageInternal = (TaskStageInternal) theEObject;
			T1 result = caseTaskStageInternal(taskStageInternal);
			if (result == null)
				result = caseTaskStage(taskStageInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.BEGIN_TASK_STAGES_BUILDER: {
			BeginTaskStagesBuilder beginTaskStagesBuilder = (BeginTaskStagesBuilder) theEObject;
			T1 result = caseBeginTaskStagesBuilder(beginTaskStagesBuilder);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.BEGIN_TASK_STAGES_BUILDER_INTERNAL: {
			BeginTaskStagesBuilderInternal beginTaskStagesBuilderInternal = (BeginTaskStagesBuilderInternal) theEObject;
			T1 result = caseBeginTaskStagesBuilderInternal(beginTaskStagesBuilderInternal);
			if (result == null)
				result = caseBeginTaskStagesBuilder(beginTaskStagesBuilderInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.COMPLETED_TASK: {
			CompletedTask completedTask = (CompletedTask) theEObject;
			T1 result = caseCompletedTask(completedTask);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.COMPLETED_TASK_INTERNAL: {
			CompletedTaskInternal completedTaskInternal = (CompletedTaskInternal) theEObject;
			T1 result = caseCompletedTaskInternal(completedTaskInternal);
			if (result == null)
				result = caseCompletedTask(completedTaskInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.MONSTER: {
			Monster monster = (Monster) theEObject;
			T1 result = caseMonster(monster);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.MONSTER_INTERNAL: {
			MonsterInternal monsterInternal = (MonsterInternal) theEObject;
			T1 result = caseMonsterInternal(monsterInternal);
			if (result == null)
				result = caseMonster(monsterInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.ITEM: {
			Item item = (Item) theEObject;
			T1 result = caseItem(item);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.ITEM_INTERNAL: {
			ItemInternal itemInternal = (ItemInternal) theEObject;
			T1 result = caseItemInternal(itemInternal);
			if (result == null)
				result = caseItem(itemInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.PENDING_OPERATION: {
			PendingOperation pendingOperation = (PendingOperation) theEObject;
			T1 result = casePendingOperation(pendingOperation);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.PENDING_OPERATION_INTERNAL: {
			PendingOperationInternal pendingOperationInternal = (PendingOperationInternal) theEObject;
			T1 result = casePendingOperationInternal(pendingOperationInternal);
			if (result == null)
				result = casePendingOperation(pendingOperationInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case MobstersPlayerPackage.COMBINE_MONSTER_PIECES_INTERNAL: {
			CombineMonsterPiecesInternal combineMonsterPiecesInternal = (CombineMonsterPiecesInternal) theEObject;
			T1 result = caseCombineMonsterPiecesInternal(combineMonsterPiecesInternal);
			if (result == null)
				result = casePendingOperationInternal(combineMonsterPiecesInternal);
			if (result == null)
				result = casePendingOperation(combineMonsterPiecesInternal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Director</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Director</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T> T1 caseDirector(Director<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Player</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Player</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePlayer(Player object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Player Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Player Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePlayerInternal(PlayerInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>User Data</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseUserData(UserData object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>User Data Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User Data Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseUserDataInternal(UserDataInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ongoing Task</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ongoing Task</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseOngoingTask(OngoingTask object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ongoing Task Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ongoing Task Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseOngoingTaskInternal(OngoingTaskInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Task Stage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Task Stage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTaskStage(TaskStage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Task Stage Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Task Stage Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTaskStageInternal(TaskStageInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Begin Task Stages Builder</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Begin Task Stages Builder</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseBeginTaskStagesBuilder(BeginTaskStagesBuilder object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Begin Task Stages Builder Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Begin Task Stages Builder Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseBeginTaskStagesBuilderInternal(
			BeginTaskStagesBuilderInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Completed Task</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Completed Task</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCompletedTask(CompletedTask object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Completed Task Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Completed Task Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCompletedTaskInternal(CompletedTaskInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Monster</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Monster</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMonster(Monster object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Monster Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Monster Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMonsterInternal(MonsterInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Item</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Item</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseItem(Item object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Item Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Item Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseItemInternal(ItemInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Pending Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Pending Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePendingOperation(PendingOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Pending Operation Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Pending Operation Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePendingOperationInternal(PendingOperationInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Combine Monster Pieces Internal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Combine Monster Pieces Internal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCombineMonsterPiecesInternal(
			CombineMonsterPiecesInternal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T1 defaultCase(EObject object) {
		return null;
	}

} //MobstersPlayerSwitch
