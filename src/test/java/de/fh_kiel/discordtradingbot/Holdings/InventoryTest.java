package de.fh_kiel.discordtradingbot.Holdings;

import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.ZuluBot;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class InventoryTest {

    @Test
    void initOfLetterArray() {
        for (int ascii = 65; ascii < 91; ascii++) {
            System.out.println((char)ascii);
        }
    }

    @Test
    void updateWallet() {

    }

    @Test
    void deposit() {
    }

    @Test
    void updateAmount() {
        //! Alle Amonts im Inventory sollen auf 0 sein sonst kein test
        //! Methode listenToChannel() soll auskommentiert werden  in    :     bot.launch();
        ZuluBot bot = new ZuluBot();
        bot.launch();
        Inventory inventory = bot.getInventory();

        inventory.updateLetterAmount(EventType.BUY_CONFIRM, "ABC".toCharArray());
        assertThat(inventory.getLetters().get(0).getAmount()).isEqualTo(1);
        assertThat(inventory.getLetters().get(1).getAmount()).isEqualTo(1);
        assertThat(inventory.getLetters().get(2).getAmount()).isEqualTo(1);

        inventory.updateLetterAmount(EventType.AUCTION_WON, "ABC".toCharArray());
        assertThat(inventory.getLetters().get(0).getAmount()).isEqualTo(0);
        assertThat(inventory.getLetters().get(1).getAmount()).isEqualTo(0);
        assertThat(inventory.getLetters().get(2).getAmount()).isEqualTo(0);

    }
}