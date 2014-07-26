package com.lvl6.mobsters.conditions;

public interface Director<T> {
    public void apply( T builder );
}
