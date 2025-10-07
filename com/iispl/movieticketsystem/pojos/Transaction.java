package com.iispl.movieticketsystem.pojos;

public class Transaction {

	private String id;
	private double amount;
	private String type;  // Refund / complete
	private String mode;  // Cash / Upi / cheque
	private boolean success;
	
	private long generateNewId = 123;
	public Transaction(double amount, String type, String mode, boolean success) {
		super();
		this.id = mode + generateNewId++;
		this.amount = amount;
		this.type = type;
		this.mode = mode;
		this.success = success;
	}
	
	public String getId() {
		return id;
	}

	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	

	@Override
	public String toString() {
		return "\nTransaction [id=" + id + ", amount=" + amount + ", type=" + type + ", mode=" + mode + " Success= "+ success + "]";
	}
}

