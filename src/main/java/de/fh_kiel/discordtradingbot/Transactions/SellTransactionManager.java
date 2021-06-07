package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.ZuluBot;
import discord4j.core.object.entity.channel.MessageChannel;

import static java.lang.String.valueOf;

public class SellTransactionManager extends AbstractTransactionManager implements EventListener {
    //!we buy
    MessageChannel channel = null;

    public SellTransactionManager(ZuluBot bot) {
        super(bot);
    }

    protected void makeOffer() {
        //TODO write a sell Offer message in the channel

    }

    @Override
    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {
        super.executeTransaction(eventType, eventId, (price * (-1)), product);
    }

    @Override
    public void update(EventItem eventItem) {
        if (eventItem.getEventType().toString().contains("SELL") || eventItem.getEventType().toString().contains("ACCEPT")) {

            //Attributes
            EventType eventType = eventItem.getEventType() ;
            String traderId = eventItem .getTraderID() ;
            String eventId;
            char[] product ;
            Integer price ;

            if (eventItem.getAuctionId() != null) {
                eventId = eventItem.getAuctionId();
                if (transactions.get(eventId) == null && eventItem.getEventType().toString().contains("ACCEPT")) return;
            } else eventId = eventItem.getLogNr().toString();

            if(eventItem.getProduct() == null){
                product = transactions.get(eventId).getProduct();
            }else{
                product = eventItem.getProduct();
            }

            if (  eventItem.getValue() == null){
                price = transactions.get(eventId).getPrice();
            }else{
                price = eventItem.getValue();
            }

            switch (eventType) {
                case SELL_OFFER:
                    if (isProductWorth(price, product) && isPriceAffordable(price)) {
                        transactions.put(eventId, new Transaction(eventItem));
                        //! wenn wir kaufen wollen, sollen wir mit dem Pattern antworten :
                        //! !step accept @USER ID


                        bot.getChannelInteracter().writeAcceptMessage(eventItem);
                    }
                    break;
                case SELL_CONFIRM:
                    //! jemand hat den den verkauf an uns bestätigt
                    if (isItMe(traderId) && transactions.get(eventId) != null ) {
                        executeTransaction(eventType, eventId, price, product);
                        //todo Löschen
                        bot.getChannelInteracter().writeThisMessage("OKAY ich habe gekauft \n", eventItem.getChannel());
                        channel = eventItem.getChannel();
                        makeBuyOffer(product);
                    } else dismissTransaction(eventId);
                    break;
                case ACCEPT:
                    //! jemand akzeptiert unser Angebot und will von uns kaufen wir sollen mit dem pattern antworten
                    //! !trd confirm @USER ourID  wts product price
                    try{
                    eventItem.setValue(transactions.get(eventId).getPrice());
                    }catch (NullPointerException e){
                        return;
                    }
                    eventItem.setProduct(transactions.get(eventId).getProduct());
                    price = transactions.get(eventId).getPrice();
                    product = transactions.get(eventId).getProduct();
                    eventType = EventType.SELL_ACCEPT;

                    bot.getChannelInteracter().writeBuyConfirmMessage(eventItem);
                    executeTransaction(eventType, eventId, price, product);
                    break;
                default:
                    break;
            }
        }
    }

    public void makeBuyOffer(char[] product){
        //* !trd wtb ID product PRICE
        String id = getRandId();
        //String id = UUID.randomUUID().toString();
        Integer value = 0;
        for (Character c: product) {
            int temp =  Inventory.getInstance().getLetters().get((int)c - 65).getValue();
            value += temp;
        }
        String s = "!trd wtb " + id +" "+ valueOf(product) + " " + value;

        bot.getChannelInteracter().writeThisMessage(s, channel);
        transactions.put(id, new Transaction(new EventItem(null, bot.getChannelInteracter().getMyId(), null, id
                , EventType.BUY_OFFER, product, value, channel)));

        //erstellt eine Transaction mit einem EventItem


    }

}
