package com.lvl6.mobsters.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.annotation.MockNice;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lvl6.mobsters.services.monster.MonsterService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(/* TODO specify classes to prepare for test */)
public class UpdateMonsterHealthControllerTest {
	@MockNice
	private MonsterService monsterService;
	
	// SUT
	private UpdateMonsterHealthController updateMonsterHealthController;

	@Before
	public void createUpdateMonsterHealthController() throws Exception {
		updateMonsterHealthController = new UpdateMonsterHealthController();
		updateMonsterHealthController.setMonsterService(monsterService);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// TODO: Setup for static mocks here
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
