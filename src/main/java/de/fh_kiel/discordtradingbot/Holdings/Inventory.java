package de.fh_kiel.discordtradingbot.Holdings;

import de.fh_kiel.discordtradingbot.Analysis.LetterListener;
import de.fh_kiel.discordtradingbot.Config;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.ZuluBot;

import java.util.ArrayList;

public class Inventory implements EventListener, LetterListener {

	private final ArrayList<Letter> letters = new ArrayList<>();
	private Integer wallet;
	private final ZuluBot bot;

	//todo delete?
	public void setWallet(Integer wallet) {
		this.wallet = wallet;
	}


	//bei der erstellung eines Objekt, wird das ArrayLetters erstellt und initialisiert
	public Inventory(ZuluBot bot) {
		this.bot = bot;
		for (int i = 0; i < 26; i++) {
			//todo amount init auf 0
		letters.add(new Letter(Config.indexToChar(i), 10, (int) ((Config.staticLetterValues[i] * 5) + 5)));
		}
	}

	@Override
	public void update(Letter l, EventType source) {
		if (source != EventType.EVALUATION) return;

		letters.get(Config.charToIndex(l.getLetter())).setValue(l.getValue());
	}

	//metode kann auch einen negativen price bekommen
	public void updateWallet(Integer price)   {
		this.wallet +=price;
	}

//	Vermindert oder erhöht den amount der Buchstaben im Array
	public void updateLetterAmount(EventType eventType, char[] product){
		ArrayList<Letter> letterArray = this.getLetters();
			for (char c : product) {
				if(eventType == EventType.BUY_CONFIRM || eventType == EventType.ZULU_CONFIRM){
					letterArray.get((int) c - 65).decrementAmount();
				}else{
					letterArray.get((int) c - 65).incrementAmount();
				}
			}
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

	@Override
	public void update(EventItem eventItem) {
		StringBuilder sb;
		switch (eventItem.getEventType()) {
			case INVENTORY:
				sb = new StringBuilder("My current Inventory is : \n");
				for (int i = 0; i < 26; i++) {
					sb.append(this.letters.get(i).getLetter().toString()).append(" - ").append(this.letters.get(i).getAmount()).append(" pieces - valued at ").append(this.letters.get(i).getValue()).append("\n");
				}
				break;
			case WALLET:
				sb = new StringBuilder("My current buying power is : \n");
				sb.append(this.wallet);
				break;
			default:
				return;
		}
		bot.getChannelInteracter().writeThisMessage(sb.toString(), eventItem.getChannel());
	}
}