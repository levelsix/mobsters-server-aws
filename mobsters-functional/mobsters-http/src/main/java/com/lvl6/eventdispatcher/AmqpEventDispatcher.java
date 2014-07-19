package com.lvl6.eventdispatcher;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.events.BroadcastResponseEvent;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.NormalResponseEvent;
import com.lvl6.mobsters.events.PreDatabaseResponseEvent;
import com.lvl6.mobsters.utils.NIOUtils;


@Component
public class AmqpEventDispatcher implements ClientEventDispatcher {

	
	@Value("{serverInstance.serverId()+'normalEvents'}")
	public String normalEvents;
	
	@Value("{serverInstance.serverId()+'preDatabaseEvents'}")
	public String preDatabaseEvents;
	
	@Value("{serverInstance.serverId()+'broadvastEvents'}")
	public String broadcastEvents;
	
	
	@Resource(name="serverMessagesTemplate")
	protected RabbitTemplate template;
	
	
	@Override
	public void dispatchEvents(EventsToDispatch events) {
		throw new RuntimeException("Use ClientEventDispatcherImpl");
	}

	@Override
	public void dispatchNormalEvents(List<NormalResponseEvent> events) {
		throw new RuntimeException("Use ClientEventDispatcherImpl");
	}

	@Override
	public void dispatchNormalEvent(NormalResponseEvent ev) {
		byte[] bytes = NIOUtils.getByteArray(ev);
		MessageProperties props = new MessageProperties();
		props.setUserId(ev.getPlayerId());
		Message msg = MessageBuilder.withBody(bytes).andProperties(props).build();
		template.send(normalEvents, msg);
	}

	@Override
	public void dispatchPreDatabaseEvents(List<PreDatabaseResponseEvent> events) {
		throw new RuntimeException("Use ClientEventDispatcherImpl");
	}

	@Override
	public void dispatchPreDatabaseEvent(PreDatabaseResponseEvent ev) {
		byte[] bytes = NIOUtils.getByteArray(ev);
		MessageProperties props = new MessageProperties();
		props.setUserId(ev.getUdid());
		Message msg = MessageBuilder.withBody(bytes).andProperties(props).build();
		template.send(preDatabaseEvents, msg);
	}

	@Override
	public void dispatchBroadcastEvents(List<BroadcastResponseEvent> events) {
		throw new RuntimeException("Use ClientEventDispatcherImpl");
	}

	@Override
	public void dispatchBroadcastEvent(BroadcastResponseEvent ev) {
		byte[] bytes = NIOUtils.getByteArray(ev);
		for(String userId : ev.getRecipients()) {
			MessageProperties props = new MessageProperties();
			props.setUserId(userId);
			Message msg = MessageBuilder.withClonedBody(bytes).andProperties(props).build();
			template.send(broadcastEvents, msg);
		}
	}

	public RabbitTemplate getTemplate() {
		return template;
	}

	public void setTemplate(RabbitTemplate template) {
		this.template = template;
	}

}
