package de.fh_kiel.discordtradingbot.Holdings;

import java.time.LocalTime;
import java.util.ArrayList;

public class Cart {

	private Integer id;
	private LocalTime time;
	private Integer value;
	private ArrayList<Letter> buchstabenSoll;
	private ArrayList<Letter> buchstabenIst;


	public Cart(LocalTime time, ArrayList<Letter> buchstabenSoll) {
		this.id = CartManager.IdCounter++;
		this.time = time;
		this.buchstabenSoll = buchstabenSoll;
		this.value = 0;
		//berechne den gesamt value des carts
		for (Letter l : buchstabenSoll) {
			value += l.getValue();
		}
	}

	public ArrayList<Letter> getBuchstabenSoll() {
		return buchstabenSoll;
	}

	public ArrayList<Letter> getBuchstabenIst() {
		return buchstabenIst;
	}

	public void setBuchstabenIst(ArrayList<Letter> buchstabenIst) {
		this.buchstabenIst = buchstabenIst;
	}

	public Integer getId() {
		return id;
	}

	public LocalTime getTime() {
		return time;
	}

	public Integer getValue() {
		return value;
	}
}
