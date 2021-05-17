package de.fh_kiel.discordtradingbot.Transactions;


import de.fh_kiel.discordtradingbot.Holdings.Cart;
import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionManager implements EventListener {
    //todo final ???
    private static final HashMap<Integer, Transaction> transactions = new HashMap<>();

    public static HashMap<Integer, Transaction> getTransactions() {
        return transactions;
    }

    private Cart checkInventory(String input) {
        // TODO - implement TransactionManager.checkInventory
        throw new UnsupportedOperationException();
    }

    public void startTransaction(Integer eventId, Integer price, String eventType, String product) {
        // TODO Test?
        //in auction/bid Fall
        if (eventType.equals("auction")) {
            if (isProduktWorth(price, product)) {
                Transaction auctionTransaction = new Transaction(eventType);
                TransactionManager.transactions.put(eventId, auctionTransaction);
                auctionTransaction.bid(eventId, price);
            }
        }
        // TODO - implement TransactionManager.startTransaction in sell Fall
    }

    //prüft ob produkt einen bestimmten Wert wert ist
    public Boolean isProduktWorth(Integer price, String product) {
        //ToDo Method is to be tested
        int tempTotalValue = 0;
        char[] char_arr = product.toCharArray();
        ArrayList<Letter> letterArray = Inventory.getLetters();
        //rechne gesamtWert vom product
        for (char c : char_arr) {
            //zugriff auf werte im Inventar Letter Array mit ascii index berechnung
            tempTotalValue += letterArray.get((int) c - 65).getValue();
        }

        // Der Fall wenn wir auf einen Buchstaben bieten können und der Buchstabe bei uns einen Wert von 0 hat, dann bieten wir nicht mit.
        if (tempTotalValue == 0) return false;
        // Der Fall wenn wir auf Buchstaben bieten und unser Value (tempTotalValue) größer ist als der Preis (price)
        return tempTotalValue >= price;
        // temp = 15 price = 10 true
        // TODO für später: falls tempTotalValue z.B. 5% mehr wäre als das Gebot, trotzdem verkaufen
    }

    public void executeTransaction(Integer eventId, Integer price, String product) {
        //TODO updateWallet und Letter Methoden sollen positiv und negativ sein!!
        //Inventory.updateWallet;(price)
        //Inventory.updateLetterAmount(product)
        TransactionManager.transactions.get(eventId).setStatus("executed");
    }

    public void dismissTransaction(Integer eventId) {
        TransactionManager.transactions.get(eventId).setStatus("dismissed");
    }

    //TODO -implement update()
    @Override
    public void update(String eventType, Integer price, Integer eventId, String product) {
        //Methode soll sich Infos vom ChannelInteractor holen (observer) wie
        // tradingPartner //price //transactionKind //eventId //Product
        //Je nach transactionKind wied eine Methode aufgerufen
        //kind kann folgends sein ?: auction start... auction versteigerung..... auction Ende/gewonnen...... seg will kaufen

        //prüfe ob eventId bekannt ist ggf. bidde mit
        if (TransactionManager.getTransactions().get(eventId) != null) {
            if (isProduktWorth(price, product)) {
                TransactionManager.getTransactions().get(eventId).bid(eventId, price);
            }
        }

        //if (transactionKind.equals("auction")){
        // isProduktWorth
        // wenn ja	startTransaction(eventId);
        //}


        //if (transactionKind.equals("auction/ende oder gewonnen")){
        //	transaction ebschließen und falls gewonnen bezahlen und amount erhöhnen
        //}

        //if (transactionKind.equals("seg wanna buy ")){
        //  check Inventory
        //	startTransaction(); ohne bidder
        //}


    }
}