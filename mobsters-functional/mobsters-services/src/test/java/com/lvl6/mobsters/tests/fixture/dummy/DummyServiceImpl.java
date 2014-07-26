package com.lvl6.mobsters.tests.fixture.dummy;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.AbstractService;
import com.lvl6.mobsters.common.utils.IRunnableAction;

@Component
public class DummyServiceImpl 
	extends AbstractService
	implements IDummyService 
{
	// All injectable dependencies are inherited in this case.
	
    @Override
	public IRunnableAction doSomethingForCaller( 
		String id, int size, String title ) 
    {
    	final DummyRunnableAction retVal = 
    		new DummyRunnableAction(this, id, size, title);
    	return retVal;
    }
}
