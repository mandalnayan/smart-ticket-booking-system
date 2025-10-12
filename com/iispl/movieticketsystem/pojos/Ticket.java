package com.iispl.movieticketsystem.pojos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
	
	private int id;
	private long totalSeats;
	private double amount;
	private String category;
	private LocalDateTime date;
	private long customerId;
	private boolean status;
	
	private String customerName;

	private List<Integer> seatNos;

	public Ticket(String customerName, long custId, double amount, long totalSeats, String category) {
		this.customerId = custId;
		this.customerName = customerName;
		this.amount = amount;
		this.totalSeats = totalSeats;
		this.category = category;
		this.date = LocalDateTime.now();
		this.seatNos = new ArrayList<>();
		this.status = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public List<Integer> getSeatNumbers() {
		return seatNos;
	}

	public long getTotalSets() {
		return totalSeats;
	}

	public void assignSeatNumbers(List<Integer> seatNos) {
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
