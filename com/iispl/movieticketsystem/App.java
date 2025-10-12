package com.iispl.movieticketsystem;

import com.iispl.movieticketsystem.data.DBOperation;

public class App {
   	public static void main(String args[]) {    
       // DBOperation.readTable("Customer");
        Menu.showMenu();
      //  DBOperation.readTable("CustomerTicket");
        DBOperation.readTable("Seat");
    }

    public static void threadPoolBlock() {
        

    }
	
  
    
}
 