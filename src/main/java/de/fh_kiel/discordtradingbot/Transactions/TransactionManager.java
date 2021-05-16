package de.fh_kiel.discordtradingbot.Transactions;


import de.fh_kiel.discordtradingbot.Holdings.Cart;
import de.fh_kiel.discordtradingbot.Holdings.Letter;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionManager {

	private HashMap<Integer, Transaction> transactions;

	/**
	 *
	 * @param input
	 */
	private Cart checkInventory(String input) {
		// TODO - implement TransactionManager.checkInventory
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param traidingpartner
	 * @param Preis
	 * @param transactionKind
	 * @param product
	 */
	public Boolean startTransaction(String traidingpartner, Integer Preis, String transactionKind, String product) {
		// TODO - implement TransactionManager.startTransaction
		//wandel den produkt in letterArray
		ArrayList<Letter> letterArray = new ArrayList<>();


		Transaction transaction= new Transaction(traidingpartner,transactionKind);



		throw new UnsupportedOperationException();
	}

	public void operation() {
		// TODO - implement TransactionManager.operation
		throw new UnsupportedOperationException();
	}

}