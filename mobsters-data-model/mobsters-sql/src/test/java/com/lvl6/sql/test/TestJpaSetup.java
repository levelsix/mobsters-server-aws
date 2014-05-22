package com.lvl6.sql.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.info.repository.AchievementRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-db.xml")
public class TestJpaSetup {

	
	@Autowired
	protected AchievementRepository achRepo;
	
	
	public AchievementRepository getAchRepo() {
		return achRepo;
	}


	public void setAchRepo(AchievementRepository achRepo) {
		this.achRepo = achRepo;
	}


	@Test
	public void test() {
		Achievement ach = new Achievement("test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, 0, 0);
		achRepo.save(ach);
		ach = new Achievement("test1", "test1", 1, 1, "test1", "test1", "test1", "test1", 1, 1, 1, 1, 1);
		achRepo.save(ach);
		ach = new Achievement("test2", "test2", 2, 2, "test2", "test2", "test2", "test2", 2, 2, 2, 2, 2);
		achRepo.save(ach);
		ach = new Achievement("test3", "test3", 3, 3, "test3", "test3", "test3", "test3", 3, 3,3, 3, 3);
		achRepo.save(ach);
		assertTrue("Quantity is 3", achRepo.findByQuantityGreaterThan(0).size() == 3);
		assertTrue("Lvl is 3", achRepo.findByLvlBetween(1, 3).size() == 3);
		achRepo.delete(achRepo.findByAchievementNameStartingWith("test"));
		
	}

}
