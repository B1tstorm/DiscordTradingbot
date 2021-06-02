package de.fh_kiel.discordtradingbot.Interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    // TODO: Aus HashMap hasMap löschen und Konstruktor anpassen
    ArrayList<EventListener> subscribers = new ArrayList<>();
    Map<String, List<EventListener>> listeners = new HashMap<>();

    public EventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(EventListener listener) {
        subscribers.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        subscribers.remove(listener);
    }

    public void notify(EventItem eventItem) {
        if (eventItem != null) {
            for (EventListener subscriber : subscribers) {
                subscriber.update(eventItem);
            }
        }
    }
}
