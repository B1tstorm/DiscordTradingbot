package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

public class TransactionManager extends AbstractTransactionManager implements EventListener {
    private static TransactionManager transactionManager;

    public static TransactionManager getInstance(ChannelInteracter channelInteracter){
        if (transactionManager == null){
            transactionManager = new TransactionManager(channelInteracter);
        }
        return transactionManager;
    }

    private TransactionManager(ChannelInteracter channelInteracter) {
    this.channelInteracter = channelInteracter;
    }

    //prüft ob produkt einen bestimmten Wert wert ist
//    @Override
//    public Boolean isProduktWorth(Integer price,@NonNull char[] product,EventType eventType) {
//        //ToDo Method is to be tested
//        int totalLocalValue = 0;
//        ArrayList<Letter> letterArray = Inventory.getInstance().getLetters();
//        //rechne gesamtWert vom product
//        for (char c : product) {
//            //zugriff auf werte im Inventar Letter Array mit ascii index berechnung
//            totalLocalValue += letterArray.get((int) c - 65).getValue();
//        }
//        // Der Fall wenn wir auf einen Buchstaben versteigern können und der Buchstabe bei uns einen Wert von 0 hat, dann bieten wir nicht mit.
//        if (totalLocalValue == 0) return false;
//        // Der Fall wenn wir  Buchstaben verkaufen und unser Value (totalLocalValue) kleiner ist als der angebotene Preis (price)
//        if (eventType == EventType.BUY) {
//            return price >= totalLocalValue;
//        }else
//            // Der Fall wenn wir auf Buchstaben bieten und unser Value (totalLocalValue) größer ist als der Preis (price)
//            return totalLocalValue >= price;
//        // TODO für später: falls TotalLocalValue z.B. 5% mehr wäre als das Gebot, trotzdem verkaufen
//    }


    @Override
    public void executeTransaction(EventType eventType,String eventId, Integer price, char[] product)   {
        Inventory.getInstance().updateWallet(price);
        Inventory.getInstance().updateLetterAmount(eventType,product);
        TransactionManager.transactions.remove(eventId);
    }



    //TODO -implement update()
    @Override
    public void update(EventItem eventItem) {
        // extract importen attributes form the EventItem
        EventType eventType = eventItem.getEventType();
        Integer price = Integer.parseInt(eventItem.getValue());
        char[] product = eventItem.getProduct();
        String traderId = eventItem.getTraderID();
        String  eventId;
        if (eventItem.getAuctionId() != null){
            eventId  =    eventItem.getAuctionId();
        }else eventId = eventItem.getLogNr().toString();

        //* new refactored code
        switch (eventType){
            //wenn Transaction neu ist, erstellen, zum Array hinzufügen und beim BID einsteigen
            case AUCTION_START:
                if (isProductWorth(price, product) && isPriceAffordable(price)){
                    Transaction t = new Transaction(eventType);
                    TransactionManager.transactions.put(eventId, t);
                    t.bid(eventId, price);
                }break;
                //TODO
                //!trader ID muss geprüft werden. wir dürfen uns selbst nicht versteigern
            case AUCTION_BID:
                if (isProductWorth(price, product) && isPriceAffordable(price) && traderId != "unsereID"){
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
                //!dieser Fall ist nur wenn Eike von uns kaufen will
                //TODO Fall BUY im handler Bot
            case BUY:
                if (checkInventory(product) && isProductWorth(price,product)) {
                    //! Antworte SEG positiv // todo Channelinteractor einschalten
                    channelInteracter.writeMessage(eventItem);
                    TransactionManager.transactions.put(eventId,new Transaction(eventType));
                    executeTransaction(eventType,eventId,price,product);
                }
                //! lehen Angebot ab todo Channelinteractor einschalten
                //! mach einen gegen angebot todo Channelinteractor einschalten
                break;
            case SELL:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + eventType);
        }


    }

}