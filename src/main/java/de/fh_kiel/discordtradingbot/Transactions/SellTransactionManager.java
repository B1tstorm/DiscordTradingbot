package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
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

    protected void makeOffer() {
        //TODO write a sell Offer message in the channel

    }

    @Override
    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {
        super.executeTransaction(eventType, eventId, (price * (-1)), product);
    }

    @Override
    public void update(EventItem eventItem) {
        if (eventItem.getEventType().toString().contains("SELL")) {

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
                case SELL_OFFER:
                    if (isProductWorth(price, product) && isPriceAffordable(price)) {
                        SegTransactionManager.transactions.put(eventId, new Transaction(eventItem));
                        //! wenn wir kaufen wollen, sollen wir mit dem Pattern antworten :
                        //! !step accept @USER ID
                        channel = eventItem.getChannel();

                        channelInteracter.writeAcceptMessage(eventItem);
                    }
                    break;
                case SELL_CONFIRM:
                    //! jemand hat den den verkauf an uns bestätigt
                    if (traderId.equals("<@!845410146913747034>")) {
                        //* "price*(-1)" macht die transaktion negativ (wie bezahlen)
                        executeTransaction(eventType, eventId, price, product);
                        channelInteracter.writeThisMessage("OKAY ich habe gekauft",channel);
                        makeSellOffer(product);
                    } else dismissTransaction(eventId);
                    break;
                case SELL_ACCEPT:
                    //! jemand akzeptiert unser Angebot und will von uns kaufen wir sollen mit dem pattern antworten
                    //! !step confirm ourID @USER sell product price
                    channelInteracter.writeSellConfirmMessage(eventItem);
                    executeTransaction(eventType, eventId, price, product);
                    break;
                default:
                    break;
            }
        }
    }

    public void makeSellOffer(char[] product){
            //todo erstellt ein Kauf- Verkaufsangebot mit dem Pattern
            //* !step offer ID buy LETTER PRICE
        String id = UUID.randomUUID().toString();
        Integer value = 0;
        for (Character c: product) {
            int temp =  Inventory.getInstance().getLetters().get((int)c - 65).getValue();
            value += temp;
        }
        String s = "!step offer " + id + " sell "+ valueOf(product) + " " + value;
        channelInteracter.writeThisMessage(s, channel);
        Transaction put = transactions.put(id, new Transaction(new EventItem(null, "<@!845410146913747034>", null, id
                , EventType.BUY_OFFER, product, value, channel)));

        //erstellt eine Transaction mit einem EventItem


    }

}
