package de.fh_kiel.discordtradingbot.Interaction;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;

public class ChannelInteracter {
    public EventManager events;
    // TODO: Auskommentiert bis EventItem existiert
    // private EventItem eventItem = new EventItem();
    GatewayDiscordClient client;
    private String channelCommand;

    public ChannelInteracter(String token) {
        //Bei Discord über Token authentifizieren
        client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();

        //Erfolgreiche Authentifikation nach Stdout loggen
        assert client != null;
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                });
    }

    /**
     * @param message
     */
    public void writeMessage(String message) {

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
        client.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();

            if ("!seg auction start 1 Q 0".equals(message.getContent()) && message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
                createEventItem(message);
                final MessageChannel channel = message.getChannel().block();
                channel.createMessage("Pong!").block();
//                channel.createMessage(message.getContent()).block();
                // Setzt die Anzeige auf Auction oder Trading
                setPresence();
            }
        });

        //Verbindung schließen
        client.onDisconnect().block();
        //TODO: Auskommentiert bis eventItem existiert
//        events.notify(eventItem);
    }

    private EventItem createEventItem(Message message) {
        // Pseudo EventItem
        HashMap<String, String> pseudoEventItem = new HashMap<>();
        String[] messageShards = message.getContent().split(" ");
        EventItem eventItem;
        Integer logNr = 0;

        switch (messageShards[2]) {
            case "start":
                eventItem = new EventItem(logNr + 1, message.getUserData().id(), null, messageShards[3], EventType.AUCTION_START, messageShards[4], messageShards[5]);
                break;
            case "bid":
                eventItem = new EventItem(logNr + 1, message.getUserData().id(), messageShards[4], messageShards[3], EventType.AUCTION_BID, null, messageShards[5]);
                break;
        }

        return eventItem;
    }

    public void setPresence() {
        //? Status anpassen ob Bot gerade in "auction" oder "trade"
        client.updatePresence(Presence.online(Activity.watching("Auction"))).block();
//        client.updatePresence(Presence.online(Activity.playing("Trading"))).block();
    }

}
