package com.lvl6.mobsters.info;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.info.BoosterDisplayItem;
import com.lvl6.mobsters.info.BoosterItem;
import com.lvl6.mobsters.info.BoosterPack;
import com.lvl6.mobsters.info.EventPersistent;
import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.IQuest;
import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.Item;
import com.lvl6.mobsters.info.MiniJob;
import com.lvl6.mobsters.info.Monster;
import com.lvl6.mobsters.info.MonsterBattleDialogue;
import com.lvl6.mobsters.info.MonsterForPvp;
import com.lvl6.mobsters.info.Obstacle;
import com.lvl6.mobsters.info.PvpLeague;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.info.QuestJob;
import com.lvl6.mobsters.info.QuestJobMonsterItem;
import com.lvl6.mobsters.info.StaticUserLevelInfo;
import com.lvl6.mobsters.info.Structure;
import com.lvl6.mobsters.info.StructureHospital;
import com.lvl6.mobsters.info.StructureLab;
import com.lvl6.mobsters.info.StructureMiniJob;
import com.lvl6.mobsters.info.StructureResidence;
import com.lvl6.mobsters.info.StructureResourceGenerator;
import com.lvl6.mobsters.info.StructureResourceStorage;
import com.lvl6.mobsters.info.StructureTownHall;
import com.lvl6.mobsters.info.Task;
import com.lvl6.mobsters.info.TaskMapElement;
import com.lvl6.mobsters.info.TaskStage;
import com.lvl6.mobsters.info.TaskStageMonster;

public class CQAttrs {

