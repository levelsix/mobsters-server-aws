package com.lvl6.mobsters.common.utils;

public abstract class AbstractObjComparable<T extends Comparable<? super T>> 
	implements Comparable<AbstractObjComparable<T>> {
	protected abstract T getOrderingObj();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getOrderingObj() == null) ? 0 : getOrderingObj().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		@SuppressWarnings("unchecked")
		T otherOrderId = ((AbstractObjComparable<T>) obj).getOrderingObj();
		T orderId = getOrderingObj();

		if (orderId == null) {
			if (otherOrderId != null)
				return false;
		} else if (!orderId.equals(otherOrderId))
			return false;
		return true;
	}

	@Override
	public int compareTo(AbstractObjComparable<T> o) {
		return getOrderingObj().compareTo(o.getOrderingObj());
	}
}
