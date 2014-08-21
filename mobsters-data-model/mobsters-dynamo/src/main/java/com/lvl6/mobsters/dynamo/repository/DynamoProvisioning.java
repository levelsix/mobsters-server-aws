package com.lvl6.mobsters.dynamo.repository;

import org.springframework.stereotype.Component;

@Component 
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
