package de.fh_kiel.discordtradingbot.Interaction;

import java.util.ArrayList;

/**
 * Benachrichtigt alle subscriber mit einem eventItem
 */
public interface EventPublisher {
    ArrayList<EventListener> listeners = new ArrayList<>();
    
    default void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    default void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    default void notifySubscriber(EventItem eventItem) {
        for (EventListener subscriber : listeners) {
            subscriber.update(eventItem);
        }
    }
}