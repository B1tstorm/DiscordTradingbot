package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Analysis.Evaluator;
import de.fh_kiel.discordtradingbot.Analysis.TransactionHistory;
import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Transactions.BuyTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SellTransactionManager;

public class ZuluBot {
    private ChannelInteracter channelInteracter;
    private BuyTransactionManager buyTransactionManager;
    private SegTransactionManager segTransactionManager;
    private SellTransactionManager sellTransactionManager;
    private TransactionHistory transactionHistory;

    public ZuluBot() {
    }

    public void launch() {
        this.channelInteracter = new ChannelInteracter(Config.getToken(), this);


        this.transactionHistory = TransactionHistory.getInstance();
        this.buyTransactionManager = new BuyTransactionManager(this);
        this.sellTransactionManager = new SellTransactionManager(this);
        this.segTransactionManager = new SegTransactionManager(this);

        channelInteracter.subscribe(TransactionHistory.getInstance());
        channelInteracter.subscribe(buyTransactionManager);
        channelInteracter.subscribe(sellTransactionManager);
        channelInteracter.subscribe(segTransactionManager);

        Evaluator evaluator = Evaluator.getInstance();
        transactionHistory.registerSubscriber(evaluator);

        //TODO DELETE
        Inventory.getInstance().setWallet(2000);
        channelInteracter.listenToChannel();
    }

    public ChannelInteracter getChannelInteracter() {
        return channelInteracter;
    }

    public BuyTransactionManager getBuyTransactionManager() {
        return buyTransactionManager;
    }

    public SegTransactionManager getSegTransactionManager() {
        return segTransactionManager;
    }

    public SellTransactionManager getSellTransactionManager() {
        return sellTransactionManager;
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}