package de.fh_kiel.discordtradingbot.Interaction;

import de.fh_kiel.discordtradingbot.ZuluBot;
import de.fh_kiel.discordtradingbot.Transactions.BuyTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SellTransactionManager;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import java.util.Arrays;


import static java.lang.String.valueOf;



public class ChannelInteracter implements EventPublisher {
    private ZuluBot zuluBot;
    //public EventManager events;

    GatewayDiscordClient client;
    static Integer logNr = 0;

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
                .filter(message -> message.getMessage().getAuthor().map(user -> !user.isBot()).orElse(true)) //TODO auf false setzen damit er auf bots reagiert
//                .filter(message -> message.getMessage().getUserData().id().equals("501500923495841792"))
                .subscribe(event -> {

                    final Message message = event.getMessage();
                    final MessageChannel channel = message.getChannel().block();

                    // Bei Auktionen filtern dass nur Messages vom SEG Bot gelesen werden
                    if (getPrefix(message).equals("!SEG") /*&& message.getUserData().id().equals("501500923495841792")*/) { //* Hier muss später die ID des SEG Bot stehen!
                        // Für den außergewöhnlichen Fall das der SEG Bot zu wenig Argumente in den Chat schreibt
                        try { //? Evtl. unnötig da der Bot niemals zu wenig Argumente liefert. Nur so stürzt das Programm nicht ab
                            // Setzt die Anzeige auf Auction oder Trading
                            setPresence(EventType.AUCTION_START);
                            EventItem eventItem = createEventItem(message);
                            //! Nur zum testen, später löschen
                            notifySubscriber(eventItem);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.err.println("Der Channel command enthält zu wenige Zeichen um ein EventItem zu generieren. Fehler: " + e);
                            assert channel != null;
                            channel.createMessage("Keine gültige Transaktion!").block();
                        }
                    } else if (message.getContent().contains("wtb")) {
                        EventItem eventItem = createBuyEventItem(message);
                        assert eventItem != null;
                        notifySubscriber(eventItem);
                    } else if (message.getContent().contains("wts")) {
                        EventItem eventItem = createSellEventItem(message);
                        assert eventItem != null;
                        notifySubscriber(eventItem);
                    } else if (message.getContent().contains("accept")) {
                        EventItem eventItem = createAcceptEventItem(message);
                        assert eventItem != null;
                        notifySubscriber(eventItem);
                    }

                    // In unserem Channel auf Präfix !ZULU reagieren
                    if (getPrefix(message).equals("!ZULU") && message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
                        // TODO: implementieren dass andere auf uns reagieren können
                        // TODO: von Eventtype.SELL geändert
                        setPresence(EventType.SELL_OFFER);
                    }
                });

        //Verbindung schließen
        client.onDisconnect().block();
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

