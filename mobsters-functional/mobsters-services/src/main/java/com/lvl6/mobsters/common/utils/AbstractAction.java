package com.lvl6.mobsters.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;


public abstract class AbstractAction implements IAction {
	private final AbstractService parentService;
	private boolean failedSyntaxCheck;
	
    public AbstractAction(AbstractService parentService) {
		this.parentService = parentService;
		this.failedSyntaxCheck = false;
	}

	public Set<ConstraintViolation<AbstractAction>> verifySyntax() {
		Set<ConstraintViolation<AbstractAction>> retVal = parentService.verifySyntax(this);    	
		failedSyntaxCheck = (retVal != null) && (!retVal.isEmpty());
		return retVal;
    }
	
	protected boolean failedSyntaxCheck() {
		return failedSyntaxCheck;
	}
}
