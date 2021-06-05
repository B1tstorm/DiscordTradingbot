package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Transactions.BuyTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SellTransactionManager;

public class DemoBot {
    public static void main(String[] args) {
        ChannelInteracter interacter = new ChannelInteracter(args[0]);
        Inventory.getInstance().setWallet(2000);
        SegTransactionManager segTransactionManager = new SegTransactionManager();
        BuyTransactionManager buyTransactionManager = new BuyTransactionManager();
        SellTransactionManager sellTransactionManager = new SellTransactionManager();

        interacter.segTransactionManager = segTransactionManager;
        interacter.sellTransactionManager = sellTransactionManager;
        interacter.buyTransactionManager = buyTransactionManager;
        segTransactionManager.channelInteracter = interacter;
        buyTransactionManager.channelInteracter = interacter;
        sellTransactionManager.channelInteracter = interacter;


        //meine id Kira 522858078782095364
        interacter.listenToChannel();
        //TODO: Nachricht senden, wenn eingeloggt.
        // Wo kann ich jetzt mit dem Bot interagieren?
    }
}