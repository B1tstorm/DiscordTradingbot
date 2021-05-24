package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TransactionManagerTest {


    //kein TDD TEST
    @Test
    void isProduktWorth() {
        int preis = 2;
        Inventory inventory = Inventory.getInstance();
        String product = "ABC";
        TransactionManager transactionManager = new TransactionManager(new ChannelInteracter("12"));

        Boolean b = transactionManager.isProduktWorth(preis,product.toCharArray() ,EventType.BUY );
        Boolean c = transactionManager.isProduktWorth(preis,product.toCharArray() ,EventType.SELL );


        System.out.println(b + "  "+ c);

    }

    @Test
    void checkInventory(){
        Inventory inventory = Inventory.getInstance();
        TransactionManager transactionManager =  TransactionManager.getInstance(new ChannelInteracter("token"));
        inventory.updateLetterAmount(EventType.BUY, ("ABCADEMLIIII").toCharArray() );
        boolean b =  transactionManager.checkInventory(("ABCAEIIII").toCharArray() );
        assertThat(b).isTrue();
    }

    @Test
    void checkWallet(){
        Inventory inventory = Inventory.getInstance();
        TransactionManager t =  TransactionManager.getInstance(new ChannelInteracter("token"));
        assertThat(t.isPriceAffordable(0)).isTrue();
        assertThat(t.isPriceAffordable(2)).isFalse();
        Inventory.getInstance().updateWallet(100);
        assertThat(t.isPriceAffordable(100)).isTrue();
        assertThat(t.isPriceAffordable(90)).isTrue();
        assertThat(t.isPriceAffordable(200)).isFalse();

    }

    @Test
    void executeTransaction(){
        TransactionManager transactionManager =  TransactionManager.getInstance(new ChannelInteracter("token"));
        transactionManager.executeTransaction(EventType.AUCTION_WON,"22",200,"ABC".toCharArray());
        assertThat(Inventory.getInstance().getWallet()).isEqualTo(200);
        assertThat(Inventory.getInstance().getLetters().get(0).getAmount()).isEqualTo(1);
        assertThat(Inventory.getInstance().getLetters().get(1).getAmount()).isEqualTo(1);
        assertThat(Inventory.getInstance().getLetters().get(2).getAmount()).isEqualTo(1);
        assertThat(TransactionManager.getTransactions().get("22")).isNull();


        }

    /*
     *         Inventory.getInstance().updateWallet(price);
     *         Inventory.getInstance().updateLetterAmount(eventType,product);
     *         TransactionManager.transactions.remove(eventId);
     */


//    @Test
//    void update(){
//
//        //! nach eventItem wieder updaten bitte
//        // Case: auction - seg bietet uns ein Z für 1
//        // Bei instanziierung von Inventory() wird im Konstruktor ein statisches Array mit allen Buchstaben angelegt
//        Inventory testInventory = Inventory.getInstance();
//        TransactionManager testTransactionManager = new TransactionManager();
//
//        // Dieser aufruf sollte eine neue (Transaktion) Instanz erzeugen, da Preis <= Buchstabenwert
//        testTransactionManager.update("auction",1,111,"Z");
//        // Dieser aufruf sollte keine neue (Transaktion) Instanz erzeugen, da Preis > Buchstabenwert
//        testTransactionManager.update("auction",11,111,"Z");
//        assertThat(TransactionManager.getTransactions().size()).isEqualTo(1);
//
//        testTransactionManager.update("auction",2,112,"Q");
//        testTransactionManager.update("auction",4,112,"Q");
//        assertThat(TransactionManager.getTransactions().get(112)).isNotNull();
//        assertThat(TransactionManager.getTransactions().size()).isEqualTo(2);
//    }

}
