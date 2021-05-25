package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BuyTranactionManagerTest extends TransactionManagerSeineMutter {

    //* Methode rechnet ob das kaufPrice fair?
    @Test
    void testIsProductWorth() {
        //der statische price des Strings ist 30
        int kaufPrice = 29;
        Inventory inventory = Inventory.getInstance();
        String product = "ABC";
        BuyTranactionManager transactionManager =  new BuyTranactionManager();

        assertThat(transactionManager.isProductWorth(kaufPrice,product.toCharArray()))
                .isEqualTo(transactionManager.isProductWorth(kaufPrice,product.toCharArray()))
                .isFalse();


    }

    @Test
    void update() {
    }
}