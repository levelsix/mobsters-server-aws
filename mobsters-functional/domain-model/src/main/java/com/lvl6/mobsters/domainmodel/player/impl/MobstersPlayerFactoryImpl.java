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
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class MobstersPlayerFactoryImpl extends EFactoryImpl implements
		MobstersPlayerFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
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
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public MobstersPlayerFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PlayerInternal createPlayerInternal() {
		PlayerInternalImpl playerInternal = new PlayerInternalImpl();
		return playerInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public OngoingTaskInternal createOngoingTaskInternal() {
		OngoingTaskInternalImpl ongoingTaskInternal = new OngoingTaskInternalImpl();
		return ongoingTaskInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public TaskStageInternal createTaskStageInternal() {
		TaskStageInternalImpl taskStageInternal = new TaskStageInternalImpl();
		return taskStageInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public BeginTaskStagesBuilderInternal createBeginTaskStagesBuilderInternal() {
		BeginTaskStagesBuilderInternalImpl beginTaskStagesBuilderInternal = new BeginTaskStagesBuilderInternalImpl();
		return beginTaskStagesBuilderInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public CompletedTaskInternal createCompletedTaskInternal() {
		CompletedTaskInternalImpl completedTaskInternal = new CompletedTaskInternalImpl();
		return completedTaskInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public MonsterInternal createMonsterInternal() {
		MonsterInternalImpl monsterInternal = new MonsterInternalImpl();
		return monsterInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ItemInternal createItemInternal() {
		ItemInternalImpl itemInternal = new ItemInternalImpl();
		return itemInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public CombineMonsterPiecesInternal createCombineMonsterPiecesInternal() {
		CombineMonsterPiecesInternalImpl combineMonsterPiecesInternal = new CombineMonsterPiecesInternalImpl();
		return combineMonsterPiecesInternal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public MonsterType createMonsterType(String literal) {
		MonsterType result = MonsterType.get(literal);
		if (result == null)
			throw new IllegalArgumentException("The value '" + literal
					+ "' is not a valid enumerator of '"
					+ MobstersPlayerPackage.Literals.MONSTER_TYPE.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MonsterType createMonsterTypeFromString(EDataType eDataType,
			String initialValue) {
		return createMonsterType(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertMonsterType(MonsterType instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertMonsterTypeToString(EDataType eDataType,
			Object instanceValue) {
		return convertMonsterType((MonsterType) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ElementType createElementType(String literal) {
		ElementType result = ElementType.get(literal);
		if (result == null)
			throw new IllegalArgumentException("The value '" + literal
					+ "' is not a valid enumerator of '"
					+ MobstersPlayerPackage.Literals.ELEMENT_TYPE.getName()
					+ "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ElementType createElementTypeFromString(EDataType eDataType,
			String initialValue) {
		return createElementType(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertElementType(ElementType instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertElementTypeToString(EDataType eDataType,
			Object instanceValue) {
		return convertElementType((ElementType) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IMonster createIMonsterFromString(EDataType eDataType,
			String initialValue) {
		return createIMonster(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertIMonster(final IMonster it) {
		int _id = it.getId();
		return String.format("Monster[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertIMonsterToString(EDataType eDataType,
			Object instanceValue) {
		return convertIMonster((IMonster) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IItem createIItemFromString(EDataType eDataType, String initialValue) {
		return createIItem(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertIItem(final IItem it) {
		int _id = it.getId();
		return String.format("Item[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertIItemToString(EDataType eDataType, Object instanceValue) {
		return convertIItem((IItem) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ITask createITaskFromString(EDataType eDataType, String initialValue) {
		return createITask(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertITask(final ITask it) {
		int _id = it.getId();
		String _plus = ("Task[" + Integer.valueOf(_id));
		return (_plus + "]");
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertITaskToString(EDataType eDataType, Object instanceValue) {
		return convertITask((ITask) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ITaskStage createITaskStageFromString(EDataType eDataType,
			String initialValue) {
		return createITaskStage(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertITaskStage(final ITaskStage it) {
		int _id = it.getId();
		return String.format("TaskStage[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertITaskStageToString(EDataType eDataType,
			Object instanceValue) {
		return convertITaskStage((ITaskStage) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ITaskStageMonster createITaskStageMonsterFromString(
			EDataType eDataType, String initialValue) {
		return createITaskStageMonster(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertITaskStageMonster(final ITaskStageMonster it) {
		int _id = it.getId();
		return String.format("TaskStageMonster[%d]", Integer.valueOf(_id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertITaskStageMonsterToString(EDataType eDataType,
			Object instanceValue) {
		return convertITaskStageMonster((ITaskStageMonster) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IQuest createIQuest(String literal) {
		return (IQuest) super.createFromString(
				MobstersPlayerPackage.Literals.IQUEST, literal);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IQuest createIQuestFromString(EDataType eDataType,
			String initialValue) {
		return createIQuest(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertIQuest(IQuest instanceValue) {
		return super.convertToString(MobstersPlayerPackage.Literals.IQUEST,
				instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertIQuestToString(EDataType eDataType,
			Object instanceValue) {
		return convertIQuest((IQuest) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IQuestJob createIQuestJob(String literal) {
		return (IQuestJob) super.createFromString(
				MobstersPlayerPackage.Literals.IQUEST_JOB, literal);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IQuestJob createIQuestJobFromString(EDataType eDataType,
			String initialValue) {
		return createIQuestJob(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertIQuestJob(IQuestJob instanceValue) {
		return super.convertToString(MobstersPlayerPackage.Literals.IQUEST_JOB,
				instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertIQuestJobToString(EDataType eDataType,
			Object instanceValue) {
		return convertIQuestJob((IQuestJob) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IQuestJobMonsterItem createIQuestJobMonsterItem(String literal) {
		return (IQuestJobMonsterItem) super
				.createFromString(
						MobstersPlayerPackage.Literals.IQUEST_JOB_MONSTER_ITEM,
						literal);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IQuestJobMonsterItem createIQuestJobMonsterItemFromString(
			EDataType eDataType, String initialValue) {
		return createIQuestJobMonsterItem(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertIQuestJobMonsterItem(IQuestJobMonsterItem instanceValue) {
		return super.convertToString(
				MobstersPlayerPackage.Literals.IQUEST_JOB_MONSTER_ITEM,
				instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertIQuestJobMonsterItemToString(EDataType eDataType,
			Object instanceValue) {
		return convertIQuestJobMonsterItem((IQuestJobMonsterItem) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Date createDate(String literal) {
		return (Date) super.createFromString(
				MobstersPlayerPackage.Literals.DATE, literal);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Date createDateFromString(EDataType eDataType, String initialValue) {
		return createDate(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertDate(Date instanceValue) {
		return super.convertToString(MobstersPlayerPackage.Literals.DATE,
				instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertDateToString(EDataType eDataType, Object instanceValue) {
		return convertDate((Date) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public UUID createUUID(final String it) {
		return UUID.fromString(it);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public UUID createUUIDFromString(EDataType eDataType, String initialValue) {
		return createUUID(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertUUID(final UUID it) {
		return it.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertUUIDToString(EDataType eDataType, Object instanceValue) {
		return convertUUID((UUID) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IndexedCollection<CompletedTask> createCompletedTaskIndex(
			String literal) {
		return (IndexedCollection<CompletedTask>) super
				.createFromString(literal);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IndexedCollection<CompletedTask> createCompletedTaskIndexFromString(
			EDataType eDataType, String initialValue) {
		return createCompletedTaskIndex(initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertCompletedTaskIndex(
			IndexedCollection<CompletedTask> instanceValue) {
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public String convertCompletedTaskIndexToString(EDataType eDataType,
			Object instanceValue) {
		return convertCompletedTaskIndex((IndexedCollection<CompletedTask>) instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public MobstersPlayerPackage getMobstersPlayerPackage() {
		return (MobstersPlayerPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MobstersPlayerPackage getPackage() {
		return MobstersPlayerPackage.eINSTANCE;
	}

} // MobstersPlayerFactoryImpl
