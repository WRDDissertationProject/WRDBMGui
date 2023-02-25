package com.example.willsrollerdiscobmgui;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> loadAnnouncement() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT announcement_details FROM announcements");
        List<String> announcementsList = new ArrayList<>();

            while (resultSet.next()) {
                String announcement = resultSet.getString("announcement_details");
                announcementsList.add(announcement);
                announcementsList.add(announcement);
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

    public static void dropCurrentSession(){
        try{
            Statement stmt = connection.createStatement();
            String sql = "TRUNCATE TABLE current_session";
            //drop table command
            stmt.executeUpdate(sql);
            System.out.println("Current Session Dropped");
        }
        catch(SQLException e){
            System.out.print(e);
            System.out.println("Current Session Table Cannot Be Dropped");

        }
    }
    public static int fetchSkateSizeAmount(String skateSize) {
            int value = 0;
            String query = "SELECT skateAmount FROM skate_inventory WHERE skateSize = '"+ skateSize + "'";
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

            //UPDATE Current_Skates
            //SET Skate_Model = <new_model>, Skate_Size = <new_size>, Skate_Color = <new_color>
            //WHERE ID = <row_ID>;

        }
        catch(SQLException e){
            System.out.println(e);
            System.out.println("Current Skate Size not inserted to Current Database");
        }
}
}
