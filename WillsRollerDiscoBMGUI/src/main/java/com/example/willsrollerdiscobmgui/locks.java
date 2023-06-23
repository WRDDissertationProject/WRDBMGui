/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: locks.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Methods to prevent concurrency issues, used to restrict access to the database when another application is midway
 *   through a transaction.
 *   */

/*Resources Used:
 * Locks:
 * Lock Theory  */


//PACKAGE
package com.example.willsrollerdiscobmgui;

//IMPORTS
import java.sql.*;

public class locks {
    /******************************************************
     Local Testing Database Connections

     String url = "jdbc:mysql://localhost:3306/wrdDatabase";
     String username = "root";
     String password = "root";
     ********************************************************/

    /*Docker Database Connections
     * Using Local host currently due to firewall issues with University Connections*/
    String url = "jdbc:mysql://localhost:3307/wrddatabase";
    //String url = "jdbc:mysql://172.17.0.2:3307/wrddatabase";
    String username = "root";
    String password = "my-secret-pw";
    static Connection connection = null;
    static ResultSet rs;

    //Duplicate code from DBConnect, used to establish a database connection for the locks
    //Could be refactored and removed on refinement
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
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Lock Table Connected");
        }
    }

    //Used to lock the database
    //Stops concurrency issues by making transactions wait for resources to be out of use before starting transaction
    public static void lock(String resourceName, String lockedBy) throws SQLException {
        String query = "UPDATE locks SET lockedBy = ?, lockTime = NOW() WHERE resourceName = ? AND lockedBy IS NULL";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, lockedBy);
        statement.setString(2, resourceName);

        int rowsUpdated = statement.executeUpdate();

        if (rowsUpdated == 0) {
            throw new SQLException("Failed to acquire lock for resource: " + resourceName);
        }
    }

    //When a transaction is finished, unlock is called and the locked resource is set to null so other
    //transactions can be completed on the same database table
    public static void unlock(String resourceName, String lockedBy) throws SQLException {
        String query = "UPDATE locks SET lockedBy = NULL, lockTime = NULL WHERE resourceName = ? AND lockedBy = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, resourceName);
        statement.setString(2, lockedBy);
        statement.executeUpdate();
    }

    //used to clear all locks current in use, used on a software restart
    //prevents issues with hanging locks preventing data transfer
    public static void resetLocks() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE locks SET lockedBy = null, lockTime = null");
        stmt.executeUpdate();
        System.out.println("Locks Reset");
    }
}
