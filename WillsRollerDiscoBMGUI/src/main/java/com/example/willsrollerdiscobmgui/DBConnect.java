package com.example.willsrollerdiscobmgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBConnect {
    String url = "jdbc:mysql://localhost:3306/wrdDatabase";
    String username = "root";
    String password = "root";

    static Connection connection = null;

    static ResultSet rs;
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
                System.out.println("Run Time Exception (Connection)");;
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Database Connected");
        }
    }

    public static void insertAnnouncement(String text){
        //insert into query
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO announcements(announcement_details) VALUES('" + text + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Announcement not inserted");
        }
    }
    public static ObservableList<String> loadAnnouncement() {
        ObservableList<String> announcementsList = null;
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT announcement_details FROM announcements");
            announcementsList = FXCollections.observableArrayList();
            while (rs.next()) {
                String details = rs.getString("announcement_details");
                System.out.println(details);
                announcementsList.add(details);
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcements Could not be loaded");
        }
        return announcementsList;
    }

    public static void insertCurrentSession(String fullDate, String date, String time){

        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO current_session(current_dateTime) VALUES('" + fullDate + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Current Session not inserted");
        }
    }
}
