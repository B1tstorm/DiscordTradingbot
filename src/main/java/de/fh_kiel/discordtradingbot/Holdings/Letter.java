package de.fh_kiel.discordtradingbot.Holdings;

import java.util.HashMap;

public class Letter {

	private Character letter;
	private Integer amount;

	public Integer getValue() {
		return value;
	}

	private Integer value;

	public Letter(Character letter, Integer value) {
		this.letter = letter;
		this.value = value;
	}
}