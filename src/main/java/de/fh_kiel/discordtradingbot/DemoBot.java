package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;

public class DemoBot {
    public static void main(String[] args) {
        ChannelInteracter interacter = new ChannelInteracter(args[0]);
        Inventory.getInstance().setWallet(2000);
        SegTransactionManager segTransactionManager = new SegTransactionManager();
        interacter.segTransactionManager = segTransactionManager;
        segTransactionManager.channelInteracter = interacter;

        //meine id Kira 522858078782095364
        interacter.listenToChannel();

        //TODO: Nachricht senden, wenn eingeloggt.
        // Wo kann ich jetzt mit dem Bot interagieren?
    }
}