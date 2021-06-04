package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Analysis.Evaluator;
import de.fh_kiel.discordtradingbot.Analysis.TransactionHistory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Transactions.BuyTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SellTransactionManager;

public class ZuluBot {
    //private final ChannelInteracter channelInteracter;

    public ZuluBot() {
        //this.channelInteracter = new ChannelInteracter(Config.getToken());
    }







    public void launch() {
        ChannelInteracter channelInteracter = new ChannelInteracter(Config.getToken());
        channelInteracter.listenToChannel();

        TransactionHistory transactionHistory = TransactionHistory.getInstance();
        BuyTransactionManager buyTransactionManager = new BuyTransactionManager();
        SellTransactionManager sellTransactionManager = new SellTransactionManager();
        SegTransactionManager segTransactionManager = new SegTransactionManager();

        channelInteracter.subscribe(transactionHistory);
        channelInteracter.subscribe(buyTransactionManager);
        channelInteracter.subscribe(sellTransactionManager);
        channelInteracter.subscribe(segTransactionManager);

        Evaluator evaluator = Evaluator.getInstance();
        transactionHistory.registerSubscriber(evaluator);
    }
}