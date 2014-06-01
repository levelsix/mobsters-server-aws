package com.lvl6.mobsters.tests.fixture;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.mobsters.tests.fixture.PartialDataSet.ObjectMeta;

@Component
public class InitWorldService {
    private HashSet<JpaRepository<?, ?>> resetRepos;

	@Autowired
    List<PartialDataSet> partialDataSets;
    
	private HashMap<String,ObjectMeta<?>> itemMap;
	
    @Transactional
    public void tearDown() {
    	for( final JpaRepository<?, ?> nextRepo : resetRepos ) {
    		nextRepo.deleteAllInBatch();
    	}
    	this.itemMap = null;
	}
	
    @Transactional
    public void buildUp() { 
    	itemMap = new HashMap<String, ObjectMeta<?>>();
    	for (PartialDataSet nextDataSet : partialDataSets) {
    		nextDataSet.repopulate();
    		for (final ObjectMeta<?> nextObject : nextDataSet.getObjects()) {
    			itemMap.put(nextObject.getName(), nextObject);
    		}
    	}
    }
    
    public <T> T getItem(String name) {
    	if (itemMap == null) {
    		throw new IllegalStateException("Must call buildUp() before getItem() after calling tearDown() or on first use.");
    	}
    	@SuppressWarnings("unchecked")
		ObjectMeta<T> meta = (ObjectMeta<T>) itemMap.get(name);

    	if (meta != null) {
    		return meta.getInst();
    	} else {
    		return null;
    	}
    }
    
    @PostConstruct
    void processDataSets() {
    	resetRepos = new HashSet<JpaRepository<?, ?>>();
    	
    	for (final PartialDataSet nextDataSet : partialDataSets) {
    		resetRepos.addAll(nextDataSet.getUsedRepositories());
    	}
    }
}
