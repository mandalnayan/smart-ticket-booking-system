package com.iispl.movieticketsystem.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    /**
     * EstablishConnection
     * @param DB_NAME
     */
    public static Connection establishConnection(String DB_NAME) {
        String url = "jdbc:mysql://localhost:3306/" + DB_NAME;
        String user = "root";
        String password = "Nayan@2002";

        try {
            //1. load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Create connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("\n***** Database connected successfully..! " + DB_NAME);
            return connection;            
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
      
}