        return new EventItem(logNr + 1,
                message.getUserData().id(),
                traderID,
                messageShards[3], eventType,
                products,
                Integer.parseInt(messageShards[5]),
                message.getChannel().block());
    }

    private EventItem createBuyEventItem(Message message) {
        String[] messageShards = message.getContent().split(" ");
        char[] products = null;
        String traderID = null;
        //* message.getAuthor().get().getId().asString() das benutzen wir
        //*und dann halt das <@ davor und das > dahinter
        String sellerId = "<@" + message.getAuthor().get().getId().asString() + ">";
        switch (messageShards[1]) {
            case "wtb": {
                //* !trd wtb ID PRODUCT PRICE
                //    0   1  2     3       4
                EventType eventType = EventType.BUY_OFFER;
                String auctionId = messageShards[2];
                products = messageShards[3].toCharArray();
                Integer price = Integer.parseInt(messageShards[4]);
                return new EventItem(logNr + 1, sellerId, null,
                        auctionId, eventType, products, price, message.getChannel().block());
            }

            case "confirm": {
                //* !trd confirm <@USER> ID   wtb PRODUCT PRICE
                //    0      1      2     3     4    5     6
                EventType eventType = EventType.BUY_CONFIRM;
                traderID = messageShards[2];
                String auctionId = messageShards[3];
                products = messageShards[5].toCharArray();
                Integer price = Integer.parseInt(messageShards[6]);
                return new EventItem(logNr + 1, sellerId, traderID, auctionId,
                        eventType, products, price, message.getChannel().block());
            }
        }
        return null;
    }

    private EventItem createSellEventItem(Message message) {

        String[] messageShards = message.getContent().split(" ");
        char[] products;
        String traderID;
        String auctionId = messageShards[2];
        String sellerId = "<@" + message.getAuthor().get().getId().asString() + ">";
        switch (messageShards[1]) {
            case "wts": {
                //* !trd wts ID PRODUCT PRICE
                //   0    1   2    3      4
                EventType eventType = EventType.SELL_OFFER;
                products = messageShards[3].toCharArray();
                Integer price = Integer.parseInt(messageShards[4]);
                return new EventItem(logNr + 1, sellerId, null,
                        auctionId, eventType, products, price, message.getChannel().block());
            }

            case "confirm": {
                //* !trd confirm <@USER> Gesuch-ID wts LETTER/STRING PRICE
                //     0    1        2       3      4        5          6
                EventType eventType = EventType.SELL_CONFIRM;
                traderID = messageShards[2];
                auctionId = messageShards[3];
                products = messageShards[5].toCharArray();
                Integer price = Integer.parseInt(messageShards[6]);
                return new EventItem(logNr + 1, sellerId, traderID, auctionId,
                        eventType, products, price, message.getChannel().block());
            }

        }
        return null;
    }

    private EventItem createAcceptEventItem(Message message) {
        //* jeamand hat unser Angebot angenommen
        //* !trd accept <@USER> Gesuch-ID
      //      0    1       2     3     4

        String[] messageShards = message.getContent().split(" ");
        if (isItMe(messageShards[2])) {
            EventType eventType = EventType.ACCEPT;
            String traderID = "<@" + message.getAuthor().get().getId().asString() + ">";
            String auctionId = messageShards[3];



            return new EventItem(logNr + 1, getZuluId(), traderID,
                    auctionId,eventType, null, null, message.getChannel().block());
        }return null;
    }


    private void setPresence(EventType eventType) {
        //? Status anpassen ob Bot gerade in "auction" oder "trade"
        if (eventType.equals(EventType.AUCTION_START)) {
            client.updatePresence(Presence.online(Activity.watching("Auction"))).block();
        } else {
            client.updatePresence(Presence.online(Activity.playing("Trading"))).block();
        }
    }

    /**
     * @param eventItem Die Methode antwortet in dem Channel, aus dem die Anfrage kam
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




    public void writeBidMessage(EventItem eventItem) {
        final MessageChannel channel = eventItem.getChannel();
        if (eventItem.getValue() != null) {
            channel.createMessage("!SEG auction bid " + eventItem.getAuctionId() + " " + (eventItem.getValue() + 1)).block();
        } else {
            Integer price = zuluBot.getSegTransactionManager().getTransactions().get(eventItem.getAuctionId()).getPrice();
            channel.createMessage("!SEG auction bid " + eventItem.getAuctionId() + " " + (price + 1)).block();
        }
    }

    public void writeThisMessage(String s, MessageChannel channel) {
        channel.createMessage(s).block();
    }

    public void writeAcceptMessage(EventItem eventItem) {
        final MessageChannel channel = eventItem.getChannel();
        //! Antworte mit dem pattern:
        //! !step accept @USER ID
        channel.createMessage("!trd accept " + eventItem.getSellerID() + " " + eventItem.getAuctionId()).block();
    }

    public void writeSellConfirmMessage(EventItem eventItem) {
        //* !step confirm ID @USER sell LETTER PRICE

        final MessageChannel channel = eventItem.getChannel();
        String id = eventItem.getAuctionId();
        String traderId = eventItem.getTraderID();
        char[] product = zuluBot.getBuyTransactionManager().getTransactions().get(id).getProduct();
        Integer price = zuluBot.getBuyTransactionManager().getTransactions().get(id).getPrice();
        channel.createMessage("!trd confirm " + traderId + " " + id + " wts " + valueOf(product) + " " + price).block();
        //*                    !trd confirm     <@USER>      Gesuch-ID  wts    LETTER/STRING    PRICE
    }

    public void writeBuyConfirmMessage(EventItem eventItem) {
        //* !trd confirm <@USER> Gesuch-ID wtb PRODUCT PRICE

        final MessageChannel channel = eventItem.getChannel();
        String id = eventItem.getAuctionId();
        String traderId = eventItem.getTraderID();
        char[] product = zuluBot.getSellTransactionManager().getTransactions().get(id).getProduct();
        Integer price = zuluBot.getSellTransactionManager().getTransactions().get(id).getPrice();
        channel.createMessage("!trd confirm " + traderId + " " + id + " wtb " + valueOf(product) + " " + price).block();

    }

    private Boolean isItMe(String botId){
        String zuluId = "<@!845410146913747034>";
        return botId.equals(zuluId);
    }

  private String  getZuluId(){
        return "<@!845410146913747034>";
  }
}
