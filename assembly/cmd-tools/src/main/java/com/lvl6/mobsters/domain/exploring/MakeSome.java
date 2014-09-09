package com.lvl6.mobsters.domain.exploring;

import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

import com.lvl6.mobsters.domainmodel.player.CompletedTaskInternal;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerFactory;
import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;
import com.lvl6.mobsters.domainmodel.player.MonsterInternal;
import com.lvl6.mobsters.domainmodel.player.PlayerInternal;
import com.lvl6.mobsters.domainmodel.player.util.MobstersPlayerResourceFactoryImpl;

public class MakeSome {

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("unused")
		EPackage player = MobstersPlayerPackage.eINSTANCE;
		
		PlayerInternal u = MobstersPlayerFactory.eINSTANCE.createPlayerInternal();
		MonsterInternal m = MobstersPlayerFactory.eINSTANCE.createMonsterInternal();
		CompletedTaskInternal t = MobstersPlayerFactory.eINSTANCE.createCompletedTaskInternal();
		
		u.getMonsters().add(m);
		
		u.getTeamSlots().add(m);
		u.getTeamSlots().add(m);
		u.getTeamSlots().add(m);

		u.getTeamSlots().set(0, m);
		u.getTeamSlots().set(1, m);
		u.getTeamSlots().set(2, m);
		
		u.getCompletedTasks().add(t);

		Resource.Factory rsrcFactory = 
			new MobstersPlayerResourceFactoryImpl();
		Resource testResource = 
			rsrcFactory.createResource(
				URI.createFileURI("src/main/resources/firstModel.mobplayer"));

		testResource.setTrackingModification(true);
		testResource.getContents().add(u);
		
		FileOutputStream writer = 
			new FileOutputStream("src/main/resources/firstModel.mobplayer");
		testResource.save(writer, null);
		writer.close();
	}

}


