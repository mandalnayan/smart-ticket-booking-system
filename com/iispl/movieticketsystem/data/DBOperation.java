package com.iispl.movieticketsystem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.iispl.movieticketsystem.pojos.Customer;

public class DBOperation {

    public static void readTable(String tableName) {
            try {
            	Connection connection = DBConnection.establishConnection();
                 // 3. Statement
                Statement statement = connection.createStatement();
            
                // 4. Query
                String readQuery = "Select * from " + tableName;

                // 5. Excute query
               ResultSet result = statement.executeQuery(readQuery);       
               ResultSetMetaData metaData = result.getMetaData();
               int columnCount = metaData.getColumnCount();
               if (!result.next()) {
                 System.out.println("No record found..!");
                 return;
               }
             do {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = result.getObject(i);
                    System.out.print(columnName + ": " + value + " | ");
                }
                System.out.print(Thread.currentThread().getName() + " \n");
            } while(result.next());


          } catch(SQLException ex) {
                ex.printStackTrace();
            }
    }
    
    public static void createTable(String tableName, String coloumns) {
        try {
            Connection connection = DBConnection.establishConnection();
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE " + tableName + " (Id int AUTO_INCREMENT Primary Key, " + coloumns +")";

            int isCreated = statement.executeUpdate(query);
            if (isCreated >= 0) {
                System.out.println(tableName + " table created successfully");
            }
       
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create Table query
    }

    public static void insertDate(Connection connection, String tableName) {
        String query = String.format("INSERT INTO %S(id, name, age) VALUES(?, ?, ?)", tableName);
        
        PreparedStatement pst;
        try {
            pst = connection.prepareStatement(query);
            for (int i = 1; i < 10; i++) {
                String mobNo = String.valueOf(ThreadLocalRandom.current().nextInt(1923, 98987));
                String name = "user-" + i;

                pst.setInt(1, ThreadLocalRandom.current().nextInt(100, 700));
                pst.setString(2, name);
                pst.setString(3, mobNo);
                pst.addBatch();
            }
            int rs[] = pst.executeBatch();
            if (rs.length > 0) {
                System.out.println(String.format("%d records has inserted successfully.", rs.length));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void insertCustomerData(Customer customer) {
    	Connection connection = DBConnection.establishConnection();
    	String query = String.format("INSERT INTO CUSTOMER(name, mobileNumber, status) VALUES(?, ?, ?)");
        
        PreparedStatement pst;
        try {
            pst = connection.prepareStatement(query);            
            String name = customer.getName();
            String mobNo = customer.getMobileNo();

            pst.setString(1, name);
            pst.setString(2, mobNo);
            pst.setBoolean(3, true);
            int rowsAffected = pst.executeUpdate();         
            
            if (rowsAffected > 0) {
                System.out.println(String.format("%s records has inserted successfully.", customer.getName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
