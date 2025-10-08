package com.iispl.movieticketsystem;

import java.sql.Connection;
import java.util.List;

import com.iispl.movieticketsystem.data.CustomerData;
import com.iispl.movieticketsystem.data.DBConnection;
import com.iispl.movieticketsystem.data.DBOperation;
import com.iispl.movieticketsystem.pojos.Customer;
import com.iispl.movieticketsystem.pojos.Ticket;
import com.iispl.movieticketsystem.services.TicketBookingServices;

public class App {
   	public static void main(String args[]) {

       final String DB_NAME1 = "ImageInfoDB";
       final String DB_NAME2 = "ticket_booking";
        Thread thread1 = new Thread(() -> {
            Connection conn1 = DBConnection.establishConnection(DB_NAME1);
            //DBOperation.insertDate(conn1, "Employee");        
            DBOperation.readTable(conn1, "Employee");
        });
        Thread thread2 = new Thread(()-> {
            Connection conn2 = DBConnection.establishConnection(DB_NAME2);
            DBOperation.readTable(conn2, "Customer");            
        });
	
        thread1.start();
        thread2.start();
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
		System.out.println(ticket);
	}
	
    
}
 