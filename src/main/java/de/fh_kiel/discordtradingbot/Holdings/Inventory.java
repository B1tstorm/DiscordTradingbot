package de.fh_kiel.discordtradingbot.Holdings;

import java.util.ArrayList;

public class Inventory {



	private static ArrayList<Letter> letters = new ArrayList<>();
	private Integer wallet;

	public Inventory() {
		// Alle 26 Buchstaben von A - Z werden mittels ascii initial gespeichert
		for (int ascii = 65; ascii < 91; ascii++) {
			Inventory.letters.add(new Letter((char)ascii, 0, 1));
		}
	}

	/**
	 * 
	 * @param letter
	 */
	private Integer calculateValue(Letter letter) {
		// TODO - implement Inventory.calculateValue
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param amount
	 */
	public Boolean updateWallet(Integer amount) {
		// TODO - implement Inventory.updateWallet
		throw new UnsupportedOperationException();
	}

	public void deposit() {
		// TODO - implement Inventory.deposit
		throw new UnsupportedOperationException();
	}

	public static ArrayList<Letter> getLetters() {
		return letters;
	}

}