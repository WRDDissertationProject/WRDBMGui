package com.example.willsrollerdiscobmgui;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class insert {

    public void insertCurrentSession(){
        //crate date time object
        //insert into database
    }

    public static void insertAnnouncement(String text){
        //insert into query
        try {
            Connection conn = null;
            Statement stmt = null;
            stmt = conn.createStatement();
            String sql = "INSERT INTO announcements(announcement_details) VALUES(" + text + ")";
            stmt.executeUpdate(sql);

        }
        catch(SQLException e){
            System.out.println("Announcement not inserted");
        }
    }
}
