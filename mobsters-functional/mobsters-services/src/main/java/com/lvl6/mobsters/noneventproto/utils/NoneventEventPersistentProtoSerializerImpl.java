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
	private static final Logger LOG =
		LoggerFactory.getLogger(NoneventEventPersistentProtoSerializerImpl.class);

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
		} catch (Throwable e) {
			LOG.error(
				String.format(
					"Could not retrieve DayOfWeek enum value by name. dayOfWeek=%s; event=%s",
					dayOfWeekStr, event),
				e);
		}

		pepb.setStartHour(startHour);
		pepb.setEventDurationMinutes(eventDurationMinutes);
		pepb.setTaskId(taskId);
		pepb.setCooldownMinutes(cooldownMinutes);

		try {
			final EventType typ =
				EventType.valueOf(eventTypeStr);
			pepb.setType(typ);
		} catch (Throwable e) {
			LOG.error(
				String.format(
					"Could not retrieve EventType enum value by name.  eventType=%s; event=%s",
					eventTypeStr, event),
				e);
		}
		try {
			Element elem = Element.valueOf(monsterElem);
			pepb.setMonsterElement(elem);
		} catch (Throwable e) {
			LOG.error(
				String.format(
					"Could not retrieve Element enum value by name.  monsterElem=%s; event=%s",
					monsterElem, event),
				e);
		}

		return pepb.build();
	}
}
