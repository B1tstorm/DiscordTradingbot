package de.fh_kiel.discordtradingbot.Interaction;

import discord4j.core.object.entity.channel.MessageChannel;

public class EventItem {
    private Integer logNr;  // chronological item number
    private String sellerID; // oder sellerID?
    private String traderID; // String besser zu verarbeiten als int/Integer
    private String auctionId; // Wird nur bei auktionen gebraucht, bei buy und sell wird die logNr verwendet
    private EventType eventType; // AUCTION, BUY, SELL
    private char[] product; // Oder char?
    private Integer value; // reicht es den aktuellen Preis zu speichern?
    private MessageChannel channel;

    // protected, weil nur im package vom ChannelInteractor instanziierbar.
    public EventItem(Integer logNr,
                        String sellerID,
                        String traderID,
                        String auctionId,
                        EventType eventType,
                        char[] product,
                        Integer value,
                     MessageChannel channel) {
        this.logNr = logNr;
        this.sellerID = sellerID;
        this.traderID = traderID;
        this.auctionId = auctionId;
        this.eventType = eventType;
        this.product = product;
        this.value = value;
        this.channel = channel;
    }

    public void setProduct(char[] product) {
        this.product = product;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    /** Defaultkonstr. da ich im Scope der createEventItem Methode sonst keine zwei Instanzen erstellen kann,
     so kann ich eine erstellen und je nach EventState entscheide ich was rein kommt */

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

    public char[] getProduct() {
        return product;
    }

    public Integer getValue() {
        return value;
    }

    public MessageChannel getChannel() {
        return channel;
    }
}
