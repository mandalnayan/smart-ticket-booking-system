package com.iispl.movieticketsystem.pojos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
	
	private long id;
	private long totalSeats;
	private double amount;
	private String category;
	private LocalDateTime date;
	private boolean status;
	
	private static long generateId = 1;
	private String customerName;
	private List<Integer> seatNos;

	public Ticket(String customerName, double amount, long totalSeats, String category) {
		this.id = generateId++;
		this.customerName = customerName;
		this.amount = amount;
		this.totalSeats = totalSeats;
		this.category = category;
		this.date = LocalDateTime.now();
		this.seatNos = new ArrayList<>();
		this.status = true;
	}

	public long getId() {
		return id;
	}

	public List<Integer> getSeatNumbers() {
		return seatNos;
	}

	public void setSeatNumbers(List<Integer> seatNos) {
		this.seatNos = seatNos;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", Name=" + customerName + ", totalSeats=" + totalSeats + ", amount=" + amount + ", category=" + category
				+ ", date=" + date + ", status=" + status + ", seatNos=" + (seatNos.isEmpty() ? "Assigned Soon..!" : seatNos) + "]";
	}
	

}
