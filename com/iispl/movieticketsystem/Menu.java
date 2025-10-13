package com.iispl.movieticketsystem;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.iispl.movieticketsystem.display.Display;
import com.iispl.movieticketsystem.services.TicketBookingServices;

public class Menu {

	private final static String starsPrint = "*************************************";
	private final static String newLine = "\n";

	public static void showMenu() {

		Scanner input = new Scanner(System.in);
		menu: while (true) {
			System.out.println(newLine + starsPrint + "| Please choose options " + starsPrint);
			System.out.println("\t\t 1. Book ticket  |  2. Show ticket  |  3. Cancle Ticket  |  4. To know available Ticket	|  5. Exit");
			int choice = 0;
			try  {
				choice = Integer.parseInt(input.nextLine());			

			switch (choice) {
				case 1:
					TicketBookingServices.bookTicket(input);
					break;
				case 2:
					System.out.println("Enter ticket id");
					int ticketId = Integer.parseInt(input.nextLine());
					TicketBookingServices.showTicket(ticketId);
					break;
				case 3:
					TicketBookingServices.cancleTicket(input);

					break;
				case 4:
					int availableSeats = TicketBookingServices.getAvailableSeats();
					Display.printMessage("Available seats: " + availableSeats);
					break;
				case 5:
					break menu;
				default:
					Display.printWarning("Invalid Choice.. Please choose correct options");
			}
		}
		 catch (NumberFormatException | NoSuchElementException ex) {
				Display.printAlert(ex.getMessage());
			}
		}
		input.close();
	}
}
