package com.lvl6.mobsters.utility.lambda;

/**
 * Float in a box.
 * 
 * Motivated by scenarios where you want to declare an anonymous subclass and need to
 * access an int counter from its outer scope, but can only access variables that have
 * a final modifier.
 * 
 * Supports every binary operator I could think of, and more can be added if I missed
 * something.
 * 
 * This class will through appropriate numeric exceptions if you pass it illegal input,
 * such as calling <code>divideBy(0)</code> or <code>set(0).divideInto(1234)</code>. 
 * 
 * @author jheinnic
 */
public class IntCounter {
    private int value;
    
    public IntCounter() {
    	value = 0;
    }
    
    public IntCounter(int value) {
    	this.value = value;
    }
    
    public IntCounter add(int value) {
    	this.value += value;
    	
    	return this;
    }
    
    public IntCounter add(long value) {
    	this.value += value;
    	
    	return this;
    }
    
    public IntCounter add(float value) {
    	this.value += value;
    	
    	return this;
    }
    
    public IntCounter add(double value) {
    	this.value += value;
    	
    	return this;
    }
    
    public IntCounter subtract(int value) {
    	this.value -= value;
    	
    	return this;
    }
    
    public IntCounter subtract(long value) {
    	this.value -= value;
    	
    	return this;
    }

    public IntCounter subtract(float value) {
    	this.value -= value;
    	
    	return this;
    }
    
    public IntCounter subtract(double value) {
    	this.value -= value;
    	
    	return this;
    }
    
    public IntCounter multiply(int value) {
    	this.value *= value;
    	
    	return this;
    }
    
    public IntCounter multiply(long value) {
    	this.value *= value;
    	
    	return this;
    }
    
    public IntCounter multiply(float value) {
    	this.value *= value;
    	
    	return this;
    }
    
    public IntCounter multiply(double value) {
    	this.value *= value;
    	
    	return this;
    }
    
    public IntCounter divideBy(int value) {
    	this.value /= value;
    	
    	return this;
    }
    
    public IntCounter divideBy(long value) {
    	this.value /= value;
    	
    	return this;
    }
   
    public IntCounter divideBy(float value) {
    	this.value /= value;
    	
    	return this;
    }
    
    public IntCounter divideBy(double value) {
    	this.value /= value;
    	
    	return this;
    }
    
    public IntCounter divideInto(int value) {
    	this.value = value / this.value;
    	
    	return this;
    }
    
    public IntCounter modulo(int value) {
    	this.value = value % this.value;
    	
    	return this;
    }
    
    public IntCounter setTo(int value) {
    	this.value = value;
    	
    	return this;
    }
    
    public boolean isEqual(int value) {
    	return this.value == value;
    }
    
    public boolean isEqual(long value) {
    	return this.value == value;
    }
    
    public boolean isEqual(float value) {
    	return this.value == value;
    }
    
    public boolean isEqual(double value) {
    	return this.value == value;
    }
    
    public boolean isInequal(int value) {
    	return this.value != value;
    }
    
    public boolean isInequal(long value) {
    	return this.value != value;
    }
    
    public boolean isInequal(float value) {
    	return this.value != value;
    }
    
    public boolean isInequal(double value) {
    	return this.value != value;
    }

    public boolean isLessThan(int value) {
    	return this.value < value;
    }
    
    public boolean isLessThan(long value) {
    	return this.value < value;
    }
    
    public boolean isLessThan(float value) {
    	return this.value < value;
    }
    
    public boolean isLessThan(double value) {
    	return this.value < value;
    }
    
    public boolean isLessOrEqual(int value) {
    	return this.value <= value;
    }
    
    public boolean isLessOrEqual(long value) {
    	return this.value <= value;
    }
    
    public boolean isLessOrEqual(float value) {
    	return this.value <= value;
    }
    
    public boolean isLessOrEqual(double value) {
    	return this.value <= value;
    }
    
    public boolean isGreaterThan(int value) {
    	return this.value > value;
    }
    
    public boolean isGreaterThan(long value) {
    	return this.value > value;
    }
    
    public boolean isGreaterThan(float value) {
    	return this.value > value;
    }
    
    public boolean isGreaterThan(double value) {
    	return this.value > value;
    }
    
    public boolean isGreaterOrEqual(int value) {
    	return this.value >= value;
    }
    
    public boolean isGreaterOrEqual(long value) {
    	return this.value >= value;
    }
    
    public boolean isGreaterOrEqual(float value) {
    	return this.value >= value;
    }
    
    public boolean isGreaterOrEqual(double value) {
    	return this.value >= value;
    }
    
    public int read() {
    	return this.value;
    }
}
