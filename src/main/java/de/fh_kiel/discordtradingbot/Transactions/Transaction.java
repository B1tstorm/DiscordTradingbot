package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventType;


public class Transaction {
	public static Integer IdCounter =0;
	private String id;
	private Integer cartId;
	private String traidingPartner;
	private Integer price;
	private char[] product;
	private String status;
	private EventType eventType;

	public Transaction(EventType eventType) {
		this.id = (Transaction.IdCounter++).toString();
		this.eventType = eventType;
		this.status="waiting";
	}

	public Transaction(EventItem eventItem) {
		this.id = (Transaction.IdCounter++).toString();
		this.eventType = eventItem.getEventType();
		this.status="waiting";
		this.price = eventItem.getValue();
		this.product = eventItem.getProduct();
	}

	//getter and setter
	public Integer getPrice() {
		return price;
	}

	public char[] getProduct() {
		return product;
	}


	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;

	}



	public void setProduct(char[] product) {
		this.product = product;
	}
}
