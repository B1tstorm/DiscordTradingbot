package de.fh_kiel.discordtradingbot.Analysis;

public class TransactionHistoryItemBuilder {

    private Integer transactionId;
    private Integer value;
    private String sellerId;
    private String traderId;
    private char[] product;

    protected TransactionHistoryItemBuilder setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    protected TransactionHistoryItemBuilder setValue(Integer value) {
        this.value   = value;
        return this;
    }

    protected TransactionHistoryItemBuilder setSellerId(String sellerId) {
        this.sellerId = sellerId;
        return this;
    }

    protected TransactionHistoryItemBuilder setTraderId(String traderId) {
        this.traderId = traderId;
        return this;
    }

    protected TransactionHistoryItemBuilder setProduct(char[] product) {
        this.product = product;
        return this;
    }

    protected TransactionHistoryItem build() {
        return new TransactionHistoryItem(transactionId, value, sellerId, traderId, product);
    }
}
