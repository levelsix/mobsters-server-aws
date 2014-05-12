package com.lvl6.dynamo.repository;

public class DynamoProvisioning {
	protected Long reads = 1l;
	protected Long writes = 1l;
	public Long getReads() {
		return reads;
	}
	public void setReads(Long reads) {
		this.reads = reads;
	}
	public Long getWrites() {
		return writes;
	}
	public void setWrites(Long writes) {
		this.writes = writes;
	}

}
