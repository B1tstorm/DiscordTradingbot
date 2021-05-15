package de.fh_kiel.discordtradingbot.Holdings;

import java.time.LocalTime;
import java.util.ArrayList;

public class CartManager {

	///für cart ID vergabe. wird jedes mal inkrementiert
	public static Integer IdCounter = 0;



	private ArrayList<Cart> carts = new ArrayList<>();

	public void createCart(LocalTime time, ArrayList<Letter> buchstabenSoll) {
		// TODO - implement CartManager.createCart
		//throw new UnsupportedOperationException();
		this.carts.add(new Cart (time, buchstabenSoll));
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
	public ArrayList<Cart> getCarts() {
		return carts;
	}


}