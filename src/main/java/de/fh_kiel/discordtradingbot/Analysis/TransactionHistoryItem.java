package de.fh_kiel.discordtradingbot.Analysis;

/**
 * Transaction History to persistently save recent Transactions
 */
public class TransactionHistoryItem {
    private final Integer transactionId;
    private final Integer value;
    private final String sellerId;
    private final String traderId;
    private final char[] product;

    protected TransactionHistoryItem(Integer transactionId, Integer value, String sellerId, String traderId, char[] product) {
        this.transactionId = transactionId;
        this.value = value;
        this.sellerId = sellerId;
        this.traderId = traderId;
        this.product = product;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public Integer getValue() {
        return value;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getTraderId() {
        return traderId;
    }

    public char[] getProduct() {
        return product;
    }
}
