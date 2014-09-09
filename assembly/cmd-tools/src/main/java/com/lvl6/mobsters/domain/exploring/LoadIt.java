package com.lvl6.mobsters.domain.exploring;

import org.eclipse.emf.ecore.EPackage;

import com.lvl6.mobsters.domainmodel.player.MobstersPlayerPackage;

public class LoadIt {

	public static void main(String[] args) {
		EPackage player = MobstersPlayerPackage.eINSTANCE;
		
		System.out.println(player.toString());
	}

}
