package de.fh_kiel.discordtradingbot.Holdings;

import static org.assertj.core.api.Assertions.*;

import de.fh_kiel.discordtradingbot.ZuluBot;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartManagerTest {

    @Test
    void createCart() {
        CartManager testManager = new CartManager();
        ArrayList<Letter> buchstabenssollTest = new ArrayList<>();
        ZuluBot bot = new ZuluBot();
        bot.launch();
        Inventory inventory = bot.getInventory();
        Letter letter1 =  bot.getInventory().getLetters().get(0);
        Letter letter2 =  bot.getInventory().getLetters().get(1);
        Letter letter3 =  bot.getInventory().getLetters().get(2);
        buchstabenssollTest.add(letter1);
        buchstabenssollTest.add(letter2);
        buchstabenssollTest.add(letter3);
        Cart cart = new Cart(buchstabenssollTest);

        testManager.createCart(buchstabenssollTest);
        //*test ob "buchstabenSoll" im cartManager/cats/index0 richtig inzialisieet wurde
        assertThat(testManager.getCarts().get(0).getBuchstabenSoll()).isEqualTo(buchstabenssollTest);
        //*test ob "value" im cartManager/cats/index0 richtig inzialisieet wurde
        assertThat(testManager.getCarts().get(0).getValue()).isEqualTo(cart.getValue());
        //*test ob die Value der Buchstaben richitg gerechnet wurde
        assertThat(cart.getValue()).isEqualTo(letter1.getValue()+letter2.getValue()+letter3.getValue());
        //*test ob negative values vorhanden sind
        for (Cart c :testManager.getCarts()) {
            for (Letter l : c.getBuchstabenSoll()) {
                assertThat(l.getValue()).isNotNegative();
            }
        }

    }

    @Test
    void deleteCart() {
    }

    @Test
    void addLetterToCart() {

    }
}
