package com.lvl6.mobsters.common.utils;

/**
 * The callable IAction variant has an execute method, just like {@link IRunnableAction}, but 
 * its call signature is different because it uses a template type to specify a builder interface
 * it understands for providing result status feedback to the caller.
 * 
 * @author jheinnic
 *
 * @param <R>
 */
public interface ICallableAction<R> extends IAction {
	/**
	 * @throws Lvl6MobstersException
	 */
    public void execute(R resultBuilder);
}
