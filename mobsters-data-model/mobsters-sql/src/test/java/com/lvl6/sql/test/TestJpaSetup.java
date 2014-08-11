package com.lvl6.sql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.info.repository.AchievementRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-commons.xml", "classpath:spring-db.xml"})
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
		Collection<Achievement> achs = achRepo.findByAchievementNameStartingWith("test");
		achRepo.delete(achs);
		Achievement ach = new Achievement(1, "test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, null, null);
		achRepo.save(ach);
		ach = new Achievement(2, "test1", "test1", 1, 1, "test1", "test1", "test1", "test1", 1, 1, 1, null, null);
		achRepo.save(ach);
		ach = new Achievement(3, "test2", "test2", 2, 2, "test2", "test2", "test2", "test2", 2, 2, 2, null, null);
		achRepo.save(ach);
		ach = new Achievement(4, "test3", "test3", 3, 3, "test3", "test3", "test3", "test3", 3, 3,3, null, null);
		achRepo.save(ach);
		int size = achRepo.findByQuantityGreaterThan(0).size();
		assertTrue("Quantity expected: 3. actual:" + size, size == 3);
		assertTrue("Lvl is 3", achRepo.findByLvlBetween(1, 3).size() == 3);
		achRepo.deleteInBatch(achRepo.findByAchievementNameStartingWith("test"));
	    assertEquals("No achievements left post-delete", 0, achRepo.findAll().size());
	}

	
}
