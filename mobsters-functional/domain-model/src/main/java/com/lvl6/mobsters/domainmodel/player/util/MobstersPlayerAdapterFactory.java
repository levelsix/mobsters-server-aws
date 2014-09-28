/**
 */
package com.lvl6.mobsters.domainmodel.player.util;

import com.lvl6.mobsters.domainmodel.player.*;

import com.lvl6.mobsters.utility.lambda.Director;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides
 * an adapter <code>createXXX</code> method for each class of the model. <!--
 * end-user-doc -->
 * 
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage
 * @generated
 */
public class MobstersPlayerAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static MobstersPlayerPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public MobstersPlayerAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MobstersPlayerPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code> if
	 * the object is either the model's package or is an instance object of the
	 * model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MobstersPlayerSwitch<Adapter> modelSwitch = new MobstersPlayerSwitch<Adapter>() {
		@Override
		public <T> Adapter caseDirector(Director<T> object) {
			return createDirectorAdapter();
		}

		@Override
		public Adapter casePlayer(Player object) {
			return createPlayerAdapter();
		}

		@Override
		public Adapter casePlayerInternal(PlayerInternal object) {
			return createPlayerInternalAdapter();
		}

		@Override
		public Adapter caseOngoingTask(OngoingTask object) {
			return createOngoingTaskAdapter();
		}

		@Override
		public Adapter caseOngoingTaskInternal(OngoingTaskInternal object) {
			return createOngoingTaskInternalAdapter();
		}

		@Override
		public Adapter caseTaskStage(TaskStage object) {
			return createTaskStageAdapter();
		}

		@Override
		public Adapter caseTaskStageInternal(TaskStageInternal object) {
			return createTaskStageInternalAdapter();
		}

		@Override
		public Adapter caseBeginTaskStagesBuilder(BeginTaskStagesBuilder object) {
			return createBeginTaskStagesBuilderAdapter();
		}

		@Override
		public Adapter caseBeginTaskStagesBuilderInternal(
				BeginTaskStagesBuilderInternal object) {
			return createBeginTaskStagesBuilderInternalAdapter();
		}

		@Override
		public Adapter caseCompletedTask(CompletedTask object) {
			return createCompletedTaskAdapter();
		}

		@Override
		public Adapter caseCompletedTaskInternal(CompletedTaskInternal object) {
			return createCompletedTaskInternalAdapter();
		}

		@Override
		public Adapter caseMonster(Monster object) {
			return createMonsterAdapter();
		}

		@Override
		public Adapter caseMonsterInternal(MonsterInternal object) {
			return createMonsterInternalAdapter();
		}

		@Override
		public Adapter caseItem(Item object) {
			return createItemAdapter();
		}

		@Override
		public Adapter caseItemInternal(ItemInternal object) {
			return createItemInternalAdapter();
		}

		@Override
		public Adapter casePendingOperation(PendingOperation object) {
			return createPendingOperationAdapter();
		}

		@Override
		public Adapter casePendingOperationInternal(
				PendingOperationInternal object) {
			return createPendingOperationInternalAdapter();
		}

		@Override
		public Adapter caseCombineMonsterPiecesInternal(
				CombineMonsterPiecesInternal object) {
			return createCombineMonsterPiecesInternalAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.utility.lambda.Director <em>Director</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.utility.lambda.Director
	 * @generated
	 */
	public Adapter createDirectorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.Player <em>Player</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.Player
	 * @generated
	 */
	public Adapter createPlayerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.PlayerInternal
	 * <em>Player Internal</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal
	 * @generated
	 */
	public Adapter createPlayerInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.OngoingTask
	 * <em>Ongoing Task</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTask
	 * @generated
	 */
	public Adapter createOngoingTaskAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal
	 * <em>Ongoing Task Internal</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal
	 * @generated
	 */
	public Adapter createOngoingTaskInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.TaskStage
	 * <em>Task Stage</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStage
	 * @generated
	 */
	public Adapter createTaskStageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal
	 * <em>Task Stage Internal</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal
	 * @generated
	 */
	public Adapter createTaskStageInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder
	 * <em>Begin Task Stages Builder</em>}'. <!-- begin-user-doc --> This
	 * default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases
	 * anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder
	 * @generated
	 */
	public Adapter createBeginTaskStagesBuilderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal
	 * <em>Begin Task Stages Builder Internal</em>}'. <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore
	 * cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal
	 * @generated
	 */
	public Adapter createBeginTaskStagesBuilderInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTask
	 * <em>Completed Task</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTask
	 * @generated
	 */
	public Adapter createCompletedTaskAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal
	 * <em>Completed Task Internal</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal
	 * @generated
	 */
	public Adapter createCompletedTaskInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.Monster <em>Monster</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.Monster
	 * @generated
	 */
	public Adapter createMonsterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.MonsterInternal
	 * <em>Monster Internal</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal
	 * @generated
	 */
	public Adapter createMonsterInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.Item <em>Item</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.Item
	 * @generated
	 */
	public Adapter createItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.ItemInternal
	 * <em>Item Internal</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.ItemInternal
	 * @generated
	 */
	public Adapter createItemInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperation
	 * <em>Pending Operation</em>}'. <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperation
	 * @generated
	 */
	public Adapter createPendingOperationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal
	 * <em>Pending Operation Internal</em>}'. <!-- begin-user-doc --> This
	 * default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases
	 * anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal
	 * @generated
	 */
	public Adapter createPendingOperationInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '
	 * {@link com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal
	 * <em>Combine Monster Pieces Internal</em>}'. <!-- begin-user-doc --> This
	 * default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases
	 * anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal
	 * @generated
	 */
	public Adapter createCombineMonsterPiecesInternalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This
	 * default implementation returns null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // MobstersPlayerAdapterFactory
