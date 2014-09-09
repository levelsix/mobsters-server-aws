/**
 */
package com.lvl6.mobsters.domainmodel.metadata;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.lvl6.mobsters.domainmodel.metadata.MobstersMetaFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/GenModel modelName='metadata' prefix='MobstersMeta' featureDelegation='Reflective' generateSchema='true' loadInitialization='true' modelPluginClass='' resource='XMI' codeFormatting='true' bundleManifest='false' basePackage='com.lvl6.mobsters.domainmodel'"
 * @generated
 */
public interface MobstersMetaPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metadata";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "com.lvl6.mobsters.domainmodel.metadata";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "metadata";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MobstersMetaPackage eINSTANCE = com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl
			.init();

	/**
	 * The meta object id for the '<em>IMonster</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.IMonster
	 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getIMonster()
	 * @generated
	 */
	int IMONSTER = 0;

	/**
	 * The meta object id for the '<em>IItem</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.IItem
	 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getIItem()
	 * @generated
	 */
	int IITEM = 1;

	/**
	 * The meta object id for the '<em>ITask</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.ITask
	 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getITask()
	 * @generated
	 */
	int ITASK = 2;

	/**
	 * The meta object id for the '<em>ITask Stage</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.ITaskStage
	 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getITaskStage()
	 * @generated
	 */
	int ITASK_STAGE = 3;

	/**
	 * The meta object id for the '<em>ITask Stage Monster</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.ITaskStageMonster
	 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getITaskStageMonster()
	 * @generated
	 */
	int ITASK_STAGE_MONSTER = 4;

	/**
	 * The meta object id for the '<em>Quest Meta</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getQuestMeta()
	 * @generated
	 */
	int QUEST_META = 5;

	/**
	 * The meta object id for the '<em>Quest Job Meta</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getQuestJobMeta()
	 * @generated
	 */
	int QUEST_JOB_META = 6;

	/**
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.IMonster <em>IMonster</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IMonster</em>'.
	 * @see com.lvl6.mobsters.info.IMonster
	 * @model instanceClass="com.lvl6.mobsters.info.IMonster"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel create='final <%java.util.regex.Pattern%> p = <%java.util.regex.Pattern%>.compile(\"^Monster\\\\[(\\\\d+)\\\\]$\");\nfinal <%java.util.regex.Matcher%> m = p.matcher(it);\nboolean _find = m.find();\nboolean _not = (!_find);\nif (_not)\n{\n\treturn null;\n}\n<%com.lvl6.mobsters.domain.config.IConfigurationRegistry%> _configurationRegistry = <%com.lvl6.mobsters.domain.svcreg.StaticRegistry%>.getConfigurationRegistry();\n<%java.lang.String%> _group = m.group();\nint _parseInt = <%java.lang.Integer%>.parseInt(_group);\nreturn _configurationRegistry.getMonsterMeta(_parseInt);' convert='int _id = it.getId();\nreturn <%java.lang.String%>.format(\"Monster[%d]\", <%java.lang.Integer%>.valueOf(_id));'"
	 * @generated
	 */
	EDataType getIMonster();

	/**
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.IItem <em>IItem</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IItem</em>'.
	 * @see com.lvl6.mobsters.info.IItem
	 * @model instanceClass="com.lvl6.mobsters.info.IItem"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel create='final <%java.util.regex.Pattern%> p = <%java.util.regex.Pattern%>.compile(\"^Item\\\\[(\\\\d+)\\\\]$\");\nfinal <%java.util.regex.Matcher%> m = p.matcher(it);\nboolean _find = m.find();\nboolean _not = (!_find);\nif (_not)\n{\n\treturn null;\n}\n<%com.lvl6.mobsters.domain.config.IConfigurationRegistry%> _configurationRegistry = <%com.lvl6.mobsters.domain.svcreg.StaticRegistry%>.getConfigurationRegistry();\n<%java.lang.String%> _group = m.group();\nint _parseInt = <%java.lang.Integer%>.parseInt(_group);\nreturn _configurationRegistry.getItemMeta(_parseInt);' convert='int _id = it.getId();\nreturn <%java.lang.String%>.format(\"Item[%d]\", <%java.lang.Integer%>.valueOf(_id));'"
	 * @generated
	 */
	EDataType getIItem();

	/**
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.ITask <em>ITask</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>ITask</em>'.
	 * @see com.lvl6.mobsters.info.ITask
	 * @model instanceClass="com.lvl6.mobsters.info.ITask"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel create='final <%java.util.regex.Pattern%> p = <%java.util.regex.Pattern%>.compile(\"^Task\\\\[(\\\\d+)\\\\]$\");\nfinal <%java.util.regex.Matcher%> m = p.matcher(it);\nboolean _find = m.find();\nboolean _not = (!_find);\nif (_not)\n{\n\treturn null;\n}\n<%com.lvl6.mobsters.domain.config.IConfigurationRegistry%> _configurationRegistry = <%com.lvl6.mobsters.domain.svcreg.StaticRegistry%>.getConfigurationRegistry();\n<%java.lang.String%> _group = m.group();\nint _parseInt = <%java.lang.Integer%>.parseInt(_group);\nreturn _configurationRegistry.getTaskMeta(_parseInt);' convert='int _id = it.getId();\n<%java.lang.String%> _plus = (\"Task[\" + <%java.lang.Integer%>.valueOf(_id));\nreturn (_plus + \"]\");'"
	 * @generated
	 */
	EDataType getITask();

	/**
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.ITaskStage <em>ITask Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>ITask Stage</em>'.
	 * @see com.lvl6.mobsters.info.ITaskStage
	 * @model instanceClass="com.lvl6.mobsters.info.ITaskStage"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel create='final <%java.util.regex.Pattern%> p = <%java.util.regex.Pattern%>.compile(\"^TaskStage\\\\[(\\\\d+)\\\\]$\");\nfinal <%java.util.regex.Matcher%> m = p.matcher(it);\nboolean _find = m.find();\nboolean _not = (!_find);\nif (_not)\n{\n\treturn null;\n}\n<%com.lvl6.mobsters.domain.config.IConfigurationRegistry%> _configurationRegistry = <%com.lvl6.mobsters.domain.svcreg.StaticRegistry%>.getConfigurationRegistry();\n<%java.lang.String%> _group = m.group();\nint _parseInt = <%java.lang.Integer%>.parseInt(_group);\nreturn _configurationRegistry.getTaskStageMeta(_parseInt);' convert='int _id = it.getId();\nreturn <%java.lang.String%>.format(\"TaskStage[%d]\", <%java.lang.Integer%>.valueOf(_id));'"
	 * @generated
	 */
	EDataType getITaskStage();

	/**
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.ITaskStageMonster <em>ITask Stage Monster</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>ITask Stage Monster</em>'.
	 * @see com.lvl6.mobsters.info.ITaskStageMonster
	 * @model instanceClass="com.lvl6.mobsters.info.ITaskStageMonster"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel create='final <%java.util.regex.Pattern%> p = <%java.util.regex.Pattern%>.compile(\"^TaskStageMonster\\\\[(\\\\d+)\\\\]$\");\nfinal <%java.util.regex.Matcher%> m = p.matcher(it);\nboolean _find = m.find();\nboolean _not = (!_find);\nif (_not)\n{\n\treturn null;\n}\n<%com.lvl6.mobsters.domain.config.IConfigurationRegistry%> _configurationRegistry = <%com.lvl6.mobsters.domain.svcreg.StaticRegistry%>.getConfigurationRegistry();\n<%java.lang.String%> _group = m.group();\nint _parseInt = <%java.lang.Integer%>.parseInt(_group);\nreturn _configurationRegistry.getTaskStageMonsterMeta(_parseInt);' convert='int _id = it.getId();\nreturn <%java.lang.String%>.format(\"TaskStageMonster[%d]\", <%java.lang.Integer%>.valueOf(_id));'"
	 * @generated
	 */
	EDataType getITaskStageMonster();

	/**
	 * Returns the meta object for data type '<em>Quest Meta</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Quest Meta</em>'.
	 * @model instanceClass="int"
	 * @generated
	 */
	EDataType getQuestMeta();

	/**
	 * Returns the meta object for data type '<em>Quest Job Meta</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Quest Job Meta</em>'.
	 * @model instanceClass="int"
	 * @generated
	 */
	EDataType getQuestJobMeta();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MobstersMetaFactory getMobstersMetaFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '<em>IMonster</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.IMonster
		 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getIMonster()
		 * @generated
		 */
		EDataType IMONSTER = eINSTANCE.getIMonster();

		/**
		 * The meta object literal for the '<em>IItem</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.IItem
		 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getIItem()
		 * @generated
		 */
		EDataType IITEM = eINSTANCE.getIItem();

		/**
		 * The meta object literal for the '<em>ITask</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.ITask
		 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getITask()
		 * @generated
		 */
		EDataType ITASK = eINSTANCE.getITask();

		/**
		 * The meta object literal for the '<em>ITask Stage</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.ITaskStage
		 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getITaskStage()
		 * @generated
		 */
		EDataType ITASK_STAGE = eINSTANCE.getITaskStage();

		/**
		 * The meta object literal for the '<em>ITask Stage Monster</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.ITaskStageMonster
		 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getITaskStageMonster()
		 * @generated
		 */
		EDataType ITASK_STAGE_MONSTER = eINSTANCE.getITaskStageMonster();

		/**
		 * The meta object literal for the '<em>Quest Meta</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getQuestMeta()
		 * @generated
		 */
		EDataType QUEST_META = eINSTANCE.getQuestMeta();

		/**
		 * The meta object literal for the '<em>Quest Job Meta</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.metadata.impl.MobstersMetaPackageImpl#getQuestJobMeta()
		 * @generated
		 */
		EDataType QUEST_JOB_META = eINSTANCE.getQuestJobMeta();

	}

} //MobstersMetaPackage
