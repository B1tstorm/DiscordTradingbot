package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory implements EventListener, Publisher{
    // Thread Safety with "double-checked-locking"
    private final List<TransactionHistoryItem> transactionHistory = new ArrayList<>();
    private final List<Subscriber> subscribers = new ArrayList<>();

    // volatile guarantees that field is not stored in cache, safety for multithreading
    private volatile static TransactionHistory singleton;

    private TransactionHistory() {
    }
    // double-checked-locking: Synchronized only for initialisation; better performance with statement synchronization
    public static TransactionHistory getInstance() {
        if (singleton == null) {
            synchronized (TransactionHistory.class) {
                if (singleton == null) {
                    singleton = new TransactionHistory();
                }
            }
        }
        return singleton;
    }

    @Override
    public void update(EventItem eventItem) {
        this.transactionHistory.add(createTransactionHistoryItem(eventItem));
    }

    private TransactionHistoryItem createTransactionHistoryItem(EventItem eventItem) {
        TransactionHistoryItem item = new TransactionHistoryItemBuilder().setTransactionId(Integer.parseInt(eventItem.getAuctionId()))
                .setValue(eventItem.getValue())
                .setSellerId(eventItem.getSellerID())
                .setTraderId(eventItem.getTraderID())
                .setProduct(eventItem.getProduct())
                .build();
        return item;
    }


    public void registerSubscriber(Subscriber s) {
        this.subscribers.add(s);
    }

    @Override
    public void removeSubscriber(Subscriber s) {
        if(this.subscribers.contains(s)) {
            this.subscribers.remove(s);
        } else {
            System.out.println("The Subscriber has not subscribed and cannot be removed.");
        }
    }

    @Override
    public void notifyObservers() {

    }
}
