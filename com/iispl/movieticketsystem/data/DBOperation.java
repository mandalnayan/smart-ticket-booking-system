package com.iispl.movieticketsystem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import com.iispl.movieticketsystem.display.Display;
import com.iispl.movieticketsystem.pojos.Customer;
import com.iispl.movieticketsystem.pojos.Ticket;
import com.iispl.movieticketsystem.services.TicketBookingServices;

public class DBOperation {

    public static void deleteRecords(String tableName) {
        Connection connection = DBConnection.establishConnection();

        String query = "Delete from " + tableName + " where status = 0";

        try {
            Statement statement = connection.createStatement();
            int effectedRows = statement.executeUpdate(query);
            if (effectedRows > 0) {
                System.out.println(effectedRows + " records has deleted!");
            } else {
                System.out.println("No record deleted!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void readTable(String tableName) {
        try {
            System.out.println("\n" + tableName + " data printing...!");
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
            } while (result.next());
            System.out.println();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void createTable(String tableName, String coloumns) {
        try {
            Connection connection = DBConnection.establishConnection();
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE " + tableName + " (Id int AUTO_INCREMENT Primary Key, " + coloumns + ")";

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

    public static boolean insertCustomerData(Customer customer) {
        Connection connection = DBConnection.establishConnection();
        String fetchQ = "SELECT id from CUSTOMER where name = ? AND mobileNumber = ?";
        String insertQ = String.format("INSERT INTO CUSTOMER(name, mobileNumber, status) VALUES(?, ?, ?)");

        try {
            PreparedStatement pst = connection.prepareStatement(fetchQ);
            pst.setString(1, customer.getName());
            pst.setString(2, customer.getMobileNo());
            ResultSet rs = pst.executeQuery();

            // If Customer doesn't exit create new user
            if (!rs.next()) {
                pst = connection.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);
                String name = customer.getName();
                String mobNo = customer.getMobileNo();

                pst.setString(1, name);
                pst.setString(2, mobNo);
                pst.setBoolean(3, true);
                pst.executeUpdate();
                ResultSet keys = pst.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);
                    customer.setId(id);
                    System.out.println(String.format("%s records has inserted successfully. \t Customer ID: %d",
                            customer.getName(), id));
                } else {
                    return false;
                }
            } else {
                customer.setId(rs.getInt(1));
                Display.printWarning("Custome already exit. \t Please continue your booking");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertTicket(Connection connection, Ticket ticket) {
        String query = String.format("INSERT INTO Ticket(totalSeats, category, amount, customerId) VALUES(?, ?, ?, ?)");

        PreparedStatement pst;
        try {
            pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            long totalNoOfSeats = ticket.getTotalSets();
            String category = ticket.getCategory();
            double totalAmount = ticket.getAmount();
            long customerId = ticket.getCustomerId();

            pst.setLong(1, totalNoOfSeats);
            pst.setString(2, category);
            pst.setDouble(3, totalAmount);
            pst.setLong(4, customerId);
            pst.executeUpdate();
            ResultSet keys = pst.getGeneratedKeys();
            if (keys.next()) {
                ticket.setId(keys.getInt(1));
                System.out.println("Record has inserted successfully. \t Ticket Id: " + ticket.getId());
                return true;
            } else {
                System.out.println("Failed to create new reocrds..!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean deleteTicketById(int ticketId, int n) {
        String fetchSeatTypeQ = "SELECT category FROM Ticket where id = ?";
        String deleteTicketQ = "DELETE FROM Ticket where id = ?";
        String updateTicketQ = "UPDATE TICKET SET totalSeats = ?, amount = ? where id = ?";
        String deleteSpecificTicketQ = "DELETE FROM CustomerTicket where seatNo = ?";
        String deleteAllTicketQ = "DELETE FROM CustomerTicket where ticketId = ?";
        String updateSeatQ = "UPDATE SEAT SET is_booked = FALSE WHERE seat_number = ?";

        try (Connection connection = DBConnection.establishConnection()) {
            List<Integer> seatNos = getSeatNosByTicketId(connection, ticketId);
            connection.setAutoCommit(false);
            int totalSeats = seatNos.size();
            Display.printMessage("You have booked " + totalSeats + " seats");
          
                String message = n + " tickets cancelled successfully!";
                if (n > totalSeats || n <= 0) {
                    Display.printWarning("Entered seats are not valid..! Please try again");
                } else if (totalSeats == n) {
                    System.out.println("All ticket");
                    PreparedStatement deletePst = connection.prepareStatement(deleteAllTicketQ);
                    PreparedStatement deleteTicketPst = connection.prepareStatement(deleteTicketQ);
                    deletePst.setInt(1, ticketId);
                    deletePst.executeUpdate();
                    deleteTicketPst.setInt(1, ticketId);
                    deleteTicketPst.executeUpdate();
                    PreparedStatement updatePst = connection.prepareStatement(updateSeatQ);
                    seatNos.forEach((var seatNo) -> {
                         try {
                            updatePst.setInt(1, seatNo);
                            updatePst.addBatch();
                         } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                         }
                    });
                    updatePst.executeBatch();
                    return true;
                } else {
                    PreparedStatement deletePst = connection.prepareStatement(deleteSpecificTicketQ);
                    PreparedStatement updatePst = connection.prepareStatement(updateSeatQ);
                    System.out.println(n + " ticket");
                    int count = 1;
                    for (int seatNo : seatNos) {
                        deletePst.setInt(1, seatNo);
                        deletePst.addBatch();
                        updatePst.setInt(1, seatNo);
                        updatePst.addBatch();
                        System.out.println(seatNo + "Seat no is deleting.. ");
                        if (count++ == n)
                            break;
                    }

                    deletePst.executeBatch();
                    updatePst.executeBatch();
                    PreparedStatement updateTicketPst = connection.prepareStatement(updateTicketQ);
                    int reamaningSeat = totalSeats - n;
                    PreparedStatement getSeatTypePst = connection.prepareStatement(fetchSeatTypeQ);
                    getSeatTypePst.setInt(1, ticketId);
                    ResultSet rs = getSeatTypePst.executeQuery();
                    if (rs.next()) {
                        String seatType = rs.getString(1);
                        double totalAmount = TicketBookingServices.calculateTotalAmount(reamaningSeat, seatType);
                        updateTicketPst.setInt(1, reamaningSeat);
                        updateTicketPst.setDouble(2, totalAmount);
                        updateTicketPst.setInt(3, ticketId);
                        updateTicketPst.executeUpdate();
                        connection.commit();
                        Display.printMessage(message);
                        return true;
                    } else {
                        Display.printAlert("Something went wrong. Please try again !!");
                    }
                }          
            connection.rollback();
            } catch (SQLException ex) {
                Display.printAlert("ERROR ");
                ex.printStackTrace();
                return false;
            }
        return false;
    }

    public static void showTicketByID(int ticketId) {
        String fetchQ = "SELECT * FROM Ticket where id = ?";

        try (Connection connection = DBConnection.establishConnection()) {
            PreparedStatement pst = connection.prepareStatement(fetchQ);
            pst.setInt(1, ticketId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ResultSetMetaData data = rs.getMetaData();
                StringBuilder ticket = new StringBuilder();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    ticket.append(data.getColumnName(i) + ": " + rs.getObject(i) + ", ");
                }
                ticket.append(getSeatNosByTicketId(connection, ticketId));
                System.out.println(ticket);
            } else {
                Display.printWarning("ID " + ticketId + " is invalid or doesn't booked successfully");
            }
        } catch (SQLException ex) {
            Display.printAlert(ex.getMessage());
        }
    }

    public static void insertAllSeats(int totalSeats) {

        String query = "INSERT INTO Seat (seat_number)" +
                "SELECT @row := @row + 1 AS seat_number" +
                "FROM (SELECT 1 FROM information_schema.columns LIMIT 100) t," +
                "(SELECT @row := 0) r;";

        try (Connection con = DBConnection.establishConnection();) {
            Statement st = con.createStatement();
            int rows = st.executeUpdate(query);
            System.out.println(rows + " inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static synchronized boolean assignSeatNo(Connection con, Ticket ticket, int totalSeats) {
        try {

            String getSeatNoQ = "Select seat_number from Seat where is_booked = FALSE ORDER By seat_number LIMIT 1";
            String updateSeatQ = "Update Seat SET is_booked = TRUE, seat_type = ? where seat_number = ?";
            String insertTicketQ = "INSERT INTO CustomerTicket values(?, ?)";

            Statement selectPst = con.createStatement();
            PreparedStatement updatePst = con.prepareStatement(updateSeatQ);
            PreparedStatement insertPst = con.prepareStatement(insertTicketQ);
            List<Integer> seats = new ArrayList<>(totalSeats);
            for (int i = 1; i <= totalSeats; i++) {
                ResultSet rs = selectPst.executeQuery(getSeatNoQ);
                if (rs.next()) {
                    int seatNo = rs.getInt(1);
                    System.out.println(seatNo + " is available to book");
                    updatePst.setString(1, ticket.getCategory());
                    updatePst.setInt(2, seatNo);
                    insertPst.setInt(1, ticket.getId());
                    insertPst.setInt(2, seatNo);
                    updatePst.executeUpdate();
                    insertPst.executeUpdate();
                    seats.add(seatNo);
                } else {
                    System.out.println("Sorry, All seat are booked!");
                    return false;
                }
            }
            ticket.assignSeatNumbers(seats);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    private static List<Integer> getSeatNosByTicketId(Connection connection, int ticketId) throws SQLException {
        String fetchQ = "SELECT seatNo FROM CustomerTicket where ticketId = ?";

        connection.setAutoCommit(false);
        PreparedStatement pst = connection.prepareStatement(fetchQ);
        pst.setInt(1, ticketId);
        ResultSet rs = pst.executeQuery();
        List<Integer> seatNos = new ArrayList<>();
        if (rs.next()) {
            do {
                seatNos.add(rs.getInt(1));
            } while (rs.next());
        }
        return seatNos;
    }

    public static int availableSeats() {
        String FetchQ = "SELECT COUNT(seat_number) FROM Seat WHERE is_booked = FALSE";

        try (Connection con = DBConnection.establishConnection(); Statement stm = con.createStatement();) {
            ResultSet rs = stm.executeQuery(FetchQ);
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

}
