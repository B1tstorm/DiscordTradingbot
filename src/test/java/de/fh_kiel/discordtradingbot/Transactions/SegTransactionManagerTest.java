package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SegTransactionManagerTest extends AbstractTransactionManager {

    @Test
    void executeTransaction() {
        SegTransactionManager transactionManager = new SegTransactionManager();
        transactionManager.executeTransaction(EventType.AUCTION_WON,"22",200,"ABC".toCharArray());
        assertThat(bot.getInventory().getWallet()).isEqualTo(-200);
        assertThat(bot.getInventory().getLetters().get(0).getAmount()).isEqualTo(1);
        assertThat(bot.getInventory().getLetters().get(1).getAmount()).isEqualTo(1);
        assertThat(bot.getInventory().getLetters().get(2).getAmount()).isEqualTo(1);
        assertThat(TransactionManager.getTransactions().get("22")).isNull();
    }

    @Override
    protected void makeOffer() {

    }
}