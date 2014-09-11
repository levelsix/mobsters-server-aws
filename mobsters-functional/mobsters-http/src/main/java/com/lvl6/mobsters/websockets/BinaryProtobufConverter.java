package com.lvl6.mobsters.websockets;

import java.lang.reflect.InvocationTargetException;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeType;

import com.google.common.base.Preconditions;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersConditions;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode;

public final class BinaryProtobufConverter extends AbstractMessageConverter
{
    private static final String MOBSTERS_MESSAGE_CLASS_HEADER_NAME = "X-Message-Class";
//    private static final String MOBSTERS_MESSAGE_INDEX_HEADER_NAME = "X-Message-Index";
//
//    private final ClassToInstanceMap<Message> protoTypeMap =
//        MutableClassToInstanceMap.<Message>create(
//            new HashMap<Class<? extends Message>, Message>());

    public BinaryProtobufConverter()
    {
    	super(
    		new MimeType("application", "mobsters-protobuf"));
    } 

	@Override
	protected boolean supports(Class<?> clazz)
	{
		Preconditions.checkNotNull(clazz);
		return Message.class.isAssignableFrom(clazz);
	}

	@Override
	public Message convertFromInternal(
		org.springframework.messaging.Message<?> message, Class<?> targetClass)
	{
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(
			message.getPayload());
		Lvl6MobstersConditions.lvl6Precondition(
			message.getPayload() instanceof byte[],
			Lvl6MobstersStatusCode.FAIL_EMPTY_MSG,
			"Conversion to ProtoBuf Message type is only supported from byte arrays");
		
		Lvl6MobstersConditions.lvl6Precondition(
			this.supports(targetClass),
			Lvl6MobstersStatusCode.FAIL_EMPTY_MSG,
			"Controller does not expect a protobuf payload type");
		
		Message retVal = decodeFromClass(message, targetClass);
		if (retVal == null) {
			retVal = decodeFromHeader(message, targetClass);
		}

		Lvl6MobstersConditions.lvl6Precondition(
			retVal != null,
			Lvl6MobstersStatusCode.FAIL_EMPTY_MSG,
			"Message either does not describe or does not include a valid protobuf object");
		
		return retVal;
	}

	@Override
	public org.springframework.messaging.Message<byte[]> convertToInternal(
		Object payload, MessageHeaders header)
	{
		Preconditions.checkNotNull(payload);
		final Message payloadObject = (Message) payload;

		// TODO: Need to get from payloadObject to its response type enum value?
		return
			MessageBuilder.withPayload(
				payloadObject.toByteArray()
			).copyHeaders(header)
//			.setHeader(
//				MOBSTERS_MESSAGE_INDEX_HEADER_NAME, -1
//			).setHeader(
//				MOBSTERS_MESSAGE_CLASS_HEADER_NAME,
//				payloadObject.getClass()
//					.getName()
			.build();
	}

	private Message decodeFromHeader(
		org.springframework.messaging.Message<?> message,
		Class<?> targetClass) 
	{
		Message retVal;
		try {
			Class<?> statedClass = Class.forName(
				StompHeaderAccessor.wrap(message)
				.getFirstNativeHeader(MOBSTERS_MESSAGE_CLASS_HEADER_NAME)
			);
			Preconditions.checkArgument(
				targetClass.isAssignableFrom(statedClass),
				"Header-declared class (<%s>) is incompatible with controller method call signature (<%s>)",
				statedClass, targetClass);

			retVal = decodeFromClass(message, statedClass);
		} catch (ClassNotFoundException e) {
			// TODO: Append message to debug log
			retVal = null;
		}

		return retVal;
	}

	private Message decodeFromClass(
		org.springframework.messaging.Message<?> message, Class<?> targetClass)
	{
		Message retVal = null;

		// Message template = protoTypeMap.get(targetClass);
		Message template = null;
		if ((template == null)
			&& (targetClass != Message.class)
		) {
			try {
				template =
					(Message) targetClass
						.getMethod("getDefaultInstance")
						.invoke(null);
				// protoTypeMap.put(
				// 	template.getClass(), template);
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