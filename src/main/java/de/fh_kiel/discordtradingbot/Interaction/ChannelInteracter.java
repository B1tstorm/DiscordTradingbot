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
    GatewayDiscordClient client;
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
     * @param eventItem
     */
    public void writeMessage(EventItem eventItem) {
        final MessageChannel channel = eventItem.getChannel();
        switch (eventItem.getEventType()) {
            // TODO: Switch-case überarbeiten sobald der TransactionManager das eventItem geändert schickt
            case AUCTION_START:
                channel.createMessage("!SEG auction bid " + eventItem.getAuctionId() + " " + eventItem.getValue()).block();
                break;
            case AUCTION_BID:
                channel.createMessage("!SEG auction bid " + eventItem.getAuctionId() + " " + eventItem.getValue()).block();
                break;
            case AUCTION_WON:
                //! If() ist nur ein Test welcher schon im TransactionManager stattfindet.
                //! Das AUCTION_WON case wird nie ausgeführt da es beim Transactionmanager
                //! endet und writeMessage() nicht aufruft
                if (eventItem.getTraderID().equals("845410146913747034")) {
                    channel.createMessage("Wir haben die auctionID " + eventItem.getAuctionId() + " gewonnen!").block();
                } else {
                    channel.createMessage("Wir haben die auctionID " + eventItem.getAuctionId() + " verloren!").block();
                }
                break;
            default:
                channel.createMessage("Defaultcase").block();
        }
    }

    /**
     * @param emoji
     * @return Boolean
     */
    private Boolean reactEmoji(String emoji) {
        // TODO - implement ChannelInteracter.reactEmoji
        throw new UnsupportedOperationException();
    }

    public void listenToChannel() {
        client.on(MessageCreateEvent.class)
                .filter(message -> message.getMessage().getAuthor().map(user -> !user.isBot()).orElse(false))
//                .filter(message -> message.getMessage().getUserData().id().equals("501500923495841792"))
                .subscribe(event -> {
            final Message message = event.getMessage();
            final MessageChannel channel = message.getChannel().block();

            // Bei Auktionen filtern dass nur Messages vom SEG Bot gelesen werden
            if (getPrefix(message).equals("!SEG") && message.getUserData().id().equals("501500923495841792")) { //* Hier muss später die ID des SEG Bot stehen!
                // Für den außergewöhnlichen Fall das der SEG Bot zu wenig Argumente in den Chat schreibt
                try { //? Evtl. unnötig da der Bot niemals zu wenig Argumente liefert. Nur so stürzt das Programm nicht ab
                    // Setzt die Anzeige auf Auction oder Trading
                    setPresence(EventType.AUCTION_START);
                    EventItem eventItem = createEventItem(message);
                    //!events.notify(eventItem);
                    writeMessage(eventItem); //! Nur zum testen, später löschen
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Der Channelcommand enthält zu wenige Zeichen um ein EventItem zu generieren. Fehler: " + e);
                    channel.createMessage("Keine gültige Transaktion!").block();
                }
            }

            // In unserem Channel auf Präfix !ZULU reagieren
            if (getPrefix(message).equals("!ZULU") && message.getAuthor().map(user -> !user.isBot()).orElse(false)) {

            }
        });

        //Verbindung schließen
        client.onDisconnect().block();
        //TODO: Auskommentiert bis eventItem existiert
//        events.notify(eventItem);
    }

    private String getPrefix(Message message) {
        String[] content = message.getContent().split(" ");
        return content[0].toUpperCase();
    }

    private EventItem createEventItem(Message message) {
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

        return new EventItem(logNr + 1, message.getUserData().id(), traderID, messageShards[3], eventType, products, messageShards[5], message.getChannel().block());
    }

    private void setPresence(EventType eventType) {
        //? Status anpassen ob Bot gerade in "auction" oder "trade"
        if (eventType.equals(EventType.AUCTION_START)) {
            client.updatePresence(Presence.online(Activity.watching("Auction"))).block();
        } else {
            client.updatePresence(Presence.online(Activity.playing("Trading"))).block();
        }
    }

}
