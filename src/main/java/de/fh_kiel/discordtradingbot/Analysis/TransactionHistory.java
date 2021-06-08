package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory implements EventListener, Publisher {

    // Thread Safety with "double-checked-locking"
    private final List<TransactionHistoryItem> transactionHistory = new ArrayList<>();
    private final List<Subscriber> subscribers = new ArrayList<>();

    // volatile guarantees that field is not stored in cache, safety for multithreading
    private volatile static TransactionHistory onlyInstance;

    private TransactionHistory() {
    }
    /**
     * Singleton Implementation of Transaction History Class. Thread-Safety trough double-checked-locking.
     * Synchronized only for initialisation; better performance with statement synchronization
     * @return instance of TransactionHistory
     */
    public static TransactionHistory getInstance() {
        if (onlyInstance == null) {
            synchronized (TransactionHistory.class) {
                if (onlyInstance == null) {
                    onlyInstance = new TransactionHistory();
                }
            }
        }
        return onlyInstance;
    }

    /**
     * Method for creating new Transaction History Items for closed or won auctions
     * @param eventItem from ChannelInteractor Class
     */
    @Override
    public void update(EventItem eventItem) {
        assert eventItem != null;
        if(eventItem.getEventType() == EventType.AUCTION_CLOSE
            || eventItem.getEventType() == EventType.AUCTION_WON) {
            this.transactionHistory.add(createTransactionHistoryItem(eventItem));
            this.notifySubscribers(extractLetterObject(eventItem));
        }
    }

    /**
     * Utility Method for extracting the letter of a single transaction
     * @param item is an eventItem containing the letter as product
     * @return Letter Object
     */
    private Letter extractLetterObject(EventItem item) {
        return new Letter(item.getProduct()[0], 1, item.getValue());
    }

    /**
     * Creation of TransactionHistoryItem with builder pattern.
     * @param eventItem
     * @return
     */
    private TransactionHistoryItem createTransactionHistoryItem(EventItem eventItem) {
        return new TransactionHistoryItemBuilder().setTransactionId(Integer.parseInt(eventItem.getAuctionId()))
                .setValue(eventItem.getValue())
                .setSellerId(eventItem.getSellerID())
                .setTraderId(eventItem.getTraderID())
                .setProduct(eventItem.getProduct())
                .build();
    }

    /**
     * Observer Utility
     * @param s
     */
    public void registerSubscriber(Subscriber s) {
        this.subscribers.add(s);
    }

    @Override
    public void removeSubscriber(Subscriber s) {
        if (this.subscribers.contains(s)) {
            this.subscribers.remove(s);
        } else {
            System.out.println("The Subscriber has not subscribed and cannot be removed.");
        }
    }

    @Override
    public void notifySubscribers(Letter l) {
        for (Subscriber s : subscribers) {
            s.update(l);
        }
    }
}
