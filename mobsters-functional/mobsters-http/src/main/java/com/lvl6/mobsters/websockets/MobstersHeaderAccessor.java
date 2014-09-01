package com.lvl6.mobsters.websockets;
  
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;

public class MobstersHeaderAccessor extends SimpMessageHeaderAccessor
{
	public static final String MOBSTERS_USER_UUID_HEADER = "user-uuid";
	public static final String MOBSTERS_REQUEST_TYPE_INDEX_HEADER = "request-type";
	public static final String MOBSTERS_CONTENT_LENGTH_HEADER = "content-length";
	public static final String MOBSTERS_TAG_HEADER = "sequence-tag";

	/**
	 * Construct a {@link MobstersHeaderAccessor} for accessing and modifying 
	 * headers of an existing {@link Message}.
	 */
	private MobstersHeaderAccessor(Message<?> message)
	{
		super(message);
	}

	/**
	 * Create {@link MobstersHeaderAccessor} for accessing and modifying 
	 * headers of an existing {@link Message}.
	 */
	public static MobstersHeaderAccessor wrap(Message<?> message)
	{
		return new MobstersHeaderAccessor(message);
	}
	
	@Override
	public Map<String, List<String>> toNativeHeaderMap() {

		Map<String, List<String>> result = super.toNativeHeaderMap();

		Principal user = super.getUser();
		if (user != null) {
			result.put(USER_HEADER, Arrays.asList(user.getName()));
		}
		result.put(MOBSTERS_USER_UUID_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_USER_UUID_HEADER)));
		result.put(MOBSTERS_REQUEST_TYPE_INDEX_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_REQUEST_TYPE_INDEX_HEADER)));
		result.put(MOBSTERS_CONTENT_LENGTH_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_CONTENT_LENGTH_HEADER)));
		result.put(MOBSTERS_TAG_HEADER, Arrays.asList((String) this.getHeader(MOBSTERS_TAG_HEADER)));

		return result;
	}


    /*
	@Override
	public void setDestination(String destination)
	{
		super.setDestination(destination);
		setNativeHeader(MOBSTERS_DESTINATION_HEADER, destination);
	}
	*/

	public Integer getContentLength()
	{
		String contentLength = (String) getHeader(MOBSTERS_CONTENT_LENGTH_HEADER);
		return StringUtils.hasText(contentLength) ? Integer.valueOf(contentLength) : Integer.valueOf(-1);
	}

	public void setContentLength(int contentLength)
	{
		setHeader(MOBSTERS_CONTENT_LENGTH_HEADER, String.valueOf(contentLength));
		setNativeHeader(MOBSTERS_CONTENT_LENGTH_HEADER, String.valueOf(contentLength));
	}
	
	public Integer getSequenceTag()
	{
		String sequenceTag = (String) getHeader(MOBSTERS_TAG_HEADER);
		return StringUtils.hasText(sequenceTag) ? Integer.valueOf(sequenceTag) : Integer.valueOf(-1);
	}

	public void setSeqeunceTag(int sequenceTag)
	{
		setHeader(MOBSTERS_TAG_HEADER, String.valueOf(sequenceTag));
		setNativeHeader(MOBSTERS_TAG_HEADER, String.valueOf(sequenceTag));
	}
	
	public String getUserUuid()
	{
		// TODO: Must check output validity as well since Spring allows mutable access to the
		//       headers hash directly without requring a code path through semantics-aware code.
		String userUuid = (String) getHeader(MOBSTERS_USER_UUID_HEADER);
		return StringUtils.hasText(userUuid) ? userUuid : "null";
	}
	
	public void setUserUuid(final String userUuid)
	{
		// TODO: Syntax validation.  However, do not attempt to verify login status here.
		setHeader(MOBSTERS_USER_UUID_HEADER, userUuid);
		setNativeHeader(MOBSTERS_USER_UUID_HEADER, userUuid);
	}
}
