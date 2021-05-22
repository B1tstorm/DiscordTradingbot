package de.fh_kiel.discordtradingbot.Holdings;

import java.util.ArrayList;

public class Inventory {

	private static Inventory inventory;

	private  ArrayList<Letter> letters = new ArrayList<>();



	private Integer wallet;

	//bei der erstellung eines Objekt, wird das ArrayLetters erstellt und befüllt
	private Inventory() {
		// Alle 26 Buchstaben von A - Z werden mittels ascii initial gespeichert
		for (int ascii = 65; ascii < 91; ascii++) {
		letters.add(new Letter((char)ascii, 0, 10));
		}
	}

	public static Inventory getInstance(){
		if (Inventory.inventory == null){
			Inventory.inventory = new Inventory();
		}
		return Inventory.inventory;
	}

	private Integer calculateValue(Letter letter) {
		// TODO - implement Inventory.calculateValue
		throw new UnsupportedOperationException();
	}


	public void updateWallet(Integer amount)   {
		this.wallet +=amount;
//		if (this.wallet<0) throw new Exception("Wallet kann nicht Nigativ werden");
	}

	public void deposit() {
		// TODO - implement Inventory.deposit
		throw new UnsupportedOperationException();
	}

	public  ArrayList<Letter> getLetters() {
		return this.letters;
	}
	public Integer getWallet() {
		return wallet;
	}

}