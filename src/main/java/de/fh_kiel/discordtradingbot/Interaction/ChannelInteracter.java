package de.fh_kiel.discordtradingbot.Interaction;

import de.fh_kiel.discordtradingbot.Config;
import de.fh_kiel.discordtradingbot.Transactions.Transaction;
import de.fh_kiel.discordtradingbot.ZuluBot;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static java.lang.String.valueOf;

import java.util.*;


public class ChannelInteracter implements EventPublisher {
    private final ZuluBot zuluBot;
    private final String myId;
    //public EventManager events;
    private final String myRawId;
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
        myId = "<@!" + client.getSelfId().asString() + ">";
        myRawId = client.getSelfId().asString();
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                });
    }

    public void listenToChannel() {
        client.on(MessageCreateEvent.class)
                .subscribe(event -> {
                    final Message message = event.getMessage();
                    final MessageChannel channel = message.getChannel().block();
                    // Der Bot soll nicht auf eigene Commands reagieren
                    if (message.getAuthor().get().getId().equals(myRawId)) return;

                    EventItem eventItem = null;
                    // Bei Auktionen filtern dass nur Messages vom SEG Bot gelesen werden
                    if (getPrefix(message).equalsIgnoreCase("!SEG")) {
                        // Für den außergewöhnlichen Fall das der SEG Bot zu wenig Argumente in den Chat schreibt
                        setPresence(EventType.AUCTION_START);
                        eventItem = createEventItem(message);
                    }

                    // In unserem Channel auf Präfix !ZULU reagieren
                    else if (getPrefix(message).equalsIgnoreCase("!ZULU")
                            && message.getAuthor().map(user -> !user.isBot()).orElse(false)) {
                        setPresence(EventType.SELL_OFFER);
                        if (message.getContent().contains("help")) {
                            eventItem = createHelpEventItem(message);
                        } else if (message.getContent().contains("wtb") || message.getContent().contains("confirm")
                                || message.getContent().contains("deny")) {
                            eventItem = createZuluEventItem(message);
                        } else {
                            eventItem = createInfoEventItem(message);
                        }
                    } else if (getPrefix(message).equals("!TRD")) {
                        if (message.getContent().contains("wtb")) {
                            eventItem = createBuyEventItem(message);
                        } else if (message.getContent().contains("wts")) {
                            eventItem = createSellEventItem(message);
                        } else if (message.getContent().contains("accept")) {
                            eventItem = createAcceptEventItem(message);
                        }
                    }
                    if (eventItem != null) notifySubscriber(eventItem);
                });

        //Verbindung schließen
        client.onDisconnect().block();
    }

    /**
     * minnt ein message an und gibt den ersten Index davon großgeschrieben
     */
    private String getPrefix(Message message) {
        String[] messageShards = message.getContent().split(" ");
        return messageShards[0].toUpperCase();
    }

    /**
     * fall !SEG:
     * ein Message wird in ein EvenItem gewandelt
     */
    private EventItem createEventItem(Message message) {
        try { //test
            String[] messageShards = message.getContent().split(" ");
            char[] products = null;
            Integer price = null;
            String traderID = null;
            EventType eventType = EventType.AUCTION_START;

            //Ja, das ist ghetto.
            if (Config.SEGID == null && messageShards.length == 3) {
                zuluBot.getInventory().setWallet(Integer.parseInt(messageShards[2]));
                this.writeThisMessage("Wallet set to " + zuluBot.getInventory().getWallet() + "\nReady to trade!", message.getChannel().block());
                Config.SEGID = message.getUserData().id();
                return null;
            }

            //das auch.
            if (!message.getUserData().id().equals(Config.SEGID)) return null;


            switch (messageShards[2]) {
                case "start":
                    products = messageShards[4]
                            .replaceAll("\\s+", "") // Entfernt alle Leerzeichen
                            .toUpperCase() // Stellt alle Buchstaben auf Großbuchstaben
                            .toCharArray(); // Erstellt aus dem String einzelne Elemente "products"
                    price = Integer.parseInt(messageShards[5]);
                    break;
                case "bid":
                    if (messageShards.length == 5) return null;
                    traderID = messageShards[4];
                    eventType = EventType.AUCTION_BID;
                    break;
                case "won":
                    traderID = messageShards[4];
                    eventType = EventType.AUCTION_WON;
                    products = zuluBot.getSegTransactionManager().getTransactions().get(messageShards[3]).getProduct();
                    price = Integer.parseInt(messageShards[5]);
                    break;
            }
            return new EventItem(++logNr,
                    message.getUserData().id(),
                    traderID,
                    messageShards[3], eventType,
                    products,
                    price,
                    message.getChannel().block());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Eingabe dieser länge ist nur zulässig zum Registrieren" + e);
        }
        return null;
    }
    /**
     * fall !ZULU traiding:
     * ein Message wird in ein EvenItem gewandelt
     */
    private EventItem createZuluEventItem(Message message) {
        try {
            String[] messageShards = message.getContent().split(" ");
            char[] product = null;
            EventType eventType;
            Integer price = null;
            String id = null;

            switch (messageShards[1]) {
                case "wtb":
                    //*        !ZULU wtb HALLO 50
                    eventType = EventType.ZULU_BUY;
                    product = messageShards[2].toUpperCase().toCharArray();
                    price = Integer.parseInt(messageShards[3]);
                    break;
                case "confirm":
                    //*        !ZULU confirm <ID>
                    eventType = EventType.ZULU_CONFIRM;
                    Transaction transaction = zuluBot.getBuyTransactionManager().getTransactions().get(messageShards[2]);
                    if (transaction == null) return null;
                    product = transaction.getProduct();
                    price = transaction.getPrice();
                    id = messageShards[2];
                    break;
                case "deny":
                    Transaction transaction2 = zuluBot.getBuyTransactionManager().getTransactions().get(messageShards[2]);
                    if (transaction2 == null) return null;
                    //*        !ZULU deny <ID>
                    eventType = EventType.ZULU_DENY;
                    id = messageShards[2];
                    break;
                default:
                    return null;
            }
            return new EventItem(++logNr, message.getUserData().id(), null, id, eventType, product, price, message.getChannel().block());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("ungültige eingabe");
        }
        return null;
    }
    /**
     * fall !trd wtb oder confirm
     * ein Message wird in ein EvenItem gewandelt
     */
    private EventItem createBuyEventItem(Message message) {
        try {
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
                    return new EventItem(++logNr, sellerId, null,
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
                    return new EventItem(++logNr, sellerId, traderID, auctionId,
                            eventType, products, price, message.getChannel().block());
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("ungültige eingabe");
        }
        return null;
    }
    /**
     * fall !trd wts  oder confirm
     * ein Message wird in ein EvenItem gewandelt
     */
    private EventItem createSellEventItem(Message message) {
        try {
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
                    return new EventItem(++logNr, sellerId, null,
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
                    return new EventItem(++logNr, sellerId, traderID, auctionId,
                            eventType, products, price, message.getChannel().block());
                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("ungültige eingabe");
        }
        return null;
    }
    /**
     * fall !trd accept:
     * ein Message wird in ein EvenItem gewandelt
     */
    private EventItem createAcceptEventItem(Message message) {
        //* jeamand hat unser Angebot angenommen
        //* !trd accept <@USER> Gesuch-ID
      //      0    1       2     3     4

        String[] messageShards = message.getContent().split(" ");
        try {
            if (isItMe(messageShards[2])) {
                EventType eventType = EventType.ACCEPT;
                String traderID = "<@" + message.getAuthor().get().getId().asString() + ">";
                String auctionId = messageShards[3];

                return new EventItem(++logNr, myId, traderID,
                        auctionId, eventType, null, null, message.getChannel().block());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("ungültige eingabe");
        }
        return null;
    }

    /**
     * fall !ZULU visualize/wallet/inventory
     * ein Message wird in ein EvenItem gewandelt
     * @param message
     * @return
     */
    private EventItem createInfoEventItem(Message message) {
        //* metriken, analysen
        //* !zulu [visualize/wallet/inventory]  [letter]
        //    0                 1                  2
        List<String> messageShards = Arrays.asList(message.getContent().split(" "));

        if (messageShards.size() == 1) {
            this.writeThisMessage("!SEG register", message.getChannel().block());
            return null;
        }

        EventItem item = new EventItem(
                null, myId, null, null, null , null, null, message.getChannel().block());

        if (messageShards.get(1).equalsIgnoreCase("visualize")) {
            item.setEventType(EventType.VISUALIZE);
        } else if (messageShards.get(1).equalsIgnoreCase("wallet")) {
            item.setEventType(EventType.WALLET);
        } else if (messageShards.get(1).equalsIgnoreCase("inventory")) {
            item.setEventType(EventType.INVENTORY);
        }

        if (messageShards.size() >= 3) {
            item.setProduct(messageShards.get(2).toUpperCase(Locale.ROOT).toCharArray());
        }
        return item;
    }

    /**
     * fall !ZULU help
     * ein Message wird in ein EvenItem gewandelt
     * @param message
     * @return
     */
    private EventItem createHelpEventItem(Message message) {
        return new EventItem(null, null, null, null, EventType.HELP, null, null, message.getChannel().block());
    }

    //Outbound Communication

    /**
     * ändert den bot status
     * @param eventType
     */
    private void setPresence(EventType eventType) {
        //? Status anpassen ob Bot gerade in "auction" oder "trade"
        if (eventType.equals(EventType.AUCTION_START)) {
            client.updatePresence(Presence.online(Activity.watching("Auction"))).block();
        } else {
            client.updatePresence(Presence.online(Activity.playing("Trading"))).block();
        }
    }



    /**
     * schickt einen File in den channel als .svg Datei.
     * Die Datei beinhaltet eine statistik zu jedem Buchstaben
     * @param file
     * @param channel
     */
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

    /**
     * bidd um eine Auktion im SEG channel
     * @param eventItem
     */
    public void writeBidMessage(EventItem eventItem) {
        final MessageChannel channel = eventItem.getChannel();
        if (eventItem.getValue() != null) {
            channel.createMessage("!SEG auction bid " + eventItem.getAuctionId() + " " + (eventItem.getValue() + 1)).block();
        } else {
            Integer price = zuluBot.getSegTransactionManager().getTransactions().get(eventItem.getAuctionId()).getPrice();
            channel.createMessage("!SEG auction bid " + eventItem.getAuctionId() + " " + (price + 1)).block();
        }
    }

    /**
     * scheibt eine bestimmte Nachricht die als String übergenen wird
     * @param s
     * @param channel
     */
    public void writeThisMessage(String s, MessageChannel channel) {
        channel.createMessage(s).block();
    }

    /**
     * scheibt //! !step accept @USER ID
     * @param eventItem
     */
    public void writeAcceptMessage(EventItem eventItem) {
        final MessageChannel channel = eventItem.getChannel();
        channel.createMessage("!trd accept " + eventItem.getSellerID() + " " + eventItem.getAuctionId()).block();
    }

    /**
     *    schreibt in den Channel:     //* !step confirm ID @USER sell LETTER PRICE
     * @param eventItem
     */
    public void writeSellConfirmMessage(EventItem eventItem) {
        final MessageChannel channel = eventItem.getChannel();
        String id = eventItem.getAuctionId();
        String traderId = eventItem.getTraderID();
        char[] product = zuluBot.getBuyTransactionManager().getTransactions().get(id).getProduct();
        Integer price = zuluBot.getBuyTransactionManager().getTransactions().get(id).getPrice();
        channel.createMessage("!trd confirm " + traderId + " " + id + " wts " + valueOf(product) + " " + price).block();
    }

    /**
     *   schreibt in den Channel      //* !trd confirm <@USER> Gesuch-ID wtb PRODUCT PRICE
     * @param eventItem
     */
    public void writeBuyConfirmMessage(EventItem eventItem) {
        final MessageChannel channel = eventItem.getChannel();
        String id = eventItem.getAuctionId();
        String traderId = eventItem.getTraderID();
        char[] product = zuluBot.getSellTransactionManager().getTransactions().get(id).getProduct();
        Integer price = zuluBot.getSellTransactionManager().getTransactions().get(id).getPrice();
        channel.createMessage("!trd confirm " + traderId + " " + id + " wtb " + valueOf(product) + " " + price).block();

    }

    /**
     * prüfe meine ID
     * @param botId
     * @return
     */
    private Boolean isItMe(String botId) {
        return botId.equals(myId);
    }

    /**
     * liefert die ID mit mention klammern    <@.......>
     * @return
     */
    public String getMyId() {
        return myId;
    }

    /**
     * liefert die ID als eine Zahlen ID
     * @return
     */
    public String getMyRawId() {
        return myRawId;
    }
}
