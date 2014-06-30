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

import com.esotericsoftware.minlog.Log;
import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.info.repository.AchievementRepository;


 @RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-db.xml", "classpath:spring-redis.xml"})
public class TestHibCache {

	
	/*@Autowired
	protected ClanRepository clanRepo;
	
	@Autowired
	protected ClanIconRepository clanIconRepo;
	
	
	public ClanRepository getClanRepo() {
		return clanRepo;
	}
	
	public ClanIconRepository getClanIconRepo() {
		return clanIconRepo;
	}*/

	/*private ClanIcon iconOne;
	private ClanIcon iconTwo;
	private Clan clanOne;
	private Clan clanTwo;*/
	
	@Before
	public void initialize() {
		/*iconOne = new ClanIcon();
		iconOne.setId(6);
		iconOne.setImgName("imageNameOne.jpg");
		iconOne.setAvailable(true);
		clanIconRepo.save(iconOne);

		clanOne = new Clan();
		clanOne.setName("Clan One");
		clanOne.setDescription("Description of Clan One");
		clanOne.setTag("ClanTag");
		clanOne.setRequestToJoinRequired(true);
		// clanOne.setClanIcon(iconOne);
		clanRepo.save(clanOne);*/

//		assertTrue("Quantity is 3", achRepo.findByQuantityGreaterThan(0).size() == 3);
//		assertTrue("Lvl is 3", achRepo.findByLvlBetween(1, 3).size() == 3);
	}
	
	@After
	public void destroy() {
		// clanRepo.delete();
		// clanIconRepo.delete(iconOne);
	}
	
	private static final int REPETITIONS = 150;
	
	// @Test
	/*public void testLotsaFetch() {
		// NOTE: At the moment, this doesn't actually do anything to prove that repeat queries for the same
		//       object actually come from cache instead of extra queries.  It does however provide a 
		//       rudimentary set of circumstances for observing whether the cache is being utilized in a
		//       simplest-case scenario, which is all time permits at present.
		long start_t = System.currentTimeMillis();
		final Clan initialRead = clanRepo.findByName("Clan One");
		assertNotNull(initialRead);
		for (int ii=0; ii<REPETITIONS; ii++) {
			final Clan nextRead = clanRepo.findByName("Clan One");
			assertNotNull(nextRead);
			assertSame("Hibernate L2 cache is not working if same repository is accessed with two different sessions and the same java object is not returned for the same input query.", initialRead, nextRead);
		}
		long delta_t = System.currentTimeMillis() - start_t;
	    System.out.println("Time for <" + REPETITIONS + ">: " + delta_t);	
	}*/
	
	
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
//		Collection<Achievement> achs = achRepo.findByAchievementNameStartingWith("test");
//		achRepo.delete(achs);
		Achievement ach = new Achievement(1, "test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, null, null);
		ach = achRepo.save(ach);
		
		Cache cache = emf.getCache();
		assertEquals("making sure achievement is not cached on save!!!", false, cache.contains(Achievement.class, ach));
		
		int size = achRepo.findAll().size();
		assertTrue("Quantity expected: 1. actual:" + size, size == 1);
//		achRepo.findAll();
		
//		if (cache.contains(Achievement.class, ach.getId())) {
//			Log.info("in the cache!");
//		}
		
//		ach = achRepo.findOne(ach.getId());
		
		assertEquals("checking if achievement is cached!!!", true, cache.contains(Achievement.class, ach.getId()));
		
		achRepo.deleteInBatch(achRepo.findByAchievementNameStartingWith("test"));
		assertEquals("No achievements left post-delete", 0, achRepo.findAll().size());
	}

	@Test
	public void testCacheNamedQuery() {
//		Collection<Achievement> achs = achRepo.findByAchievementNameStartingWith("test");
//		achRepo.delete(achs);
		Achievement ach = new Achievement(1, "test0", "test0", 0, 0, "test0", "test0", "test0", "test0", 0, 0, 0, null, null);
		ach = achRepo.save(ach);
		
		Cache cache = emf.getCache();
		assertEquals("making sure achievement is not cached on save!!!", false, cache.contains(Achievement.class, ach));

		//it is cached...
//		assertEquals("checking writing to db that achievement is not cached.", false, cache.contains(Achievement.class, ach.getId()));
		
		cache.evict(Achievement.class, ach.getId());
		assertEquals("checking writing to db that achievement is not cached.", false, cache.contains(Achievement.class, ach.getId()));
		
		int size = achRepo.findByAchievementNameStartingWith("test").size();
		assertTrue("Quantity expected: 1. actual:" + size, size == 1);
		
		assertEquals("checking if achievement is cached!!!", true, cache.contains(Achievement.class, ach.getId()));
		
		achRepo.deleteInBatch(achRepo.findByAchievementNameStartingWith("test"));
		assertEquals("No achievements left post-delete", 0, achRepo.findAll().size());
	}
}
