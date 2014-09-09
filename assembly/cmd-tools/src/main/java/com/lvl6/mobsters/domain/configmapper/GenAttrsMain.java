package com.lvl6.mobsters.domain.configmapper;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.googlecode.cqengine.codegen.AttributesGenerator;
import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.info.BoosterDisplayItem;
import com.lvl6.mobsters.info.BoosterItem;
import com.lvl6.mobsters.info.BoosterPack;
import com.lvl6.mobsters.info.EventPersistent;
import com.lvl6.mobsters.info.Item;
import com.lvl6.mobsters.info.MiniJob;
import com.lvl6.mobsters.info.Monster;
import com.lvl6.mobsters.info.MonsterBattleDialogue;
import com.lvl6.mobsters.info.MonsterForPvp;
import com.lvl6.mobsters.info.MonsterLevelInfo;
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

public class GenAttrsMain {

	public static void main(String[] args) throws IOException {
		final Class<?>[] sources = { 
			Task.class, TaskStage.class, TaskStageMonster.class, Monster.class, MonsterLevelInfo.class, MonsterForPvp.class,
			Achievement.class, BoosterPack.class, BoosterItem.class, BoosterDisplayItem.class, EventPersistent.class, Item.class,
			MiniJob.class, Obstacle.class, Quest.class, QuestJob.class, QuestJobMonsterItem.class, StaticUserLevelInfo.class,
			Structure.class, StructureHospital.class, StructureLab.class, StructureMiniJob.class, StructureResidence.class,
			StructureResourceGenerator.class, StructureResourceStorage.class, StructureTownHall.class, TaskMapElement.class,
			StaticUserLevelInfo.class, MonsterBattleDialogue.class, PvpLeague.class};
		//final String thisPackage = GenAttrsMain.class.getPackage().toString().substring(8);

		for( Class<?> nextClass : sources ) {
			FileWriter output = new FileWriter( "CQ" + nextClass.getSimpleName() + ".java");
			output.write(
				AttributesGenerator.generateSeparateAttributesClass(nextClass, nextClass.getPackage())
			);
			output.close();
		}
	}
}
