package de.fh_kiel.discordtradingbot.Holdings;

import java.util.ArrayList;

public class CartManager {

	///für cart ID vergabe. wird jedes mal inkrementiert
	private Integer IdCounter = 0;
	private ArrayList<Cart> carts;

	public void createCart() {
		// TODO - implement CartManager.createCart
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param cartID
	 */
	public void deleteCart(Integer cartID) {
		// TODO - implement CartManager.deleteCart
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param letter
	 * @param cartID
	 */
	public void addLetterToCart(Character letter, Integer cartID) {
		// TODO - implement CartManager.addLetterToCart
		throw new UnsupportedOperationException();
	}
	public Integer getIdCounter() {
		return IdCounter;
	}
}