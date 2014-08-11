package com.lvl6.mobsters.tests.e2e;


import java.util.Collections;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lvl6.mobsters.common.utils.AbstractAction;
import com.lvl6.mobsters.common.utils.IAction;
import com.lvl6.mobsters.common.utils.IRunnableAction;
import com.lvl6.mobsters.tests.fixture.dummy.IDummyService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-commons.xml",
		"classpath:spring-db.xml", "classpath:spring-redis.xml",
		"classpath:spring-dynamo.xml", "classpath:spring-services.xml",
		"classpath:spring-local-test-fixtures.xml" })
@TransactionConfiguration(transactionManager="lvl6Txm", defaultRollback=true)
public class E2ETestServiceValidation { 
	// Subject under test -- Includes the base object of this field's class and the AbstractAction base class of its children.
    @Autowired
    IDummyService userService;
	
    private final static String GOOD_ID = "0ec2f8a83a7d92ef728c092ba2dc62f5";
    private final static String BAD_ID = "cookies!";
    
    private final int GOOD_SIZE = 10;
    private final int MIN_SIZE = 4;
    private final int MAX_SIZE = 16;
    private final int BAD_SIZE = 0;

    private final static String GOOD_TITLE = "Once Upon a Time, Revised!";
    private final static String BAD_CHARS_TITLE = "hack0rz@neverland.com";
    private final static String BAD_SHORT_TITLE = "Me";
    private final static String BAD_BOTH_TITLE = "#";
    
    @Test
	public void testValidationPasses() {
		IAction goodAction = userService.doSomethingForCaller(GOOD_ID, GOOD_SIZE, GOOD_TITLE);
		Set<ConstraintViolation<AbstractAction>> retVal = goodAction.verifySyntax();
		
		Assert.assertNotNull("Return result must be empty, not null", retVal);
		Assert.assertEquals("Return result must be empty", Collections.emptySet(), retVal);
		Assert.assertEquals("Return result must be empty", 0, retVal.size());
	}
    
    @Test
	public void testValidSyntaxMayExecute() {
		IRunnableAction goodAction = userService.doSomethingForCaller(GOOD_ID, GOOD_SIZE, GOOD_TITLE);
		goodAction.verifySyntax();
		goodAction.execute();
	}
    
    @Test
	public void testValidSyntaxNoCheckMayExecute() {
		IRunnableAction goodAction = userService.doSomethingForCaller(GOOD_ID, GOOD_SIZE, GOOD_TITLE);
		goodAction.execute();
	}
    
    @Test 
    public void testOneFailureTriggers()
    {
    	IAction oneFlawAction = userService.doSomethingForCaller(GOOD_ID, BAD_SIZE, GOOD_TITLE);
    	Set<ConstraintViolation<AbstractAction>> retVal = oneFlawAction.verifySyntax();
    		
    	Assert.assertNotNull("Return result be non-null", retVal);
    		
    	// TODO: Enable a better comparison that prints more on failure.
    	Assert.assertEquals("Return result must non-empty", 1, retVal.size());
    }
    
    @Test 
    public void testTwoFailedPropertiesTriggers()
    {
    	IAction twoByOneFlawsAction = userService.doSomethingForCaller(GOOD_ID, BAD_SIZE, BAD_CHARS_TITLE);
    	Set<ConstraintViolation<AbstractAction>> retVal = twoByOneFlawsAction.verifySyntax();
    		
    	Assert.assertNotNull("Return result be non-null", retVal);
    		
    	// TODO: Enable a better comparison that prints more on failure.
    	Assert.assertEquals("Return result must non-empty", 2, retVal.size());
    }
    
    @Test 
    public void testTwoFailuresOnePropertyTriggers()
    {
    	IAction oneByTwoFlawsAction = userService.doSomethingForCaller(GOOD_ID, GOOD_SIZE, BAD_BOTH_TITLE);
    	Set<ConstraintViolation<AbstractAction>> retVal = oneByTwoFlawsAction.verifySyntax();
    		
    	Assert.assertNotNull("Return result be non-null", retVal);
    		
    	// TODO: Enable a better comparison that prints more on failure.
    	Assert.assertEquals("Return result must non-empty", 2, retVal.size());
    }
    
    @Test 
    public void testMaxFailuresTriggers()
    {
    	IAction manyFlawsAction = userService.doSomethingForCaller(BAD_ID, BAD_SIZE, BAD_BOTH_TITLE);
    	Set<ConstraintViolation<AbstractAction>> retVal = manyFlawsAction.verifySyntax();
    		
    	Assert.assertNotNull("Return result be non-null", retVal);
    		
    	// TODO: Enable a better comparison that prints more on failure.
    	Assert.assertEquals("Return result must non-empty", 5, retVal.size());
    }
    
    @Test(expected=IllegalStateException.class) 
    public void testOneFailureBlocksExecute()
    {
    	final IRunnableAction oneFlawAction = 
    		userService.doSomethingForCaller(null, GOOD_SIZE, GOOD_TITLE);
    	oneFlawAction.verifySyntax();
    	oneFlawAction.execute();
    }
    
    @Test(expected=IllegalStateException.class) 
    public void testTwoFailedPropertiesBlocksExecute()
    {
    	final IRunnableAction twoByOneFlawsAction = 
        	userService.doSomethingForCaller(null, GOOD_SIZE, BAD_SHORT_TITLE);
    	twoByOneFlawsAction.verifySyntax();
    	twoByOneFlawsAction.execute();
    }
    
    @Test(expected=IllegalStateException.class) 
    public void testTwoFailuresOnePropertyBlocksExecute()
    {
    	final IRunnableAction oneByTwoFlawsAction = 
        	userService.doSomethingForCaller(BAD_ID, GOOD_SIZE, GOOD_TITLE);
        oneByTwoFlawsAction.verifySyntax();
        oneByTwoFlawsAction.execute();

    }
    
    @Test(expected=IllegalStateException.class) 
    public void testManyFailuresBlocksExecute()
    {
    	final IRunnableAction manyFlawsAction = 
    		userService.doSomethingForCaller(BAD_ID, BAD_SIZE, BAD_BOTH_TITLE);
    	manyFlawsAction.verifySyntax();
    	manyFlawsAction.execute();
    }
    
    @Test
    public void testOneFailureNoCheckAllowsExecute()
    {
    	final IRunnableAction oneFlawAction = 
    		userService.doSomethingForCaller(GOOD_ID, GOOD_SIZE, BAD_CHARS_TITLE);
    	oneFlawAction.execute();
    }
    
    @Test 
    public void testTwoFailedPropertiesNoCheckAllowsExecute()
    {
    	final IRunnableAction oneByTwoFlawsAction = 
    		userService.doSomethingForCaller(GOOD_ID, BAD_SIZE, null);
    	oneByTwoFlawsAction.execute();
    }
    
    @Test
    public void testTwoFailuresOnePropertyNoCheckAllowsExecute()
    {
    	final IRunnableAction twoByOneFlawsAction = 
    		userService.doSomethingForCaller(GOOD_ID, GOOD_SIZE, BAD_BOTH_TITLE);
    	twoByOneFlawsAction.execute();
    }
    
    @Test
    public void testManyFailuresNoCheckAllowsExecute()
    {
    	final IRunnableAction manyFlawsAction = 
    		userService.doSomethingForCaller(BAD_ID, BAD_SIZE, BAD_BOTH_TITLE);
    	manyFlawsAction.execute();
    }
}
