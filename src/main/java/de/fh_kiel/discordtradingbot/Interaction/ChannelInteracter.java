package de.fh_kiel.discordtradingbot.Interaction;

public class ChannelInteracter implements EventListener {
	private String tag;
	private String channelCommand;

	public void operation() {
		// TODO - implement ChannelInteracter.operation
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param message
	 */
	public Boolean writeMessage(String message) {
		// TODO - implement ChannelInteracter.writeMessage
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param emoji
	 * @return Boolean
	 */
	public Boolean reactEmoji(String emoji) {
		// TODO - implement ChannelInteracter.reactEmoji
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(String eventType, Integer price, Integer eventId, String product) {
		System.out.println("EventType: " + eventType + " Preis: " + price);
	}
}