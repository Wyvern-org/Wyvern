package com.owen2k6.chat.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventSystem
{
    private final Map<String, List<EventListener>> eventListeners;

    public EventSystem()
    {
        eventListeners = new HashMap<>();
    }

    public void addEventListener(String eventType, EventListener listener)
    {
        if (!eventListeners.containsKey(eventType))
            eventListeners.put(eventType, new ArrayList<>());
        eventListeners.get(eventType).add(listener);
    }

    public void removeEventListener(String eventType, EventListener listener)
    {
        if (eventListeners.containsKey(eventType))
            eventListeners.get(eventType).remove(listener);
    }

    public void fireEvent(String eventType, Object eventData)
    {
        if (eventListeners.containsKey(eventType))
            for (EventListener listener : eventListeners.get(eventType))
                listener.onEvent(eventType, eventData);
    }
}
