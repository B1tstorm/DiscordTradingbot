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
        ArrayList<Letter> solltestArray = new ArrayList<>();
        Letter letter1 = new Letter('A', 20);
        Letter letter2 = new Letter('B', 10);
        solltestArray.add(letter1);
        solltestArray.add(letter2);
        LocalTime time = java.time.LocalTime.now();
        Cart cart = new Cart(time, solltestArray);

        testManager.createCart(time,solltestArray);

       // assertSame(testManager.getCarts().get(0), cart);
       // assertEquals(testManager.getCarts().get(0), cart);
        assertThat(testManager.getCarts().get(0).getBuchstabenSoll()).isEqualTo(solltestArray);
        assertThat(testManager.getCarts().get(0).getValue()).isEqualTo(cart.getValue());
        //assertThat(testManager.createCart(time,solltestArray).getCats,)
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
        Cart cart = new Cart(java.time.LocalTime.now(), sollTestArray);

        assertThat(cart.getBuchstabenIst().size()).isLessThanOrEqualTo(cart.getBuchstabenSoll().size());
        //assertThat(testManager.addLetterToCart('A', 1));
    }
}