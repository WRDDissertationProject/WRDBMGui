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

    public static void insertAnnouncement(String text) {
        //insert into query
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO announcements(announcement_details) VALUES('" + text + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
        }
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

    public static List<String> loadTicket() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT ticket_details FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        while (resultSet.next()) {
            String ticket = resultSet.getString("ticket_details");
            ticketsList.add(ticket);
        }
        return ticketsList;
    }

    public static List<String> loadMaintenance() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT maintenance_details FROM maintenance");
        List<String> maintenanceList = new ArrayList<>();

        while (resultSet.next()) {
            String record = resultSet.getString("maintenance_details");
            maintenanceList.add(record);
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
        String query = "SELECT skateAmount FROM skate_inventory WHERE skateSize = '" + skateSize + "'";
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
            String sql = "UPDATE current_skates SET skateAmount = " + skateAmount + " WHERE skateSize = " + skateSize + " ";
            //String sql = "INSERT INTO current_session(current_dateTime) VALUES('" + fullDate + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Skate Size Into Current Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Current Skate Size not inserted to Current Database");
        }
    }

    public static void insertTicket(String text) {
        //insert into query
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
            //Table: tickets
            //Columns:
            //ticket_id int AI PK
            //ticket_date varchar(45)
            //ticket_time varchar(45)
            //ticket_details varchar(255)
            //staff_id varchar(45)
            //resolved char(1)

            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
        }
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

    public static void deleteTicket(String value){
        String query = "SELECT * FROM tickets WHERE ticket_details = " + value + " ";
    }

    public static void deleteAnnouncement(String value) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = "DELETE FROM announcements WHERE announcement_details = '" + value + "'";
        stmt.executeUpdate(query);

    }
}

