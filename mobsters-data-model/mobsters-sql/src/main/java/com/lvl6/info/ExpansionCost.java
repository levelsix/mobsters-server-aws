package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ExpansionCost extends BasePersistentObject{	

	@Column(name = "final")
	private static final long serialVersionUID = 1486947262199932773L;
	@Column(name = "expansion_cost_cash")
	private int expansionCostCash;
	@Column(name = "num_minutes_to_expand")
	private int numMinutesToExpand;	
	public ExpansionCost(){}
	public ExpansionCost(int id, int expansionCostCash, int numMinutesToExpand) {
		super();
		this.expansionCostCash = expansionCostCash;
		this.numMinutesToExpand = numMinutesToExpand;
	}



	public int getExpansionCostCash() {
		return expansionCostCash;
	}

	public void setExpansionCostCash(int expansionCostCash) {
		this.expansionCostCash = expansionCostCash;
	}

	public int getNumMinutesToExpand() {
		return numMinutesToExpand;
	}

	public void setNumMinutesToExpand(int numMinutesToExpand) {
		this.numMinutesToExpand = numMinutesToExpand;
	}

	@Override
	public String toString() {
		return "ExpansionCost [id=" + id + ", expansionCostCash="
				+ expansionCostCash + ", numMinutesToExpand=" + numMinutesToExpand
				+ "]";
	}
	
}
