package de.fh_kiel.discordtradingbot.Holdings;

import java.time.LocalTime;
import java.util.ArrayList;

public class Cart {

	private Integer ID;
	private LocalTime Time;
	private Integer value;
	private ArrayList<Letter> BuchstabenSoll;
	private ArrayList<Letter> BuchstabenIst;

	public Cart(Integer ID, LocalTime time, ArrayList<Letter> buchstabenSoll) {
		this.ID = ID;
		Time = time;
		BuchstabenSoll = buchstabenSoll;
		this.value = 0;
		//berechne den gesamt value des carts
		for (Letter l : buchstabenSoll) {
			value += l.getValue();
		}
	}
}
