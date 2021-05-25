package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

import java.util.ArrayList;

public class BuyTranactionManager extends TransactionManagerSeineMutter implements EventListener {
    @Override
    public Boolean isProduktWorth(Integer price, char[] product, EventType eventType) {
        //ToDo Method is to be tested
        int totalLocalValue = 0;
        ArrayList<Letter> letterArray = Inventory.getInstance().getLetters();
        //rechne gesamtWert vom product
        for (char c : product) {
            //zugriff auf werte im Inventar Letter Array mit ascii index berechnung
            totalLocalValue += letterArray.get((int) c - 65).getValue();
        }
        //* wir verkaufen nur wenn der angebotene price >= unseren internen Wert ist
            return price >= totalLocalValue;
        // TODO für später: falls TotalLocalValue z.B. 5% mehr wäre als das Gebot, trotzdem verkaufen
    }



    @Override
    public void update(EventItem eventItem) {
        EventType eventType = eventItem.getEventType();
        Integer price = Integer.parseInt(eventItem.getValue());
        char[] product = eventItem.getProduct();
        String traderId = eventItem.getTraderID();
        String  eventId;
        if (eventItem.getAuctionId() != null){
            eventId  =    eventItem.getAuctionId();
        }else eventId = eventItem.getLogNr().toString();


        switch (eventType){
            case BUY:
                if (checkInventory(product) && isProduktWorth(price,product,eventType)) {
                    //! Antworte SEG positiv // todo Channelinteractor einschalten
                    channelInteracter.writeMessage("eine sehr sinnlose Nachricht");
                    TransactionManager.transactions.put(eventId,new Transaction(eventType));
                    executeTransaction(eventType,eventId,price,product);
                }
                //! lehen Angebot ab todo Channelinteractor einschalten
                //! mach einen gegen angebot todo Channelinteractor einschalten
                break;
        }
    }
}
