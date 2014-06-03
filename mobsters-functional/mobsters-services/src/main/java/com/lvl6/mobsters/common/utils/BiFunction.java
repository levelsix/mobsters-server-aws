package com.lvl6.mobsters.common.utils;
public interface BiFunction<T, U> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    void apply(T t, U u);
}
