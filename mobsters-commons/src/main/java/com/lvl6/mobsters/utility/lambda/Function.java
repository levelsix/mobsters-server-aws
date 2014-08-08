package com.lvl6.mobsters.utility.lambda;
public interface Function<T> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    void apply(T t);
}
