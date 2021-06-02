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
	private EventType transactionKind;

	public Transaction(EventType eventType) {
		this.id = (Transaction.IdCounter++).toString();
		this.transactionKind = eventType;
		this.status="waiting";
	}

	public Transaction(EventItem eventItem) {
		this.id = (Transaction.IdCounter++).toString();
		this.transactionKind = eventItem.getEventType();
		this.status="waiting";
		this.price = eventItem.getValue();
		this.product = eventItem.getProduct();
	}

	//getter and setter
	public void setId(String id) {
		this.id = id;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public void setTraidingPartner(String traidingPartner) {
		this.traidingPartner = traidingPartner;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public Integer getCartId() {
		return cartId;
	}

	public String getTraidingPartner() {
		return traidingPartner;
	}

	public Integer getPrice() {
		return price;
	}

	public char[] getProduct() {
		return product;
	}

	public String getStatus() {
		return status;
	}

	public EventType getTransactionKind() {
		return transactionKind;
	}

	public void setTransactionKind(EventType transactionKind) {
		this.transactionKind = transactionKind;

	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setProduct(char[] product) {
		this.product = product;
	}
}
