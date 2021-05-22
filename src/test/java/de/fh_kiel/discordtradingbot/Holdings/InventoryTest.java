package de.fh_kiel.discordtradingbot.Holdings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

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
        Inventory inventory = Inventory.getInstance();

        inventory.updateAmount("jemandWillKaufen", "ABC");
        assertThat(inventory.getLetters().get(0).getAmount()).isEqualTo(1);
        assertThat(inventory.getLetters().get(1).getAmount()).isEqualTo(1);
        assertThat(inventory.getLetters().get(2).getAmount()).isEqualTo(1);

        inventory.updateAmount("auction", "ABC");
        assertThat(inventory.getLetters().get(0).getAmount()).isEqualTo(0);
        assertThat(inventory.getLetters().get(1).getAmount()).isEqualTo(0);
        assertThat(inventory.getLetters().get(2).getAmount()).isEqualTo(0);

    }
}