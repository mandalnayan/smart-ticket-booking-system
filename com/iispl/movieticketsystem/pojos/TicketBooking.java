package com.iispl.movieticketsystem.pojos;

import java.time.LocalDateTime;

public class TicketBooking {
	private String id;
	private String paymentStatus;
	private LocalDateTime date;
	private boolean status;	
	
	private static long getNewId = 1;
	
	public TicketBooking(String paymentStatus, boolean status) {
		this.id = "BookMyShow_" + getNewId++;
		this.paymentStatus = paymentStatus;
		this.date = LocalDateTime.now();
		this.status = status;		
	}

	public String getId() {
		return id;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getDate() {
		return date;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
