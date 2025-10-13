package com.iispl.movieticketsystem.pojos;

public class CustomerTicket {
    private int ticketId;
    private int seatNo;

    public CustomerTicket(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public 
     void setTicketId(int ticketId) {
        ticketId = ticketId;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        seatNo = seatNo;
    }
    
    
}
