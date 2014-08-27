package com.lvl6.mobsters.noneventproto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.info.EventPersistent;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.DayOfWeek;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Element;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.PersistentEventProto;
import com.lvl6.mobsters.noneventproto.NoneventTaskProto.PersistentEventProto.EventType;

@Component
public class NoneventEventPersistentProtoSerializerImpl implements NoneventEventPersistentProtoSerializer 
{

	private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());

	@Override
	public PersistentEventProto createPersistentEventProtoFromEvent(
		EventPersistent event)
	{
		PersistentEventProto.Builder pepb = PersistentEventProto.newBuilder();

		int eventId = event.getId();
		String dayOfWeekStr = event.getDayOfWeek();
		int startHour = event.getStartHour();
		int eventDurationMinutes = event.getEventDurationMinutes();
		int taskId = event.getTask().getId();
		int cooldownMinutes = event.getCooldownMinutes();
		String eventTypeStr = event.getEventType();
		String monsterElem = event.getMonsterElement();

		pepb.setEventId(eventId);
		try {
			DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr);
			pepb.setDayOfWeek(dayOfWeek);
		} catch (Exception e) {
			log.error("can't create enum type. dayOfWeek=" + dayOfWeekStr + ".\t event=" + event);
		}

		pepb.setStartHour(startHour);
		pepb.setEventDurationMinutes(eventDurationMinutes);
		pepb.setTaskId(taskId);
		pepb.setCooldownMinutes(cooldownMinutes);

		try {
			EventType typ = EventType.valueOf(eventTypeStr);
			pepb.setType(typ);
		} catch (Exception e) {
			log.error("can't create enum type. eventType=" + eventTypeStr + ".\t event=" + event);
		}
		try {
			Element elem = Element.valueOf(monsterElem);
			pepb.setMonsterElement(elem);
		} catch (Exception e) {
			log.error("can't create enum type. monster elem=" + monsterElem + 
				".\t event=" + event);
		}

		return pepb.build();
	}

}
