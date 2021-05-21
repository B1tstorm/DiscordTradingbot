package de.fh_kiel.discordtradingbot.Interaction;

public class ChannelInteracter {
    public EventManager events;
    private String channelCommand;
    private EventItem eventItem = new EventItem();

    /**
     * @param message
     */
    public Boolean writeMessage(String message) {
        // TODO - implement ChannelInteracter.writeMessage
        throw new UnsupportedOperationException();
    }

    /**
     * @param emoji
     * @return Boolean
     */
    public Boolean reactEmoji(String emoji) {
        // TODO - implement ChannelInteracter.reactEmoji
        throw new UnsupportedOperationException();
    }

    // TODO Channel abhören
    public void listenToChannel() {
        events.notify(eventItem);
    }
}