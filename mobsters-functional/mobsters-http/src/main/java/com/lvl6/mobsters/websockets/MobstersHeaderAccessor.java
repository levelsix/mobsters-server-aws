package com.lvl6.mobsters.websockets;
  
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.websockets.MobstersPlayerPrincipal.UserIdentityType;

/**
 * Header accessor class for Mobsters protocol.
 * 
 * TODO: What is the difference between a native header and an ordinary header?
 * 
 * @author jheinnic
 *
 */
public class MobstersHeaderAccessor
{
	public static final String MOBSTERS_PLAYER_ID_HEADER = "player-id";
	public static final String MOBSTERS_PLAYER_TYPE_HEADER = "player-type";
	public static final String MOBSTERS_REQUEST_TYPE_INDEX_HEADER = "request-type-index";
	public static final String MOBSTERS_CONTENT_LENGTH_HEADER = "content-length";
	public static final String MOBSTERS_TAG_HEADER = "sequence-tag";
	public static final String MOBSTERS_REPLY_TO_HEADER = "reply-to";

	// public static final String MOBSTERS_REQUEST_TYPE_INDEX = "X-Mobsters-Request-Type";
	
	// Not currently in use--see BinaryProtobufConverter's root comment about Destination paths for more.
    public static final String MOBSTERS_MESSAGE_CLASS_HEADER_NAME = "X-Message-Class";

	private final StompHeaderAccessor headerAccessor;
	
	private MobstersHeaderAccessor(StompHeaderAccessor headerAccessor) {
		this.headerAccessor = headerAccessor;
	}

	/**
	 * Create {@link MobstersHeaderAccessor} for accessing and modifying 
	 * headers of an existing {@link Message}.
	 */
	public static MobstersHeaderAccessor wrap(Message<?> message)
	{
		return
			new MobstersHeaderAccessor(
				StompHeaderAccessor.wrap(message));
	}
	
	/**
	 * Construct a {@link MobstersHeaderAccessor} for accessing and modifying 
	 * headers through an existing {@link StompHeadersAccessor}.
	 */
	public static MobstersHeaderAccessor wrap(StompHeaderAccessor headerAccessor) {
		return new MobstersHeaderAccessor(headerAccessor);
	}
	
	public Map<String, List<String>> toNativeHeaderMap() {

		final Map<String, List<String>> result = 
			headerAccessor.toNativeHeaderMap();

		final Principal user = headerAccessor.getUser();
		if (user != null) {
			result.put(
				MOBSTERS_PLAYER_ID_HEADER, 
				Collections.singletonList(
					user.getName()));
		}
		
		// This is probably unneeded since we use the same header name value, but its still good form
		// since the purpose of this method is to extract any headers from deeper protocol layer that
		// have an equivalent at this layer and that may have been modified through the accessor 
		// method in a subclass without this layer's consumer protocol having been informed of the change.
		final Integer contentLength = headerAccessor.getContentLength();
		if(contentLength != null) {
			result.put(
				MOBSTERS_CONTENT_LENGTH_HEADER, 
				Collections.singletonList(
					contentLength.toString()));
		}
		
//		result.put(MOBSTERS_PLAYER_ID_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_PLAYER_ID_HEADER)));
//		result.put(MOBSTERS_PLAYER_TYPE_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_PLAYER_TYPE_HEADER)));
//		result.put(MOBSTERS_REQUEST_TYPE_INDEX_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_REQUEST_TYPE_INDEX_HEADER)));
//		result.put(MOBSTERS_CONTENT_LENGTH_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_CONTENT_LENGTH_HEADER)));
//		result.put(MOBSTERS_TAG_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_TAG_HEADER)));

		return result;
	}

	// TODO: Delegate from this class rather than exposing the internal detail...  Only reason that is
	//       a necessity is the setUser() method that can accept a non-Mobsters Principal, but its enough.
	public StompHeaderAccessor accessStompHeaders() {
		return this.headerAccessor;
	}
	
	// TODO: Use the Protobuf message type enum as the return type instead
	public EventProtocolRequest getRequestType()
	{
		final String requestType = 
			headerAccessor.getFirstNativeHeader(MOBSTERS_REQUEST_TYPE_INDEX_HEADER);
		return 
			StringUtils.hasText(requestType) 
				? EventProtocolRequest.valueOf(
					Integer.parseInt(requestType))
				: null;
	}

	// TODO: Use the Protobuf message type enum as the argument type instead
	public void setRequestType(EventProtocolRequest requestType)
	{
		headerAccessor.setNativeHeader(
			MOBSTERS_REQUEST_TYPE_INDEX_HEADER,
			Integer.toString(
				requestType.getNumber()));
	}
	
	public Integer getContentLength()
	{
		return headerAccessor.getContentLength();
	}

	public void setContentLength(int contentLength)
	{
		headerAccessor.setContentLength(contentLength);
	}
	
	public int getSequenceTag()
	{
		final String sequenceTag = 
			headerAccessor.getFirstNativeHeader(MOBSTERS_TAG_HEADER);
		return StringUtils.hasText(sequenceTag) ? Integer.parseInt(sequenceTag) : -1;
	}

	public void setSeqeunceTag(int sequenceTag)
	{
		headerAccessor.setNativeHeader(
			MOBSTERS_TAG_HEADER,
			Integer.toString(sequenceTag));
	}
	
	public String getReplyTo() {
		return this.headerAccessor.getFirstNativeHeader(MOBSTERS_REPLY_TO_HEADER);
	}
	
	public void setReplyTo( String replyTo )
	{
		headerAccessor.setNativeHeader(MOBSTERS_REPLY_TO_HEADER, replyTo);
	}
	
	public MobstersPlayerPrincipal getPlayer()
	{
		// TODO: Must check output validity as well since Spring allows mutable access to the
		//       headers hash directly without requiring a code path through semantics-aware 
		//       code.	
		Principal user = headerAccessor.getUser();
		Preconditions.checkState(
			user instanceof MobstersPlayerPrincipal, 
			"User has been set to with a Principal type other than MobstersPlayerPrincipal");

		return (MobstersPlayerPrincipal) user;
	}
	
	public void setPlayer(final String userUuid, final UserIdentityType uuidType)
	{
		// TODO: Syntax validation.  However, do not attempt to verify login status here.
		this.headerAccessor.setUser(
			new MobstersPlayerPrincipal(userUuid, uuidType)
		);
		headerAccessor.setNativeHeader(
			MOBSTERS_PLAYER_ID_HEADER, userUuid);
		headerAccessor.setNativeHeader(
			MOBSTERS_PLAYER_TYPE_HEADER, uuidType.toString());
	}
	
	public void setPlayer(final MobstersPlayerPrincipal player)
	{
		// TODO: Syntax validation.  However, do not attempt to verify login status here.
		headerAccessor.setUser(player);
		headerAccessor.setNativeHeader(
			MOBSTERS_PLAYER_TYPE_HEADER, 
			player.getIdType()
			.toString()
		);
		headerAccessor.setNativeHeader(
			MOBSTERS_PLAYER_ID_HEADER, 
			player.getName()
		);
	}

//	public void applyReplyToHeader()
//	{
//		String replyTo = getReplyTo();
//		headerAccessor.setDestination(replyTo);
//		setReplyTo(null);
//	}
}
