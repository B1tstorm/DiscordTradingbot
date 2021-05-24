package de.fh_kiel.discordtradingbot.Transactions;

import de.fh_kiel.discordtradingbot.Interaction.EventType;

public class Transaction {
	public static Integer IdCounter =0;
	private String id;
	private Integer cartId;
	private String traidingPartner;
	private Bidder bidder;
	private String status;
	private EventType transactionKind;

	public Transaction(EventType eventType) {
		this.id = (Transaction.IdCounter++).toString();
		this.transactionKind = eventType;
		this.status="waiting";
	}

	public void bid(String eventId, Integer price) {
		//* zum testen
		System.out.println("ich bidde gerade");
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

	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
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

	public Bidder getBidder() {
		return bidder;
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
}
