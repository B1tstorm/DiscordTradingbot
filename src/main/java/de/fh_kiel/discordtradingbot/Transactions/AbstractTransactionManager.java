package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class AbstractTransactionManager {
    protected static final HashMap<String, Transaction> transactions = new HashMap<>();
    public ChannelInteracter channelInteracter;

    public AbstractTransactionManager(ChannelInteracter channelInteracter) {
        this.channelInteracter = channelInteracter;
    }

    public static HashMap<String, Transaction> getTransactions() {
        return transactions;
    }

    public Boolean checkInventory(@NonNull char[] product) {
        ArrayList<Letter> letters = Inventory.getInstance().getLetters();
        HashMap<Character, Integer> hashmap = fillHashmap(new HashMap<>());

        for (Character buchstabe : product) {
            hashmap.put(buchstabe, hashmap.get(buchstabe) + 1);
        }
        for (Letter letter : letters) {
            if (!(letter.getAmount() >= hashmap.get(letter.getLetter()))) {
                return false;
            }
        }
        return true;
    }

    //interne Funktion, ergänzt ChenInventory
    private HashMap<Character, Integer> fillHashmap(HashMap<Character, Integer> hashMap) {
        for (int ascii = 65; ascii < 91; ascii++) {
            hashMap.put((char) ascii, 0);
        }
        return hashMap;
    }

    public Boolean isPriceAffordable(Integer price) {
        return price <= Inventory.getInstance().getWallet();
    }

    public Transaction startTransaction(EventType eventType) {
        return new Transaction(eventType);
    }

    public void dismissTransaction(String eventId) {
        AbstractTransactionManager.transactions.remove(eventId);
    }

    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {
        Inventory.getInstance().updateLetterAmount(eventType, product);
        transactions.remove(eventId);
        Inventory.getInstance().updateWallet(price);
        System.out.println("Tranaction wurde excuted");
    }

    public void executeTransaction(EventItem eventItem) {
        Transaction transaction = transactions.get(eventItem.getAuctionId());
        Inventory.getInstance().updateLetterAmount(eventItem.getEventType(), transaction.getProduct());
        Inventory.getInstance().updateWallet(transaction.getPrice());
        AbstractTransactionManager.transactions.remove(eventItem.getAuctionId());
    }

    public Boolean isProductWorth(Integer price, char[] product) {
        //ToDo Method is to be tested
        int totalLocalValue = 0;
        ArrayList<Letter> letterArray = Inventory.getInstance().getLetters();
        //rechne gesamtInternWert vom product
        for (char c : product) {
            //zugriff auf werte im Inventar Letter Array mit ascii index berechnung
            totalLocalValue += letterArray.get((int) c - 65).getValue();
        }
        // Der Fall wenn wir auf einen Buchstaben versteigern können und der Buchstabe bei uns einen Wert von 0 hat, dann bieten wir nicht mit.
        if (totalLocalValue == 0) return false;
        // * bei Auction: wir bidden immer weiter geld solang der geforderte Price unter unserem internen price legt
        return totalLocalValue >= price;
        // TODO für später: falls TotalLocalValue z.B. 5% mehr wäre als das Gebot, trotzdem verkaufen
    }

    protected Boolean isItMe(String botId) {
        String zuluId = "<@!845410146913747034>";
        return botId.equals(zuluId);
    }

    protected String getRandId (){
        Random r = new Random();
        int i = r.nextInt(9000);
        return i + 1000+"";
    }



}
