package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

public class BuyTranactionManager extends TransactionManagerSeineMutter implements EventListener {
    @Override
    public Boolean isProduktWorth(Integer price, char[] product, EventType eventType) {
        return null;
    }

    @Override
    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {

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
