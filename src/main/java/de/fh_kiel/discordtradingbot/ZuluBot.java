package de.fh_kiel.discordtradingbot;

import de.fh_kiel.discordtradingbot.Analysis.Evaluator;
import de.fh_kiel.discordtradingbot.Analysis.TransactionHistory;
import de.fh_kiel.discordtradingbot.Analysis.Visualizer;
import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.Transactions.BuyTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SegTransactionManager;
import de.fh_kiel.discordtradingbot.Transactions.SellTransactionManager;
import discord4j.core.object.entity.channel.MessageChannel;

public class ZuluBot implements EventListener {
    private ChannelInteracter channelInteracter;
    private BuyTransactionManager buyTransactionManager;
    private SegTransactionManager segTransactionManager;
    private SellTransactionManager sellTransactionManager;
    private TransactionHistory transactionHistory;
    private Visualizer visualizer;
    private Inventory inventory;

    public ZuluBot() {
    }

    public void launch() {
        this.channelInteracter = new ChannelInteracter(Config.getToken(), this);
        this.transactionHistory = TransactionHistory.getInstance();
        this.buyTransactionManager = new BuyTransactionManager(this);
        this.sellTransactionManager = new SellTransactionManager(this);
        this.segTransactionManager = new SegTransactionManager(this);
        this.visualizer = new Visualizer (this);
        this.inventory = new Inventory(this);

        channelInteracter.subscribe(TransactionHistory.getInstance());
        channelInteracter.subscribe(buyTransactionManager);
        channelInteracter.subscribe(sellTransactionManager);
        channelInteracter.subscribe(segTransactionManager);
        channelInteracter.subscribe(visualizer);
        channelInteracter.subscribe(inventory);
        // Der ZuluBot subscribed sich selbst für die Help funktion
        channelInteracter.subscribe(this);

        Evaluator evaluator = Evaluator.getInstance();
        transactionHistory.registerSubscriber(evaluator);
        evaluator.registerSubscriber(inventory);

        //TODO DELETE
        inventory.setWallet(2000);
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

    public Visualizer getVisualizer() {
        return visualizer;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void update(EventItem eventItem) {
        if(eventItem.getEventType() != EventType.HELP) return;
        provideHelp(eventItem.getChannel());
    }

    /**
     * wenn jemand !ZULU help schreibt bekommt er die folgende Ausgabe
     * @param channel
     */
    private void provideHelp(MessageChannel channel) {
        StringBuilder sb = new StringBuilder()
                .append("Moin, ich bin Zulu \n")
                .append("Interagiere mit mir durch folgende Befehle \n\n")
                .append("Kaufe verfügbare Worte \n")
                .append("Pattern: !ZULU wtb <String> [Preis]\n")
                .append("Beispiel 1: !ZULU wtb HALLO 50\n")
                .append("Im Falle eines Gegenangebotes antworten mir mit \n")
                .append("!ZULU counterOffer <ID> <String> [Preis] \n")
                .append("Bestätige ein Gegenangebot \n")
                .append("Pattern: !ZULU confirm <ID> \n")
                .append("Beispiel 3: !ZULU confirm 01 \n")
                .append("Lehne ein Gegenangebot ab\n")
                .append("Pattern: !ZULU deny <ID> \n")
                .append("Beispiel 4: !ZULU deny 01 \n")

                .append("um eine Statistik zu bekommen schreibe\n!ZULU visualize <char>\n")
                .append("um unseren Wallet zu sehen:\n!ZULU wallet\n")
                .append("um unseren Inventory zu sehen:\n!ZULU inventory\n")
                ;
        channelInteracter.writeThisMessage(sb.toString(), channel);
    }
}