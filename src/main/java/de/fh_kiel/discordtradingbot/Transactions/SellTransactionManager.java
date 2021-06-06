package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import discord4j.core.object.entity.channel.MessageChannel;

import java.lang.reflect.Array;
import java.nio.channels.Channel;
import java.util.Arrays;
import java.util.UUID;

import static java.lang.String.valueOf;

public class SellTransactionManager extends AbstractTransactionManager implements EventListener {
    //!we buy
    MessageChannel channel = null;

    public SellTransactionManager(ChannelInteracter channelInteracter) {
        super(channelInteracter);
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
            } else eventId = eventItem.getLogNr().toString();

            if(eventItem.getProduct() == null){
                product = SellTransactionManager.getTransactions().get(eventId).getProduct();
            }else{
                product = eventItem.getProduct();
            }

            if (  eventItem.getValue() == null){
                price = SellTransactionManager.getTransactions().get(eventId).getPrice();
            }else{
                price = eventItem.getValue();
            }

            switch (eventType) {
                case SELL_OFFER:
                    if (isProductWorth(price, product) && isPriceAffordable(price)) {
                        SellTransactionManager.transactions.put(eventId, new Transaction(eventItem));
                        //! wenn wir kaufen wollen, sollen wir mit dem Pattern antworten :
                        //! !step accept @USER ID


                        channelInteracter.writeAcceptMessage(eventItem);
                    }
                    break;
                case SELL_CONFIRM:
                    //! jemand hat den den verkauf an uns bestätigt
                    if (isItMe(traderId)) {
                        executeTransaction(eventType, eventId, price, product);
                        //Löschen
                        channelInteracter.writeThisMessage("OKAY ich habe gekauft \n", eventItem.getChannel());
                        channel = eventItem.getChannel();
                        makeSellOffer(product);
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

                    channelInteracter.writeSellConfirmMessage(eventItem);
                    executeTransaction(eventType, eventId, price, product);
                    break;
                default:
                    break;
            }
        }
    }

    public void makeSellOffer(char[] product){
        //* !trd wtb ID product PRICE
        String id = getRandId();
        //String id = UUID.randomUUID().toString();
        Integer value = 0;
        for (Character c: product) {
            int temp =  Inventory.getInstance().getLetters().get((int)c - 65).getValue();
            value += temp;
        }
        String s = "!trd wtb " + id +" "+ valueOf(product) + " " + value;
        channelInteracter.writeThisMessage(s, channel);
        transactions.put(id, new Transaction(new EventItem(null, getZuluId(), null, id
                , EventType.BUY_OFFER, product, value, channel)));

        //erstellt eine Transaction mit einem EventItem


    }
    private String  getZuluId(){
        return "<@!845410146913747034>";
    }

}
