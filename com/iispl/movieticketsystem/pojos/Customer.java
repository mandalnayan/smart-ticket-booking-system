package com.iispl.movieticketsystem.pojos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer {
	private long id;
	private String name;
	private String mobileNo;
	private String email;
	private LocalDateTime createdDate;
	
	private static long generateId = 1;
	
	List<Ticket> allTicket;

	public Customer(String name, String mobileNo, String email) {
		this.id = generateId++;
		this.name = name;
		this.mobileNo = mobileNo;
		this.email = email;
		this.createdDate = LocalDateTime.now();
		this.allTicket = new ArrayList<>();
	}

	public Customer(String name, String mobileNo) {
		this(name, mobileNo, "");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public List<Ticket> getAllTicket() {
		return allTicket;
	}

	public void setAllTicket(List<Ticket> allTicket) {
		this.allTicket = allTicket;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", mobileNo=" + mobileNo + ", email=" + email
				+ ", createdDate=" + createdDate + ", allTicket=" + allTicket + "]";
	}	
	
}
