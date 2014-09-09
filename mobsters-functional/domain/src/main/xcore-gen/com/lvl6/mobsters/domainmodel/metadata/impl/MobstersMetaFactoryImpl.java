/**
 */
package com.lvl6.mobsters.domainmodel.metadata.impl;

import com.lvl6.mobsters.domain.config.IConfigurationRegistry;

import com.lvl6.mobsters.domain.svcreg.StaticRegistry;

import com.lvl6.mobsters.domainmodel.metadata.*;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

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
public class MobstersMetaFactoryImpl extends EFactoryImpl implements
		MobstersMetaFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MobstersMetaFactory init() {
		try {
			MobstersMetaFactory theMobstersMetaFactory = (MobstersMetaFactory) EPackage.Registry.INSTANCE
					.getEFactory(MobstersMetaPackage.eNS_URI);
			if (theMobstersMetaFactory != null) {
				return theMobstersMetaFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MobstersMetaFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MobstersMetaFactoryImpl() {
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
		case MobstersMetaPackage.IMONSTER:
			return createIMonsterFromString(eDataType, initialValue);
		case MobstersMetaPackage.IITEM:
			return createIItemFromString(eDataType, initialValue);
		case MobstersMetaPackage.ITASK:
			return createITaskFromString(eDataType, initialValue);
		case MobstersMetaPackage.ITASK_STAGE:
			return createITaskStageFromString(eDataType, initialValue);
		case MobstersMetaPackage.ITASK_STAGE_MONSTER:
			return createITaskStageMonsterFromString(eDataType, initialValue);
		case MobstersMetaPackage.QUEST_META:
			return createQuestMetaFromString(eDataType, initialValue);
		case MobstersMetaPackage.QUEST_JOB_META:
			return createQuestJobMetaFromString(eDataType, initialValue);
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
		case MobstersMetaPackage.IMONSTER:
			return convertIMonsterToString(eDataType, instanceValue);
		case MobstersMetaPackage.IITEM:
			return convertIItemToString(eDataType, instanceValue);
		case MobstersMetaPackage.ITASK:
			return convertITaskToString(eDataType, instanceValue);
		case MobstersMetaPackage.ITASK_STAGE:
			return convertITaskStageToString(eDataType, instanceValue);
		case MobstersMetaPackage.ITASK_STAGE_MONSTER:
			return convertITaskStageMonsterToString(eDataType, instanceValue);
		case MobstersMetaPackage.QUEST_META:
			return convertQuestMetaToString(eDataType, instanceValue);
		case MobstersMetaPackage.QUEST_JOB_META:
			return convertQuestJobMetaToString(eDataType, instanceValue);
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
	public Integer createQuestMetaFromString(EDataType eDataType,
			String initialValue) {
		return (Integer) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertQuestMetaToString(EDataType eDataType,
			Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createQuestJobMetaFromString(EDataType eDataType,
			String initialValue) {
		return (Integer) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertQuestJobMetaToString(EDataType eDataType,
			Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MobstersMetaPackage getMobstersMetaPackage() {
		return (MobstersMetaPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MobstersMetaPackage getPackage() {
		return MobstersMetaPackage.eINSTANCE;
	}

} //MobstersMetaFactoryImpl
