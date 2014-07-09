package com.lvl6.sql.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.mobsters.info.MonsterLevelInfo;
import com.lvl6.mobsters.info.repository.MonsterLevelInfoRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-db.xml")
public class TestExerciseFilteringQuery {

	
	@Autowired
	protected MonsterLevelInfoRepository mliRepo;
	
	public MonsterLevelInfoRepository getAchRepo() {
		return mliRepo;
	}


	public void setMonsterLevelInfoRepository(MonsterLevelInfoRepository mliRepo) {
		this.mliRepo = mliRepo;
	}


	@Test
	public void testGetFirst() {
		MonsterLevelInfo myFoo = mliRepo.findFirstLevelByMonsterId(30, new PageRequest(0, 1)).getContent().get(0);
		Assert.assertNotNull(myFoo);
		Assert.assertEquals(1, myFoo.getLevel());
		Assert.assertEquals(30, myFoo.getMonster().getId());
	}	

	@Test
	public void testGetSome() {
		List<MonsterLevelInfo> myFoo = mliRepo.findFirstLevelByMonsterId(30, new PageRequest(0, 2)).getContent();
		System.out.println(myFoo.toString());
	}	
}