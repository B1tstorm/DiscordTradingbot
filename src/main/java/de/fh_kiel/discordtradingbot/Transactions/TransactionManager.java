package de.fh_kiel.discordtradingbot.Transactions;


import de.fh_kiel.discordtradingbot.Holdings.Cart;
import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionManager implements EventListener {

    private static final HashMap<String, Transaction> transactions = new HashMap<>();

    public static HashMap<String, Transaction> getTransactions() {
        return transactions;
    }

    private Boolean checkInventory(String product) {
        ArrayList<Letter> letters = Inventory.getInstance().getLetters();
        HashMap<Character,Integer> hashmap = fillHashmap(new HashMap<>());
        char[] buchstaben = product.toCharArray();

        //todo: vereinfachung testen
        for (Character buchstabe: buchstaben) {
                hashmap.put(buchstabe, hashmap.get(buchstabe)+1);
        }
        for (Letter letter : letters) {
            if (!(letter.getAmount()>= hashmap.get(letter.getLetter())));
            return false;
        }
        return true;
    }

    private Boolean checkWallet(Integer price) {
       return price <= Inventory.getInstance().getWallet();
    }

    public Transaction startTransaction(String eventType) {
        // TODO Test?
        //in auction/bid Fall
        return new Transaction(eventType);

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

    public void executeTransaction(String eventId, Integer price, String product) {
        //TODO updateWallet und Letter Methoden sollen positiv und negativ sein!!
        //Inventory.updateWallet;(price)
        //Inventory.updateLetterAmount(product)
        TransactionManager.transactions.get(eventId).setStatus("executed");
    }

    public void dismissTransaction(String eventId) {
        TransactionManager.transactions.remove(eventId).setStatus("dismissed");
    }

    //TODO -implement update()
    @Override
    public void update(EventItem eventItem) {
        //*Methode soll  Infos vom ChannelInteractor bekommen (observer) wie
        //? tradingPartner //price //transactionKind //eventId //Product und winnerID
        //*Je nach transactionKind wied eine Methode aufgerufen

        //kind kann folgends sein ?: auction start... auction versteigerung..... auction Ende/gewonnen...... seg will kaufen

        String eventType = eventItem.evenType;
        Integer price = eventItem.price;
        String eventId = eventItem.eventId;
        String product = eventItem.product;
        String winnerId = eventType.winnerId;

        //! +++++++++++++++++++++++++++++++FALL auction +++++++++++++++++++++++++++++++
        //!falls winnerId eienen Wert hat. und wir der Winner wind sollen wir Überweien
        //!Falls winner einen Wert hat und wir nicht gewonnen haben soll die transaction geschlossen werden

         if (null !=winnerId){
            if (winnerId.equals("ZuluId")){
            executeTransaction(eventId,price,product);
            }else{
                  dismissTransaction(eventId);
             }
            return;
        }


        //prüfe ob eventId bekannt ist ggf. bidde mit
        if (eventType.equals("auction") && isProduktWorth(price, product)) {
            if (TransactionManager.getTransactions().get(eventId) != null) {
                TransactionManager.getTransactions().get(eventId).bid(eventId, price);
                //wenn evenID neu ist erstelle eine neue transaction
            } else {
                Transaction auctionTransaction = startTransaction(eventType);
                TransactionManager.transactions.put(eventId, auctionTransaction);
                auctionTransaction.bid(eventId, price);
            }
        }// ! +++++++++++++++++++++++++++++++ FAll "jemand will Kaufen" +++++++++++++++++++++++++++++++
        else if (eventType.equals("SegWillKaufen")){
            if (!isProduktWorth(price,product) && checkInventory(product)){
                //? wie läuft die communikaton mit Kunde? ist es 3 way hand shake? soll man hier execute machen
                //! Antworte SEG positiv // todo Channelinteractor einschalten
                TransactionManager.transactions.put(eventId,new Transaction(eventType));
                executeTransaction(eventId,price,product);
            }else{
                //! lehen Angebot ab todo Channelinteractor einschalten
                //! mach einen gegen angebot todo Channelinteractor einschalten
            }
        }





    }
    private HashMap<Character,Integer> fillHashmap (HashMap<Character,Integer> hashMap){
        for (int ascii = 65; ascii < 91; ascii++) {
            hashMap.put((char)ascii,0);
        }
        return hashMap;
    }
}