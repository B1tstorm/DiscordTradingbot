package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

import java.util.ArrayList;

public class BuyTransactionManager extends AbstractTransactionManager implements EventListener {
    //! we sell
    protected void makeOffer() {
        //TODO write a BuyOffer message in the channel
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
        EventType eventType = eventItem.getEventType();
        Integer price = Integer.parseInt(eventItem.getValue());
        char[] product = eventItem.getProduct();
        String traderId = eventItem.getTraderID();
        String eventId;
        if (eventItem.getAuctionId() != null) {
            eventId = eventItem.getAuctionId();
        } else eventId = eventItem.getLogNr().toString();


        if (checkInventory(product) && isProductWorth(price, product)) {
            //! Antworte EIKE positiv // todo Channelinteractor einschalten
            channelInteracter.writeMessage(eventItem);
            BuyTransactionManager.transactions.put(eventId, new Transaction(eventType));
            executeTransaction(eventType, eventId, price, product);
        }
        //! lehen Angebot ab todo Channelinteractor einschalten
        //! mach einen gegen angebot todo Channelinteractor einschalten

        //*buyer kann Eike sein oder ein anderer Bot, Im Fall Eike müssen wir
        //*begründen warum wir nicht verkaufen können und wir müssen ein gegenangebot machen

        switch (eventState) {
                //! jemand hat was angeboten und wir wollen ihm sagen "geilo, das würde ich gerne kaufen"
            case "offer":
                if (isProductWorth(price, product) && checkInventory(product)) {
                    BuyTransactionManager.transactions.put(eventId, new Transaction(eventType));
                    //! Antworte mit dem pattern:
                    //! !step accept @USER ID
                    channelInteracter.writeAcceptMessage(eventItem);
                } else if (eventItem.getSellerID().equals("HIER KOMMT EIKES ID")) {
                    //! begrunde warum wir nicht kaufen können
                    channelInteracter.writeThisMessage("Wir haben das Produkt -> " + checkInventory(product));
                    channelInteracter.writeThisMessage("Dein Preis ist fair -> " + isProductWorth(price, product));
                    //! Ein GegenAngebot TODO GegenAngebot
                }
                break;
            case "confirm":
                if (traderId.equals("845410146913747034")){
                executeTransaction(eventType, eventId, price, product);
                }else dismissTransaction(eventId);
                break;
            case "accept": {
                //!jemand hat unser Angebot angenommen und wir müssen ihm bestätigen "wir machen eine confirm Ansage"
                channelInteracter.writeConfirmMessage(eventItem);
                executeTransaction(eventType,eventId,price,product);
            }

        }


    }
}
