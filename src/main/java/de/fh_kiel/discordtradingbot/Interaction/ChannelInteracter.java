package de.fh_kiel.discordtradingbot.Interaction;

import de.fh_kiel.discordtradingbot.Analysis.Visualizer;
import de.fh_kiel.discordtradingbot.ZuluBot;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.spec.MessageCreateSpec;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;


public class ChannelInteracter implements EventPublisher {
    private ZuluBot zuluBot;
    GatewayDiscordClient client; //not private?
    static Integer logNr = 0; //not private?

    public ChannelInteracter(String token, ZuluBot bot) {
        this.zuluBot = bot;
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
     * Die Methode antwortet in dem Channel, aus dem die Anfrage kam
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
                //! If() ist ein Test welcher schon im TransactionManager stattfindet.
                //! Das AUCTION_WON case wird nie ausgeführt da es beim TransactionManager
                //! endet und writeMessage() nicht aufruft
                if (eventItem.getTraderID().equals("845410146913747034")) {
                    channel.createMessage("Wir haben die auctionID " + eventItem.getAuctionId() + " gewonnen!").block();
                } else {
                    channel.createMessage("Wir haben die auctionID " + eventItem.getAuctionId() + " verloren!").block();
                }
                break;
            default:
                channel.createMessage("Default case").block();
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

    public void uploadFile(File file, MessageChannel channel) {
        try {
            InputStream stream = new FileInputStream(file);
            channel.createMessage(messageCreateSpec -> {
                messageCreateSpec.setContent("Visualization of letter value history:");
                messageCreateSpec.addFile("File.svg", stream);
            }).block();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void listenToChannel() {
        client.on(MessageCreateEvent.class)
                .filter(message -> message.getMessage().getAuthor().map(user -> !user.isBot()).orElse(false))
//                .filter(message -> message.getMessage().getUserData().id().equals("501500923495841792"))
                .subscribe(event -> {
            final Message message = event.getMessage();
            final MessageChannel channel = message.getChannel().block();

            // Bei Auktionen filtern dass nur Messages vom SEG Bot gelesen werden
            if (getPrefix(message).equalsIgnoreCase("!SEG") && message.getUserData().id().equals("501500923495841792")) { //* Hier muss später die ID des SEG Bot stehen!
                // Für den außergewöhnlichen Fall das der SEG Bot zu wenig Argumente in den Chat schreibt
                try { //? Evtl. unnötig da der Bot niemals zu wenig Argumente liefert. Nur so stürzt das Programm nicht ab
                    // Setzt die Anzeige auf Auction oder Trading
                    setPresence(EventType.AUCTION_START);
                    EventItem eventItem = createEventItem(message);
                    notifySubscriber(eventItem);
                    //! Nur zum testen, später löschen
//                    writeMessage(eventItem);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Der Channel command enthält zu wenige Zeichen um ein EventItem zu generieren. Fehler: " + e);
                    assert channel != null;
                    channel.createMessage("Keine gültige Transaktion!").block();
                }
            }

            // In unserem Channel auf Präfix !ZULU reagieren
            if (getPrefix(message).equalsIgnoreCase("!ZULU") && message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
                Visualizer.getInstance().notify(message);

                // TODO: implementieren dass andere auf uns reagieren können
                // TODO: von Eventtype.SELL geändert
                setPresence(EventType.SELL_OFFER);
            }
        });

        //Verbindung schließen
        client.onDisconnect().block();
    }

    private String getPrefix(Message message) {
        String[] messageShards = message.getContent().split(" ");
        return messageShards[0].toUpperCase();
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

        return new EventItem(logNr + 1,
                              message.getUserData().id(),
                              traderID,
                              messageShards[3], eventType,
                              products,
                              Integer.parseInt(messageShards[5]),
                              message.getChannel().block());
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
