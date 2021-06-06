package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.ZuluBot;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class BuyTransactionManager extends AbstractTransactionManager implements EventListener {
    //! we sell
    MessageChannel channel = null;



    public BuyTransactionManager(ZuluBot bot) {
        super(bot);
    }

    @Override
    public Boolean isProductWorth(Integer price, char[] product) {
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
        if (eventItem.getEventType().toString().contains("BUY") || eventItem.getEventType().toString().contains("ACCEPT")) {
            EventType eventType = eventItem.getEventType() ;
            String traderId = eventItem .getTraderID() ;
            String eventId;
            if (eventItem.getAuctionId() != null) {
                eventId = eventItem.getAuctionId();
                if (transactions.get(eventId) == null && eventItem.getEventType().toString().contains("ACCEPT")) return;
            } else eventId = eventItem.getLogNr().toString();

            char[] product ;
            Integer price ;

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



            //*buyer kann Eike sein oder ein anderer Bot, Im Fall Eike müssen wir
            //*begründen warum wir nicht verkaufen können und wir müssen ein gegenangebot machen

            switch (eventType) {
                //! jemand hat was angeboten und wir wollen ihm sagen "geilo, das würde ich gerne kaufen"
                case BUY_OFFER:
                    channel = eventItem.getChannel();
                    if (isProductWorth(price, product) && checkInventory(product)) {
                        transactions.put(eventId, new Transaction(eventItem));
                        //! Antworte mit dem pattern:
                        //! !step accept @USER ID
                        bot.getChannelInteracter().writeAcceptMessage(eventItem);
                    } else if (eventItem.getSellerID().equals("HIER KOMMT EIKES ID")) {
                        //! begrunde warum wir nicht kaufen können
                        bot.getChannelInteracter().writeThisMessage(("Wir haben das Produkt -> " + checkInventory(product)).toString(),eventItem.getChannel());
                        bot.getChannelInteracter().writeThisMessage(("Dein Preis ist fair -> " + isProductWorth(price, product)).toString(),eventItem.getChannel());
                        //! Ein GegenAngebot TODO GegenAngebot
                    }
                    break;
                case BUY_CONFIRM:
                    if (isItMe(traderId)) {
                        executeTransaction(eventType, eventId, price, product);
                        bot.getChannelInteracter().writeThisMessage("OKAY ich habe verkauft \n", eventItem.getChannel());
                        channel = eventItem.getChannel();
                        makeSellOffer(product);


                    } else dismissTransaction(eventId);
                    break;
                case ACCEPT: {
                    //!jemand hat unser Angebot angenommen und wir müssen ihm bestätigen "wir machen eine confirm Ansage"
                    try {
                    eventItem.setValue(transactions.get(eventId).getPrice());
                    }catch (NullPointerException e ){ return;}

                    eventItem.setProduct(transactions.get(eventId).getProduct());
                    eventItem.setEventType(transactions.get(eventId).getEventType());


                    bot.getChannelInteracter().writeSellConfirmMessage(eventItem);
                    executeTransaction(eventItem);
                }
            }

        }


    }

    public void makeSellOffer(char[] product){
        //* !trd wts ID product PRICE
        String id = getRandId();
        Integer value = 0;
        for (Character c: product) {
            int temp =  Inventory.getInstance().getLetters().get((int)c - 65).getValue();
            value += temp;
        }
        String s = "!trd wts " + id +" "+ valueOf(product) + " " + value;
        bot.getChannelInteracter().writeThisMessage(s, channel);
        transactions.put(id, new Transaction(new EventItem(null, bot.getChannelInteracter().getMyId(), null, id
                , EventType.BUY_OFFER, product, value, channel)));

        //erstellt eine Transaction mit einem EventItem


    }



}
