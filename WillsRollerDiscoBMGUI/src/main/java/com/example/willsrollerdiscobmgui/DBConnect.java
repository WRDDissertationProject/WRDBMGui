package com.example.willsrollerdiscobmgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {
    String url = "jdbc:mysql://localhost:3306/wrdDatabase";
    String username = "root";
    String password = "root";

    static Connection connection = null;

    static ResultSet rs;

    public static ObservableList<Skate> loadSkates() {
        ObservableList<Skate> data = FXCollections.observableArrayList();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT skateSize, skateAmount FROM current_skates");

            // iterate through the result set and add Skate objects to the ObservableList
            while (rs.next()) {
                String size = rs.getString("skateSize");
                int amount = rs.getInt("skateAmount");
                data.add(new Skate(size, amount));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    public static void insertMaintenance(String typeIn, String details, String skateSizeIn) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO maintenance(maintenance_type, maintenance_details, skateSize)" +
                    " VALUES('" + typeIn + "', '" + details + "', '" + skateSizeIn + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount - 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount - 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
        }
    }

    public static void updateSkateSizeAmount(String skateSize, int newAmount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE current_skates SET skateAmount = ? WHERE skateSize = ?");
            stmt.setInt(1, newAmount);
            stmt.setString(2, skateSize);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Current skate amount not updated");
        }
    }

    public static void updateSkateInventory(String skateSize, int newAmount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE skate_inventory SET skateAmount = ? WHERE skateSize = ?");
            stmt.setInt(1, newAmount);
            stmt.setString(2, skateSize);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Skate inventory not updated");
        }

    }

    public static void resetLocks() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE locks SET lockedBy = null, lockTime = null");
        stmt.executeUpdate();
        System.out.println("Locks Reset");
    }

    public void connect() {
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Class Not Found");
            }
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Connection)");
                ;
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Database Connected");
        }
    }

    public static boolean insertAnnouncement(String text) {
        //insert into query
        boolean success = false;
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO announcements(announcement_details) VALUES('" + text + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");
            success = true;

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
            success = false;
        }
        return success;
    }

    public static List<String> loadAnnouncement() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT announcement_details FROM announcements");
        List<String> announcementsList = new ArrayList<>();

        while (resultSet.next()) {
            String announcement = resultSet.getString("announcement_details");
            announcementsList.add(announcement);
        }
        return announcementsList;
    }

    public static List<String> loadAnnouncementSH() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM current_skates WHERE skateAmount <= 5");
        List<String> announcementsSHList = new ArrayList<>();

        while (resultSet.next()) {
            String skateSize = resultSet.getString("skateSize");
            String skateAmount = resultSet.getString("skateAmount");
            announcementsSHList.add("Skate Size: " + skateSize + "\nSkate Amount: " + skateAmount);
        }
        return announcementsSHList;
    }

    public static List<String> loadTicket() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        while (resultSet.next()) {
            String ticket = resultSet.getString("ticket_details");
            String ticketDate = resultSet.getString("ticket_date");
            String ticketTime = resultSet.getString("ticket_time");
            String postedBy = resultSet.getString("staff_id");

            ticketsList.add("Ticket Details: " + ticket
            + "\nDate and Time Posted: " + ticketDate + " " +  ticketTime
            + "\nPosted By: " + postedBy);
        }
        return ticketsList;
    }

    public static List<String> loadMaintenance() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM maintenance");
        List<String> maintenanceList = new ArrayList<>();

        while (resultSet.next()) {
            String record = resultSet.getString("maintenance_details");
            String type = resultSet.getString("maintenance_type");
            String skateSize = resultSet.getString("skateSize");

            if((skateSize == null) || (skateSize.equals("null"))){
                skateSize = "N/A";
            }
            maintenanceList.add(
                    "Details: " + record + "\nType: " + type + "\nSkate Size: " +skateSize);
        }
        return maintenanceList;
    }


    public static void insertCurrentSession(String fullDate, String date, String time) {

        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO current_session(current_dateTime) VALUES('" + fullDate + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Current Session not inserted");
        }
    }


    public static void moveToPreviousSessions() {
        String sessionDateTime = null;

        //select from current session
        try {
            String query = "SELECT * FROM current_session";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                sessionDateTime = rs.getString("current_dateTime");
            }
        }
        catch (SQLException e) {
            System.out.print(e);
            System.out.println("Cannot select from current table");
            return; //exit method if there is an error
        }

        //move into the previous table
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO previous_sessions(session_dateTime) VALUES (?)");
            pstmt.setString(1, sessionDateTime);
            pstmt.executeUpdate();
            System.out.println("Moved to Previous Session");
        }
        catch (SQLException e) {
            System.out.print(e);
            System.out.println("Table could not be moved to Previous Session");
            return; //exit method if there is an error
        }

        //drop current session
        try {
            Statement stmt = connection.createStatement();
            String sql = "TRUNCATE TABLE current_session";
            stmt.executeUpdate(sql);
            System.out.println("Current Session Dropped");
        }
        catch (SQLException e) {
            System.out.print(e);
            System.out.println("Current Session Table Cannot Be Dropped");
        }
    }

    public static int fetchSkateSizeAmount(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM skate_inventory WHERE skateSize = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, skateSize);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static int fetchSkateSizeCurrent(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM current_skates WHERE skateSize = '" + skateSize + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void insertIntoCurrentSkates(String skateSize, int skateAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE current_skates SET skateAmount = " + skateAmount + " WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Skate Size Into Current Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Current Skate Size not inserted to Current Database");
        }
    }

    public static boolean insertTicket(String text) {
        //insert into query
        boolean success = false;
        String ticketDate = dateTime.justDate();
        String ticketTime = dateTime.justTime();
        String postedBy = "Business Management Portal";
        try {
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO tickets(ticket_date, ticket_time, ticket_details, staff_id) VALUES(?, ?, ?, ?)");

            pstmt.setString(1, ticketDate);
            pstmt.setString(2, ticketTime);
            pstmt.setString(3, text);
            pstmt.setString(4, postedBy);

            pstmt.executeUpdate();

            System.out.println("Inserted Into Database");
            success = true;

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
            success = false;
        }
        return success;
    }
    public static boolean checkForSessionStart() throws SQLException {
        boolean session = false;
        String query = "SELECT * FROM current_session WHERE current_session_id = true";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                session = true;
            } return session;
    }

    public static boolean deleteTicket(String value){
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM tickets WHERE ticket_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;
        }
        catch (SQLException e) {
            System.out.println(e);
            System.out.println("Ticket not Deleted");
            success = false;
        }
        return success;
    }

    public static boolean deleteAnnouncement(String value) throws SQLException {
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM announcements WHERE announcement_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;
        }
        catch (SQLException e) {
        System.out.println(e);
        System.out.println("Announcement not Deleted");
        success = false;
        }
        return success;
    }

    public static boolean deleteMaintenance(String value, String skateSizeIn) throws SQLException {
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM maintenance WHERE maintenance_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;

            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount + 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount + 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
            System.out.println("Maintenance not Deleted");
            success = false;
        }
        return success;
    }

    public static boolean checkLogin(String username, String password) throws SQLException {
        boolean loggedIn = false;
        String query = "SELECT * FROM login WHERE username=? AND password=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            loggedIn = true;
            System.out.print("Logged In");
        }
        else {
            loggedIn = false;
            System.out.print("Not Logged In");
        }
        return loggedIn;
    }

    public static String getSkateSizeFromMaintenance(String value) throws SQLException {
        String skateSize = null;
        PreparedStatement stmt = connection.prepareStatement("SELECT skateSize FROM maintenance WHERE maintenance_details = ?");
        stmt.setString(1, value);
        String queryString = stmt.toString();
        System.out.println(queryString);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            skateSize = rs.getString("skateSize");
        }

        rs.close();
        stmt.close();

        return skateSize;
    }

}

