package com.lvl6.mobsters.common.utils;

public abstract class AbstractIntComparable 
	implements Comparable<AbstractIntComparable> 
{
	protected abstract int getOrderingInt();

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = prime * getOrderingInt();
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		int otherOrderInt = ((AbstractIntComparable) obj).getOrderingInt();
		int orderInt = getOrderingInt();

		if (orderInt != otherOrderInt)
			return false;
		
		return true;
	}

	@Override
	public int compareTo(AbstractIntComparable o) 
	{
		int otherOrderInt = o.getOrderingInt();
		int orderInt = getOrderingInt();
		
		if (orderInt < otherOrderInt) {
			return -1;
		} else if( otherOrderInt < orderInt) {
			return 1;
		}
		
		return 0;
	}
}
