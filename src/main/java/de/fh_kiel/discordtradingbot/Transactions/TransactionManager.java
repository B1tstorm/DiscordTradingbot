package de.fh_kiel.discordtradingbot.Transactions;


import de.fh_kiel.discordtradingbot.Holdings.Cart;
import de.fh_kiel.discordtradingbot.Holdings.Inventory;
import de.fh_kiel.discordtradingbot.Holdings.Letter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TransferQueue;

public class TransactionManager {
	//todo final ???
	private static HashMap<Integer, Transaction> transactions = new HashMap<>();


	private Cart checkInventory(String input) {
		// TODO - implement TransactionManager.checkInventory
		throw new UnsupportedOperationException();
	}


	public void startTransaction(String traidingpartner,Integer eventId, Integer preis, String transactionKind, String product) {
		// TODO Test?
		//in auction/bid Fall
		if (transactionKind.equals("auction/start")){
			if (isProduktWorth(preis, product)){
				if (TransactionManager.transactions.get(eventId) != null){
				TransactionManager.transactions.get(eventId).bid(eventId);

				} else {
				Transaction auctionTransaction= new Transaction(traidingpartner,transactionKind);
				TransactionManager.transactions.put(eventId,auctionTransaction);
				auctionTransaction.bid(eventId);
				}
			}


		}
		// TODO - implement TransactionManager.startTransaction in sell Fall

	}

	//prüft ob produkt einen bestimmten Wert wert ist
	public Boolean isProduktWorth(Integer price, String product) {
		//ToDo Method is to be tested
		int tempTotalValue = 0;
		char[] char_arr = product.toCharArray();
		ArrayList<Letter> letterArray = Inventory.getLetters();
		//rechne gesamtWert vom product
		for (char c : char_arr) {
			//zugriff auf werte im Inventar Letter Array mit ascii index berechnung
			 tempTotalValue += letterArray.get((int)c-65).getValue();
		}

		// Der Fall wenn wir auf einen Buchstaben bieten können und der Buchstabe bei uns einen Wert von 0 hat, dann bieten wir nicht mit.
		if (tempTotalValue == 0) return false;
		// Der Fall wenn wir Buchstaben verkaufen und mehr oder gleichviel zahlen möchte (price), wie alle Buchstaben zusammen wert sind (tempTotalValue, dann true
		return tempTotalValue <= price;
		// TODO für später: falls tempTotalValue z.B. 5% mehr wäre als das Gebot, trotzem verkaufen
	}

	public void executeTransaction(Integer eventId, Integer price, String product){
		//TODO updateWallet und Letter Methoden sollen positiv und negativ sein!!
		//Inventory.updateWallet;(price)
		//Inventory.updateLetterAmount(product)
		TransactionManager.transactions.get(eventId).setStatus("executed");
	}

	public void dismissTransaction(Integer eventId){
		TransactionManager.transactions.get(eventId).setStatus("dismissed");
	}

	//TODO -implement update()
	public void update() {
		//Methode soll sich Infos vom ChannelInteractor holen (observer) wie
		// tradingPartner //price //transactionKind //eventId //Product
		//Je nach transactionKind wied eine Methode aufgerufen
		//kind kann folgends sein ?: auction start... auction versteigerung..... auction Ende/gewonnen...... seg will kaufen

		//if (transactionKind.equals("auction/start")){
		// isProduktWorth
		// wenn ja	startTransaction();
		//}

		//if (transactionKind.equals("auction/versteigern")){
		//  isProduktWorth
		//	transaction.bid();
		//}

		//if (transactionKind.equals("auction/ende oder gewonnen")){
		//	transaction ebschließen und falls gewonnen bezahlen und amount erhöhnen
		//}

		//if (transactionKind.equals("seg wanna buy ")){
		//  check Inventory
		//	startTransaction(); ohne bidder
		//}



	}
}