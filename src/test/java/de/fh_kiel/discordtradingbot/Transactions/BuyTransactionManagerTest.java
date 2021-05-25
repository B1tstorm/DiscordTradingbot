package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BuyTransactionManagerTest extends TransactionManagerSeineMutter {

    //* Methode rechnet ob das kaufPrice fair?
    @Test
    void testIsProductWorth() {
        //der statische price des Strings ist 30
        int kaufPrice = 29;
        Inventory inventory = Inventory.getInstance();
        String product = "ABC";
        BuyTransactionManager transactionManager =  new BuyTransactionManager();

        assertThat(transactionManager.isProductWorth(kaufPrice,product.toCharArray()))
                .isEqualTo(transactionManager.isProductWorth(kaufPrice,product.toCharArray()))
                .isFalse();


    }

    @Test
    void update() {
    }
}