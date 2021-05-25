package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SegTransactionManagerTest extends TransactionManagerSeineMutter {

    @Test
    void executeTransaction() {
        SegTransactionManager transactionManager = new SegTransactionManager();
        transactionManager.executeTransaction(EventType.AUCTION_WON,"22",200,"ABC".toCharArray());
        assertThat(Inventory.getInstance().getWallet()).isEqualTo(-200);
        assertThat(Inventory.getInstance().getLetters().get(0).getAmount()).isEqualTo(1);
        assertThat(Inventory.getInstance().getLetters().get(1).getAmount()).isEqualTo(1);
        assertThat(Inventory.getInstance().getLetters().get(2).getAmount()).isEqualTo(1);
        assertThat(TransactionManager.getTransactions().get("22")).isNull();
    }

}