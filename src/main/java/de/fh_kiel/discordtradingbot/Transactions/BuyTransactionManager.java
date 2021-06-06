package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class BuyTransactionManager extends AbstractTransactionManager implements EventListener {
    //* we sell
    MessageChannel channel = null;

    public BuyTransactionManager(ChannelInteracter channelInteracter) {
        super(channelInteracter);
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
        EventType eventType = eventItem.getEventType() ;
        if (eventType.toString().contains("BUY") || eventType.toString().contains("ACCEPT")) {
            String traderId = eventItem .getTraderID() ;
            String eventId;
            if (eventItem.getAuctionId() != null) {
                eventId = eventItem.getAuctionId();
            } else eventId = eventItem.getLogNr().toString();

            char[] product ;
            Integer price ;

            if(eventItem.getProduct() == null){
                product = BuyTransactionManager.getTransactions().get(eventId).getProduct();
            }else{
                product = eventItem.getProduct();
            }


            if (  eventItem.getValue() == null){
                price = BuyTransactionManager.getTransactions().get(eventId).getPrice();
            }else{
                price = eventItem.getValue();
            }



            //*buyer kann Eike sein oder ein anderer Bot, Im Fall Eike müssen wir
            //*begründen warum wir nicht verkaufen können und wir müssen ein gegenangebot machen

            switch (eventType) {
                case BUY_OFFER:
                    channel = eventItem.getChannel();
                    if (isProductWorth(price, product) && checkInventory(product)) {
                        BuyTransactionManager.transactions.put(eventId, new Transaction(eventItem));
                        channelInteracter.writeAcceptMessage(eventItem);
                    } else if (eventItem.getSellerID().equals("HIER KOMMT EIKES ID")) {
                        //! begrunde warum wir nicht kaufen können
                        channelInteracter.writeThisMessage(("Wir haben das Produkt -> " + checkInventory(product)).toString(),eventItem.getChannel());
                        channelInteracter.writeThisMessage(("Dein Preis ist fair -> " + isProductWorth(price, product)).toString(),eventItem.getChannel());
                        //! Ein GegenAngebot TODO GegenAngebot
                    }
                    break;
                case BUY_CONFIRM:
                    if (isItMe(traderId)) {
                        executeTransaction(eventType, eventId, price, product);
                        //TODO LÖSCHEN
                        channelInteracter.writeThisMessage("OKAY ich habe verkauft \n", eventItem.getChannel());
                        channel = eventItem.getChannel();
                        makeSellOffer(product);
                    } else dismissTransaction(eventId);
                    break;
                case ACCEPT: {
                    //!jemand hat unser Angebot angenommen und wir müssen ihm bestätigen "wir machen eine confirm Ansage"
                    try {
                        eventItem.setValue(transactions.get(eventId).getPrice());
                        eventItem.setProduct(transactions.get(eventId).getProduct());
                        eventItem.setEventType(EventType.BUY_CONFIRM);
                    }catch (NullPointerException e ){ return;}
                    executeTransaction(eventItem);
                    channelInteracter.writeSellConfirmMessage(eventItem);
                }
            }

        }


    }

    public void makeSellOffer(char[] product){
        //* !trd wts ID product PRICE
        String id = getRandId();
        int value = 0;
        for (Character c: product) {
            int temp =  Inventory.getInstance().getLetters().get((int)c - 65).getValue();
            value += temp;
        }
        String s = "!trd wts " + id +" "+ valueOf(product) + " " + value;
        channelInteracter.writeThisMessage(s, channel);
        transactions.put(id, new Transaction(new EventItem(null, getZuluId(), null, id
                , EventType.BUY_OFFER, product, value, channel)));

        //erstellt eine Transaction mit einem EventItem


    }

    private String  getZuluId(){
        return "<@!845410146913747034>";
    }


}
