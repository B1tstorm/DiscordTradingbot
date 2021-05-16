package de.fh_kiel.discordtradingbot.Holdings;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartManagerTest {

    @Test
    void createCart() {
        CartManager testManager = new CartManager();
        ArrayList<Letter> buchstabenssollTest = new ArrayList<>();
        Letter letter1 = new Letter('A', 20);
        Letter letter2 = new Letter('B', 10);
        Letter letter3 = new Letter('C', -100);
        buchstabenssollTest.add(letter1);
        buchstabenssollTest.add(letter2);
        buchstabenssollTest.add(letter3);
        Cart cart = new Cart(buchstabenssollTest);

        testManager.createCart(buchstabenssollTest);
        //test ob "buchstabenSoll" im cartManager/cats/index0 richtig inzialisieet wurde
        assertThat(testManager.getCarts().get(0).getBuchstabenSoll()).isEqualTo(buchstabenssollTest);
        //test ob "value" im cartManager/cats/index0 richtig inzialisieet wurde
        assertThat(testManager.getCarts().get(0).getValue()).isEqualTo(cart.getValue());
        //test ob die Value der Buchstaben richitg gerechnet wurde
        assertThat(cart.getValue()).isEqualTo(letter1.getValue()+letter2.getValue());
        //test ob negative values vorhanden sind
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
        CartManager testManager = new CartManager();
        ArrayList<Letter> sollTestArray = new ArrayList<>();
        Letter letter1 = new Letter('A', 20);
        sollTestArray.add(letter1);
        Cart cart = new Cart(sollTestArray);

        assertThat(cart.getBuchstabenIst().size()).isLessThanOrEqualTo(cart.getBuchstabenSoll().size());
        //assertThat(testManager.addLetterToCart('A', 1));
    }
}
