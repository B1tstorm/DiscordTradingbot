package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

public class SellTransactionManager extends AbstractTransactionManager implements EventListener {
    //!we buy

    protected void makeOffer() {
    //TODO write a sell Offer message in the channel

    }

    @Override
    public void executeTransaction(EventType eventType, String eventId, Integer price, char[] product) {
        super.executeTransaction(eventType, eventId, (price * (-1)), product);
    }

    @Override
    public void update(EventItem eventItem) {
        // extract importen attributes form the EventItem
        EventType eventType = eventItem.getEventType();
        Integer price = Integer.parseInt(eventItem.getValue());
        char[] product = eventItem.getProduct();
        String traderId = eventItem.getTraderID();
        String eventId;
        if (eventItem.getAuctionId() != null) {
            eventId = eventItem.getAuctionId();
        } else eventId = eventItem.getLogNr().toString();
        String eventState = eventItem.getState;

        switch(eventState){
            case "offer" :
                if (isProductWorth(price,product) && isPriceAffordable(price)){
                    SegTransactionManager.transactions.put(eventId, new Transaction(eventType));
                    //! wenn wir kaufen wollen, sollen wir mit dem Pattern antworten :
                    //! !step accept @USER ID
                    channelInteracter.writeAcceptMessage(eventItem);
                }
                break;
            case "confirm":
                //! jemand hat den den verkauf an uns bestätigt
                if (traderId.equals("845410146913747034")){
                    //* "price*(-1)" macht die transaktion negativ (wie bezahlen)
                    executeTransaction(eventType, eventId, price * (-1), product);
                }else  dismissTransaction(eventId);
                break;
            case "accept":
                //! jemand akzeptiert unser Angebot und will von uns kaufen wir sollen mit dem pattern antworten
                //! !step confirm ourID @USER sell product price
                channelInteracter.writeConfirmMessage();
                executeTransaction(eventType,eventId,price,product);
            break;
            default:
                break;
        }
    }

}
