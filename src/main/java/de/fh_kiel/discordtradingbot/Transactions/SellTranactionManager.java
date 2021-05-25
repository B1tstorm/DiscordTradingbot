package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;

import java.util.ArrayList;

public class SellTranactionManager extends TransactionManagerSeineMutter implements EventListener {

    @Override
    public void update(EventItem eventItem) {
        // extract importen attributes form the EventItem
        EventType eventType = eventItem.getEventType();
        Integer price = Integer.parseInt(eventItem.getValue());
        char[] product = eventItem.getProduct();
        String traderId = eventItem.getTraderID();
        String  eventId;
        if (eventItem.getAuctionId() != null){
            eventId  =    eventItem.getAuctionId();
        }else eventId = eventItem.getLogNr().toString();

        switch (eventType) {
            case SELL:
                break;
        }
    }
}
