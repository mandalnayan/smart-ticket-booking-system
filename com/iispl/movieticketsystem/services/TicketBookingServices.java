package com.iispl.movieticketsystem.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.iispl.movieticketsystem.data.DBOperation;
import com.iispl.movieticketsystem.display.Display;
import com.iispl.movieticketsystem.exceptions.FailedTicketBookingException;
import com.iispl.movieticketsystem.pojos.Customer;
import com.iispl.movieticketsystem.pojos.Ticket;
import com.iispl.movieticketsystem.pojos.TicketBooking;
import com.iispl.movieticketsystem.pojos.Transaction;

public class TicketBookingServices {

	// Total number of seats
	private static final int TOTAL_SEATS = 50;

	// Available
	private static int availableSeats = TOTAL_SEATS;

	// Booked SeatNo

	// Mapping ticket-type with Price
	private static Map<String, Double> ticket_Prices = new HashMap<>();
	static {
		ticket_Prices.put("Prime", 400.0);
		ticket_Prices.put("Diamond", 200.0);
	}

	//private Map<Integer, Ticket> ticketInfo = new HashMap<>();

	//private boolean seats[] = new boolean[TOTAL_SEATS];

	private static List<Transaction> transactions = new ArrayList<>();

	public static Ticket bookTicket() {
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter your name");
		String name = input.nextLine();
		
		System.out.println("Enter number of tickets");
		int numberOfTickets = Integer.parseInt(input.nextLine());
		
		System.out.println("Enter mobile number");
		String mobNo = input.nextLine();
		
//		Saving customer data
		Customer customer = new Customer(name, mobNo);
		DBOperation.insertCustomerData(customer);
		int attempts = 2;
		List<String> ticketsTypes = new ArrayList<>(ticket_Prices.keySet());
		String seatType = "Diamond";
		while(attempts-- > 0) {
			
			System.out.println("\nTypes of ticket.");
			int i = 1;
			for(String type : ticketsTypes) {
				System.out.printf("Press %d for %s | ", i++, type);				
			}
			
			int type = input.nextInt();
			if (type < 1 || type > ticketsTypes.size()) {
				System.out.println("\n Invalide ticket type. Please choose correct type");
				continue;
			}
			seatType = ticketsTypes.get(type-1);
			break;
			
	}
		return bookTicket(numberOfTickets, seatType, customer.getName());		
	}
	/**
	 * Booking ticket
	 * 
	 * @return
	 */
	public static Ticket bookTicket(int numberOfTicket, String seatType, String customerName) {
		try {
			synchronized (TicketBookingServices.class) {
				// Checking availability of ticket
				if (availableSeats < numberOfTicket) {
					throw new FailedTicketBookingException(
							String.format("%s : %d seats are not available to book. Available seats %d\n",
									customerName, numberOfTicket, availableSeats));
				}

				// Reduce number of available ticket
				availableSeats -= numberOfTicket;
			}
			double totalAmount = calculateTotalAmount(numberOfTicket, seatType);
			boolean paymentStatus = doPayment(totalAmount, "Booking", "UPI");

			TicketBooking booked = null;
			if (paymentStatus) {
				synchronized (TicketBookingServices.class){
				Display.print(customerName + " : "+ numberOfTicket + " Ticket boooked successfully.  Total Price: - " + totalAmount + "\nfrom " + Thread.currentThread().getName()
						+ " Remaining seats: " + availableSeats + "\n");
				}
				
				booked = new TicketBooking("success", true);
				Ticket ticket = new Ticket(customerName, totalAmount, numberOfTicket, seatType);

				// Assign seat No... Need to implement
				// ticket.setSeatNumbers(assignSeats(numberOfTicket, seatType));
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

	// Assign seat to specifc ticket
	private List<Integer> assignSeat(int numberOfTicket, String type) {
		return null;
	}

	public static double calculateTotalAmount(int numberOTicket, String type) {
		double price = ticket_Prices.get(type);

		return numberOTicket * price;
	}

	public static boolean doPayment(double amount, String type, String mode) {
		return transactions.add(new Transaction(amount, type, mode, true));
	}

	public static int bookedTicket(int numberOfTicket) {
		return TOTAL_SEATS - availableSeats;
	}

	public static void setTicketPrice(String name, double price) {
		ticket_Prices.put(name, price);
	}

	public static int availableSeats() {
		return availableSeats;
	}
}
