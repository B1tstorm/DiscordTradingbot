package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.ZuluBot;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class AbstractTransactionManager {

    protected ZuluBot bot;


    protected  final HashMap<String, Transaction> transactions = new HashMap<>();
    public  HashMap<String, Transaction> getTransactions() {
        return transactions;
    }

    AbstractTransactionManager(ZuluBot bot) {
        this.bot = bot;
    }

    //Attributes
    EventType eventType ;
    Integer price ;
    char[] product ;
    String traderId ;
    String eventId;

    //methode extrahiert die Attribute aus dem EventItem
    protected void fillAttributes (EventItem eventItem){
        this.eventType = eventItem.getEventType();
        this.price = eventItem.getValue();
        this.product = eventItem.getProduct();
        this.traderId = eventItem.getTraderID();
        if (eventItem.getAuctionId() != null) {
            this.eventId = eventItem.getAuctionId();
        } else this.eventId = eventItem.getLogNr().toString();
    }

    public Boolean checkInventory(@NonNull char[] product) {
        ArrayList<Letter> letters = bot.getInventory().getLetters();
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
    protected HashMap<Character, Integer> fillHashmap(HashMap<Character, Integer> hashMap) {
        for (int ascii = 65; ascii < 91; ascii++) {
            hashMap.put((char) ascii, 0);
        }
        return hashMap;
    }

    public Boolean isPriceAffordable(Integer price) {
        return price <= bot.getInventory().getWallet();
    }

    public Transaction startTransaction(EventType eventType) {
        return new Transaction(eventType);
    }

    public void dismissTransaction(String eventId) {
        transactions.remove(eventId);
    }

    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {
        bot.getInventory().updateLetterAmount(eventType, product);
        bot.getInventory().updateWallet(price);
        transactions.remove(eventId);
        //TODO delete
        System.out.println("Tranaction wurde excuted");
    }

    public void executeTransaction(EventItem eventItem) {
            Transaction transaction = transactions.get(eventItem.getAuctionId());
            if (transaction != null){
                bot.getInventory().updateLetterAmount(eventItem.getEventType(), transaction.getProduct());
                bot.getInventory().updateWallet(transaction.getPrice());
                transactions.remove(eventItem.getAuctionId());
                //TODO delete
                System.out.println("Tranaction wurde excuted");
        }
    }

    public Boolean isProductWorth(Integer price, char[] product) {
        //ToDo Method is to be tested
        int totalLocalValue = calculateProductValue(product);

        // Der Fall wenn wir auf einen Buchstaben versteigern können und der Buchstabe bei uns einen Wert von 0 hat, dann bieten wir nicht mit.
        if (totalLocalValue == 0) return false;
        // * bei Auction: wir bidden immer weiter geld solang der geforderte Price unter unserem internen price legt
        return totalLocalValue >= price;
        // TODO für später: falls TotalLocalValue z.B. 5% mehr wäre als das Gebot, trotzdem verkaufen
    }

    protected Boolean isItMe(String botId) {
        return botId.equals(bot.getChannelInteracter().getMyId());
    }

    protected String getRandId (){
        Random r = new Random();
        int i = r.nextInt(9000);
        return i + 1000+"";
    }

    protected int calculateProductValue(char[] product){
        int totalLocalValue = 0;
        ArrayList<Letter> letterArray = bot.getInventory().getLetters();
        //rechne gesamtInternWert vom product
        for (char c : product) {
            //zugriff auf werte im Inventar Letter Array mit ascii index berechnung
            totalLocalValue += letterArray.get((int) c - 65).getValue();
        }
        return totalLocalValue;
    }

}
