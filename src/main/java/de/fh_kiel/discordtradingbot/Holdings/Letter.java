package de.fh_kiel.discordtradingbot.Holdings;

import java.util.HashMap;

public class Letter {

	private final Character letter;
	private Integer amount;
	private Integer value;


	public Letter(Character letter, Integer amount,Integer value) {
		this.letter = letter;
		this.amount = amount;
		this.value = (value<0)? 0 : value;
	}

	public Character getLetter() {
		return letter;
	}

	public Integer getValue() {
		return value;
	}


}