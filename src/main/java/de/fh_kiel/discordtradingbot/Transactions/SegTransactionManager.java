package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
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

            if(eventItem.getProduct() == null){
               product = SegTransactionManager.getTransactions().get(eventId).getProduct();
            }else{
                product = eventItem.getProduct();
            }


            if (  eventItem.getValue() == null){
                price = SegTransactionManager.getTransactions().get(eventId).getPrice();
            }else{
                price = eventItem.getValue();
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
                //TODO
                //!trader ID muss geprüft werden. wir dürfen uns selbst nicht versteigern
                case AUCTION_BID:
                    if (isProductWorth(price, product) && isPriceAffordable(price) && !traderId.equals("<!845410146913747034>")) {
                        //TransactionManager.getTransactions().get(eventId).bid(eventId, price);
                        bot.getChannelInteracter().writeBidMessage(eventItem);
                    }
                    break;
                case AUCTION_WON:
                    if (traderId.equals("<!845410146913747034>")) {
                        //* "price*(-1)" macht die transaktion negativ (wir bezahlen)
                        executeTransaction(eventType, eventId, price, product);
                    } else {
                        dismissTransaction(eventId);
                    }
                    break;
                case AUCTION_CLOSE:
                    break;
            }
        }
    }

}
