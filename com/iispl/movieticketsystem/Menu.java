package com.iispl.movieticketsystem;

import java.util.Scanner;

import com.iispl.movieticketsystem.data.DBOperation;
import com.iispl.movieticketsystem.services.TicketBookingServices;

public class Menu {

	private final static String starsPrint = "*************";
	private final static String newLine = "\n";
	
	public static void showMenu() {
		System.out.println(newLine + starsPrint + "Please choose options" + starsPrint);
		System.out.println("***** 1. Book ticket     2. Show ticket");
		
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		
		switch(choice) {
			case 1:
				TicketBookingServices.bookTicket();
				break;
			default:
				DBOperation.readTable("CUSTOMER");
		}
		
		
	}
}
