package de.fh_kiel.discordtradingbot.Transactions;


import de.fh_kiel.discordtradingbot.Holdings.Cart;
import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionManager implements EventListener {

    private static final HashMap<String, Transaction> transactions = new HashMap<>();

    public static HashMap<String, Transaction> getTransactions() {
        return transactions;
    }

    public Boolean checkInventory(String product) {
        ArrayList<Letter> letters = Inventory.getInstance().getLetters();
        HashMap<Character,Integer> hashmap = fillHashmap(new HashMap<>());
        char[] buchstaben = product.toCharArray();

        for (Character buchstabe: buchstaben) {
                hashmap.put(buchstabe, hashmap.get(buchstabe)+1);
        }
        for (Letter letter : letters) {
            if (!(letter.getAmount()>= hashmap.get(letter.getLetter()))){
                return false;
            }
        }
        return true;
    }

    public Boolean isPriceAffordable(Integer price) {
       return price <= Inventory.getInstance().getWallet();
    }

    public Transaction startTransaction(EventType eventType) {
        return new Transaction(eventType);
    }

    //prüft ob produkt einen bestimmten Wert wert ist
    public Boolean isProduktWorth(Integer price, String product) {
        //ToDo Method is to be tested
        int tempTotalValue = 0;
        char[] char_arr = product.toCharArray();
        ArrayList<Letter> letterArray = Inventory.getInstance().getLetters();
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

    //todo unbedingt test schreiben
    public void executeTransaction(EventType eventType,String eventId, Integer price, String product)   {
        //TODO updateWallet und Letter Methoden sollen positiv und negativ sein!!
        Inventory.getInstance().updateWallet(price);
        //TODO methodenAufruf
        //Inventory.updateLetterAmount(product)
        TransactionManager.transactions.remove(eventId);
    }

    public void dismissTransaction(String eventId) {
        TransactionManager.transactions.remove(eventId);
    }

    //TODO -implement update()
    @Override
    public void update(EventItem eventItem) {
        // extract importen attributes form the EventItem
        EventType eventType = eventItem.getEventType();
        Integer price = eventItem.getValue();
        String eventId = Integer.toString(eventItem.getAuctionId());
        //! else nimm logID TODO
        String product = eventItem.getProduct();
        String traderId = Integer.toString(eventItem.getTraderID());

        //* new refactored code
        switch (eventType){
            //wenn Transaction neu ist, erstellen, zum Array hinzufügen und beim BID einsteigen
            case AUCTION_START:
                if (isProduktWorth(price, product) && isPriceAffordable(price)){
                    Transaction t = new Transaction(eventType);
                    TransactionManager.transactions.put(eventId, t);
                    t.bid(eventId, price);
                }break;
                //TODO
                //!trader ID muss geprüft werden. wir dürfen uns selbst nicht versteigern
            case AUCTION_BID:
                if (isProduktWorth(price, product) && isPriceAffordable(price) && traderId != "unsereID"){
                        TransactionManager.getTransactions().get(eventId).bid(eventId, price);
                }break;
            case AUCTION_WON:
                if (traderId == "unsereId"){
                    //* "price*(-1)" macht die transaktion negativ (wie bezahlen)
                    executeTransaction(eventType,eventId,price*(-1),product);
                }else {
                    dismissTransaction(eventId);
                }break;
            case AUCTION_CLOSE:
                break;
            case BUY:
                //? wie läuft die communikaton mit Kunde? ist es 3 way hand shake? soll man hier execute machen
                //! bitte das ! in der Mthode behandeln
                if (checkInventory(product) && !isProduktWorth(price,product)) {
                    //! Antworte SEG positiv // todo Channelinteractor einschalten
                    TransactionManager.transactions.put(eventId,new Transaction(eventType));
                    executeTransaction(eventType,eventId,price,product);
                }
                //! lehen Angebot ab todo Channelinteractor einschalten
                //! mach einen gegen angebot todo Channelinteractor einschalten
                break;
            case SELL:

                break;



        }


    }
    //interne Funktion
    private HashMap<Character,Integer> fillHashmap (HashMap<Character,Integer> hashMap){
        for (int ascii = 65; ascii < 91; ascii++) {
            hashMap.put((char)ascii,0);
        }
        return hashMap;
    }
}