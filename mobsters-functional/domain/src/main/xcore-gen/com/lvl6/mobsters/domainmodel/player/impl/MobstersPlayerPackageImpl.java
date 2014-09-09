/**
 */
package com.lvl6.mobsters.domainmodel.player.impl;

import com.lvl6.mobsters.domainmodel.metadata.MobstersMetaPackage;

import com.lvl6.mobsters.domainmodel.player.MobstersPlayerFactory;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;

import java.io.IOException;

import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MobstersPlayerPackageImpl extends EPackageImpl implements
		MobstersPlayerPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected String packageFilename = "player.ecore";

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass directorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass playerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass playerInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass userDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass userDataInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ongoingTaskEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ongoingTaskInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass taskStageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass taskStageInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beginTaskStagesBuilderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beginTaskStagesBuilderInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass completedTaskEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass completedTaskInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass monsterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass monsterInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass itemEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass itemInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pendingOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pendingOperationInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass combineMonsterPiecesInternalEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum monsterTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum elementTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dateEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType uuidEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType completedTaskIndexEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MobstersPlayerPackageImpl() {
		super(eNS_URI, MobstersPlayerFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MobstersPlayerPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @generated
	 */
	public static MobstersPlayerPackage init() {
		if (isInited)
			return (MobstersPlayerPackage) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI);

		// Obtain or create and register package
		MobstersPlayerPackageImpl theMobstersPlayerPackage = (MobstersPlayerPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof MobstersPlayerPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI) : new MobstersPlayerPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();
		MobstersMetaPackage.eINSTANCE.eClass();

		// Load packages
		theMobstersPlayerPackage.loadPackage();

		// Fix loaded packages
		theMobstersPlayerPackage.fixPackageContents();

		// Mark meta-data to indicate it can't be changed
		theMobstersPlayerPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MobstersPlayerPackage.eNS_URI,
				theMobstersPlayerPackage);
		return theMobstersPlayerPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDirector() {
		if (directorEClass == null) {
			directorEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(3);
		}
		return directorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPlayer() {
		if (playerEClass == null) {
			playerEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(6);
		}
		return playerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPlayer__BeginTask__ITask_Director() {
		return getPlayer().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPlayer__GetCompletedTaskFor__ITask() {
		return getPlayer().getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPlayerInternal() {
		if (playerInternalEClass == null) {
			playerInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(7);
		}
		return playerInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPlayerInternal_UserUuid() {
		return (EAttribute) getPlayerInternal().getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPlayerInternal_Gems() {
		return (EAttribute) getPlayerInternal().getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPlayerInternal_Cash() {
		return (EAttribute) getPlayerInternal().getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPlayerInternal_Oil() {
		return (EAttribute) getPlayerInternal().getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPlayerInternal_Experience() {
		return (EAttribute) getPlayerInternal().getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPlayerInternal_UserData() {
		return (EReference) getPlayerInternal().getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPlayerInternal_OngoingTask() {
		return (EReference) getPlayerInternal().getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPlayerInternal_CompletedTasks() {
		return (EReference) getPlayerInternal().getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPlayerInternal_Items() {
		return (EReference) getPlayerInternal().getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPlayerInternal_Monsters() {
		return (EReference) getPlayerInternal().getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPlayerInternal_TeamSlots() {
		return (EReference) getPlayerInternal().getEStructuralFeatures()
				.get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPlayerInternal_PendingOperations() {
		return (EReference) getPlayerInternal().getEStructuralFeatures()
				.get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPlayerInternal_Indexed() {
		return (EAttribute) getPlayerInternal().getEStructuralFeatures()
				.get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPlayerInternal_CompletedTaskIndex() {
		return (EAttribute) getPlayerInternal().getEStructuralFeatures()
				.get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPlayerInternal__BeginTask__ITask_Director() {
		return getPlayerInternal().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPlayerInternal__CheckForIndices() {
		return getPlayerInternal().getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPlayerInternal__GetCompletedTaskFor__ITask() {
		return getPlayerInternal().getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUserData() {
		if (userDataEClass == null) {
			userDataEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(8);
		}
		return userDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUserDataInternal() {
		if (userDataInternalEClass == null) {
			userDataInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(9);
		}
		return userDataInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUserDataInternal_Player() {
		return (EReference) getUserDataInternal().getEStructuralFeatures().get(
				0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_UdidForHistory() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_DeviceToken() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_FbIdSetOnUserCreate() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_GameCenterId() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_AvatarMonsterMeta() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_LastLogin() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_LastLogout() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_CreateTime() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_LastObstacleSpawnTime() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUserDataInternal_LastMiniJobGeneratedTime() {
		return (EAttribute) getUserDataInternal().getEStructuralFeatures().get(
				10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOngoingTask() {
		if (ongoingTaskEClass == null) {
			ongoingTaskEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(10);
		}
		return ongoingTaskEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getOngoingTask__CompleteTask() {
		return getOngoingTask().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOngoingTaskInternal() {
		if (ongoingTaskInternalEClass == null) {
			ongoingTaskInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(11);
		}
		return ongoingTaskInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOngoingTaskInternal_TaskUuid() {
		return (EAttribute) getOngoingTaskInternal().getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOngoingTaskInternal_Player() {
		return (EReference) getOngoingTaskInternal().getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOngoingTaskInternal_TaskMeta() {
		return (EAttribute) getOngoingTaskInternal().getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOngoingTaskInternal_Stages() {
		return (EReference) getOngoingTaskInternal().getEStructuralFeatures()
				.get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getOngoingTaskInternal__CompleteTask() {
		return getOngoingTaskInternal().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTaskStage() {
		if (taskStageEClass == null) {
			taskStageEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(12);
		}
		return taskStageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTaskStageInternal() {
		if (taskStageInternalEClass == null) {
			taskStageInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(13);
		}
		return taskStageInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_TaskStageUuid() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTaskStageInternal_Task() {
		return (EReference) getTaskStageInternal().getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_TaskStage() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_StageNum() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_TaskStageMonster() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_DmgMultiplier() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_MonsterType() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_MonsterPieceDropped() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_ItemDropped() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_ExpGained() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_CashGained() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskStageInternal_OilGained() {
		return (EAttribute) getTaskStageInternal().getEStructuralFeatures()
				.get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBeginTaskStagesBuilder() {
		if (beginTaskStagesBuilderEClass == null) {
			beginTaskStagesBuilderEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(14);
		}
		return beginTaskStagesBuilderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getBeginTaskStagesBuilder__AddStage__int_ITaskStage_ITaskStageMonster_int_int_int_boolean_IItem() {
		return getBeginTaskStagesBuilder().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBeginTaskStagesBuilderInternal() {
		if (beginTaskStagesBuilderInternalEClass == null) {
			beginTaskStagesBuilderInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(15);
		}
		return beginTaskStagesBuilderInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeginTaskStagesBuilderInternal_NewArtifact() {
		return (EReference) getBeginTaskStagesBuilderInternal()
				.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeginTaskStagesBuilderInternal_Player() {
		return (EReference) getBeginTaskStagesBuilderInternal()
				.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getBeginTaskStagesBuilderInternal__Init__ITask_Player() {
		return getBeginTaskStagesBuilderInternal().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getBeginTaskStagesBuilderInternal__AddStage__int_ITaskStage_ITaskStageMonster_int_int_int_IItem_boolean() {
		return getBeginTaskStagesBuilderInternal().getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getBeginTaskStagesBuilderInternal__Build() {
		return getBeginTaskStagesBuilderInternal().getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCompletedTask() {
		if (completedTaskEClass == null) {
			completedTaskEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(16);
		}
		return completedTaskEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCompletedTaskInternal() {
		if (completedTaskInternalEClass == null) {
			completedTaskInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(17);
		}
		return completedTaskInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCompletedTaskInternal_Player() {
		return (EReference) getCompletedTaskInternal().getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCompletedTaskInternal_TaskMeta() {
		return (EAttribute) getCompletedTaskInternal().getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCompletedTaskInternal_TimeOfEntry() {
		return (EAttribute) getCompletedTaskInternal().getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMonster() {
		if (monsterEClass == null) {
			monsterEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(18);
		}
		return monsterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMonsterInternal() {
		if (monsterInternalEClass == null) {
			monsterInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(19);
		}
		return monsterInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_MonsterUuid() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMonsterInternal_Player() {
		return (EReference) getMonsterInternal().getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_MonsterMeta() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_CurrentExp() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_CurrentLvl() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_CurrentHealth() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_NumPieces() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_IsComplete() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_CombineStartTime() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_TeamSlotNum() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures()
				.get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMonsterInternal_Restricted() {
		return (EAttribute) getMonsterInternal().getEStructuralFeatures().get(
				10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getItem() {
		if (itemEClass == null) {
			itemEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(20);
		}
		return itemEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getItemInternal() {
		if (itemInternalEClass == null) {
			itemInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(21);
		}
		return itemInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getItemInternal_ItemUuid() {
		return (EAttribute) getItemInternal().getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getItemInternal_Player() {
		return (EReference) getItemInternal().getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getItemInternal_ItemMeta() {
		return (EAttribute) getItemInternal().getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPendingOperation() {
		if (pendingOperationEClass == null) {
			pendingOperationEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(22);
		}
		return pendingOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPendingOperation__CheckTimer() {
		return getPendingOperation().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPendingOperationInternal() {
		if (pendingOperationInternalEClass == null) {
			pendingOperationInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(23);
		}
		return pendingOperationInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPendingOperationInternal_Player() {
		return (EReference) getPendingOperationInternal()
				.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPendingOperationInternal_OpStartTimer() {
		return (EAttribute) getPendingOperationInternal()
				.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPendingOperationInternal_OpEndTimer() {
		return (EAttribute) getPendingOperationInternal()
				.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPendingOperationInternal__Happen() {
		return getPendingOperationInternal().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPendingOperationInternal__CheckTimer() {
		return getPendingOperationInternal().getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCombineMonsterPiecesInternal() {
		if (combineMonsterPiecesInternalEClass == null) {
			combineMonsterPiecesInternalEClass = (EClass) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(24);
		}
		return combineMonsterPiecesInternalEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCombineMonsterPiecesInternal_NewMonster() {
		return (EReference) getCombineMonsterPiecesInternal()
				.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getCombineMonsterPiecesInternal__Happen() {
		return getCombineMonsterPiecesInternal().getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getMonsterType() {
		if (monsterTypeEEnum == null) {
			monsterTypeEEnum = (EEnum) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(4);
		}
		return monsterTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getElementType() {
		if (elementTypeEEnum == null) {
			elementTypeEEnum = (EEnum) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(5);
		}
		return elementTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDate() {
		if (dateEDataType == null) {
			dateEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(0);
		}
		return dateEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getUUID() {
		if (uuidEDataType == null) {
			uuidEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(1);
		}
		return uuidEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getCompletedTaskIndex() {
		if (completedTaskIndexEDataType == null) {
			completedTaskIndexEDataType = (EDataType) EPackage.Registry.INSTANCE
					.getEPackage(MobstersPlayerPackage.eNS_URI)
					.getEClassifiers().get(2);
		}
		return completedTaskIndexEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MobstersPlayerFactory getMobstersPlayerFactory() {
		return (MobstersPlayerFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isLoaded = false;

	/**
	 * Laods the package and any sub-packages from their serialized form.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void loadPackage() {
		if (isLoaded)
			return;
		isLoaded = true;

		URL url = getClass().getResource(packageFilename);
		if (url == null) {
			throw new RuntimeException("Missing serialized package: "
					+ packageFilename);
		}
		URI uri = URI.createURI(url.toString());
		Resource resource = new EcoreResourceFactoryImpl().createResource(uri);
		try {
			resource.load(null);
		} catch (IOException exception) {
			throw new WrappedException(exception);
		}
		initializeFromLoadedEPackage(this, (EPackage) resource.getContents()
				.get(0));
		createResource(eNS_URI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isFixed = false;

	/**
	 * Fixes up the loaded package, to make it appear as if it had been programmatically built.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fixPackageContents() {
		if (isFixed)
			return;
		isFixed = true;
		fixEClassifiers();
	}

	/**
	 * Sets the instance class on the given classifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void fixInstanceClass(EClassifier eClassifier) {
		if (eClassifier.getInstanceClassName() == null) {
			eClassifier
					.setInstanceClassName("com.lvl6.mobsters.domainmodel.player."
							+ eClassifier.getName());
			setGeneratedClassName(eClassifier);
		}
	}

} //MobstersPlayerPackageImpl
