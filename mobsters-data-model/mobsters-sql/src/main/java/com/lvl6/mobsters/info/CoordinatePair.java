package com.lvl6.mobsters.info;


//@Entity
public class CoordinatePair {//extends BasePersistentObject{
	
	//@Column(name = "x")
	private float x;
	//@Column(name = "y")
	private float y;
	public CoordinatePair(){}
	public CoordinatePair(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public String toString() {
		return "CoordinatePair [x=" + x + ", y=" + y + "]";
	}
}
