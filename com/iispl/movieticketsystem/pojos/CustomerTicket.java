package com.iispl.movieticketsystem.pojos;

public class CustomerTicket {
    private static int ticketId;
    private static int seatNo;

    public CustomerTicket(int ticketId) {
        this.ticketId = ticketId;
    }

    public static int getTicketId() {
        return ticketId;
    }

    public static void setTicketId(int ticketId) {
        CustomerTicket.ticketId = ticketId;
    }

    public static int getSeatNo() {
        return seatNo;
    }

    public static void setSeatNo(int seatNo) {
        CustomerTicket.seatNo = seatNo;
    }
    
    
}
