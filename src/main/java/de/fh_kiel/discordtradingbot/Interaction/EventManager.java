package de.fh_kiel.discordtradingbot.Interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    Map<String, List<EventListener>> listeners = new HashMap<>();

    public EventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(EventListener listener) {
        /*List<EventListener> users = listeners.get(eventType);
        users.add(listener);*/
        listeners.put()
    }

    public void unsubscribe(EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(EventItem eventItem) {
        /* Da es kein eventType mehr gibt, braucht man auch nicht mehr prüfen für wen das ist
        List<EventListener> users = listeners.get(eventType);

        for (EventListener listener : users) {
            listener.update(eventItem);
        }*/
        listeners.get();
    }
}
