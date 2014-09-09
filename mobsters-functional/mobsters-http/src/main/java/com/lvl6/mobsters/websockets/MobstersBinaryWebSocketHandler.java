package com.lvl6.mobsters.websockets;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import com.lvl6.mobsters.utils.MobstersPlayerPrincipal;

public class MobstersBinaryWebSocketHandler extends BinaryWebSocketHandler implements MessageHandler
{
	private static final Logger LOG =
		LoggerFactory.getLogger(MobstersBinaryWebSocketHandler.class);
	
	private final MobstersCodec codec = new MobstersCodec();
	
	// Active sessions, keyed by User UUID.  A user may only be connected to one game session 
	// through one web socket to one game server at any point in time.
	private final ConcurrentHashMap<String,WebSocketSessionHolder> sessionRegistry = 
	 	new ConcurrentHashMap<String,WebSocketSessionHolder>();
	
	private ExecutorSubscribableChannel wsClientRequests;
	private ExecutorSubscribableChannel wsClientResponses;
	
	private int sendTimeLimit = 10 * 1000;

	private int sendBufferSizeLimit = 512 * 1024;

	private long latencyOnThread;

	private long latencyOffThread;

	private boolean dispatchOffThread;

	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage wsRequest) throws Exception
	{
		final Message<byte[]> msg =
			codec.decode(wsRequest);
			
		if (dispatchOffThread) {
			// TODO: There is a second indirection through Principal to take care of... 
			//       Check out the StompSubProtoHandler for a pointer towards it.
			final MobstersPlayerPrincipal user = (MobstersPlayerPrincipal) session.getPrincipal();

			final MobstersHeaderAccessor headers =
				MobstersHeaderAccessor.wrap(msg);

			// TODO: Check for an incorrectly set header or missing header before applying one.
			// TODO: Observe that this assignment doesn't actually accomplish anything...
			headers.setPlayer(user);

			// To avoid writing a input-to-output transform, we're making an intentional error and
			// publishing directly to the response channel.  Sufficient for this exercise, but technically
			// very much incorrect.
			wsClientResponses.send(
				MessageBuilder.fromMessage(msg)
				.setHeader(SimpMessageHeaderAccessor.USER_HEADER, user)
				.setHeader(
					MobstersHeaderAccessor.MOBSTERS_PLAYER_ID_HEADER, 
					user.getName())
				.setHeader(
					MobstersHeaderAccessor.MOBSTERS_PLAYER_TYPE_HEADER,
					user.getIdType())
				.build());
		} else {
			if (latencyOnThread > 0) {
				Thread.sleep(latencyOnThread);
			}
			
			session.sendMessage(
				codec.encode(msg));
		}
	}
	

	@Override
	public void afterConnectionEstablished( WebSocketSession session ) 
		throws Exception
	{
		session =
			new ConcurrentWebSocketSessionDecorator(
				session, sendTimeLimit, sendBufferSizeLimit);

		final String userUuid =
			(String) session.getAttributes()
			.get(MobstersHeaderAccessor.MOBSTERS_PLAYER_ID_HEADER);
		
		// TODO Verify userUuid syntax before using it.
		final WebSocketSessionHolder putResult = this.sessionRegistry.put(
			userUuid, new WebSocketSessionHolder(session)
		);

		if ((putResult != null) && (putResult.getSession() != session)) {
			this.sessionRegistry.put(userUuid, putResult);
			throw new IllegalStateException(
				"Protocol error!  There is a login session already open for user " + userUuid
				+".  Close all of this user's outstanding sessions before attempting a new one."
			);
		}
		
		LOG.debug(
			"Started WebSocket session=%s.  Session count is now %d",
			userUuid, this.sessionRegistry.size()
		);
	}

	private static class WebSocketSessionHolder
	{

		private final WebSocketSession session;

		private final long createTime = System.currentTimeMillis();

		private volatile boolean handledMessages;


		private WebSocketSessionHolder(WebSocketSession session)
		{
			this.session = session;
		}

		public WebSocketSession getSession()
		{
			return this.session;
		}

		public long getCreateTime()
		{
			return this.createTime;
		}

		public void setHasHandledMessages()
		{
			this.handledMessages = true;
		}

		public boolean hasHandledMessages()
		{
			return this.handledMessages;
		}

		@Override
		public String toString()
		{
			return "WebSocketSessionHolder[=session=" + this.session + ", createTime=" +
					this.createTime + ", hasHandledMessages=" + this.handledMessages + "]";
		}
	}
	
	
	/**
	 * This method is called through the "wsClientResponseChannel", which is provided for Messaging
	 * applications that want to sent replies back to a websocket as responses to some previous request
	 * message.
	 * 
	 * When a Session was first opened, this class wrapped it with a thread-safety decorator and put
	 * it into a ConcurrentHashMap keyed by Player UUID.
	 * 
	 * When a request arrived for processing, this class sent it to the wsClientRequestChannel for
	 * incoming requests.  That channel is privately consumed by two built-in MessageHandlers, one
	 * that uses annotations to route messages to candidate MessageHandlers, and another that 
	 * recognizes and re-writes to-user app-to-app messages for routing through RabbitMQ over the
	 * STOMP protocol. (** This is not fully implemented here yet, but this is what is already 
	 * fully implemented in the STOMP from Client Websocket implementation)
	 * 
	 * If the result of any downstream processing channel is a message that is a reply to 
	 * a sender, an OutboundMessagingTemplate will have been used to place that message on the
	 * wsClientResponseChannel, which will land it in this MessageHandler, which knows how to 
	 * use a "user-uuid" (MOBSTERS_USER_UUID_HEADER) header as a key to retrieve the WebSocketSession
	 * and call its send() method with a binary encoding of the message as an argument.
	 * 
	 * In order for all this to work, the consumer has a critical responsibility--to copy the 
	 * MOBSTERS_USER_UUID_HEADER header value from the original incoming Message<GeneratedMessage>
	 * to the outgoing Message<GeneratedMessage> whose payload is a response to the request.`
	 * If this is forgotten, nothing will get sent back to the websocket client (we wouldn't know
	 * who they were anymore), but an error will appear in the local log file.
	 * 
	 * @param outgoingMsg
	 */
	@SuppressWarnings("unchecked")
	public void handleMessage(Message<?> outgoingMsg) throws MessagingException 
	{
		if (latencyOffThread > 0) {
			try {
				Thread.sleep(latencyOffThread);
			} catch (InterruptedException e) {
				LOG.error("Interrupted sleep", e);
				return;
			}
		}
	
		final MobstersHeaderAccessor headers =
			MobstersHeaderAccessor.wrap(outgoingMsg);
		final String userUuid = headers.getPlayer().getName();
		if ((! StringUtils.hasText(userUuid)) && (! "null".equals(userUuid)))
		{
			throw new IllegalArgumentException(
				"Cannot route messages to websocket clients unless their "
				+ "MOBSTERS_PLAYER_ID_HEADER"
				+ "session attribute and message header have both already been set.");
		}

		final WebSocketSessionHolder sessionHolder =
		    sessionRegistry.get(userUuid);
		if (sessionHolder == null) {
			throw new IllegalArgumentException(
				"User " + userUuid + " is not currently logged int...");
		}

		final WebSocketSession session =
			sessionHolder.getSession();
		try {
			session.sendMessage(
				codec.encode((Message<byte[]>) outgoingMsg));
		} catch (IOException e) {
			LOG.error("IOException while sending message", e);
		}
	}

	public void setSendTimeLimit(int sendTimeLimit)
	{
		this.sendTimeLimit = sendTimeLimit;
	}

	public void setSendBufferSizeLimit(int sendBufferSizeLimit)
	{
		this.sendBufferSizeLimit = sendBufferSizeLimit;
	}

	public void setLatencyOnThread(long latencyOnThread)
	{
		this.latencyOnThread = latencyOnThread;
	}

	public void setLatencyOffThread(long latencyOffThread)
	{
		this.latencyOffThread = latencyOffThread;
	}

	public void setDispatchOffThread(boolean dispatchOffThread)
	{
		this.dispatchOffThread = dispatchOffThread;
	}

	public void setWsClientRequests(ExecutorSubscribableChannel wsClientRequests)
	{
		// TODO: None of the actual request dispatchers are wired yet.
		this.wsClientRequests = wsClientRequests;
	}

	public void setWsClientResponses(ExecutorSubscribableChannel wsClientResponses)
	{
		this.wsClientResponses = wsClientResponses;
		this.wsClientResponses.subscribe(this);
	}
}
