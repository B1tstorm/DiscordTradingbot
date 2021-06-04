package de.fh_kiel.discordtradingbot.Interaction;

import java.util.ArrayList;

public interface EventPublisher {
    ArrayList<EventListener> listeners = new ArrayList<>();
    
    public default void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public default void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    public default void notifySubscriber(EventItem eventItem) {
        for (EventListener subscriber : listeners) {
            subscriber.update(eventItem);
        }
    }
}
