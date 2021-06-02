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
        if (eventItem.getEventType().toString().contains("BUY")) {
            // extract importen attributes form the EventItem
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



            //*buyer kann Eike sein oder ein anderer Bot, Im Fall Eike müssen wir
            //*begründen warum wir nicht verkaufen können und wir müssen ein gegenangebot machen

            switch (eventType) {
                //! jemand hat was angeboten und wir wollen ihm sagen "geilo, das würde ich gerne kaufen"
                case BUY_OFFER:
                    if (isProductWorth(price, product) && checkInventory(product)) {
                        BuyTransactionManager.transactions.put(eventId, new Transaction(eventItem));
                        //! Antworte mit dem pattern:
                        //! !step accept @USER ID
                        channelInteracter.writeAcceptMessage(eventItem);
                    } else if (eventItem.getSellerID().equals("HIER KOMMT EIKES ID")) {
                        //! begrunde warum wir nicht kaufen können
                        channelInteracter.writeThisMessage(("Wir haben das Produkt -> " + checkInventory(product)).toString(),eventItem);
                        channelInteracter.writeThisMessage(("Dein Preis ist fair -> " + isProductWorth(price, product)).toString(),eventItem);
                        //! Ein GegenAngebot TODO GegenAngebot
                    }
                    break;
                case BUY_CONFIRM:
                    if (traderId.equals("845410146913747034")) {
                        executeTransaction(eventType, eventId, price, product);
                    } else dismissTransaction(eventId);
                    break;
                case BUY_ACCEPT: {
                    //!jemand hat unser Angebot angenommen und wir müssen ihm bestätigen "wir machen eine confirm Ansage"
                    channelInteracter.writeBuyConfirmMessage(eventItem);
                    executeTransaction(eventItem);
                }
            }

        }


    }

    private void makeBuyOffer(){
        //todo erstellt ein Kaufangebot mit dem Pattern
        //* !step offer ID buy LETTER PRICE
        //generiere ein EventItem mit : auctionId ,EventType, price, Produkt, sellerId als ZULU id , Channel: traderChannel
        //erstellt eine Transaction mit einem EventItem


    }
}
