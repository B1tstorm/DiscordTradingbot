package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.ZuluBot;

public class SegTransactionManager extends AbstractTransactionManager implements EventListener {

    //! we buy

    public SegTransactionManager(ZuluBot bot) {
        super(bot);
    }



    @Override
    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {
        super.executeTransaction(eventType, eventId, (price * (-1)), product);
    }

    @Override
    public void update(EventItem eventItem) {
        if (eventItem.getEventType().toString().contains("AUCTION")) {

            //Attributes
            EventType eventType = eventItem.getEventType() ;
            String traderId = eventItem .getTraderID() ;
            String eventId;
            if (eventItem.getAuctionId() != null) {
                eventId = eventItem.getAuctionId();
            } else eventId = eventItem.getLogNr().toString();

            char[] product ;
            Integer price ;
            try{
            if(eventItem.getProduct() == null){
               product = transactions.get(eventId).getProduct();
            }else{
                product = eventItem.getProduct();
            }

            if (  eventItem.getValue() == null){
                price = transactions.get(eventId).getPrice();
            }else{
                price = eventItem.getValue();
            }}catch (Exception e){
                System.out.println("Es wurde um eine transaction, die es nicht gibt, versteigert");
                return;
            }

            switch (eventType) {
                //wenn Transaction neu ist, erstellen, zum Array hinzufügen und beim BID einsteigen
                case AUCTION_START:
                    if (isProductWorth(price, product) && isPriceAffordable(price)) {
                        Transaction transaction = new Transaction(eventItem);
                        transactions.put(eventId, transaction);
                        bot.getChannelInteracter().writeBidMessage(eventItem);
                    }
                    break;
                case AUCTION_BID:
                    if (isProductWorth(price, product) && isPriceAffordable(price) && !isItMe(traderId)) {
                        bot.getChannelInteracter().writeBidMessage(eventItem);
                    }
                    break;
                case AUCTION_WON:
                    if (isItMe(traderId)) {
                        executeTransaction(eventType, eventId, price, product);
                    } else {
                        dismissTransaction(eventId);
                    }
                    break;
            }
        }
    }

}
