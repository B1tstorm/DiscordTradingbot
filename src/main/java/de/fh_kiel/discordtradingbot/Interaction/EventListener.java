package de.fh_kiel.discordtradingbot.Interaction;

public interface EventListener {
    void update(String eventType, Integer price, Integer eventId, String product);
}
