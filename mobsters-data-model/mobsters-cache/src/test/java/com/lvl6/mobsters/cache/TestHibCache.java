package com.lvl6.mobsters.cache;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lvl6.mobsters.info.Clan;
import com.lvl6.mobsters.info.ClanIcon;
import com.lvl6.mobsters.info.repository.ClanIconRepository;
import com.lvl6.mobsters.info.repository.ClanRepository;


// @RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-db.xml", "classpath:spring-redis.xml"})
public class TestHibCache {

	
	@Autowired
	protected ClanRepository clanRepo;
	
	@Autowired
	protected ClanIconRepository clanIconRepo;
	
	
	public ClanRepository getClanRepo() {
		return clanRepo;
	}
	
	public ClanIconRepository getClanIconRepo() {
		return clanIconRepo;
	}

	private ClanIcon iconOne;
	private ClanIcon iconTwo;
	private Clan clanOne;
	private Clan clanTwo;
	
	@Before
	public void initialize() {
		iconOne = new ClanIcon();
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
		clanRepo.save(clanOne);

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
	public void testLotsaFetch() {
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
	}
}
