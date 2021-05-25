package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

public class SegTransactionManager extends TransactionManagerSeineMutter implements EventListener {
    //! we buy

    @Override
    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {
        super.executeTransaction(eventType,eventId,(price * (-1)),product);
    }

    @Override
    public void update( EventItem eventItem) {
        EventType eventType = eventItem.getEventType();
        Integer price = Integer.parseInt(eventItem.getValue());
        char[] product = eventItem.getProduct();
        String traderId = eventItem.getTraderID();
        String eventId;
        if (eventItem.getAuctionId() != null) {
            eventId = eventItem.getAuctionId();
        } else eventId = eventItem.getLogNr().toString();


        switch (eventType) {
            //wenn Transaction neu ist, erstellen, zum Array hinzufügen und beim BID einsteigen
            case AUCTION_START:
                if (isProductWorth(price, product) && isPriceAffordable(price)) {
                    Transaction t = new Transaction(eventType);
                    TransactionManager.transactions.put(eventId, t);
                    t.bid(eventId, price);
                }
                break;
            //TODO
            //!trader ID muss geprüft werden. wir dürfen uns selbst nicht versteigern
            case AUCTION_BID:
                if (isProductWorth(price, product) && isPriceAffordable(price) && traderId.equals("845410146913747034")) {
                    //TransactionManager.getTransactions().get(eventId).bid(eventId, price);
                    bid(eventItem);
                }
                break;
            case AUCTION_WON:
                if (traderId.equals("845410146913747034")) {
                    //* "price*(-1)" macht die transaktion negativ (wie bezahlen)
                    executeTransaction(eventType, eventId, price * (-1), product);
                } else {
                    dismissTransaction(eventId);
                }
                break;
            case AUCTION_CLOSE:
                break;
        }
    }

    protected void bid(EventItem eventItem){
    //* SEG auction bid 12 11 -> - - auctionState auctionId value
        EventItem newItem = new EventItem(eventItem.getLogNr(),eventItem.getSellerID(),
                "845410146913747034",eventItem.getAuctionId(),eventItem.getEventType(),
                eventItem.getProduct(),eventItem.getValue()+1);
        //channelInteracter.writeMessage(newItem);
    }
}
