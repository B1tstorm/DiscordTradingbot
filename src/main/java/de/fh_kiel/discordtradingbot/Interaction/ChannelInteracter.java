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
import java.util.Arrays;
import java.util.HashMap;

public class ChannelInteracter {
    public EventManager events;
    // TODO: Auskommentiert bis EventItem existiert
    // private EventItem eventItem = new EventItem();
    GatewayDiscordClient client;
    private String channelCommand;
    static Integer logNr = 0;

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
        client.on(MessageCreateEvent.class)
                .filter(message -> message.getMessage().getContent().startsWith("!seg".toUpperCase()))
                // TODO: Bei Auktionen filtern das nur Messages vom SEG Bot gelesen werden (nicht unsere und nicht die anderen, der SEG Antwortet sowieso mit den geboten etc.)
                .subscribe(event -> {
            final Message message = event.getMessage();

            if ("!SEG auction start 1 Q 0".equals(message.getContent()) && message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
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
        String[] messageShards = message.getContent().split(" ");
        char[] products = null;
        String traderID = null;
        EventType eventType = EventType.AUCTION_START;

        switch (messageShards[2]) {
            case "start":
                products = messageShards[4]
                        .replaceAll("\\s+", "") // Entfernt alle Leerzeichen
                        .toUpperCase() // Stellt alle Buchstaben auf Großbuchstaben
                        .toCharArray(); // Erstellt aus dem String einzelne Elemente "products"
                break;
            case "bid":
                traderID = messageShards[4];
                eventType = EventType.AUCTION_BID;
                break;
            case "won":
                traderID = messageShards[4];
                eventType = EventType.AUCTION_WON;
                break;
            default:
                break;
        }

        return new EventItem(logNr + 1, message.getUserData().id(), traderID, messageShards[3], eventType, products, messageShards[5]);
    }

    public void setPresence() {
        //? Status anpassen ob Bot gerade in "auction" oder "trade"
        client.updatePresence(Presence.online(Activity.watching("Auction"))).block();
//        client.updatePresence(Presence.online(Activity.playing("Trading"))).block();
    }

}
