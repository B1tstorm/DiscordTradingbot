package de.fh_kiel.discordtradingbot.Interaction;

public class EventItem {

    // Strings to process:
    // !SEG auction start 12 A 10 -> - - auctionState auctionId product value
    // !SEG auction bid 12 11 -> - - auctionState auctionId value
    // !SEG auction bid 12 901251 11 -> auctionState auctionId traderId value
    // !SEG auction won 12 901251 345 -> auctionState auctionId traderId value
    // !SEG auction closed 12 -> auctionState auctionId



    private Integer logNr;  // chronological item number
    private String sellerID; // oder sellerID?
    private String traderID; // String besser zu verarbeiten als int/Integer
    private String auctionId; // Wird nur bei auktionen gebraucht, bei buy und sell wird die logNr verwendet

    private EventType eventType; // AUCTION, BUY, SELL
    private EventState eventState; // START, BID, WON, CLOSE

    private char[] product; // Oder char?
    private Integer value; // reicht es den aktuellen Preis zu speichern?

    // protected, weil nur im package vom ChannelInteractor instanziierbar.
    protected EventItem(Integer logNr,
                        String sellerID,
                        String traderID,
                        String auctionId,
                        EventType eventType,
                        EventState eventState,
                        char[] product,
                        Integer value) {
        this.logNr = logNr;
        this.sellerID = sellerID;
        this.traderID = traderID;
        this.auctionId = auctionId;
        this.eventType = eventType;
        this.eventState = eventState;
        this.product = product;
        this.value = value;

    }

    /** Defaultkonstr. da ich im Scope der createEventItem Methode sonst keine zwei Instanzen erstellen kann,
     so kann ich eine erstellen und je nach EventState entscheide ich was rein kommt */
    public EventItem() {
    }
    /*
     LogItem = {
        logNr = 1;
        sellerID = !SEG
        eventType = AUCTION
        auctionId = 123145
        traderId = 123414 / Tom Calvin Haak?
        product = "A"
        currentPrice = 12
        eventState = BID
     }
     * */

    public Integer getLogNr() {
        return logNr;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getTraderID() {
        return traderID;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventState getEventState() {
        return eventState;
    }

    public char[] getProduct() {
        return product;
    }

    public Integer getValue() {
        return value;
    }
}
