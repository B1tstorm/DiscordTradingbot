package de.fh_kiel.discordtradingbot.Interaction;

public class EventItem {

    // Strings to process:
    // !SEG auction start 12 A 10 -> - - auctionState auctionId product value
    // !SEG auction bid 12 11 -> - - auctionState auctionId value
    // !SEG auction bid 12 901251 11 -> auctionState auctionId traderId value
    // !SEG auction won 12 901251 345 -> auctionState auctionId traderId value
    // !SEG auction closed 12 -> auctionState auctionId



    private int logNr;  // chronological item number
    private int sellerID; // oder sellerID?
    private int traderID; // String besser zu verarbeiten als int/Integer
    private int auctionId;
    private EventType eventType; // AUCTION_START, AUCTION_BID, AUCTION_WON, AUCTION_CLOSE, BUY, SELL

    private String product; // Oder char?
    private int value; // reicht es den aktuellen Preis zu speichern?

    // protected, weil nur im package vom ChannelInteractor instanziierbar.
    protected EventItem(int logNr,
                        int sellerID,
                        int traderID,
                        int auctionId,
                        EventType eventType,
                        String product,
                        int value) {
        this.logNr = logNr;
        this.sellerID = sellerID;
        this.traderID = traderID;
        this.auctionId = auctionId;
        this.eventType = eventType;
        this.product = product;
        this.value = value;

    }

    /*
     LogItem = {
        logNr = 1;
        from = !SEG
        AuctionState = START
        auctionId = 123145
        traderId = 123414 / Tom Calvin Haak?
        product = "A"
        currentPrice = 12
        isActive = true

     }
     * */

    public int getLogNr() {
        return logNr;
    }

    public int getSellerID() {
        return sellerID;
    }

    public int getTraderID() {
        return traderID;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getProduct() {
        return product;
    }

    public int getValue() {
        return value;
    }
}
