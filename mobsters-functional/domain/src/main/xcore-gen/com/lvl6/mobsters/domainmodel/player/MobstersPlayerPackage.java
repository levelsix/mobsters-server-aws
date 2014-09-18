/**
 */
package com.lvl6.mobsters.domainmodel.player;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see com.lvl6.mobsters.domainmodel.player.MobstersPlayerFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/GenModel modelName='player' prefix='MobstersPlayer' featureDelegation='Reflective' generateSchema='true' loadInitialization='true' resource='XMI' codeFormatting='true' bundleManifest='false' publicationLocation='/domain/src/main/model' basePackage='com.lvl6.mobsters.domainmodel'"
 * @generated
 */
public interface MobstersPlayerPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "player";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "com.lvl6.mobsters.domainmodel.player";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "player";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MobstersPlayerPackage eINSTANCE = com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl
			.init();

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.utility.lambda.Director <em>Director</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.utility.lambda.Director
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getDirector()
	 * @generated
	 */
	int DIRECTOR = 0;

	/**
	 * The number of structural features of the '<em>Director</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTOR_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Director</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECTOR_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.Player <em>Player</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.Player
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPlayer()
	 * @generated
	 */
	int PLAYER = 1;

	/**
	 * The number of structural features of the '<em>Player</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Begin Task</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER___BEGIN_TASK__ITASK_DIRECTOR = 0;

	/**
	 * The operation id for the '<em>Get Completed Task For</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER___GET_COMPLETED_TASK_FOR__ITASK = 1;

	/**
	 * The number of operations of the '<em>Player</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_OPERATION_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl <em>Player Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPlayerInternal()
	 * @generated
	 */
	int PLAYER_INTERNAL = 2;

	/**
	 * The feature id for the '<em><b>User Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__USER_UUID = PLAYER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Gems</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__GEMS = PLAYER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cash</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__CASH = PLAYER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Oil</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__OIL = PLAYER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Experience</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__EXPERIENCE = PLAYER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Ongoing Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__ONGOING_TASK = PLAYER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Completed Tasks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__COMPLETED_TASKS = PLAYER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Items</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__ITEMS = PLAYER_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Monsters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__MONSTERS = PLAYER_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Team Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__TEAM_SLOTS = PLAYER_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Pending Operations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__PENDING_OPERATIONS = PLAYER_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Udid For History</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__UDID_FOR_HISTORY = PLAYER_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Device Token</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__DEVICE_TOKEN = PLAYER_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Fb Id Set On User Create</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__FB_ID_SET_ON_USER_CREATE = PLAYER_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Game Center Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__GAME_CENTER_ID = PLAYER_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Avatar Monster Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__AVATAR_MONSTER_META = PLAYER_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Last Login</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__LAST_LOGIN = PLAYER_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Last Logout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__LAST_LOGOUT = PLAYER_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Create Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__CREATE_TIME = PLAYER_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Last Obstacle Spawn Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__LAST_OBSTACLE_SPAWN_TIME = PLAYER_FEATURE_COUNT + 19;

	/**
	 * The feature id for the '<em><b>Last Mini Job Generated Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__LAST_MINI_JOB_GENERATED_TIME = PLAYER_FEATURE_COUNT + 20;

	/**
	 * The feature id for the '<em><b>Indexed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__INDEXED = PLAYER_FEATURE_COUNT + 21;

	/**
	 * The feature id for the '<em><b>Completed Task Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL__COMPLETED_TASK_INDEX = PLAYER_FEATURE_COUNT + 22;

	/**
	 * The number of structural features of the '<em>Player Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL_FEATURE_COUNT = PLAYER_FEATURE_COUNT + 23;

	/**
	 * The operation id for the '<em>Begin Task</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL___BEGIN_TASK__ITASK_DIRECTOR = PLAYER_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Check For Indices</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL___CHECK_FOR_INDICES = PLAYER_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Completed Task For</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL___GET_COMPLETED_TASK_FOR__ITASK = PLAYER_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Player Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAYER_INTERNAL_OPERATION_COUNT = PLAYER_OPERATION_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.OngoingTask <em>Ongoing Task</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTask
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getOngoingTask()
	 * @generated
	 */
	int ONGOING_TASK = 3;

	/**
	 * The number of structural features of the '<em>Ongoing Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Complete Task</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK___COMPLETE_TASK = 0;

	/**
	 * The number of operations of the '<em>Ongoing Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl <em>Ongoing Task Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getOngoingTaskInternal()
	 * @generated
	 */
	int ONGOING_TASK_INTERNAL = 4;

	/**
	 * The feature id for the '<em><b>Task Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_INTERNAL__TASK_UUID = ONGOING_TASK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Player</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_INTERNAL__PLAYER = ONGOING_TASK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Task Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_INTERNAL__TASK_META = ONGOING_TASK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Stages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_INTERNAL__STAGES = ONGOING_TASK_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Ongoing Task Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_INTERNAL_FEATURE_COUNT = ONGOING_TASK_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Complete Task</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_INTERNAL___COMPLETE_TASK = ONGOING_TASK_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Ongoing Task Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ONGOING_TASK_INTERNAL_OPERATION_COUNT = ONGOING_TASK_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.TaskStage <em>Task Stage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStage
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getTaskStage()
	 * @generated
	 */
	int TASK_STAGE = 5;

	/**
	 * The number of structural features of the '<em>Task Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Task Stage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl <em>Task Stage Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getTaskStageInternal()
	 * @generated
	 */
	int TASK_STAGE_INTERNAL = 6;

	/**
	 * The feature id for the '<em><b>Task Stage Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__TASK_STAGE_UUID = TASK_STAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Task</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__TASK = TASK_STAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Task Stage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__TASK_STAGE = TASK_STAGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Stage Num</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__STAGE_NUM = TASK_STAGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Task Stage Monster</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__TASK_STAGE_MONSTER = TASK_STAGE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Dmg Multiplier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__DMG_MULTIPLIER = TASK_STAGE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Monster Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__MONSTER_TYPE = TASK_STAGE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Monster Piece Dropped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__MONSTER_PIECE_DROPPED = TASK_STAGE_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Item Dropped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__ITEM_DROPPED = TASK_STAGE_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Exp Gained</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__EXP_GAINED = TASK_STAGE_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Cash Gained</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__CASH_GAINED = TASK_STAGE_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Oil Gained</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL__OIL_GAINED = TASK_STAGE_FEATURE_COUNT + 11;

	/**
	 * The number of structural features of the '<em>Task Stage Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL_FEATURE_COUNT = TASK_STAGE_FEATURE_COUNT + 12;

	/**
	 * The number of operations of the '<em>Task Stage Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_STAGE_INTERNAL_OPERATION_COUNT = TASK_STAGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder <em>Begin Task Stages Builder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getBeginTaskStagesBuilder()
	 * @generated
	 */
	int BEGIN_TASK_STAGES_BUILDER = 7;

	/**
	 * The number of structural features of the '<em>Begin Task Stages Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Add Stage</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_BOOLEAN_IITEM = 0;

	/**
	 * The number of operations of the '<em>Begin Task Stages Builder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.BeginTaskStagesBuilderInternalImpl <em>Begin Task Stages Builder Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.BeginTaskStagesBuilderInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getBeginTaskStagesBuilderInternal()
	 * @generated
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL = 8;

	/**
	 * The feature id for the '<em><b>New Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL__NEW_ARTIFACT = BEGIN_TASK_STAGES_BUILDER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Player</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL__PLAYER = BEGIN_TASK_STAGES_BUILDER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Begin Task Stages Builder Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL_FEATURE_COUNT = BEGIN_TASK_STAGES_BUILDER_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Add Stage</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_BOOLEAN_IITEM = BEGIN_TASK_STAGES_BUILDER___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_BOOLEAN_IITEM;

	/**
	 * The operation id for the '<em>Init</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL___INIT__ITASK_PLAYER = BEGIN_TASK_STAGES_BUILDER_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Add Stage</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_IITEM_BOOLEAN = BEGIN_TASK_STAGES_BUILDER_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Build</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL___BUILD = BEGIN_TASK_STAGES_BUILDER_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Begin Task Stages Builder Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEGIN_TASK_STAGES_BUILDER_INTERNAL_OPERATION_COUNT = BEGIN_TASK_STAGES_BUILDER_OPERATION_COUNT + 3;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.CompletedTask <em>Completed Task</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTask
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCompletedTask()
	 * @generated
	 */
	int COMPLETED_TASK = 9;

	/**
	 * The number of structural features of the '<em>Completed Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLETED_TASK_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Completed Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLETED_TASK_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.CompletedTaskInternalImpl <em>Completed Task Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.CompletedTaskInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCompletedTaskInternal()
	 * @generated
	 */
	int COMPLETED_TASK_INTERNAL = 10;

	/**
	 * The feature id for the '<em><b>Player</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLETED_TASK_INTERNAL__PLAYER = COMPLETED_TASK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Task Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLETED_TASK_INTERNAL__TASK_META = COMPLETED_TASK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Time Of Entry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLETED_TASK_INTERNAL__TIME_OF_ENTRY = COMPLETED_TASK_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Completed Task Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLETED_TASK_INTERNAL_FEATURE_COUNT = COMPLETED_TASK_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Completed Task Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLETED_TASK_INTERNAL_OPERATION_COUNT = COMPLETED_TASK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.Monster <em>Monster</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.Monster
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getMonster()
	 * @generated
	 */
	int MONSTER = 11;

	/**
	 * The number of structural features of the '<em>Monster</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Monster</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl <em>Monster Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getMonsterInternal()
	 * @generated
	 */
	int MONSTER_INTERNAL = 12;

	/**
	 * The feature id for the '<em><b>Monster Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__MONSTER_UUID = MONSTER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Player</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__PLAYER = MONSTER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Monster Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__MONSTER_META = MONSTER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Current Exp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__CURRENT_EXP = MONSTER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Current Lvl</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__CURRENT_LVL = MONSTER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Current Health</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__CURRENT_HEALTH = MONSTER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Num Pieces</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__NUM_PIECES = MONSTER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Is Complete</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__IS_COMPLETE = MONSTER_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Combine Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__COMBINE_START_TIME = MONSTER_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Team Slot Num</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__TEAM_SLOT_NUM = MONSTER_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Restricted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL__RESTRICTED = MONSTER_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>Monster Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL_FEATURE_COUNT = MONSTER_FEATURE_COUNT + 11;

	/**
	 * The number of operations of the '<em>Monster Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONSTER_INTERNAL_OPERATION_COUNT = MONSTER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.Item <em>Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.Item
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getItem()
	 * @generated
	 */
	int ITEM = 13;

	/**
	 * The number of structural features of the '<em>Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.ItemInternalImpl <em>Item Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.ItemInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getItemInternal()
	 * @generated
	 */
	int ITEM_INTERNAL = 14;

	/**
	 * The feature id for the '<em><b>Item Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_INTERNAL__ITEM_UUID = ITEM_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Player</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_INTERNAL__PLAYER = ITEM_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Item Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_INTERNAL__ITEM_META = ITEM_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Item Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_INTERNAL_FEATURE_COUNT = ITEM_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Item Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITEM_INTERNAL_OPERATION_COUNT = ITEM_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.PendingOperation <em>Pending Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperation
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPendingOperation()
	 * @generated
	 */
	int PENDING_OPERATION = 15;

	/**
	 * The number of structural features of the '<em>Pending Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Check Timer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION___CHECK_TIMER = 0;

	/**
	 * The number of operations of the '<em>Pending Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.PendingOperationInternalImpl <em>Pending Operation Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.PendingOperationInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPendingOperationInternal()
	 * @generated
	 */
	int PENDING_OPERATION_INTERNAL = 16;

	/**
	 * The feature id for the '<em><b>Player</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_INTERNAL__PLAYER = PENDING_OPERATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Op Start Timer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_INTERNAL__OP_START_TIMER = PENDING_OPERATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Op End Timer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_INTERNAL__OP_END_TIMER = PENDING_OPERATION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Pending Operation Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_INTERNAL_FEATURE_COUNT = PENDING_OPERATION_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Happen</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_INTERNAL___HAPPEN = PENDING_OPERATION_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Check Timer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_INTERNAL___CHECK_TIMER = PENDING_OPERATION_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Pending Operation Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PENDING_OPERATION_INTERNAL_OPERATION_COUNT = PENDING_OPERATION_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.impl.CombineMonsterPiecesInternalImpl <em>Combine Monster Pieces Internal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.impl.CombineMonsterPiecesInternalImpl
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCombineMonsterPiecesInternal()
	 * @generated
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL = 17;

	/**
	 * The feature id for the '<em><b>Player</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL__PLAYER = PENDING_OPERATION_INTERNAL__PLAYER;

	/**
	 * The feature id for the '<em><b>Op Start Timer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL__OP_START_TIMER = PENDING_OPERATION_INTERNAL__OP_START_TIMER;

	/**
	 * The feature id for the '<em><b>Op End Timer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL__OP_END_TIMER = PENDING_OPERATION_INTERNAL__OP_END_TIMER;

	/**
	 * The feature id for the '<em><b>New Monster</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL__NEW_MONSTER = PENDING_OPERATION_INTERNAL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Combine Monster Pieces Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL_FEATURE_COUNT = PENDING_OPERATION_INTERNAL_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Check Timer</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL___CHECK_TIMER = PENDING_OPERATION_INTERNAL___CHECK_TIMER;

	/**
	 * The operation id for the '<em>Happen</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL___HAPPEN = PENDING_OPERATION_INTERNAL_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Combine Monster Pieces Internal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMBINE_MONSTER_PIECES_INTERNAL_OPERATION_COUNT = PENDING_OPERATION_INTERNAL_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.MonsterType <em>Monster Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterType
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getMonsterType()
	 * @generated
	 */
	int MONSTER_TYPE = 18;

	/**
	 * The meta object id for the '{@link com.lvl6.mobsters.domainmodel.player.ElementType <em>Element Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.domainmodel.player.ElementType
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getElementType()
	 * @generated
	 */
	int ELEMENT_TYPE = 19;

	/**
	 * The meta object id for the '<em>IMonster</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.IMonster
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIMonster()
	 * @generated
	 */
	int IMONSTER = 20;

	/**
	 * The meta object id for the '<em>IItem</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.IItem
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIItem()
	 * @generated
	 */
	int IITEM = 21;

	/**
	 * The meta object id for the '<em>ITask</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.ITask
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getITask()
	 * @generated
	 */
	int ITASK = 22;

	/**
	 * The meta object id for the '<em>ITask Stage</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.ITaskStage
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getITaskStage()
	 * @generated
	 */
	int ITASK_STAGE = 23;

	/**
	 * The meta object id for the '<em>ITask Stage Monster</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.ITaskStageMonster
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getITaskStageMonster()
	 * @generated
	 */
	int ITASK_STAGE_MONSTER = 24;

	/**
	 * The meta object id for the '<em>IQuest</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.IQuest
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIQuest()
	 * @generated
	 */
	int IQUEST = 25;

	/**
	 * The meta object id for the '<em>IQuest Job</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.IQuestJob
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIQuestJob()
	 * @generated
	 */
	int IQUEST_JOB = 26;

	/**
	 * The meta object id for the '<em>IQuest Job Monster Item</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIQuestJobMonsterItem()
	 * @generated
	 */
	int IQUEST_JOB_MONSTER_ITEM = 27;

	/**
	 * The meta object id for the '<em>Date</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Date
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getDate()
	 * @generated
	 */
	int DATE = 28;

	/**
	 * The meta object id for the '<em>UUID</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.UUID
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getUUID()
	 * @generated
	 */
	int UUID = 29;

	/**
	 * The meta object id for the '<em>Completed Task Index</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.googlecode.cqengine.IndexedCollection
	 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCompletedTaskIndex()
	 * @generated
	 */
	int COMPLETED_TASK_INDEX = 30;

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.utility.lambda.Director <em>Director</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Director</em>'.
	 * @see com.lvl6.mobsters.utility.lambda.Director
	 * @model instanceClass="com.lvl6.mobsters.utility.lambda.Director" typeParameters="T"
	 * @generated
	 */
	EClass getDirector();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.Player <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Player</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.Player
	 * @generated
	 */
	EClass getPlayer();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.Player#beginTask(com.lvl6.mobsters.info.ITask, com.lvl6.mobsters.utility.lambda.Director) <em>Begin Task</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Begin Task</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.Player#beginTask(com.lvl6.mobsters.info.ITask, com.lvl6.mobsters.utility.lambda.Director)
	 * @generated
	 */
	EOperation getPlayer__BeginTask__ITask_Director();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.Player#getCompletedTaskFor(com.lvl6.mobsters.info.ITask) <em>Get Completed Task For</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Completed Task For</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.Player#getCompletedTaskFor(com.lvl6.mobsters.info.ITask)
	 * @generated
	 */
	EOperation getPlayer__GetCompletedTaskFor__ITask();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal <em>Player Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Player Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal
	 * @generated
	 */
	EClass getPlayerInternal();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserUuid <em>User Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>User Uuid</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUserUuid()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_UserUuid();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGems <em>Gems</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Gems</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGems()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_Gems();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCash <em>Cash</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cash</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCash()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_Cash();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOil <em>Oil</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Oil</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOil()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_Oil();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getExperience <em>Experience</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Experience</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getExperience()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_Experience();

	/**
	 * Returns the meta object for the containment reference '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOngoingTask <em>Ongoing Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ongoing Task</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getOngoingTask()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EReference getPlayerInternal_OngoingTask();

	/**
	 * Returns the meta object for the containment reference list '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTasks <em>Completed Tasks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Completed Tasks</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTasks()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EReference getPlayerInternal_CompletedTasks();

	/**
	 * Returns the meta object for the containment reference list '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getItems <em>Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Items</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getItems()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EReference getPlayerInternal_Items();

	/**
	 * Returns the meta object for the containment reference list '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getMonsters <em>Monsters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Monsters</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getMonsters()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EReference getPlayerInternal_Monsters();

	/**
	 * Returns the meta object for the reference list '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getTeamSlots <em>Team Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Team Slots</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getTeamSlots()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EReference getPlayerInternal_TeamSlots();

	/**
	 * Returns the meta object for the containment reference list '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getPendingOperations <em>Pending Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Pending Operations</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getPendingOperations()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EReference getPlayerInternal_PendingOperations();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUdidForHistory <em>Udid For History</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Udid For History</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getUdidForHistory()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_UdidForHistory();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getDeviceToken <em>Device Token</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Device Token</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getDeviceToken()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_DeviceToken();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#isFbIdSetOnUserCreate <em>Fb Id Set On User Create</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fb Id Set On User Create</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#isFbIdSetOnUserCreate()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_FbIdSetOnUserCreate();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGameCenterId <em>Game Center Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Game Center Id</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getGameCenterId()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_GameCenterId();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getAvatarMonsterMeta <em>Avatar Monster Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Avatar Monster Meta</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getAvatarMonsterMeta()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_AvatarMonsterMeta();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogin <em>Last Login</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Login</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogin()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_LastLogin();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogout <em>Last Logout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Logout</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastLogout()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_LastLogout();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCreateTime <em>Create Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Create Time</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCreateTime()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_CreateTime();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastObstacleSpawnTime <em>Last Obstacle Spawn Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Obstacle Spawn Time</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastObstacleSpawnTime()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_LastObstacleSpawnTime();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastMiniJobGeneratedTime <em>Last Mini Job Generated Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Mini Job Generated Time</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getLastMiniJobGeneratedTime()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_LastMiniJobGeneratedTime();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#isIndexed <em>Indexed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Indexed</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#isIndexed()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_Indexed();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskIndex <em>Completed Task Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Completed Task Index</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskIndex()
	 * @see #getPlayerInternal()
	 * @generated
	 */
	EAttribute getPlayerInternal_CompletedTaskIndex();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#beginTask(com.lvl6.mobsters.info.ITask, com.lvl6.mobsters.utility.lambda.Director) <em>Begin Task</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Begin Task</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#beginTask(com.lvl6.mobsters.info.ITask, com.lvl6.mobsters.utility.lambda.Director)
	 * @generated
	 */
	EOperation getPlayerInternal__BeginTask__ITask_Director();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#checkForIndices() <em>Check For Indices</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Check For Indices</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#checkForIndices()
	 * @generated
	 */
	EOperation getPlayerInternal__CheckForIndices();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskFor(com.lvl6.mobsters.info.ITask) <em>Get Completed Task For</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Completed Task For</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.PlayerInternal#getCompletedTaskFor(com.lvl6.mobsters.info.ITask)
	 * @generated
	 */
	EOperation getPlayerInternal__GetCompletedTaskFor__ITask();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.OngoingTask <em>Ongoing Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ongoing Task</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTask
	 * @generated
	 */
	EClass getOngoingTask();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.OngoingTask#completeTask() <em>Complete Task</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Complete Task</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTask#completeTask()
	 * @generated
	 */
	EOperation getOngoingTask__CompleteTask();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal <em>Ongoing Task Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ongoing Task Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal
	 * @generated
	 */
	EClass getOngoingTaskInternal();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskUuid <em>Task Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Task Uuid</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskUuid()
	 * @see #getOngoingTaskInternal()
	 * @generated
	 */
	EAttribute getOngoingTaskInternal_TaskUuid();

	/**
	 * Returns the meta object for the container reference '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Player</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getPlayer()
	 * @see #getOngoingTaskInternal()
	 * @generated
	 */
	EReference getOngoingTaskInternal_Player();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskMeta <em>Task Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Task Meta</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getTaskMeta()
	 * @see #getOngoingTaskInternal()
	 * @generated
	 */
	EAttribute getOngoingTaskInternal_TaskMeta();

	/**
	 * Returns the meta object for the containment reference list '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getStages <em>Stages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Stages</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#getStages()
	 * @see #getOngoingTaskInternal()
	 * @generated
	 */
	EReference getOngoingTaskInternal_Stages();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#completeTask() <em>Complete Task</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Complete Task</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.OngoingTaskInternal#completeTask()
	 * @generated
	 */
	EOperation getOngoingTaskInternal__CompleteTask();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.TaskStage <em>Task Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task Stage</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStage
	 * @generated
	 */
	EClass getTaskStage();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal <em>Task Stage Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task Stage Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal
	 * @generated
	 */
	EClass getTaskStageInternal();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageUuid <em>Task Stage Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Task Stage Uuid</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageUuid()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_TaskStageUuid();

	/**
	 * Returns the meta object for the container reference '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Task</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTask()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EReference getTaskStageInternal_Task();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStage <em>Task Stage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Task Stage</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStage()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_TaskStage();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getStageNum <em>Stage Num</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Stage Num</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getStageNum()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_StageNum();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageMonster <em>Task Stage Monster</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Task Stage Monster</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getTaskStageMonster()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_TaskStageMonster();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getDmgMultiplier <em>Dmg Multiplier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dmg Multiplier</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getDmgMultiplier()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_DmgMultiplier();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getMonsterType <em>Monster Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Monster Type</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getMonsterType()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_MonsterType();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#isMonsterPieceDropped <em>Monster Piece Dropped</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Monster Piece Dropped</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#isMonsterPieceDropped()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_MonsterPieceDropped();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getItemDropped <em>Item Dropped</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Item Dropped</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getItemDropped()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_ItemDropped();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getExpGained <em>Exp Gained</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exp Gained</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getExpGained()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_ExpGained();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getCashGained <em>Cash Gained</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cash Gained</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getCashGained()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_CashGained();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getOilGained <em>Oil Gained</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Oil Gained</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.TaskStageInternal#getOilGained()
	 * @see #getTaskStageInternal()
	 * @generated
	 */
	EAttribute getTaskStageInternal_OilGained();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder <em>Begin Task Stages Builder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Begin Task Stages Builder</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder
	 * @generated
	 */
	EClass getBeginTaskStagesBuilder();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder#addStage(int, com.lvl6.mobsters.info.ITaskStage, com.lvl6.mobsters.info.ITaskStageMonster, int, int, int, boolean, com.lvl6.mobsters.info.IItem) <em>Add Stage</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Add Stage</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder#addStage(int, com.lvl6.mobsters.info.ITaskStage, com.lvl6.mobsters.info.ITaskStageMonster, int, int, int, boolean, com.lvl6.mobsters.info.IItem)
	 * @generated
	 */
	EOperation getBeginTaskStagesBuilder__AddStage__int_ITaskStage_ITaskStageMonster_int_int_int_boolean_IItem();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal <em>Begin Task Stages Builder Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Begin Task Stages Builder Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal
	 * @generated
	 */
	EClass getBeginTaskStagesBuilderInternal();

	/**
	 * Returns the meta object for the reference '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getNewArtifact <em>New Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Artifact</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getNewArtifact()
	 * @see #getBeginTaskStagesBuilderInternal()
	 * @generated
	 */
	EReference getBeginTaskStagesBuilderInternal_NewArtifact();

	/**
	 * Returns the meta object for the reference '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Player</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#getPlayer()
	 * @see #getBeginTaskStagesBuilderInternal()
	 * @generated
	 */
	EReference getBeginTaskStagesBuilderInternal_Player();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#init(com.lvl6.mobsters.info.ITask, com.lvl6.mobsters.domainmodel.player.Player) <em>Init</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Init</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#init(com.lvl6.mobsters.info.ITask, com.lvl6.mobsters.domainmodel.player.Player)
	 * @generated
	 */
	EOperation getBeginTaskStagesBuilderInternal__Init__ITask_Player();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#addStage(int, com.lvl6.mobsters.info.ITaskStage, com.lvl6.mobsters.info.ITaskStageMonster, int, int, int, com.lvl6.mobsters.info.IItem, boolean) <em>Add Stage</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Add Stage</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#addStage(int, com.lvl6.mobsters.info.ITaskStage, com.lvl6.mobsters.info.ITaskStageMonster, int, int, int, com.lvl6.mobsters.info.IItem, boolean)
	 * @generated
	 */
	EOperation getBeginTaskStagesBuilderInternal__AddStage__int_ITaskStage_ITaskStageMonster_int_int_int_IItem_boolean();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#build() <em>Build</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Build</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilderInternal#build()
	 * @generated
	 */
	EOperation getBeginTaskStagesBuilderInternal__Build();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.CompletedTask <em>Completed Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Completed Task</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTask
	 * @generated
	 */
	EClass getCompletedTask();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal <em>Completed Task Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Completed Task Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal
	 * @generated
	 */
	EClass getCompletedTaskInternal();

	/**
	 * Returns the meta object for the container reference '{@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Player</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getPlayer()
	 * @see #getCompletedTaskInternal()
	 * @generated
	 */
	EReference getCompletedTaskInternal_Player();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTaskMeta <em>Task Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Task Meta</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTaskMeta()
	 * @see #getCompletedTaskInternal()
	 * @generated
	 */
	EAttribute getCompletedTaskInternal_TaskMeta();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTimeOfEntry <em>Time Of Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Of Entry</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal#getTimeOfEntry()
	 * @see #getCompletedTaskInternal()
	 * @generated
	 */
	EAttribute getCompletedTaskInternal_TimeOfEntry();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.Monster <em>Monster</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Monster</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.Monster
	 * @generated
	 */
	EClass getMonster();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal <em>Monster Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Monster Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal
	 * @generated
	 */
	EClass getMonsterInternal();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterUuid <em>Monster Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Monster Uuid</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterUuid()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_MonsterUuid();

	/**
	 * Returns the meta object for the container reference '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Player</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getPlayer()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EReference getMonsterInternal_Player();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterMeta <em>Monster Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Monster Meta</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getMonsterMeta()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_MonsterMeta();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentExp <em>Current Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Exp</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentExp()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_CurrentExp();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentLvl <em>Current Lvl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Lvl</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentLvl()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_CurrentLvl();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentHealth <em>Current Health</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Health</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCurrentHealth()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_CurrentHealth();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getNumPieces <em>Num Pieces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Num Pieces</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getNumPieces()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_NumPieces();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#isIsComplete <em>Is Complete</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Complete</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#isIsComplete()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_IsComplete();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCombineStartTime <em>Combine Start Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Combine Start Time</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getCombineStartTime()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_CombineStartTime();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#getTeamSlotNum <em>Team Slot Num</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Team Slot Num</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#getTeamSlotNum()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_TeamSlotNum();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.MonsterInternal#isRestricted <em>Restricted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Restricted</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterInternal#isRestricted()
	 * @see #getMonsterInternal()
	 * @generated
	 */
	EAttribute getMonsterInternal_Restricted();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.Item <em>Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Item</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.Item
	 * @generated
	 */
	EClass getItem();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.ItemInternal <em>Item Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Item Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.ItemInternal
	 * @generated
	 */
	EClass getItemInternal();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemUuid <em>Item Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Item Uuid</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemUuid()
	 * @see #getItemInternal()
	 * @generated
	 */
	EAttribute getItemInternal_ItemUuid();

	/**
	 * Returns the meta object for the container reference '{@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Player</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.ItemInternal#getPlayer()
	 * @see #getItemInternal()
	 * @generated
	 */
	EReference getItemInternal_Player();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemMeta <em>Item Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Item Meta</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.ItemInternal#getItemMeta()
	 * @see #getItemInternal()
	 * @generated
	 */
	EAttribute getItemInternal_ItemMeta();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.PendingOperation <em>Pending Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Pending Operation</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperation
	 * @generated
	 */
	EClass getPendingOperation();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.PendingOperation#checkTimer() <em>Check Timer</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Check Timer</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperation#checkTimer()
	 * @generated
	 */
	EOperation getPendingOperation__CheckTimer();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal <em>Pending Operation Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Pending Operation Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal
	 * @generated
	 */
	EClass getPendingOperationInternal();

	/**
	 * Returns the meta object for the container reference '{@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getPlayer <em>Player</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Player</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getPlayer()
	 * @see #getPendingOperationInternal()
	 * @generated
	 */
	EReference getPendingOperationInternal_Player();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpStartTimer <em>Op Start Timer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Op Start Timer</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpStartTimer()
	 * @see #getPendingOperationInternal()
	 * @generated
	 */
	EAttribute getPendingOperationInternal_OpStartTimer();

	/**
	 * Returns the meta object for the attribute '{@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpEndTimer <em>Op End Timer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Op End Timer</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#getOpEndTimer()
	 * @see #getPendingOperationInternal()
	 * @generated
	 */
	EAttribute getPendingOperationInternal_OpEndTimer();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#happen() <em>Happen</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Happen</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#happen()
	 * @generated
	 */
	EOperation getPendingOperationInternal__Happen();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#checkTimer() <em>Check Timer</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Check Timer</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.PendingOperationInternal#checkTimer()
	 * @generated
	 */
	EOperation getPendingOperationInternal__CheckTimer();

	/**
	 * Returns the meta object for class '{@link com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal <em>Combine Monster Pieces Internal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Combine Monster Pieces Internal</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal
	 * @generated
	 */
	EClass getCombineMonsterPiecesInternal();

	/**
	 * Returns the meta object for the reference '{@link com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal#getNewMonster <em>New Monster</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Monster</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal#getNewMonster()
	 * @see #getCombineMonsterPiecesInternal()
	 * @generated
	 */
	EReference getCombineMonsterPiecesInternal_NewMonster();

	/**
	 * Returns the meta object for the '{@link com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal#happen() <em>Happen</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Happen</em>' operation.
	 * @see com.lvl6.mobsters.domainmodel.player.CombineMonsterPiecesInternal#happen()
	 * @generated
	 */
	EOperation getCombineMonsterPiecesInternal__Happen();

	/**
	 * Returns the meta object for enum '{@link com.lvl6.mobsters.domainmodel.player.MonsterType <em>Monster Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Monster Type</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.MonsterType
	 * @generated
	 */
	EEnum getMonsterType();

	/**
	 * Returns the meta object for enum '{@link com.lvl6.mobsters.domainmodel.player.ElementType <em>Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Element Type</em>'.
	 * @see com.lvl6.mobsters.domainmodel.player.ElementType
	 * @generated
	 */
	EEnum getElementType();

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
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.IQuest <em>IQuest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IQuest</em>'.
	 * @see com.lvl6.mobsters.info.IQuest
	 * @model instanceClass="com.lvl6.mobsters.info.IQuest"
	 * @generated
	 */
	EDataType getIQuest();

	/**
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.IQuestJob <em>IQuest Job</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IQuest Job</em>'.
	 * @see com.lvl6.mobsters.info.IQuestJob
	 * @model instanceClass="com.lvl6.mobsters.info.IQuestJob"
	 * @generated
	 */
	EDataType getIQuestJob();

	/**
	 * Returns the meta object for data type '{@link com.lvl6.mobsters.info.IQuestJobMonsterItem <em>IQuest Job Monster Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IQuest Job Monster Item</em>'.
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem
	 * @model instanceClass="com.lvl6.mobsters.info.IQuestJobMonsterItem"
	 * @generated
	 */
	EDataType getIQuestJobMonsterItem();

	/**
	 * Returns the meta object for data type '{@link java.util.Date <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Date</em>'.
	 * @see java.util.Date
	 * @model instanceClass="java.util.Date"
	 * @generated
	 */
	EDataType getDate();

	/**
	 * Returns the meta object for data type '{@link java.util.UUID <em>UUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>UUID</em>'.
	 * @see java.util.UUID
	 * @model instanceClass="java.util.UUID"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel create='return <%java.util.UUID%>.fromString(it);' convert='return it.toString();'"
	 * @generated
	 */
	EDataType getUUID();

	/**
	 * Returns the meta object for data type '{@link com.googlecode.cqengine.IndexedCollection <em>Completed Task Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Completed Task Index</em>'.
	 * @see com.googlecode.cqengine.IndexedCollection
	 * @model instanceClass="com.googlecode.cqengine.IndexedCollection<com.lvl6.mobsters.domainmodel.player.CompletedTask>"
	 * @generated
	 */
	EDataType getCompletedTaskIndex();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MobstersPlayerFactory getMobstersPlayerFactory();

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
		 * The meta object literal for the '{@link com.lvl6.mobsters.utility.lambda.Director <em>Director</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.utility.lambda.Director
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getDirector()
		 * @generated
		 */
		EClass DIRECTOR = eINSTANCE.getDirector();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.Player <em>Player</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.Player
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPlayer()
		 * @generated
		 */
		EClass PLAYER = eINSTANCE.getPlayer();

		/**
		 * The meta object literal for the '<em><b>Begin Task</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PLAYER___BEGIN_TASK__ITASK_DIRECTOR = eINSTANCE
				.getPlayer__BeginTask__ITask_Director();

		/**
		 * The meta object literal for the '<em><b>Get Completed Task For</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PLAYER___GET_COMPLETED_TASK_FOR__ITASK = eINSTANCE
				.getPlayer__GetCompletedTaskFor__ITask();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl <em>Player Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.PlayerInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPlayerInternal()
		 * @generated
		 */
		EClass PLAYER_INTERNAL = eINSTANCE.getPlayerInternal();

		/**
		 * The meta object literal for the '<em><b>User Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__USER_UUID = eINSTANCE
				.getPlayerInternal_UserUuid();

		/**
		 * The meta object literal for the '<em><b>Gems</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__GEMS = eINSTANCE.getPlayerInternal_Gems();

		/**
		 * The meta object literal for the '<em><b>Cash</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__CASH = eINSTANCE.getPlayerInternal_Cash();

		/**
		 * The meta object literal for the '<em><b>Oil</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__OIL = eINSTANCE.getPlayerInternal_Oil();

		/**
		 * The meta object literal for the '<em><b>Experience</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__EXPERIENCE = eINSTANCE
				.getPlayerInternal_Experience();

		/**
		 * The meta object literal for the '<em><b>Ongoing Task</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAYER_INTERNAL__ONGOING_TASK = eINSTANCE
				.getPlayerInternal_OngoingTask();

		/**
		 * The meta object literal for the '<em><b>Completed Tasks</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAYER_INTERNAL__COMPLETED_TASKS = eINSTANCE
				.getPlayerInternal_CompletedTasks();

		/**
		 * The meta object literal for the '<em><b>Items</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAYER_INTERNAL__ITEMS = eINSTANCE.getPlayerInternal_Items();

		/**
		 * The meta object literal for the '<em><b>Monsters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAYER_INTERNAL__MONSTERS = eINSTANCE
				.getPlayerInternal_Monsters();

		/**
		 * The meta object literal for the '<em><b>Team Slots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAYER_INTERNAL__TEAM_SLOTS = eINSTANCE
				.getPlayerInternal_TeamSlots();

		/**
		 * The meta object literal for the '<em><b>Pending Operations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAYER_INTERNAL__PENDING_OPERATIONS = eINSTANCE
				.getPlayerInternal_PendingOperations();

		/**
		 * The meta object literal for the '<em><b>Udid For History</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__UDID_FOR_HISTORY = eINSTANCE
				.getPlayerInternal_UdidForHistory();

		/**
		 * The meta object literal for the '<em><b>Device Token</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__DEVICE_TOKEN = eINSTANCE
				.getPlayerInternal_DeviceToken();

		/**
		 * The meta object literal for the '<em><b>Fb Id Set On User Create</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__FB_ID_SET_ON_USER_CREATE = eINSTANCE
				.getPlayerInternal_FbIdSetOnUserCreate();

		/**
		 * The meta object literal for the '<em><b>Game Center Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__GAME_CENTER_ID = eINSTANCE
				.getPlayerInternal_GameCenterId();

		/**
		 * The meta object literal for the '<em><b>Avatar Monster Meta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__AVATAR_MONSTER_META = eINSTANCE
				.getPlayerInternal_AvatarMonsterMeta();

		/**
		 * The meta object literal for the '<em><b>Last Login</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__LAST_LOGIN = eINSTANCE
				.getPlayerInternal_LastLogin();

		/**
		 * The meta object literal for the '<em><b>Last Logout</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__LAST_LOGOUT = eINSTANCE
				.getPlayerInternal_LastLogout();

		/**
		 * The meta object literal for the '<em><b>Create Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__CREATE_TIME = eINSTANCE
				.getPlayerInternal_CreateTime();

		/**
		 * The meta object literal for the '<em><b>Last Obstacle Spawn Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__LAST_OBSTACLE_SPAWN_TIME = eINSTANCE
				.getPlayerInternal_LastObstacleSpawnTime();

		/**
		 * The meta object literal for the '<em><b>Last Mini Job Generated Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__LAST_MINI_JOB_GENERATED_TIME = eINSTANCE
				.getPlayerInternal_LastMiniJobGeneratedTime();

		/**
		 * The meta object literal for the '<em><b>Indexed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__INDEXED = eINSTANCE
				.getPlayerInternal_Indexed();

		/**
		 * The meta object literal for the '<em><b>Completed Task Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAYER_INTERNAL__COMPLETED_TASK_INDEX = eINSTANCE
				.getPlayerInternal_CompletedTaskIndex();

		/**
		 * The meta object literal for the '<em><b>Begin Task</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PLAYER_INTERNAL___BEGIN_TASK__ITASK_DIRECTOR = eINSTANCE
				.getPlayerInternal__BeginTask__ITask_Director();

		/**
		 * The meta object literal for the '<em><b>Check For Indices</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PLAYER_INTERNAL___CHECK_FOR_INDICES = eINSTANCE
				.getPlayerInternal__CheckForIndices();

		/**
		 * The meta object literal for the '<em><b>Get Completed Task For</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PLAYER_INTERNAL___GET_COMPLETED_TASK_FOR__ITASK = eINSTANCE
				.getPlayerInternal__GetCompletedTaskFor__ITask();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.OngoingTask <em>Ongoing Task</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.OngoingTask
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getOngoingTask()
		 * @generated
		 */
		EClass ONGOING_TASK = eINSTANCE.getOngoingTask();

		/**
		 * The meta object literal for the '<em><b>Complete Task</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ONGOING_TASK___COMPLETE_TASK = eINSTANCE
				.getOngoingTask__CompleteTask();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl <em>Ongoing Task Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.OngoingTaskInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getOngoingTaskInternal()
		 * @generated
		 */
		EClass ONGOING_TASK_INTERNAL = eINSTANCE.getOngoingTaskInternal();

		/**
		 * The meta object literal for the '<em><b>Task Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ONGOING_TASK_INTERNAL__TASK_UUID = eINSTANCE
				.getOngoingTaskInternal_TaskUuid();

		/**
		 * The meta object literal for the '<em><b>Player</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ONGOING_TASK_INTERNAL__PLAYER = eINSTANCE
				.getOngoingTaskInternal_Player();

		/**
		 * The meta object literal for the '<em><b>Task Meta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ONGOING_TASK_INTERNAL__TASK_META = eINSTANCE
				.getOngoingTaskInternal_TaskMeta();

		/**
		 * The meta object literal for the '<em><b>Stages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ONGOING_TASK_INTERNAL__STAGES = eINSTANCE
				.getOngoingTaskInternal_Stages();

		/**
		 * The meta object literal for the '<em><b>Complete Task</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ONGOING_TASK_INTERNAL___COMPLETE_TASK = eINSTANCE
				.getOngoingTaskInternal__CompleteTask();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.TaskStage <em>Task Stage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.TaskStage
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getTaskStage()
		 * @generated
		 */
		EClass TASK_STAGE = eINSTANCE.getTaskStage();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl <em>Task Stage Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.TaskStageInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getTaskStageInternal()
		 * @generated
		 */
		EClass TASK_STAGE_INTERNAL = eINSTANCE.getTaskStageInternal();

		/**
		 * The meta object literal for the '<em><b>Task Stage Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__TASK_STAGE_UUID = eINSTANCE
				.getTaskStageInternal_TaskStageUuid();

		/**
		 * The meta object literal for the '<em><b>Task</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TASK_STAGE_INTERNAL__TASK = eINSTANCE
				.getTaskStageInternal_Task();

		/**
		 * The meta object literal for the '<em><b>Task Stage</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__TASK_STAGE = eINSTANCE
				.getTaskStageInternal_TaskStage();

		/**
		 * The meta object literal for the '<em><b>Stage Num</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__STAGE_NUM = eINSTANCE
				.getTaskStageInternal_StageNum();

		/**
		 * The meta object literal for the '<em><b>Task Stage Monster</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__TASK_STAGE_MONSTER = eINSTANCE
				.getTaskStageInternal_TaskStageMonster();

		/**
		 * The meta object literal for the '<em><b>Dmg Multiplier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__DMG_MULTIPLIER = eINSTANCE
				.getTaskStageInternal_DmgMultiplier();

		/**
		 * The meta object literal for the '<em><b>Monster Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__MONSTER_TYPE = eINSTANCE
				.getTaskStageInternal_MonsterType();

		/**
		 * The meta object literal for the '<em><b>Monster Piece Dropped</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__MONSTER_PIECE_DROPPED = eINSTANCE
				.getTaskStageInternal_MonsterPieceDropped();

		/**
		 * The meta object literal for the '<em><b>Item Dropped</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__ITEM_DROPPED = eINSTANCE
				.getTaskStageInternal_ItemDropped();

		/**
		 * The meta object literal for the '<em><b>Exp Gained</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__EXP_GAINED = eINSTANCE
				.getTaskStageInternal_ExpGained();

		/**
		 * The meta object literal for the '<em><b>Cash Gained</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__CASH_GAINED = eINSTANCE
				.getTaskStageInternal_CashGained();

		/**
		 * The meta object literal for the '<em><b>Oil Gained</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_STAGE_INTERNAL__OIL_GAINED = eINSTANCE
				.getTaskStageInternal_OilGained();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder <em>Begin Task Stages Builder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.BeginTaskStagesBuilder
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getBeginTaskStagesBuilder()
		 * @generated
		 */
		EClass BEGIN_TASK_STAGES_BUILDER = eINSTANCE
				.getBeginTaskStagesBuilder();

		/**
		 * The meta object literal for the '<em><b>Add Stage</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BEGIN_TASK_STAGES_BUILDER___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_BOOLEAN_IITEM = eINSTANCE
				.getBeginTaskStagesBuilder__AddStage__int_ITaskStage_ITaskStageMonster_int_int_int_boolean_IItem();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.BeginTaskStagesBuilderInternalImpl <em>Begin Task Stages Builder Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.BeginTaskStagesBuilderInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getBeginTaskStagesBuilderInternal()
		 * @generated
		 */
		EClass BEGIN_TASK_STAGES_BUILDER_INTERNAL = eINSTANCE
				.getBeginTaskStagesBuilderInternal();

		/**
		 * The meta object literal for the '<em><b>New Artifact</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEGIN_TASK_STAGES_BUILDER_INTERNAL__NEW_ARTIFACT = eINSTANCE
				.getBeginTaskStagesBuilderInternal_NewArtifact();

		/**
		 * The meta object literal for the '<em><b>Player</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEGIN_TASK_STAGES_BUILDER_INTERNAL__PLAYER = eINSTANCE
				.getBeginTaskStagesBuilderInternal_Player();

		/**
		 * The meta object literal for the '<em><b>Init</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BEGIN_TASK_STAGES_BUILDER_INTERNAL___INIT__ITASK_PLAYER = eINSTANCE
				.getBeginTaskStagesBuilderInternal__Init__ITask_Player();

		/**
		 * The meta object literal for the '<em><b>Add Stage</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BEGIN_TASK_STAGES_BUILDER_INTERNAL___ADD_STAGE__INT_ITASKSTAGE_ITASKSTAGEMONSTER_INT_INT_INT_IITEM_BOOLEAN = eINSTANCE
				.getBeginTaskStagesBuilderInternal__AddStage__int_ITaskStage_ITaskStageMonster_int_int_int_IItem_boolean();

		/**
		 * The meta object literal for the '<em><b>Build</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation BEGIN_TASK_STAGES_BUILDER_INTERNAL___BUILD = eINSTANCE
				.getBeginTaskStagesBuilderInternal__Build();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.CompletedTask <em>Completed Task</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.CompletedTask
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCompletedTask()
		 * @generated
		 */
		EClass COMPLETED_TASK = eINSTANCE.getCompletedTask();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.CompletedTaskInternalImpl <em>Completed Task Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.CompletedTaskInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCompletedTaskInternal()
		 * @generated
		 */
		EClass COMPLETED_TASK_INTERNAL = eINSTANCE.getCompletedTaskInternal();

		/**
		 * The meta object literal for the '<em><b>Player</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPLETED_TASK_INTERNAL__PLAYER = eINSTANCE
				.getCompletedTaskInternal_Player();

		/**
		 * The meta object literal for the '<em><b>Task Meta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLETED_TASK_INTERNAL__TASK_META = eINSTANCE
				.getCompletedTaskInternal_TaskMeta();

		/**
		 * The meta object literal for the '<em><b>Time Of Entry</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLETED_TASK_INTERNAL__TIME_OF_ENTRY = eINSTANCE
				.getCompletedTaskInternal_TimeOfEntry();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.Monster <em>Monster</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.Monster
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getMonster()
		 * @generated
		 */
		EClass MONSTER = eINSTANCE.getMonster();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl <em>Monster Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MonsterInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getMonsterInternal()
		 * @generated
		 */
		EClass MONSTER_INTERNAL = eINSTANCE.getMonsterInternal();

		/**
		 * The meta object literal for the '<em><b>Monster Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__MONSTER_UUID = eINSTANCE
				.getMonsterInternal_MonsterUuid();

		/**
		 * The meta object literal for the '<em><b>Player</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MONSTER_INTERNAL__PLAYER = eINSTANCE
				.getMonsterInternal_Player();

		/**
		 * The meta object literal for the '<em><b>Monster Meta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__MONSTER_META = eINSTANCE
				.getMonsterInternal_MonsterMeta();

		/**
		 * The meta object literal for the '<em><b>Current Exp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__CURRENT_EXP = eINSTANCE
				.getMonsterInternal_CurrentExp();

		/**
		 * The meta object literal for the '<em><b>Current Lvl</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__CURRENT_LVL = eINSTANCE
				.getMonsterInternal_CurrentLvl();

		/**
		 * The meta object literal for the '<em><b>Current Health</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__CURRENT_HEALTH = eINSTANCE
				.getMonsterInternal_CurrentHealth();

		/**
		 * The meta object literal for the '<em><b>Num Pieces</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__NUM_PIECES = eINSTANCE
				.getMonsterInternal_NumPieces();

		/**
		 * The meta object literal for the '<em><b>Is Complete</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__IS_COMPLETE = eINSTANCE
				.getMonsterInternal_IsComplete();

		/**
		 * The meta object literal for the '<em><b>Combine Start Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__COMBINE_START_TIME = eINSTANCE
				.getMonsterInternal_CombineStartTime();

		/**
		 * The meta object literal for the '<em><b>Team Slot Num</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__TEAM_SLOT_NUM = eINSTANCE
				.getMonsterInternal_TeamSlotNum();

		/**
		 * The meta object literal for the '<em><b>Restricted</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MONSTER_INTERNAL__RESTRICTED = eINSTANCE
				.getMonsterInternal_Restricted();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.Item <em>Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.Item
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getItem()
		 * @generated
		 */
		EClass ITEM = eINSTANCE.getItem();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.ItemInternalImpl <em>Item Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.ItemInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getItemInternal()
		 * @generated
		 */
		EClass ITEM_INTERNAL = eINSTANCE.getItemInternal();

		/**
		 * The meta object literal for the '<em><b>Item Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ITEM_INTERNAL__ITEM_UUID = eINSTANCE
				.getItemInternal_ItemUuid();

		/**
		 * The meta object literal for the '<em><b>Player</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ITEM_INTERNAL__PLAYER = eINSTANCE.getItemInternal_Player();

		/**
		 * The meta object literal for the '<em><b>Item Meta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ITEM_INTERNAL__ITEM_META = eINSTANCE
				.getItemInternal_ItemMeta();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.PendingOperation <em>Pending Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.PendingOperation
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPendingOperation()
		 * @generated
		 */
		EClass PENDING_OPERATION = eINSTANCE.getPendingOperation();

		/**
		 * The meta object literal for the '<em><b>Check Timer</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PENDING_OPERATION___CHECK_TIMER = eINSTANCE
				.getPendingOperation__CheckTimer();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.PendingOperationInternalImpl <em>Pending Operation Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.PendingOperationInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getPendingOperationInternal()
		 * @generated
		 */
		EClass PENDING_OPERATION_INTERNAL = eINSTANCE
				.getPendingOperationInternal();

		/**
		 * The meta object literal for the '<em><b>Player</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PENDING_OPERATION_INTERNAL__PLAYER = eINSTANCE
				.getPendingOperationInternal_Player();

		/**
		 * The meta object literal for the '<em><b>Op Start Timer</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PENDING_OPERATION_INTERNAL__OP_START_TIMER = eINSTANCE
				.getPendingOperationInternal_OpStartTimer();

		/**
		 * The meta object literal for the '<em><b>Op End Timer</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PENDING_OPERATION_INTERNAL__OP_END_TIMER = eINSTANCE
				.getPendingOperationInternal_OpEndTimer();

		/**
		 * The meta object literal for the '<em><b>Happen</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PENDING_OPERATION_INTERNAL___HAPPEN = eINSTANCE
				.getPendingOperationInternal__Happen();

		/**
		 * The meta object literal for the '<em><b>Check Timer</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PENDING_OPERATION_INTERNAL___CHECK_TIMER = eINSTANCE
				.getPendingOperationInternal__CheckTimer();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.impl.CombineMonsterPiecesInternalImpl <em>Combine Monster Pieces Internal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.impl.CombineMonsterPiecesInternalImpl
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCombineMonsterPiecesInternal()
		 * @generated
		 */
		EClass COMBINE_MONSTER_PIECES_INTERNAL = eINSTANCE
				.getCombineMonsterPiecesInternal();

		/**
		 * The meta object literal for the '<em><b>New Monster</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMBINE_MONSTER_PIECES_INTERNAL__NEW_MONSTER = eINSTANCE
				.getCombineMonsterPiecesInternal_NewMonster();

		/**
		 * The meta object literal for the '<em><b>Happen</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation COMBINE_MONSTER_PIECES_INTERNAL___HAPPEN = eINSTANCE
				.getCombineMonsterPiecesInternal__Happen();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.MonsterType <em>Monster Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.MonsterType
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getMonsterType()
		 * @generated
		 */
		EEnum MONSTER_TYPE = eINSTANCE.getMonsterType();

		/**
		 * The meta object literal for the '{@link com.lvl6.mobsters.domainmodel.player.ElementType <em>Element Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.domainmodel.player.ElementType
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getElementType()
		 * @generated
		 */
		EEnum ELEMENT_TYPE = eINSTANCE.getElementType();

		/**
		 * The meta object literal for the '<em>IMonster</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.IMonster
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIMonster()
		 * @generated
		 */
		EDataType IMONSTER = eINSTANCE.getIMonster();

		/**
		 * The meta object literal for the '<em>IItem</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.IItem
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIItem()
		 * @generated
		 */
		EDataType IITEM = eINSTANCE.getIItem();

		/**
		 * The meta object literal for the '<em>ITask</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.ITask
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getITask()
		 * @generated
		 */
		EDataType ITASK = eINSTANCE.getITask();

		/**
		 * The meta object literal for the '<em>ITask Stage</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.ITaskStage
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getITaskStage()
		 * @generated
		 */
		EDataType ITASK_STAGE = eINSTANCE.getITaskStage();

		/**
		 * The meta object literal for the '<em>ITask Stage Monster</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.ITaskStageMonster
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getITaskStageMonster()
		 * @generated
		 */
		EDataType ITASK_STAGE_MONSTER = eINSTANCE.getITaskStageMonster();

		/**
		 * The meta object literal for the '<em>IQuest</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.IQuest
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIQuest()
		 * @generated
		 */
		EDataType IQUEST = eINSTANCE.getIQuest();

		/**
		 * The meta object literal for the '<em>IQuest Job</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.IQuestJob
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIQuestJob()
		 * @generated
		 */
		EDataType IQUEST_JOB = eINSTANCE.getIQuestJob();

		/**
		 * The meta object literal for the '<em>IQuest Job Monster Item</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getIQuestJobMonsterItem()
		 * @generated
		 */
		EDataType IQUEST_JOB_MONSTER_ITEM = eINSTANCE.getIQuestJobMonsterItem();

		/**
		 * The meta object literal for the '<em>Date</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.Date
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getDate()
		 * @generated
		 */
		EDataType DATE = eINSTANCE.getDate();

		/**
		 * The meta object literal for the '<em>UUID</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.UUID
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getUUID()
		 * @generated
		 */
		EDataType UUID = eINSTANCE.getUUID();

		/**
		 * The meta object literal for the '<em>Completed Task Index</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.googlecode.cqengine.IndexedCollection
		 * @see com.lvl6.mobsters.domainmodel.player.impl.MobstersPlayerPackageImpl#getCompletedTaskIndex()
		 * @generated
		 */
		EDataType COMPLETED_TASK_INDEX = eINSTANCE.getCompletedTaskIndex();

	}

} //MobstersPlayerPackage
