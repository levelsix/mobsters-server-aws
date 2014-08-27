package com.lvl6.mobsters.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;

public abstract class AbstractService
{
	@Autowired
	protected DataServiceTxManager txManager;
    
	@Autowired
    protected ValidatorFactory validatorFactory;
	
	Set<ConstraintViolation<IAction>> verifySyntax(final IAction action)
	{
		final Validator validator = validatorFactory.getValidator();
		return validator.validate(action);
	}
	
    final void setDataServiceTxManager(final DataServiceTxManager txManager)
    {
    	this.txManager = txManager;
    }
	
    final void setValidatorFactory(final ValidatorFactory validatorFactory)
    {
    	this.validatorFactory = validatorFactory;
    }
}
