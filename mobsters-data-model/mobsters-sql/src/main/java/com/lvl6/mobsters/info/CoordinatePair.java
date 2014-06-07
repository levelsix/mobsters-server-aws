package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CoordinatePair extends BasePersistentObject{
	
	private static final long serialVersionUID = -116541086912448006L;
	@Column(name = "x")
	private float x;
	@Column(name = "y")
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
