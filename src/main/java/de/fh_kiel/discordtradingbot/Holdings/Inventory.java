package de.fh_kiel.discordtradingbot.Holdings;

import java.util.ArrayList;

public class Inventory {
	//singelton pattern
	private static Inventory inventory;

	private final ArrayList<Letter> letters = new ArrayList<>();
	private Integer wallet = 0;

	//bei der erstellung eines Objekt, wird das ArrayLetters erstellt und initialisiert
	private Inventory() {
		// Alle 26 Buchstaben von A - Z werden mittels ascii initial gespeichert
		for (int ascii = 65; ascii < 91; ascii++) {
		letters.add(new Letter((char)ascii, 0, 10));
		}
	}

	public static Inventory getInstance(){
		if (inventory == null){
			inventory = new Inventory();
		}
		return Inventory.inventory;
	}

	private Integer calculateValue(Letter letter) {
		// TODO - implement Inventory.calculateValue
		throw new UnsupportedOperationException();
	}

	//metode kann auch einen negativen price bekommen
	public void updateWallet(Integer price)   {
		this.wallet +=price;
	}

//	Vermindert oder erhöht den amount der Buchstaben im Array
	public void updateAmount( String eventType,String product){
		char[] buchstaben = product.toCharArray();
		ArrayList<Letter> letterArray = Inventory.getInstance().getLetters();
			for (char c : buchstaben) {
				if(eventType.equals("auction")){
					letterArray.get((int) c - 65).incrementAmount();
				}else{
					letterArray.get((int) c - 65).decrementAmount();
				}
			}
	}

	public void deposit() {
		// TODO - implement Inventory.deposit
		throw new UnsupportedOperationException();
	}

	public  ArrayList<Letter> getLetters() {
		return inventory.letters;
	}
	public Integer getWallet() {
		return wallet;
	}

}