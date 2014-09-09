package com.lvl6.mobsters.websockets;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeType;

import com.google.common.base.Preconditions;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode;

public final class BinaryProtobufDecoder extends AbstractMessageConverter
{
    private static final String MOBSTERS_MESSAGE_CLASS_HEADER_NAME = "X-Message-Class";
    private static final String MOBSTERS_MESSAGE_INDEX_HEADER_NAME = "X-Message-Index";

	private final Class<Message> protoRootClass = Message.class;
	private final ClassToInstanceMap<Message> protoTypeMap =
		MutableClassToInstanceMap.<Message>create(
			new HashMap<Class<? extends Message>, Message>());

    public BinaryProtobufDecoder()
    {
    	super(
    		new MimeType("application", "mobsters-protobuf"));
    } 

	@Override
	protected boolean supports(Class<?> clazz)
	{
		Preconditions.checkNotNull(clazz);
		return clazz.isAssignableFrom(protoRootClass);
	}

	@Override
	public Message convertFromInternal(
		org.springframework.messaging.Message<?> message, Class<?> targetClass)
	{
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(
			message.getPayload());
		Preconditions.checkArgument(
			this.supports(targetClass));
		
		Message retVal = decodeFromClass(message, targetClass);
		if (retVal == null) {
			retVal = decodeFromHeader(message);
		}

		Lvl6MobstersConditions.lvl6Precondition(
			retVal != null,
			Lvl6MobstersStatusCode.FAIL_EMPTY_MSG,
			"Message either does not describe or does not include a valid protobuf object");
		
		return retVal;
	}

	@Override
	public org.springframework.messaging.Message<?> convertToInternal(
		Object payload, MessageHeaders header)
	{
		Preconditions.checkNotNull(payload);
		final Message payloadObject = (Message) payload;

		// TODO: Need to get from payloadObject to its response type enum value
		return
			MessageBuilder.withPayload(
				payloadObject.toByteArray()
			).copyHeaders(header)
			.setHeader(
				MOBSTERS_MESSAGE_INDEX_HEADER_NAME, -1
			).setHeader(
				MOBSTERS_MESSAGE_CLASS_HEADER_NAME,
				payloadObject.getClass()
					.getName()
			).build();
	}

	private Message decodeFromHeader(org.springframework.messaging.Message<?> message) {
		@SuppressWarnings("unchecked")
		Class<? extends Message> targetClass =
			message.getHeaders()
				.get(MOBSTERS_MESSAGE_CLASS_HEADER_NAME, Class.class);

		return decodeFromClass(message, targetClass);
	}

	private Message decodeFromClass(
		org.springframework.messaging.Message<?> message, Class<?> targetClass)
	{
		Message retVal = null;

		Message template = protoTypeMap.get(targetClass);
		if ((template == null)
			&& (targetClass != protoRootClass)
		) {
			try {
				template =
					(Message) targetClass
						.getMethod("getDefaultInstance")
						.invoke(null);
				protoTypeMap.put(
					template.getClass(), template);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				// TODO: Append message to debug log
				retVal = null;
			}

			if (template != null) {
				retVal = decodeWithTemplate(message, template);
			}
		}

		return retVal;
	}

	private Message decodeWithTemplate(
		org.springframework.messaging.Message<?> message, Message template)
	{
		Message retVal;

		try {
			retVal =
				template.newBuilderForType()
					.mergeFrom(
						(byte []) message.getPayload()
					).build();
		} catch (InvalidProtocolBufferException e) {
			// TODO: Append message to debug log
			retVal = null;
		}
		
		return retVal;
	}
}