package com.lvl6.eventdispatcher;

public interface BinaryClientEventDispatcher {
	public abstract void dispatchNormalEvent(String userId, byte[] event);
	public abstract void dispatchPreDatabaseEvent(String udid, byte[] event);
	public abstract void dispatchBroadcastEvent(String userId, byte[] event);
}
