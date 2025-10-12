package com.iispl.movieticketsystem.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.iispl.movieticketsystem.data.DBConnection;
import com.iispl.movieticketsystem.data.DBOperation;
import com.iispl.movieticketsystem.display.Display;
import com.iispl.movieticketsystem.exceptions.FailedTicketBookingException;
import com.iispl.movieticketsystem.pojos.Customer;
import com.iispl.movieticketsystem.pojos.CustomerTicket;
import com.iispl.movieticketsystem.pojos.Ticket;
import com.iispl.movieticketsystem.pojos.TicketBooking;
import com.iispl.movieticketsystem.pojos.Transaction;

public class TicketBookingServices {

	// Total number of seats
	private static final int TOTAL_SEATS = 50;

	// Booked SeatNo

	// Mapping ticket-type with Price
	private static Map<String, Double> ticket_Prices = new HashMap<>();
	static {
		ticket_Prices.put("Prime", 400.0);
		ticket_Prices.put("Diamond", 600.0);
	}

	// private boolean seats[] = new boolean[TOTAL_SEATS];

	private static List<Transaction> transactions = new ArrayList<>();

	/*
	 * Method to book ticket and save to database
	 */
	public static void bookTicket() {
		Customer customer = null;
		Scanner input = new Scanner(System.in);
		try {
			System.out.println("Enter your name");
			String name = input.nextLine();
			System.out.println("Enter mobile number");
			String mobNo = input.nextLine();
			System.out.println("Enter number of tickets");
			int numberOfTickets = Integer.parseInt(input.nextLine());

			// Creating Customer object
			customer = new Customer(name, mobNo);

			try (Connection connection = DBConnection.establishConnection();) {
				connection.setAutoCommit(false);
				// Saving customer data
				if (!DBOperation.insertCustomerData(customer)) {
					System.out.println("Failed to insert data");
				} else {
					// DBOperation.read
					int attempts = 2;
					List<String> ticketsTypes = new ArrayList<>(ticket_Prices.keySet());
					String seatType = "Diamond";
					while (attempts-- > 0) {

						System.out.println("\nTypes of ticket.");
						int i = 1;
						for (String type : ticketsTypes) {
							System.out.printf("Press %d for %s | ", i++, type);
						}

						int type = input.nextInt();
						if (type < 1 || type > ticketsTypes.size()) {
							System.out.println("\n Invalide ticket type. Please choose correct type");
							continue;
						}
						seatType = ticketsTypes.get(type - 1);
						break;
					}
					Ticket ticket = bookTicket(numberOfTickets, seatType, customer);
					if (ticket != null) {
						if (DBOperation.insertTicket(connection, ticket)
								&& DBOperation.assignSeatNo(connection, ticket, numberOfTickets)) {
							connection.commit();
							System.out.println(ticket);
						} else {
							System.out.println("Sorry failed to book ticket..");
						}
					} else {
						System.out.println("Sorry failed to book ticket..");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (NoSuchElementException ex) {
			Display.printAlert("Server error..! Please come after some time");
			return;
		}
	}

	/**
	 * Check the available of ticket, generate new ticket
	 * 
	 * @param numberOfTicket
	 * @param seatType
	 * @param customer
	 * @return
	 */
	public static Ticket bookTicket(int numberOfTicket, String seatType, Customer customer) {
		try {
			String customerName = customer.getName();
			synchronized (TicketBookingServices.class) {
				int availableSeats = getAvailableSeats();
				// Checking availability of ticket
				if (availableSeats < numberOfTicket) {
					throw new FailedTicketBookingException(
							String.format("%s : %d seats are not available to book. Available seats %d\n",
									customerName, numberOfTicket, availableSeats));
				}

				// Reduce number of available ticket
				availableSeats -= Math.min(numberOfTicket, availableSeats);
			}
			double totalAmount = calculateTotalAmount(numberOfTicket, seatType);
			boolean paymentStatus = doPayment(totalAmount, "Booking", "UPI");

			TicketBooking booked = null;
			if (paymentStatus) {
				synchronized (TicketBookingServices.class) {
					Display.printMessage(
							customerName + " : " + numberOfTicket + " Ticket boooked successfully.  Total Price: - "
									+ totalAmount + "\nfrom " + Thread.currentThread().getName());
				}

				booked = new TicketBooking("success", true);
				Ticket ticket = new Ticket(customerName, customer.getId(), totalAmount, numberOfTicket, seatType);
				CustomerTicket customerTicket = new CustomerTicket(ticket.getId());

				// Assign seat No...
				return ticket;
			} else {
				booked = new TicketBooking("failed", false);
				throw new FailedTicketBookingException(String.format(
						"%s = Payment was not successful. Please try again..!", Thread.currentThread().getName()));
			}
		} catch (FailedTicketBookingException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public static void cancleTicket(Scanner input) {
		System.out.println("Enter Ticket Id");
		int ticketId = input.nextInt();
		System.out.println("Enter No of tickets you want to cancell");
		int numberOfTickets = Integer.parseInt(input.next());

		if (ticketId != -1) {
			String message = "Ticket ID: " + ticketId;
			message += DBOperation.deleteTicketById(ticketId, numberOfTickets) ? " has cancelled successfully!"
					: " is invalid or Doesn't exit";
			Display.printMessage(message);
		}
	}

	/**
	 * Calculate Total amount for ticket
	 * 
	 * @param numberOTicket
	 * @param type
	 * @return
	 */
	public static double calculateTotalAmount(int numberOTicket, String type) {
		double price = ticket_Prices.get(type);

		return numberOTicket * price;
	}

	/**
	 * Handle transaction
	 * 
	 * @param amount
	 * @param type
	 * @param mode
	 * @return
	 */
	public static boolean doPayment(double amount, String type, String mode) {
		return transactions.add(new Transaction(amount, type, mode, true));
	}

	/**
	 * Showing ticket from ticket id
	 */
	public static void showTicket(int ticketId) {
		DBOperation.showTicketByID(ticketId);
	}

	/**
	 * 
	 * @param numberOfTicket
	 * @return
	 */
	public static void setTicketPrice(String name, double price) {
		ticket_Prices.put(name, price);
	}

	/**
	 * Fetch available seats from database
	 */
	public static int getAvailableSeats() {
		return DBOperation.availableSeats();
	}

	/**
	 * Take ticket id input from user
	 * 
	 * @return
	 */
	private static int getTicketIdFromUser() {
		try (Scanner input = new Scanner(System.in);) {
			System.out.println("Enter ticket id");
			int ticketId = input.nextInt();
			return ticketId;
		} catch (NumberFormatException ex) {
			Display.printAlert(ex.getMessage());
			return -1;
		}
	}
}
