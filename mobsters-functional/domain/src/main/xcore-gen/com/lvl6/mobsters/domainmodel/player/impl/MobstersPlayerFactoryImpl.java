/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.googlecode.cqengine.IndexedCollection;

import com.lvl6.mobsters.domain.config.IConfigurationRegistry;

import com.lvl6.mobsters.domain.svcreg.StaticRegistry;

import com.lvl6.mobsters.domainmodel.player.*;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		case MobstersPlayerPackage.IMONSTER:
			return createIMonsterFromString(eDataType, initialValue);
		case MobstersPlayerPackage.IITEM:
			return createIItemFromString(eDataType, initialValue);
		case MobstersPlayerPackage.ITASK:
			return createITaskFromString(eDataType, initialValue);
		case MobstersPlayerPackage.ITASK_STAGE:
			return createITaskStageFromString(eDataType, initialValue);
		case MobstersPlayerPackage.ITASK_STAGE_MONSTER:
			return createITaskStageMonsterFromString(eDataType, initialValue);
		case MobstersPlayerPackage.IQUEST:
			return createIQuestFromString(eDataType, initialValue);
		case MobstersPlayerPackage.IQUEST_JOB:
			return createIQuestJobFromString(eDataType, initialValue);
		case MobstersPlayerPackage.IQUEST_JOB_MONSTER_ITEM:
			return createIQuestJobMonsterItemFromString(eDataType, initialValue);
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
		case MobstersPlayerPackage.IMONSTER:
			return convertIMonsterToString(eDataType, instanceValue);
		case MobstersPlayerPackage.IITEM:
			return convertIItemToString(eDataType, instanceValue);
		case MobstersPlayerPackage.ITASK:
			return convertITaskToString(eDataType, instanceValue);
		case MobstersPlayerPackage.ITASK_STAGE:
			return convertITaskStageToString(eDataType, instanceValue);
		case MobstersPlayerPackage.ITASK_STAGE_MONSTER:
			return convertITaskStageMonsterToString(eDataType, instanceValue);
		case MobstersPlayerPackage.IQUEST:
			return convertIQuestToString(eDataType, instanceValue);
		case MobstersPlayerPackage.IQUEST_JOB:
			return convertIQuestJobToString(eDataType, instanceValue);
		case MobstersPlayerPackage.IQUEST_JOB_MONSTER_ITEM:
			return convertIQuestJobMonsterItemToString(eDataType, instanceValue);
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
	public IMonster createIMonster(final String it) {
		final Pattern p = Pattern.compile("^Monster\\[(\\d+)\\]$");
		final Matcher m = p.matcher(it);
		boolean _find = m.find();
		boolean _not = (!_find);
		if (_not) {
			return null;
		}
		IConfigurationRegistry _configurationRegistry = StaticRegistry
				.getConfigurationRegistry();
		String _group = m.group();
		int _parseInt = Integer.parseInt(_group);
		return _configurationRegistry.getMonsterMeta(_parseInt);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMonster createIMonsterFromString(EDataType eDataType,
			String initialValue) {
		return createIMonster(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIMonster(final IMonster it) {
		int _id = it.getId();
		return String.format("Monster[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIMonsterToString(EDataType eDataType,
			Object instanceValue) {
		return convertIMonster((IMonster) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IItem createIItem(final String it) {
		final Pattern p = Pattern.compile("^Item\\[(\\d+)\\]$");
		final Matcher m = p.matcher(it);
		boolean _find = m.find();
		boolean _not = (!_find);
		if (_not) {
			return null;
		}
		IConfigurationRegistry _configurationRegistry = StaticRegistry
				.getConfigurationRegistry();
		String _group = m.group();
		int _parseInt = Integer.parseInt(_group);
		return _configurationRegistry.getItemMeta(_parseInt);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IItem createIItemFromString(EDataType eDataType, String initialValue) {
		return createIItem(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIItem(final IItem it) {
		int _id = it.getId();
		return String.format("Item[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIItemToString(EDataType eDataType, Object instanceValue) {
		return convertIItem((IItem) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITask createITask(final String it) {
		final Pattern p = Pattern.compile("^Task\\[(\\d+)\\]$");
		final Matcher m = p.matcher(it);
		boolean _find = m.find();
		boolean _not = (!_find);
		if (_not) {
			return null;
		}
		IConfigurationRegistry _configurationRegistry = StaticRegistry
				.getConfigurationRegistry();
		String _group = m.group();
		int _parseInt = Integer.parseInt(_group);
		return _configurationRegistry.getTaskMeta(_parseInt);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITask createITaskFromString(EDataType eDataType, String initialValue) {
		return createITask(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertITask(final ITask it) {
		int _id = it.getId();
		String _plus = ("Task[" + Integer.valueOf(_id));
		return (_plus + "]");
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertITaskToString(EDataType eDataType, Object instanceValue) {
		return convertITask((ITask) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITaskStage createITaskStage(final String it) {
		final Pattern p = Pattern.compile("^TaskStage\\[(\\d+)\\]$");
		final Matcher m = p.matcher(it);
		boolean _find = m.find();
		boolean _not = (!_find);
		if (_not) {
			return null;
		}
		IConfigurationRegistry _configurationRegistry = StaticRegistry
				.getConfigurationRegistry();
		String _group = m.group();
		int _parseInt = Integer.parseInt(_group);
		return _configurationRegistry.getTaskStageMeta(_parseInt);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITaskStage createITaskStageFromString(EDataType eDataType,
			String initialValue) {
		return createITaskStage(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertITaskStage(final ITaskStage it) {
		int _id = it.getId();
		return String.format("TaskStage[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertITaskStageToString(EDataType eDataType,
			Object instanceValue) {
		return convertITaskStage((ITaskStage) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITaskStageMonster createITaskStageMonster(final String it) {
		final Pattern p = Pattern.compile("^TaskStageMonster\\[(\\d+)\\]$");
		final Matcher m = p.matcher(it);
		boolean _find = m.find();
		boolean _not = (!_find);
		if (_not) {
			return null;
		}
		IConfigurationRegistry _configurationRegistry = StaticRegistry
				.getConfigurationRegistry();
		String _group = m.group();
		int _parseInt = Integer.parseInt(_group);
		return _configurationRegistry.getTaskStageMonsterMeta(_parseInt);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ITaskStageMonster createITaskStageMonsterFromString(
			EDataType eDataType, String initialValue) {
		return createITaskStageMonster(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertITaskStageMonster(final ITaskStageMonster it) {
		int _id = it.getId();
		return String.format("TaskStageMonster[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertITaskStageMonsterToString(EDataType eDataType,
			Object instanceValue) {
		return convertITaskStageMonster((ITaskStageMonster) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IQuest createIQuestFromString(EDataType eDataType,
			String initialValue) {
		return (IQuest) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIQuestToString(EDataType eDataType,
			Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IQuestJob createIQuestJobFromString(EDataType eDataType,
			String initialValue) {
		return (IQuestJob) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIQuestJobToString(EDataType eDataType,
			Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IQuestJobMonsterItem createIQuestJobMonsterItemFromString(
			EDataType eDataType, String initialValue) {
		return (IQuestJobMonsterItem) super.createFromString(eDataType,
				initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIQuestJobMonsterItemToString(EDataType eDataType,
			Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
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