	/**
	 * CQEngine attribute for accessing field {@code Achievement.id}.
	 */
	public static final Attribute<Achievement, Integer> ACHIEVEMENT_ID = new SimpleAttribute<Achievement, Integer>(
			"ID") {
		public Integer getValue(Achievement achievement) {
			return achievement.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code BoosterDisplayItem.id}.
	 */
	public static final Attribute<BoosterDisplayItem, Integer> BOOSTERDISPLAYITEM_ID = new SimpleAttribute<BoosterDisplayItem, Integer>(
			"ID") {
		public Integer getValue(BoosterDisplayItem boosterdisplayitem) {
			return boosterdisplayitem.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code BoosterItem.id}.
	 */
	public static final Attribute<BoosterItem, Integer> BOOSTERITEM_ID = new SimpleAttribute<BoosterItem, Integer>(
			"ID") {
		public Integer getValue(BoosterItem boosteritem) {
			return boosteritem.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code BoosterPack.id}.
	 */
	public static final Attribute<BoosterPack, Integer> BOOSTERPACK_ID = new SimpleAttribute<BoosterPack, Integer>(
			"ID") {
		public Integer getValue(BoosterPack boosterpack) {
			return boosterpack.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code EventPersistent.id}.
	 */
	public static final Attribute<EventPersistent, Integer> EVENTPERSISTENT_ID = new SimpleAttribute<EventPersistent, Integer>(
			"ID") {
		public Integer getValue(EventPersistent eventpersistent) {
			return eventpersistent.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code Item.id}.
	 */
	public static final Attribute<Item, Integer> ITEM_ID = new SimpleAttribute<Item, Integer>(
			"ID") {
		public Integer getValue(Item item) {
			return item.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code MiniJob.id}.
	 */
	public static final Attribute<MiniJob, Integer> MINIJOB_ID = new SimpleAttribute<MiniJob, Integer>(
			"ID") {
		public Integer getValue(MiniJob minijob) {
			return minijob.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code Monster.id}.
	 */
	public static final Attribute<Monster, Integer> MONSTER_ID = new SimpleAttribute<Monster, Integer>(
			"ID") {
		public Integer getValue(Monster monster) {
			return monster.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code MonsterBattleDialogue.id}.
	 */
	public static final Attribute<MonsterBattleDialogue, Integer> MONSTERBATTLEDIALOGUE_ID = new SimpleAttribute<MonsterBattleDialogue, Integer>(
			"ID") {
		public Integer getValue(MonsterBattleDialogue monsterbattledialogue) {
			return monsterbattledialogue.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code MonsterForPvp.id}.
	 */
	public static final Attribute<MonsterForPvp, Integer> MONSTERFORPVP_ID = new SimpleAttribute<MonsterForPvp, Integer>(
			"ID") {
		public Integer getValue(MonsterForPvp monsterforpvp) {
			return monsterforpvp.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code Obstacle.id}.
	 */
	public static final Attribute<Obstacle, Integer> OBSTACLE_ID = new SimpleAttribute<Obstacle, Integer>(
			"ID") {
		public Integer getValue(Obstacle obstacle) {
			return obstacle.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code PvpLeague.id}.
	 */
	public static final Attribute<PvpLeague, Integer> PVPLEAGUE_ID = new SimpleAttribute<PvpLeague, Integer>(
			"ID") {
		public Integer getValue(PvpLeague pvpleague) {
			return pvpleague.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code Quest.id}.
	 */
	public static final Attribute<Quest, Integer> QUEST_ID = new SimpleAttribute<Quest, Integer>(
			"ID") {
		public Integer getValue(Quest quest) {
			return quest.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code QuestJob.quest}.
	 */
	// Note: For best performance:
	// - if this field cannot be null, replace this SimpleNullableAttribute with
	// a SimpleAttribute
	public static final Attribute<QuestJob, IQuest> QUESTJOB_QUEST = new SimpleNullableAttribute<QuestJob, IQuest>(
			"QUEST") {
		public IQuest getValue(QuestJob questjob) {
			return questjob.quest;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code QuestJob.questJobType}.
	 */
	// Note: For best performance:
	// - if this field cannot be null, replace this SimpleNullableAttribute with
	// a SimpleAttribute
	public static final Attribute<QuestJob, String> QUESTJOB_QUEST_JOB_TYPE = 
		new SimpleNullableAttribute<QuestJob, String>("QUEST_JOB_TYPE") {
			public String getValue(QuestJob questjob) {
				return questjob.questJobType;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code QuestJob.quantity}.
	 */
	public static final Attribute<QuestJob, Integer> QUESTJOB_QUANTITY = 
		new SimpleAttribute<QuestJob, Integer>("QUANTITY") {
			public Integer getValue(QuestJob questjob) {
				return questjob.quantity;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code QuestJob.priority}.
	 */
	public static final Attribute<QuestJob, Integer> QUESTJOB_PRIORITY = 
		new SimpleAttribute<QuestJob, Integer>("PRIORITY") {
			public Integer getValue(QuestJob questjob) {
				return questjob.priority;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code QuestJob.task}.
	 */
	// Note: For best performance:
	// - if this field cannot be null, replace this SimpleNullableAttribute with
	// a SimpleAttribute
	public static final Attribute<QuestJob, ITask> QUESTJOB_TASK = 
		new SimpleNullableAttribute<QuestJob, ITask>("TASK") {
			public ITask getValue(QuestJob questjob) {
				return questjob.task;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code QuestJob.id}.
	 */
	public static final Attribute<QuestJob, Integer> QUESTJOB_ID = 
		new SimpleAttribute<QuestJob, Integer>("ID") {
			public Integer getValue(QuestJob questjob) {
				return questjob.id;
			}
		};

	/**
	 * CQEngine attribute for accessing field {@code QuestJobMonsterItem.id}.
	 */
	public static final Attribute<QuestJobMonsterItem, Integer> QUESTJOBMONSTERITEM_ID = 
		new SimpleAttribute<QuestJobMonsterItem, Integer>("ID") {
		public Integer getValue(QuestJobMonsterItem qjmi) {
			return qjmi.id;
		}
	};

	/**
	 * CQEngine attribute for accessing field {@code QuestJobMonsterItem.id}.
	 */
	public static final Attribute<QuestJobMonsterItem, IQuestJob> QUESTJOBMONSTERITEM_QUEST_JOB = 
		new SimpleAttribute<QuestJobMonsterItem, IQuestJob>("QUEST_JOB") {
			public IQuestJob getValue(QuestJobMonsterItem qjmi) {
				return qjmi.questJob;
			}
		};

	/**
	 * CQEngine attribute for accessing field {@code QuestJobMonsterItem.id}.
	 */
	public static final Attribute<QuestJobMonsterItem, IMonster> QUESTJOBMONSTERITEM_MONSTER = 
		new SimpleAttribute<QuestJobMonsterItem, IMonster>("MONSTER") {
			public IMonster getValue(QuestJobMonsterItem qjmi) {
				return qjmi.monster;
			}
		};

	/**
	 * CQEngine attribute for accessing field {@code QuestJobMonsterItem.id}.
	 */
	public static final Attribute<QuestJobMonsterItem, IItem> QUESTJOBMONSTERITEM_ITEM = 
		new SimpleAttribute<QuestJobMonsterItem, IItem>("ITEM") {
			public IItem getValue(QuestJobMonsterItem qjmi) {
				return qjmi.item;
			}
		};

	/**
	 * CQEngine attribute for accessing field {@code QuestJobMonsterItem.id}.
	 */
	public static final Attribute<QuestJobMonsterItem, Float> QUESTJOBMONSTERITEM_ITEM_DROP_RATE = 
		new SimpleAttribute<QuestJobMonsterItem, Float>("ITEM_DROP_RATE") {
			public Float getValue(QuestJobMonsterItem qjmi) {
				return qjmi.itemDropRate;
			}
		};
	
	/**
	 * CQEngine attribute for accessing field {@code StaticUserLevelInfo.id}.
	 */
	public static final Attribute<StaticUserLevelInfo, Integer> STATICUSERLEVELINFO_ID = 
		new SimpleAttribute<StaticUserLevelInfo, Integer>("ID") {
			public Integer getValue(StaticUserLevelInfo staticuserlevelinfo) {
				return staticuserlevelinfo.id;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code Structure.id}.
	 */
	public static final Attribute<Structure, Integer> STRUCTURE_ID = 
		new SimpleAttribute<Structure, Integer>("ID") {
			public Integer getValue(Structure structure) {
				return structure.id;
			}
		};
	/**
	 * CQEngine attribute for accessing field
	 * {@code StructureHospital.healthPerSecond}.
	 */
	public static final Attribute<StructureHospital, Float> STRUCTUREHOSPITAL_HEALTH_PER_SECOND = 
		new SimpleAttribute<StructureHospital, Float>("HEALTH_PER_SECOND") {
			public Float getValue(StructureHospital structurehospital) {
				return structurehospital.healthPerSecond;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code StructureHospital.id}.
	 */
	public static final Attribute<StructureHospital, Integer> STRUCTUREHOSPITAL_ID = new SimpleAttribute<StructureHospital, Integer>(
			"ID") {
		public Integer getValue(StructureHospital structurehospital) {
			return structurehospital.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code StructureLab.id}.
	 */
	public static final Attribute<StructureLab, Integer> STRUCTURELAB_ID = new SimpleAttribute<StructureLab, Integer>(
			"ID") {
		public Integer getValue(StructureLab structurelab) {
			return structurelab.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code StructureMiniJob.id}.
	 */
	public static final Attribute<StructureMiniJob, Integer> STRUCTUREMINIJOB_ID = new SimpleAttribute<StructureMiniJob, Integer>(
			"ID") {
		public Integer getValue(StructureMiniJob structureminijob) {
			return structureminijob.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code StructureResidence.id}.
	 */
	public static final Attribute<StructureResidence, Integer> STRUCTURERESIDENCE_ID = new SimpleAttribute<StructureResidence, Integer>(
			"ID") {
		public Integer getValue(StructureResidence structureresidence) {
			return structureresidence.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field
	 * {@code StructureResourceGenerator.id}.
	 */
	public static final Attribute<StructureResourceGenerator, Integer> STRUCTURERESOURCEGENERATOR_ID = new SimpleAttribute<StructureResourceGenerator, Integer>(
			"ID") {
		public Integer getValue(
				StructureResourceGenerator structureresourcegenerator) {
			return structureresourcegenerator.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field
	 * {@code StructureResourceStorage.id}.
	 */
	public static final Attribute<StructureResourceStorage, Integer> STRUCTURERESOURCESTORAGE_ID = new SimpleAttribute<StructureResourceStorage, Integer>(
			"ID") {
		public Integer getValue(
				StructureResourceStorage structureresourcestorage) {
			return structureresourcestorage.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code StructureTownHall.id}.
	 */
	public static final Attribute<StructureTownHall, Integer> STRUCTURETOWNHALL_ID = new SimpleAttribute<StructureTownHall, Integer>(
			"ID") {
		public Integer getValue(StructureTownHall structuretownhall) {
			return structuretownhall.id;
		}
	};
	/**
	 * CQEngine attribute for accessing field {@code Task.id}.
	 */
	public static final Attribute<Task, Integer> TASK_ID = 
		new SimpleAttribute<Task, Integer>("ID")
		{
			public Integer getValue(Task task) {
				return task.id;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code TaskMapElement.id}.
	 */
	public static final Attribute<TaskMapElement, Integer> TASKMAPELEMENT_ID = 
		new SimpleAttribute<TaskMapElement, Integer>("ID")
		{
			public Integer getValue(TaskMapElement taskmapelement) {
				return taskmapelement.id;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code TaskStage.id}.
	 */
	public static final Attribute<TaskStage, Integer> TASKSTAGE_ID = 
		new SimpleAttribute<TaskStage, Integer>("ID")
		{
			public Integer getValue(TaskStage taskstage) {
				return taskstage.id;
			}
		};
	/**
	 * CQEngine attribute for accessing field {@code TaskStageMonster.id}.
	 */
	public static final Attribute<TaskStageMonster, Integer> TASKSTAGEMONSTER_ID = 
		new SimpleAttribute<TaskStageMonster, Integer>("ID")
		{
			public Integer getValue(TaskStageMonster taskstagemonster) {
				return taskstagemonster.id;
			}
		};

}
