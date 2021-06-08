package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory implements EventListener, LetterPublisher {

    // Thread Safety with "double-checked-locking"
    private final List<TransactionHistoryItem> transactionHistory = new ArrayList<>();

    // volatile guarantees that field is not stored in cache, safety for multithreading
    private volatile static TransactionHistory onlyInstance;

    private TransactionHistory() {
    }

    // double-checked-locking: Synchronized only for initialisation; better performance with statement synchronization
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

    @Override
    public void update(EventItem eventItem) {
        assert eventItem != null;
        if(eventItem.getEventType() == EventType.AUCTION_CLOSE
            || eventItem.getEventType() == EventType.AUCTION_WON) {
            this.transactionHistory.add(createTransactionHistoryItem(eventItem));
            this.notifySubscribers(extractLetterObject(eventItem));
        }
    }

    private Letter extractLetterObject(EventItem item) {
        return new Letter(item.getProduct()[0], 1, item.getValue());
    }
    // Test
    // Does not work as intended since it requires a value per letter, String only has combined val.
    private List<Letter> extractLetterObjectList(EventItem item) {
        List <Letter> letterObjectList = new ArrayList<>();
        for(char chars : item.getProduct()){
            letterObjectList.add(new Letter(chars, 1, item.getValue()));
        }
        return letterObjectList;
    }

    // Builder Pattern
    private TransactionHistoryItem createTransactionHistoryItem(EventItem eventItem) {
        return new TransactionHistoryItemBuilder().setTransactionId(Integer.parseInt(eventItem.getAuctionId()))
                .setValue(eventItem.getValue())
                .setSellerId(eventItem.getSellerID())
                .setTraderId(eventItem.getTraderID())
                .setProduct(eventItem.getProduct())
                .build();
    }


    public void registerSubscriber(LetterListener s) {
        this.subscribers.add(s);
    }

    @Override
    public void removeSubscriber(LetterListener s) {
        if (this.subscribers.contains(s)) {
            this.subscribers.remove(s);
        } else {
            System.out.println("The Subscriber has not subscribed and cannot be removed.");
        }
    }

    @Override
    public void notifySubscribers(Letter l) {
        for (LetterListener s : subscribers) {
            s.update(l, EventType.HISTORY);
        }
    }
}
