package com.lvl6.mobsters.info.repository;

// import java.util.List;
// import org.springframework.data.jpa.repository.JpaRepository;

// import com.lvl6.mobsters.info.BoosterItem;

@Deprecated
public interface BoosterItemRepository // extends JpaRepository<BoosterItem, Integer> 
{
	/**
	 * Unnecessary and encourages Hibernate to index each collected item rather than the collection as a whole.
	 * 
	 * Use BoosterPackRepository.findById( int boosterPackId ).getPackItems() instead.
	 */
	// List<BoosterItem> findByBoosterPackId( int boosterPackId );
	
}
