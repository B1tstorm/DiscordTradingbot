package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Analysis.Evaluator;
import de.fh_kiel.discordtradingbot.Analysis.TransactionHistory;
import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Transactions.BuyTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SellTransactionManager;

public class ZuluBot {
 //   private final ChannelInteracter channelInteracter;

    public ZuluBot() {
       // this.channelInteracter = new ChannelInteracter(Config.getToken());
    }







    public void launch() {
        ChannelInteracter channelInteracter = new ChannelInteracter(Config.getToken());

        TransactionHistory transactionHistory = TransactionHistory.getInstance();
        BuyTransactionManager buyTransactionManager = new BuyTransactionManager(channelInteracter);
        SellTransactionManager sellTransactionManager = new SellTransactionManager(channelInteracter);
        SegTransactionManager segTransactionManager = new SegTransactionManager(channelInteracter);

        channelInteracter.subscribe(transactionHistory);
        channelInteracter.subscribe(buyTransactionManager);
        channelInteracter.subscribe(sellTransactionManager);
        channelInteracter.subscribe(segTransactionManager);

        Evaluator evaluator = Evaluator.getInstance();
        transactionHistory.registerSubscriber(evaluator);

        //todo löschen (test bedingt)
        Inventory.getInstance().setWallet(2000);
        channelInteracter.listenToChannel();
    }
}