package com.lvl6.mobsters.utility.lambda;

/**
 * Float in a box.
 * 
 * Motivated by scenarios where you want to declare an anonymous subclass and need to
 * access a float counter from its outer scope, but can only access variables that have
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
public class FloatCounter {
    private float value;
    
    public FloatCounter() {
    	value = 0f;
    }
    
    public FloatCounter(float value) {
    	this.value = value;
    }
    
    public FloatCounter add(int value) {
    	this.value += value;
    	
    	return this;
    }
    
    public FloatCounter add(float value) {
    	this.value += value;
    	
    	return this;
    }
    
    public FloatCounter add(double value) {
    	this.value += value;
    	
    	return this;
    }
    
    public FloatCounter subtract(int value) {
    	this.value -= value;
    	
    	return this;
    }

    public FloatCounter subtract(float value) {
    	this.value -= value;
    	
    	return this;
    }
    
    public FloatCounter subtract(double value) {
    	this.value -= value;
    	
    	return this;
    }
    
    public FloatCounter multiply(int value) {
    	this.value *= value;
    	
    	return this;
    }
    
    public FloatCounter multiply(float value) {
    	this.value *= value;
    	
    	return this;
    }
    
    public FloatCounter multiply(double value) {
    	this.value *= value;
    	
    	return this;
    }
    
    public FloatCounter divideBy(int value) {
    	this.value /= value;
    	
    	return this;
    }
    
    public FloatCounter divideBy(float value) {
    	this.value /= value;
    	
    	return this;
    }
    
    public FloatCounter divideBy(double value) {
    	this.value /= value;
    	
    	return this;
    }
    
    public FloatCounter divideInto(int value) {
    	this.value = value / this.value;
    	
    	return this;
    }
    
    public FloatCounter divideInto(float value) {
    	this.value = value / this.value;
    	
    	return this;
    }
    
    public FloatCounter setTo(int value) {
    	this.value = value;
    	
    	return this;
    }
    
    public FloatCounter set(float value) {
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
    
    public float read() {
    	return this.value;
    }
}
