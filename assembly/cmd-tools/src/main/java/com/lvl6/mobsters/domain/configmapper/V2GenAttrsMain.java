package com.lvl6.mobsters.domain.configmapper;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.googlecode.cqengine.codegen.AttributesGenerator;
import com.lvl6.mobsters.info.IAchievement;
import com.lvl6.mobsters.info.IBoosterDisplayItem;
import com.lvl6.mobsters.info.IBoosterItem;
import com.lvl6.mobsters.info.IBoosterPack;
import com.lvl6.mobsters.info.IEventPersistent;
import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.IMiniJob;
import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.IMonsterBattleDialogue;
import com.lvl6.mobsters.info.IMonsterForPvp;
import com.lvl6.mobsters.info.IMonsterLevelInfo;
import com.lvl6.mobsters.info.IObstacle;
import com.lvl6.mobsters.info.IPvpLeague;
import com.lvl6.mobsters.info.IQuest;
import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.IQuestJobMonsterItem;
import com.lvl6.mobsters.info.IStaticUserLevelInfo;
import com.lvl6.mobsters.info.IStructure;
import com.lvl6.mobsters.info.IStructureHospital;
import com.lvl6.mobsters.info.IStructureLab;
import com.lvl6.mobsters.info.IStructureMiniJob;
import com.lvl6.mobsters.info.IStructureResidence;
import com.lvl6.mobsters.info.IStructureResourceGenerator;
import com.lvl6.mobsters.info.IStructureResourceStorage;
import com.lvl6.mobsters.info.IStructureTownHall;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.ITaskMapElement;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

public class V2GenAttrsMain {

	public static void main(String[] args) throws IOException {
		final Class<?>[] sources = { 
			ITask.class, ITaskStage.class, ITaskStageMonster.class, IMonster.class, IMonsterLevelInfo.class, IMonsterForPvp.class,
			IAchievement.class, IBoosterPack.class, IBoosterItem.class, IBoosterDisplayItem.class, IEventPersistent.class, IItem.class,
			IMiniJob.class, IObstacle.class, IQuest.class, IQuestJob.class, IQuestJobMonsterItem.class, IStaticUserLevelInfo.class,
			IStructure.class, IStructureHospital.class, IStructureLab.class, IStructureMiniJob.class, IStructureResidence.class,
			IStructureResourceGenerator.class, IStructureResourceStorage.class, IStructureTownHall.class, ITaskMapElement.class,
			IStaticUserLevelInfo.class, IMonsterBattleDialogue.class, IPvpLeague.class};
		final String thisPackage = V2GenAttrsMain.class.getPackage().toString().substring(8);

		for( Class<?> nextClass : sources ) {
			FileWriter output = new FileWriter( nextClass.getSimpleName().substring(1) + "Attrs.java");
			output.write(
				AttributesGenerator.generateSeparateAttributesClass(nextClass, thisPackage)
			);
			output.close();
		}
	}
}
