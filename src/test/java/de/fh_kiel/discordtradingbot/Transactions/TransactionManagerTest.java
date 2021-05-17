package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TransactionManagerTest {


    //kein TDD TEST
    @Test
    void isProduktWorth() {
        int preis = 2;
        Inventory inventory = new Inventory();
        String product = "ABC";
        TransactionManager transactionManager = new TransactionManager();

        Boolean b = transactionManager.isProduktWorth(preis,product );

        System.out.println(b);

    }

    @Test
    void update(){
        // Case: auction - seg bietet uns ein Z für 10
        String tradingPartner = "SEG";
        String transactionKind = "auction";
        String product = "Z";
        Integer price = 1;
        Integer eventId = 111;
        // Bei instanziierung von Inventory() wird im Konstruktor ein statisches Array mit allen Buchstaben angelegt
        Inventory testInventory = new Inventory();
        TransactionManager testTransactionManager = new TransactionManager();
        // Dieser aufruf sollte eine neue (Transaktion) Instanz erzeugen, da Preis <= Buchstabenwert
        testTransactionManager.startTransaction( eventId, price, transactionKind, product);
        assertThat(TransactionManager.getTransactions().get(eventId)).isNotNull();
        //price erhöht durch andere mitbieter
        price += 5;
        // Dieser aufruf sollte keine neue (Transaktion) Instanz erzeugen, da Preis > Buchstabenwert
        testTransactionManager.startTransaction( eventId, price, transactionKind, product);
        assertThat(TransactionManager.getTransactions().size()).isEqualTo(1);

    }

}