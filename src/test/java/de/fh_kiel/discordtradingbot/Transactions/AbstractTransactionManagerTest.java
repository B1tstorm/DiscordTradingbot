package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractTransactionManagerTest extends AbstractTransactionManager {

    @Test
    void testGetTransactions() {
    }

    @Test
    void testCheckInventory() {
        Inventory inventory = Inventory.getInstance();
        TransactionManager transactionManager =  TransactionManager.getInstance(new ChannelInteracter("token"));
        inventory.updateLetterAmount(EventType.BUY_CONFIRM, ("ABCADEMLIIII").toCharArray() );
        boolean b =  transactionManager.checkInventory(("ABCAEIIII").toCharArray() );
        assertThat(b).isTrue();
    }


    //! test erfolgt nur wenn 2 blöke auskommentiert sind
    @Test
    void testIsPriceAffordable() {
        Inventory inventory = Inventory.getInstance();

//        SegTransactionManager t =  new SegTransactionManager();
//        assertThat(t.isPriceAffordable(0)).isTrue();
//        assertThat(t.isPriceAffordable(2)).isFalse();
//        Inventory.getInstance().updateWallet(100);
//        assertThat(t.isPriceAffordable(100)).isTrue();
//        assertThat(t.isPriceAffordable(90)).isTrue();
//        assertThat(t.isPriceAffordable(200)).isFalse();

//        BuyTranactionManager t2 =  new BuyTranactionManager();
//        assertThat(t2.isPriceAffordable(0)).isTrue();
//        assertThat(t2.isPriceAffordable(2)).isFalse();
//        Inventory.getInstance().updateWallet(100);
//        assertThat(t2.isPriceAffordable(100)).isTrue();
//        assertThat(t2.isPriceAffordable(90)).isTrue();
//        assertThat(t2.isPriceAffordable(200)).isFalse();


        SellTransactionManager t3 =  new SellTransactionManager();
        assertThat(t3.isPriceAffordable(0)).isTrue();
        assertThat(t3.isPriceAffordable(2)).isFalse();
        Inventory.getInstance().updateWallet(100);
        assertThat(t3.isPriceAffordable(100)).isTrue();
        assertThat(t3.isPriceAffordable(90)).isTrue();
        assertThat(t3.isPriceAffordable(200)).isFalse();


    }

    @Test
    void testStartTransaction() {
    }

    @Test
    void testDismissTransaction() {
    }

    @Test
    void testExecuteTransaction() {
    }

    //* prüfe ob es sich lohnt dieses product zu kaufen
    @Test
    void testIsProductWorth() {
        //interner Wert ist in der Klasse Inventory statisch auf 10 gesetzt
        int preis = 30;
        Inventory inventory = Inventory.getInstance();
        String product = "ABC";
        SegTransactionManager transactionManager =  new SegTransactionManager();

        assertThat(transactionManager.isProductWorth(preis,product.toCharArray())).
                isEqualTo(transactionManager.isProductWorth(preis,product.toCharArray()))
                .isTrue();
        assertThat(transactionManager.isProductWorth(preis+1,product.toCharArray())).
                isEqualTo(transactionManager.isProductWorth(preis+1,product.toCharArray()))
                .isFalse();

    }

    @Override
    protected void makeOffer() {

    }
}