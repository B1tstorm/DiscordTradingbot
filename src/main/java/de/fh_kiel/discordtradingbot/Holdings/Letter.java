package de.fh_kiel.discordtradingbot.Holdings;



public class Letter {

	private final Character letter;
	private Integer amount;
	private Integer value;


	public Character getLetter() {
		return letter;
	}

	public Integer getValue() {
		return value;
	}

	public Letter(Character letter, Integer amount,Integer value) {
		this.letter = letter;
		this.amount = amount;
		this.value = (value<0)? 0 : value;
	}

	public void incrementAmount() {
		this.amount++;
	}

	public void decrementAmount() {
		this.amount--;
		//TODO amount soll nicht nigativ werden (exeption schreiben)
	}

	public Integer getAmount() {
		return amount;
	}
}