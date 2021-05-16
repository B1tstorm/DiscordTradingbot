package de.fh_kiel.discordtradingbot.Transactions;

public class Transaction {
	public static Integer IdCounter =0;
	private Integer id;
	private Integer cartId;
	private String traidingPartner;
	private Bidder bidder;
	private String status;
	private String transactionKind;




	public Transaction(String traidingpartner,String transactionKind) {
		this.id = Transaction.IdCounter++;
		this.transactionKind = transactionKind;
		this.traidingPartner = traidingpartner;
	}

	//getter and setter
	public void setId(Integer id) {
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

	public Integer getId() {
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
	public void setTransactionKind(String transactionKind) {
		this.transactionKind = transactionKind;
	}

	public String getTransactionKind() {
		return transactionKind;
	}
}
