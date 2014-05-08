package com.lvl6.sql.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.info.Achievement;
import com.lvl6.info.repository.AchievementRepository;


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
		Achievement ach = new Achievement("test", "test", 0, 0, "test", "test", "test", "test", 0, 0, 0, 0, 0);
		achRepo.save(ach);
		//fail("Not yet implemented");
	}

}
