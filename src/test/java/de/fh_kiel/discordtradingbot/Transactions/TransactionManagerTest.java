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
        // Case: auction - seg bietet uns ein Z für 1
        // Bei instanziierung von Inventory() wird im Konstruktor ein statisches Array mit allen Buchstaben angelegt
        Inventory testInventory = new Inventory();
        TransactionManager testTransactionManager = new TransactionManager();

        // Dieser aufruf sollte eine neue (Transaktion) Instanz erzeugen, da Preis <= Buchstabenwert
        testTransactionManager.update("auction",1,111,"Z");
        // Dieser aufruf sollte keine neue (Transaktion) Instanz erzeugen, da Preis > Buchstabenwert
        testTransactionManager.update("auction",11,111,"Z");
        assertThat(TransactionManager.getTransactions().size()).isEqualTo(1);

        testTransactionManager.update("auction",2,112,"Q");
        testTransactionManager.update("auction",4,112,"Q");
        assertThat(TransactionManager.getTransactions().get(112)).isNotNull();
        assertThat(TransactionManager.getTransactions().size()).isEqualTo(2);
    }

}
