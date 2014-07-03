package com.lvl6.mobsters.cache;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.Cache;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.info.repository.AchievementRepository;


 @RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-db.xml", "classpath:spring-redis.xml"})
public class TestHibCache {

	@Before
	public void initialize() {
	}
	
	@After
	public void destroy() {
		achRepo.deleteInBatch(achRepo.findByAchievementNameStartingWith("test"));
		assertEquals("No achievements left post-delete", 0, achRepo.findAll().size());
	}
	
	
	@Autowired
	protected AchievementRepository achRepo;
	
	public AchievementRepository getAchRepo()
	{
		return achRepo;
	}

	public void setAchRepo( AchievementRepository achRepo )
	{
		this.achRepo = achRepo;
	}

	@Autowired
	protected EntityManagerFactory emf;
	
	
	public EntityManagerFactory getEmf()
	{
		return emf;
	}
	public void setEmf( EntityManagerFactory emf )
	{
		this.emf = emf;
	}
	
	@Test
	public void testCache() {
		Achievement ach = new Achievement(1, "test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, null, null);
		ach = achRepo.save(ach);
		
		Cache cache = emf.getCache();
		cache.evict(Achievement.class, ach.getId());
		int size = achRepo.findAll().size();
		assertEquals("Checking num achievements", 1, size);
		
		assertTrue("checking if achievement is cached!!!", cache.contains(Achievement.class, ach.getId()));
	}
	


	@Test
	public void testNoCacheOnSave() {
		Achievement ach = new Achievement(1, "test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, null, null);
		ach = achRepo.save(ach);
		
		Cache cache = emf.getCache();
		assertEquals("making sure achievement is not cached on save!!!", false, cache.contains(Achievement.class, ach));
	}

	@Test
	public void testCacheMethodNameQuery() {
		Achievement ach = new Achievement(1, "test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, null, null);
		ach = achRepo.save(ach);
		
		Cache cache = emf.getCache();
		cache.evict(Achievement.class, ach.getId());
		
		int size = achRepo.findByAchievementNameStartingWith("test").size();
		assertEquals("Checking num achievements", 1, size);
		
		assertTrue("checking if achievement is cached!!!", cache.contains(Achievement.class, ach.getId()));
	}
	
//	@Test
//	public void testCacheReturnMap() {
//		Achievement ach = new Achievement(1, "test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, null, null);
//		ach = achRepo.save(ach);
//		
//		Cache cache = emf.getCache();
//		assertEquals("making sure achievement is not cached on save!!!", false, cache.contains(Achievement.class, ach));
//
//		cache.evict(Achievement.class, ach.getId());
//		assertEquals("checking writing to db that achievement is not cached.", false, cache.contains(Achievement.class, ach.getId()));
//		
//		Map<Integer, Achievement> achievementsAsMap = achRepo.findAllAsMap();
//		
//		assertEquals("checking if repository returns a map.", true, achievementsAsMap.containsKey(ach.getId()));
//		assertEquals("checking if achievement is cached!!!", true, cache.contains(Achievement.class, ach.getId()));
//	}
}
