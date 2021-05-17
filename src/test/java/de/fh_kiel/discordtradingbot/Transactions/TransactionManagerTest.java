package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    }

}