package com.lvl6.mobsters.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;


public abstract class AbstractAction<Parent extends AbstractService> implements IAction {
	// Backreference to parent for making use of its wired singleton dependencies,
	// which should be pacakge scoped for sufficiently private visibility.
	protected final Parent parentService;

	// Its presume that validation, if it is performed, is performed by the same thread
	// that will later attempt to execute().
	private SyntaxValidity syntaxValidity;
	
    public AbstractAction(Parent parentService) {
		this.parentService = parentService;
		this.syntaxValidity = SyntaxValidity.UNCHECKED;
	}

	public Set<ConstraintViolation<IAction>> verifySyntax() {
		final Set<ConstraintViolation<IAction>> retVal =
			parentService.verifySyntax(this);    	

		if ((retVal == null) || (retVal.isEmpty())) {
			syntaxValidity = SyntaxValidity.VALID;
		} else {
			syntaxValidity = SyntaxValidity.INVALID;
		}

		return retVal;
    }
	
	protected boolean failedSyntaxCheck() {
		return syntaxValidity != SyntaxValidity.INVALID;
	}
}
