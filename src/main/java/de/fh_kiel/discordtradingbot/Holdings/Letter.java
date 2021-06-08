package de.fh_kiel.discordtradingbot.Holdings;


/**
 * ein Letter stellt einen Buchstaben dar:
 * der LETTER hat 3 variablen:
 * letter: der buchstabe selbst, Amount, die vorhandene Anzahl im Inventar und Value: interner Wert des Buchstaben
 */
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

	public void setValue(Integer value) {
		this.value = value;
	}

	public Letter(Character letter, Integer amount, Integer value) {
		this.letter = letter;
		this.amount = amount;
		this.value = (value<0)? 0 : value;
	}

	/**
	 * erhöht die Anzahl eies Buchstaben
	 */
	public void incrementAmount() {
		this.amount++;
	}

	/**
	 * erniedrigt die Anzahl eies Buchstaben
	 */
	public void decrementAmount() {
		this.amount--;
		//TODO amount soll nicht nigativ werden (exeption schreiben)
	}

	public Integer getAmount() {
		return amount;
	}
}