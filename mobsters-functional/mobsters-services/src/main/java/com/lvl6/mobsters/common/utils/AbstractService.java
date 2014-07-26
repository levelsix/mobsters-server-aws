package com.lvl6.mobsters.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {
	@Autowired
    private ValidatorFactory validatorFactory;
    
	<T extends AbstractAction> Set<ConstraintViolation<T>> verifySyntax(T action) {
		final Validator validator = validatorFactory.getValidator();
		return validator.validate(action);
	}
	
    void setValidatorFactory( ValidatorFactory validatorFactory ) {
    	this.validatorFactory = validatorFactory;
    }
}
