package com.iispl.movieticketsystem;

import java.util.List;

import com.iispl.movieticketsystem.data.CustomerData;
import com.iispl.movieticketsystem.data.DBOperation;
import com.iispl.movieticketsystem.pojos.Customer;
import com.iispl.movieticketsystem.pojos.Ticket;
import com.iispl.movieticketsystem.services.TicketBookingServices;

public class App {
   	public static void main(String args[]) {

        String columns = "totalSeats int, category varchar(20), amount double(15, 2), date DATETIME Default CURRENT_TIMESTAMP, status boolean";

       // DBOperation.createTable("Ticket", columns);
        DBOperation.readTable("Ticket");
        // Thread thread1 = new Thread(() -> {
        //     Menu.showMenu();
        // });
       
	
       // thread1.start();
    }

    public static void threadPoolBlock() {
        
    }
	
    public static void bookingByMultithreading() {
        Runnable r1 = () -> {
                book();
            };
            
            Runnable r2 = () -> {
                book();
            };
            
            Runnable r3 = () -> {
                book();
            };
            Thread counterA = new Thread(r1, "CounterA");
            Thread counterB = new Thread(r2, "CounterB");
            Thread onlineMode = new Thread(r3, "CounterC");

            counterA.start();
            counterB.start();
            onlineMode.start();	
    }

	public synchronized static void book() {
		List<Customer> customers = CustomerData.getCustomers();
		Customer customer = customers.get(0);
		String type = "Diamond";
		int totalNoOfTickets = (int)(Math.random() * 50) + 1;
//		Book the ticket
		Ticket ticket = TicketBookingServices.bookTicket(totalNoOfTickets, type, customer.getName());

		// Display the ticket
	//	System.out.println(ticket);
	}
	
    
}
 