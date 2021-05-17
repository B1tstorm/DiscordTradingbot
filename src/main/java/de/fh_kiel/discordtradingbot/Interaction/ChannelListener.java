package de.fh_kiel.discordtradingbot.Interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelListener {
	public EventManager events;
	private String channelCommand;

	public ChannelListener() {
		this.events = new EventManager("kaufen", "verkaufen", "bieten");
	}

	// TODO Channel abhören
	public void listenToChannel(String eventType, Integer price, Integer eventId, String product) {
		if (channelCommand.equals("!bid")) {
			events.notify(eventType, price, eventId, product);
		}
	}
}
