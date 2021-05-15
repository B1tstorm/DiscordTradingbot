package de.fh_kiel.discordtradingbot.Holdings;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartManagerTest {

    @Test
    void createCart() {
        CartManager testManager = new CartManager();
        ArrayList<Letter> sollTestArray = new ArrayList<>();
        Letter letter1 = new Letter('A', 20);
        Letter letter2 = new Letter('B', 10);
        sollTestArray.add(letter1);
        sollTestArray.add(letter2);
        Cart cart = new Cart(testManager.getIdCounter(), java.time.LocalTime.now(), sollTestArray);
        assertThat(sollTestArray).isNotEmpty();

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
        Cart cart = new Cart(testManager.getIdCounter(), java.time.LocalTime.now(), sollTestArray);

        assertThat(cart.getBuchstabenIst().size()).isLessThanOrEqualTo(cart.getBuchstabenSoll().size());
        //assertThat(testManager.addLetterToCart('A', 1));
    }
}