package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.ZuluBot;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.String.valueOf;

public class BuyTransactionManager extends AbstractTransactionManager implements EventListener {
    //! we sell
    MessageChannel channel = null;

    public BuyTransactionManager(ZuluBot bot) {
        super(bot);
    }

    @Override
    public Boolean isProductWorth(Integer price, char[] product) {
        int totalLocalValue = 0;
        ArrayList<Letter> letterArray = Inventory.getInstance().getLetters();
        //rechne gesamtWert vom product
        for (char c : product) {
            //zugriff auf werte im Inventar Letter Array mit ascii index berechnung
            totalLocalValue += letterArray.get((int) c - 65).getValue();
        }
        //* wir verkaufen nur wenn der angebotene price >= unseren internen Wert ist
        return price >= totalLocalValue;
    }

    @Override
    public void update(EventItem eventItem) {
        if (eventItem.getEventType().toString().contains("BUY") || eventItem.getEventType().toString().contains("ACCEPT") || eventItem.getEventType().toString().contains("ZULU")) {
            EventType eventType = eventItem.getEventType();
            String traderId = eventItem.getTraderID();
            channel = eventItem.getChannel();
            String eventId;
            if (eventItem.getAuctionId() != null) {
                eventId = eventItem.getAuctionId();
                //bei Accept fälle von anderen klassen soll das programm diese Methode verlassen
                if (transactions.get(eventId) == null && eventItem.getEventType().toString().contains("ACCEPT")) return;
            } else {
                eventId = eventItem.getLogNr().toString();
            }

            char[] product;
            Integer price;

            if (eventItem.getProduct() == null) {
                product = transactions.get(eventId).getProduct();
            } else {
                product = eventItem.getProduct();
            }

            if (eventItem.getValue() == null) {
                price = transactions.get(eventId).getPrice();
            } else {
                price = eventItem.getValue();
            }

            switch (eventType) {
                case ZULU_BUY:
                    if (isProductWorth(price, product) && checkInventory(product)) {
                        transactions.put(eventId, new Transaction(eventItem));
                        executeTransaction(EventType.BUY_CONFIRM, eventId, price, product);
                        bot.getChannelInteracter().writeThisMessage("Danke für deinen Kauf. Transaktion war erfolgreich", channel);
                    } else {
                        //! begründe warum wir nicht verkaufen können
                        bot.getChannelInteracter().writeThisMessage(("Wir haben das Produkt -––-–-> " + checkInventory(product)), eventItem.getChannel());
                        bot.getChannelInteracter().writeThisMessage(("Dein  Preis  ist fair ––-–--> " + isProductWorth(price, product)), eventItem.getChannel());
                        makeCounterOffer(eventItem);
                        bot.getChannelInteracter().writeThisMessage("-zum Bestätigen schreib:\n!ZULU confirm " + eventItem.getLogNr(), channel);
                        bot.getChannelInteracter().writeThisMessage("-zum Ablehnen schreib:\n!ZULU deny " + eventItem.getLogNr(), channel);
                    }
                    break;
                case ZULU_CONFIRM:
                    executeTransaction(eventItem);
                    bot.getChannelInteracter().writeThisMessage("Danke für deinen Kauf. Transaktion war erfolgreich", channel);
                    break;
                case ZULU_DENY:
                    bot.getChannelInteracter().writeThisMessage("Schade! \n Du kannst uns jeder Zeit gerne ein Gegenangebot machen", channel);
                    dismissTransaction(eventId);
                    break;
                case BUY_OFFER:
                    if (isProductWorth(price, product) && checkInventory(product)) {
                        transactions.put(eventId, new Transaction(eventItem));
                        bot.getChannelInteracter().writeAcceptMessage(eventItem);
                    }
                    break;
                case BUY_CONFIRM:
                    if (isItMe(traderId) && transactions.get(eventId) != null) {
                        executeTransaction(eventType, eventId, price, product);
                        bot.getChannelInteracter().writeThisMessage("OKAY ich habe verkauft \n", eventItem.getChannel());
                        channel = eventItem.getChannel();
                        makeSellOffer(product);

                    } else dismissTransaction(eventId);
                    break;
                case ACCEPT: {
                    try {
                        eventItem.setValue(transactions.get(eventId).getPrice());
                    } catch (NullPointerException e) {
                        return;
                    }

                    eventItem.setProduct(transactions.get(eventId).getProduct());
                    eventItem.setEventType(transactions.get(eventId).getEventType());


                    bot.getChannelInteracter().writeSellConfirmMessage(eventItem);
                    executeTransaction(eventItem);
                }
            }

        }


    }

    public void makeSellOffer(char[] product) {
        //* !trd wts ID product PRICE
        String id = getRandId();
        int value = 0;
        for (Character c : product) {
            int temp = Inventory.getInstance().getLetters().get((int) c - 65).getValue();
            value += temp;
        }
        String s = "!trd wts " + id + " " + valueOf(product) + " " + value;
        bot.getChannelInteracter().writeThisMessage(s, channel);
        transactions.put(id, new Transaction(new EventItem(null, bot.getChannelInteracter().getMentionId(), null, id
                , EventType.BUY_OFFER, product, value, channel)));
    }

    /**
     * die methode vergleicht das product mit dem Inventory. und liefert die davon im Inventory vorhandene Buchstaben zurück
     * bsp. er will halloe aber wir haben kein o,e also liefern wir hall zum Gegenangebot
     *
     * @param product das product zum gegenAngebot
     * @return ein product den wir stattdessen anbieten
     */
    private String getCounterString(char[] product) {
        HashMap<Character, Integer> hashmap = fillHashmap(new HashMap<>());
        ArrayList<Letter> letters = Inventory.getInstance().getLetters();
        StringBuilder counterOffer = new StringBuilder();

        // Hashmap mit angeforderten Buchstaben (Buchstabe , Angeforderte Anzahl)
        for (Character buchstabe : product) {
            hashmap.put(buchstabe, hashmap.get(buchstabe) + 1);
        }

        //schreibe die Buchstaben, die wir liefern können zusammen in den Variablen counterOffer
        for (Letter letter : letters) {
            char c = letter.getLetter();
            if (letter.getAmount() >= hashmap.get(c)) {
                counterOffer.append(String.valueOf(c).repeat(Math.max(0, hashmap.get(c))));
            } else {
                counterOffer.append(String.valueOf(c).repeat(Math.max(0, letter.getAmount())));
            }

        }
        return counterOffer.toString();
    }

    private void makeCounterOffer(EventItem eventItem) {
        //* !ZULU counterOffer <ID> <String> [Preis]
        char[] product = eventItem.getProduct();
        channel = eventItem.getChannel();
        String counterOfferMessage = "!ZULU counterOffer " + eventItem.getLogNr() + " " + getCounterString(product) + " "
                + (int) (calculateProductValue(getCounterString(product).toCharArray()) * 1.2);
        bot.getChannelInteracter().writeThisMessage(counterOfferMessage, channel);
        eventItem.setValue((int) (calculateProductValue(getCounterString(product).toCharArray()) * 1.2));
        transactions.put(eventItem.getLogNr().toString(), new Transaction(eventItem));
    }

}
