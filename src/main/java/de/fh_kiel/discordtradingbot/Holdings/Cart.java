package de.fh_kiel.discordtradingbot.Holdings;

import java.time.LocalTime;
import java.util.ArrayList;

public class Cart {

	private Integer ID;
	private LocalTime Time;
	private Integer value;
	private ArrayList<Letter> BuchstabenSoll;
	private ArrayList<Letter> BuchstabenIst;

	public Cart(LocalTime time, ArrayList<Letter> buchstabenSoll) {
		this.ID = CartManager.IdCounter++;
		Time = time;
		BuchstabenSoll = buchstabenSoll;
		this.value = 0;
		//berechne den gesamt value des carts
		for (Letter l : buchstabenSoll) {
			value += l.getValue();
		}
	}
}
