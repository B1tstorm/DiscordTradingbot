package de.fh_kiel.discordtradingbot.Holdings;

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
        Inventory inventory = Inventory.getInstance();

        inventory.updateLetterAmount("jemandWillKaufen", "ABC");
        assertThat(inventory.getLetters().get(0).getAmount()).isEqualTo(1);
        assertThat(inventory.getLetters().get(1).getAmount()).isEqualTo(1);
        assertThat(inventory.getLetters().get(2).getAmount()).isEqualTo(1);

        inventory.updateLetterAmount("auction", "ABC");
        assertThat(inventory.getLetters().get(0).getAmount()).isEqualTo(0);
        assertThat(inventory.getLetters().get(1).getAmount()).isEqualTo(0);
        assertThat(inventory.getLetters().get(2).getAmount()).isEqualTo(0);

    }
}