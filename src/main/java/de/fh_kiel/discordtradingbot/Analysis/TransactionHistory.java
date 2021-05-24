package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory implements EventListener {

    private final List<TransactionHistoryItem> transactionHistory = new ArrayList<>();

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
}
