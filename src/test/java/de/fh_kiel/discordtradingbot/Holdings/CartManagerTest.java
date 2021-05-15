package de.fh_kiel.discordtradingbot.Holdings;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartManagerTest {

    @Test
    void createCart() {
        CartManager testManager = new CartManager();
        ArrayList<Letter> solltestArray = new ArrayList<>();
        Letter letter1 = new Letter('A', 20);
        Letter letter2 = new Letter('B', 10);
        solltestArray.add(letter1);
        solltestArray.add(letter2);
        Cart cart = new Cart(testManager.getIdCounter(), java.time.LocalTime.now(), solltestArray);
        assertThat(solltestArray).isNotEmpty();

    }

    @Test
    void deleteCart() {
    }

    @Test
    void addLetterToCart() {
    }
}