package com.lvl6.mobsters.binaryproto;

import com.google.protobuf.Message;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;

public class ParsedProtoRequest<M extends Message> {
    private final M wrappedMessage;
    private final byte[] binarySource;
    private final EventProtocolRequest requestType;
    
    public ParsedProtoRequest(
    	M wrappedMessage, byte[] binarySource, int requestType )
    {
    	this.wrappedMessage = wrappedMessage;
    	this.binarySource = binarySource;
    	this.requestType = EventProtocolRequest.valueOf(requestType);
    }

	public M getWrappedMessage() {
		return wrappedMessage;
	}

	public byte[] getBinarySource() {
		return binarySource;
	}

	public EventProtocolRequest getRequestType() {
		return requestType;
	}
}
