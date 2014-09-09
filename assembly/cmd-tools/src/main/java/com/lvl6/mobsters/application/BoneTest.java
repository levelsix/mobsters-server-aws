package com.lvl6.mobsters.application;

import com.jolbox.bonecp.BoneCPDataSource;

public class BoneTest {

	public static void main(String[] args) {
		BoneCPDataSource foo = new BoneCPDataSource();
		System.out.println("Got foo: " + foo);
	}
}
