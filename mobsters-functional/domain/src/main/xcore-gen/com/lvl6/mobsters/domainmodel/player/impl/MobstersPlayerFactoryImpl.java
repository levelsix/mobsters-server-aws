/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.googlecode.cqengine.IndexedCollection;

import com.lvl6.mobsters.domainmodel.player.*;

import java.util.Date;
import java.util.UUID;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MobstersPlayerFactoryImpl extends EFactoryImpl implements
		MobstersPlayerFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MobstersPlayerFactory init() {
		try {
			MobstersPlayerFactory theMobstersPlayerFactory = (MobstersPlayerFactory) EPackage.Registry.INSTANCE
					.getEFactory(MobstersPlayerPackage.eNS_URI);
			if (theMobstersPlayerFactory != null) {
				return theMobstersPlayerFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MobstersPlayerFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MobstersPlayerFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case MobstersPlayerPackage.PLAYER_INTERNAL:
			return createPlayerInternal();
		case MobstersPlayerPackage.USER_DATA_INTERNAL:
			return createUserDataInternal();
		case MobstersPlayerPackage.ONGOING_TASK_INTERNAL:
			return createOngoingTaskInternal();
		case MobstersPlayerPackage.TASK_STAGE_INTERNAL:
			return createTaskStageInternal();
		case MobstersPlayerPackage.BEGIN_TASK_STAGES_BUILDER_INTERNAL:
			return createBeginTaskStagesBuilderInternal();
		case MobstersPlayerPackage.COMPLETED_TASK_INTERNAL:
			return createCompletedTaskInternal();
		case MobstersPlayerPackage.MONSTER_INTERNAL:
			return createMonsterInternal();
		case MobstersPlayerPackage.ITEM_INTERNAL:
			return createItemInternal();
		case MobstersPlayerPackage.COMBINE_MONSTER_PIECES_INTERNAL:
			return createCombineMonsterPiecesInternal();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case MobstersPlayerPackage.MONSTER_TYPE:
			return createMonsterTypeFromString(eDataType, initialValue);
		case MobstersPlayerPackage.ELEMENT_TYPE:
			return createElementTypeFromString(eDataType, initialValue);
		case MobstersPlayerPackage.DATE:
			return createDateFromString(eDataType, initialValue);
		case MobstersPlayerPackage.UUID:
			return createUUIDFromString(eDataType, initialValue);
		case MobstersPlayerPackage.COMPLETED_TASK_INDEX:
			return createCompletedTaskIndexFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '"
					+ eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case MobstersPlayerPackage.MONSTER_TYPE:
			return convertMonsterTypeToString(eDataType, instanceValue);
		case MobstersPlayerPackage.ELEMENT_TYPE:
			return convertElementTypeToString(eDataType, instanceValue);
		case MobstersPlayerPackage.DATE:
			return convertDateToString(eDataType, instanceValue);
		case MobstersPlayerPackage.UUID:
			return convertUUIDToString(eDataType, instanceValue);
		case MobstersPlayerPackage.COMPLETED_TASK_INDEX:
			return convertCompletedTaskIndexToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '"
					+ eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlayerInternal createPlayerInternal() {
		PlayerInternalImpl playerInternal = new PlayerInternalImpl();
		return playerInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserDataInternal createUserDataInternal() {
		UserDataInternalImpl userDataInternal = new UserDataInternalImpl();
		return userDataInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OngoingTaskInternal createOngoingTaskInternal() {
		OngoingTaskInternalImpl ongoingTaskInternal = new OngoingTaskInternalImpl();
		return ongoingTaskInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TaskStageInternal createTaskStageInternal() {
		TaskStageInternalImpl taskStageInternal = new TaskStageInternalImpl();
		return taskStageInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BeginTaskStagesBuilderInternal createBeginTaskStagesBuilderInternal() {
		BeginTaskStagesBuilderInternalImpl beginTaskStagesBuilderInternal = new BeginTaskStagesBuilderInternalImpl();
		return beginTaskStagesBuilderInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CompletedTaskInternal createCompletedTaskInternal() {
		CompletedTaskInternalImpl completedTaskInternal = new CompletedTaskInternalImpl();
		return completedTaskInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MonsterInternal createMonsterInternal() {
		MonsterInternalImpl monsterInternal = new MonsterInternalImpl();
		return monsterInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ItemInternal createItemInternal() {
		ItemInternalImpl itemInternal = new ItemInternalImpl();
		return itemInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CombineMonsterPiecesInternal createCombineMonsterPiecesInternal() {
		CombineMonsterPiecesInternalImpl combineMonsterPiecesInternal = new CombineMonsterPiecesInternalImpl();
		return combineMonsterPiecesInternal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MonsterType createMonsterTypeFromString(EDataType eDataType,
			String initialValue) {
		MonsterType result = MonsterType.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue
					+ "' is not a valid enumerator of '" + eDataType.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMonsterTypeToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ElementType createElementTypeFromString(EDataType eDataType,
			String initialValue) {
		ElementType result = ElementType.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue
					+ "' is not a valid enumerator of '" + eDataType.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertElementTypeToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date createDateFromString(EDataType eDataType, String initialValue) {
		return (Date) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDateToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUID createUUID(final String it) {
		return UUID.fromString(it);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUID createUUIDFromString(EDataType eDataType, String initialValue) {
		return createUUID(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUUID(final UUID it) {
		return it.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUUIDToString(EDataType eDataType, Object instanceValue) {
		return convertUUID((UUID) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public IndexedCollection<CompletedTask> createCompletedTaskIndexFromString(
			EDataType eDataType, String initialValue) {
		return (IndexedCollection<CompletedTask>) super
				.createFromString(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCompletedTaskIndexToString(EDataType eDataType,
			Object instanceValue) {
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MobstersPlayerPackage getMobstersPlayerPackage() {
		return (MobstersPlayerPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MobstersPlayerPackage getPackage() {
		return MobstersPlayerPackage.eINSTANCE;
	}

} //MobstersPlayerFactoryImpl
