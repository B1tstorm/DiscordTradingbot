package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;

public class SegTransactionManager extends TransactionManagerSeineMutter implements EventListener {


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
                if (isProduktWorth(price, product, eventType) && isPriceAffordable(price)) {
                    Transaction t = new Transaction(eventType);
                    TransactionManager.transactions.put(eventId, t);
                    t.bid(eventId, price);
                }
                break;
            //TODO
            //!trader ID muss geprüft werden. wir dürfen uns selbst nicht versteigern
            case AUCTION_BID:
                if (isProduktWorth(price, product, eventType) && isPriceAffordable(price) && traderId != "unsereID") {
                    TransactionManager.getTransactions().get(eventId).bid(eventId, price);
                }
                break;
            case AUCTION_WON:
                if (traderId == "unsereId") {
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


}
