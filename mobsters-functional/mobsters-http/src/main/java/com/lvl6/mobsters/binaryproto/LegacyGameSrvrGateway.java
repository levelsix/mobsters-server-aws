package com.lvl6.mobsters.binaryproto;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.lvl6.mobsters.binaryproto.MobstersEncoder.MobstersRequestEncoder;

/**
 * Messaging client interface that uses the MessagingTemplate encapsulating the
 * to-Broker channel of a Spring STOMP relay over WebSocket component.
 * 
 * TODO: I think this is more aptly considered to be a Gateway than an Adapter,
 * in Spring terms. Consider renaming and re-wiring for clarity and a more
 * suitable implementation-oriented feature set.
 * 
 * @author jheinnic
 *
 */
public class LegacyGameSrvrGateway {
	private final SimpMessagingTemplate brokerMessageTemplate;
	private final MobstersEncoder encoder;

	LegacyGameSrvrGateway(SimpMessagingTemplate brokerMessageTemplate) {
		this.brokerMessageTemplate = brokerMessageTemplate;
		this.encoder = new MobstersRequestEncoder();
	}

	/**
	 * Translates a Spring Messaging representation of a binary game server
	 * ProtoBuf request message that has not yet been encoded by prepending a
	 * 12-byte Lvl6 header, then places it in the game server's inbound queue.
	 * 
	 * All information needed to complete that header can be found in the
	 * out-of-band header fields of the Spring encapsulation.
	 * 
	 * @param request
	 *            A Spring Messaging message encapsulating a serialized
	 *            protobuf, and carrying all metadata required for Lvl6 
	 *            encoding in its headers.
	 */
	public void sendRequest(final Message<byte[]> request) {
		brokerMessageTemplate.convertAndSend(
			"/exchange/gamemessages/messagesFromPlayers",
			encoder.encode(request));
	}
}
