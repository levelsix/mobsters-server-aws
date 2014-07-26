package com.lvl6.mobsters.common.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;

public interface IAction {
    public Set<ConstraintViolation<AbstractAction>> verifySyntax();
}
