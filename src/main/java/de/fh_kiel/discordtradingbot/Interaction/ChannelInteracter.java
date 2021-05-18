package de.fh_kiel.discordtradingbot.Interaction;

public class ChannelInteracter {
    public EventManager events;
    private String channelCommand;

    public void operation() {
        // TODO - implement ChannelInteracter.operation
        throw new UnsupportedOperationException();
    }

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
    public void listenToChannel(String eventType, Integer price, Integer eventId, String product) {
        if (channelCommand.equals("!bid")) {
            events.notify(eventType, price, eventId, product);
        }
    }
}