package com.iispl.movieticketsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.iispl.movieticketsystem.display.Display;
import com.iispl.movieticketsystem.pojos.Ticket;
import com.iispl.movieticketsystem.services.TicketBookingServices;

public class ExecutorServiceFramework {
    public static void main(String[] args) {
        newFixedThreadPool();
    }

    public static void newFixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
 
        List<Future<Ticket>> tickets = new ArrayList<>();

        final int NUMBER_OF_REQUEST = 6;

        for (int i = 1; i <= NUMBER_OF_REQUEST; i++) {
            Callable<Ticket> request = () -> {
                return TicketBookingServices.bookTicket();
            };
            tickets.add(executorService.submit(request));
        }       

        tickets.forEach((var ticket) -> {
            try {
                Ticket t1 = ticket.get();
                if (t1 != null) {
                   Display.print(t1);
               }
            } catch(ExecutionException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        executorService.shutdown();

    }
}
